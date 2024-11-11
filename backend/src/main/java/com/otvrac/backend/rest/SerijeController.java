package com.otvrac.backend.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.otvrac.backend.domain.Epizode;
import com.otvrac.backend.domain.Serije;
import com.otvrac.backend.service.SerijeService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/api/serije")
public class SerijeController {

    private final SerijeService serijeService;
    private final ObjectMapper jacksonObjectMapper;

    public SerijeController(SerijeService serijeService, ObjectMapper jacksonObjectMapper) {
        this.serijeService = serijeService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @GetMapping
    public List<Serije> getSerije(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "sve") String attribute) {
        return serijeService.search(filter, attribute);
    }

    @GetMapping("/download/json")
    public ResponseEntity<InputStreamResource> downloadJson(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "sve") String attribute) throws IOException {

        List<Serije> serijeList = serijeService.search(filter, attribute);

        String json = jacksonObjectMapper.writeValueAsString(serijeList);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(json.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"serije.json\"")
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @GetMapping("/download/csv")
    public ResponseEntity<InputStreamResource> downloadCsv(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "sve") String attribute) throws IOException {

        List<Serije> serijeList = serijeService.search(filter, attribute);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream);

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Id", "Naslov", "Zanr", "Godina Izlaska", "Ocjena", "Broj Sezona", "Jezik", "Autor", "Mreza", "Naziv Epizode", "Sezona", "Broj Epizode", "Datum Emitiranja", "Trajanje", "Ocjena Epizode", "Scenarist", "Redatelj"));
        for (Serije serija : serijeList) {
            for (Epizode epizoda : serija.getEpizode()) {
                csvPrinter.printRecord(
                        serija.getId(),
                        serija.getNaslov(),
                        serija.getZanr(),
                        serija.getGodinaIzlaska(),
                        serija.getOcjena(),
                        serija.getBrojSezona(),
                        serija.getJezik(),
                        serija.getAutor(),
                        serija.getMreza(),
                        epizoda.getNazivEpizode(),
                        epizoda.getSezona(),
                        epizoda.getBrojEpizode(),
                        epizoda.getDatumEmitiranja(),
                        epizoda.getTrajanje(),
                        epizoda.getOcjena(),
                        epizoda.getScenarist(),
                        epizoda.getRedatelj()
                );
            }
        }
        csvPrinter.flush();
        csvPrinter.close();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"serije.csv\"")
                .body(new InputStreamResource(byteArrayInputStream));
    }
}
