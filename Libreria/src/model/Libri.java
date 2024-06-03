package model;

public class Libri {
    public int id;
    public String titolo;
    public String autore;
    public int idGenere;
    public int idGenere2;
    public int idCasaEd;
    public int anno;
    public int quantita;
    public String codiceIsbn;

    @Override
    public String toString() {
        return "Libro ID: " + id +
                "\nTitolo: " + titolo +
                "\nAutore: " + autore +
                "\nID Genere: " + idGenere +
                "\nID Genere 2: " + (idGenere2 == 0 ? "N/A" : idGenere2) +
                "\nID Casa Editrice: " + idCasaEd +
                "\nAnno di Pubblicazione: " + anno +
                "\nQuantità: " + quantita +
                "\nCodice ISBN: " + codiceIsbn;
    }
}
