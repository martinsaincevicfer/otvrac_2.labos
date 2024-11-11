export interface Epizode {
    id: number;
    nazivEpizode: string;
    sezona: number;
    brojEpizode: number;
    datumEmitiranja: string;
    trajanje: number;
    ocjena: number;
    scenarist: string;
    redatelj: string;
}

export interface Serije {
    id: number;
    naslov: string;
    zanr: string;
    godinaIzlaska: number;
    ocjena: number;
    brojSezona: number;
    jezik: string;
    autor: string;
    mreza: string;
    epizode: Epizode[];
}