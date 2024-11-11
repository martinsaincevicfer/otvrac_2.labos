import React, { useEffect, useState } from "react";
import { Serije } from "../types/Serije";
import axios from "axios";

const downloadCSV = (data: Serije[]) => {
    const headers = [
        "Naslov", "Žanr", "Godina Izlaska", "Ocjena", "Broj Sezona", "Jezik", "Autor", "Mreža",
        "Naziv Epizode", "Sezona", "Broj Epizode", "Datum Emitiranja", "Trajanje", "Ocjena Epizode",
        "Scenarist", "Redatelj"
    ];

    const rows = data.flatMap(serija =>
        serija.epizode.map(epizoda => [
            serija.naslov,
            serija.zanr,
            serija.godinaIzlaska,
            serija.ocjena,
            serija.brojSezona,
            serija.jezik,
            serija.autor,
            serija.mreza,
            epizoda.nazivEpizode,
            epizoda.sezona,
            epizoda.brojEpizode,
            epizoda.datumEmitiranja,
            epizoda.trajanje,
            epizoda.ocjena,
            epizoda.scenarist,
            epizoda.redatelj
        ])
    );

    const csvContent = [headers, ...rows].map(row => row.join(",")).join("\n");

    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "serije.csv";
    link.click();
};

const downloadJSON = (data: Serije[]) => {
    const blob = new Blob([JSON.stringify(data, null, 2)], { type: "application/json" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "serije.json";
    link.click();
};

const Datatable: React.FC = () => {
    const params = new URLSearchParams(window.location.search);
    const initialFilter = params.get('filter') || '';
    const initialAttribute = params.get('attribute') || 'sve';

    const [serije, setSerije] = useState<Serije[]>([]);
    const [filter, setFilter] = useState<string>(initialFilter);
    const [attribute, setAttribute] = useState<string>(initialAttribute);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/serije`, {
                    params: { filter, attribute }
                });
                setSerije(response.data);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };
        fetchData();
    }, [filter, attribute]);

    return (
        <div>
            <h1>Prikaz serija</h1>
            <form onSubmit={(e) => e.preventDefault()}>
                <label htmlFor="pretragaPolje">Polje za pretragu:</label>
                <input
                    type="text"
                    value={filter}
                    onChange={(e) => setFilter(e.target.value)}
                    placeholder="Pretraži..."
                />

                <label>Pretraži po:</label>
                <select value={attribute} onChange={(e) => setAttribute(e.target.value)}>
                    <option value="sve">Sve</option>
                    <option value="naslov">Naslov</option>
                    <option value="zanr">Žanr</option>
                    <option value="godinaizlaska">Godina izlaska</option>
                    <option value="ocjena">Ocjena</option>
                    <option value="brojsezona">Broj sezona</option>
                    <option value="jezik">Jezik</option>
                    <option value="autor">Autor</option>
                    <option value="mreza">Mreža</option>
                    <option value="nazivepizode">Naziv epizode</option>
                    <option value="sezona">Sezona</option>
                    <option value="brojepizode">Broj epizode</option>
                    <option value="datumemitiranja">Datum emitiranja</option>
                    <option value="trajanje">Trajanje</option>
                    <option value="scenarist">Scenarist</option>
                    <option value="redatelj">Redatelj</option>
                </select>

                <button type="submit">Pretraži</button>
            </form>

            <table>
                <thead>
                <tr>
                    <th>Naslov</th>
                    <th>Žanr</th>
                    <th>Godina Izlaska</th>
                    <th>Ocjena</th>
                    <th>Broj Sezona</th>
                    <th>Jezik</th>
                    <th>Autor</th>
                    <th>Mreža</th>
                    <th>Naziv Epizode</th>
                    <th>Sezona</th>
                    <th>Broj Epizode</th>
                    <th>Datum Emitiranja</th>
                    <th>Trajanje</th>
                    <th>Ocjena Epizode</th>
                    <th>Scenarist</th>
                    <th>Redatelj</th>
                </tr>
                </thead>
                <tbody>
                {serije.map(serija =>
                    serija.epizode.map(epizoda => (
                        <tr key={`${serija.id}-${epizoda.id}`}>
                            <td>{serija.naslov}</td>
                            <td>{serija.zanr}</td>
                            <td>{serija.godinaIzlaska}</td>
                            <td>{serija.ocjena}</td>
                            <td>{serija.brojSezona}</td>
                            <td>{serija.jezik}</td>
                            <td>{serija.autor}</td>
                            <td>{serija.mreza}</td>
                            <td>{epizoda.nazivEpizode}</td>
                            <td>{epizoda.sezona}</td>
                            <td>{epizoda.brojEpizode}</td>
                            <td>{epizoda.datumEmitiranja}</td>
                            <td>{epizoda.trajanje}</td>
                            <td>{epizoda.ocjena}</td>
                            <td>{epizoda.scenarist}</td>
                            <td>{epizoda.redatelj}</td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>

            <div>
                <button onClick={() => downloadCSV(serije)}>Preuzmi CSV</button>
                <button onClick={() => downloadJSON(serije)}>Preuzmi JSON</button>
            </div>
        </div>
    );
};

export default Datatable;
