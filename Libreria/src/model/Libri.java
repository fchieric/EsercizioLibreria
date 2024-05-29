package model;

public class Libri {
    public int id;
    public String titolo;
    public String autore;
    public int idGenere;
    public Integer idGenere2;
    public int idCasaEd;
    public int anno;
    public int quantita;
    public String codiceIsbn;

    @Override
    public String toString() {
        return "Libri{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", idGenere=" + idGenere +
                ", idGenere2=" + idGenere2 +
                ", idCasaEd=" + idCasaEd +
                ", data=" + anno +
                ", quantita=" + quantita +
                ", codiceIsbn=" + codiceIsbn +
                '}';
    }
}
