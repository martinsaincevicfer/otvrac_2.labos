package com.otvrac.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "epizode")
public class Epizode {
    @Id
    @ColumnDefault("nextval('epizode_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "serija_id")
    private Integer serija_id;

    @Column(name = "naziv_epizode", nullable = false)
    private String nazivEpizode;

    @Column(name = "sezona")
    private Integer sezona;

    @Column(name = "broj_epizode")
    private Integer brojEpizode;

    @Column(name = "datum_emitiranja")
    private LocalDate datumEmitiranja;

    @Column(name = "trajanje")
    private Integer trajanje;

    @Column(name = "ocjena")
    private Double ocjena;

    @Column(name = "scenarist")
    private String scenarist;

    @Column(name = "redatelj")
    private String redatelj;

}