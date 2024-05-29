import model.Libri;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<Libri> elencoLibri = new ArrayList<Libri>();

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3306/Libreria";
        Scanner sc = new Scanner(System.in);
        Libri l;
        int scelta;
        boolean exit = false;

        System.out.println("Connettendomi ...");
        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
            System.out.println("Connessione riuscita correttamente.\n");

            // all'avvio, caricare i libri presenti nel db in una arraylist
            String sql = "SELECT * FROM libri";
            try (PreparedStatement ps = conn.prepareStatement(sql)) { //provo a creare una query sql
                try (ResultSet rs=ps.executeQuery()) {    //il ResultSet mi consente di scorrere il risultato della SELECT una riga alla volta
                    while (rs.next()) {
                        l = new Libri();
                        l.id = rs.getInt("id");
                        l.titolo = rs.getString("titolo");
                        l.autore = rs.getString("autore");
                        l.idGenere = rs.getInt("idGenere");
                        l.idGenere2 = rs.getInt("idGenere2");
                        l.idCasaEd = rs.getInt("idCasaEd");
                        l.anno = rs.getInt("anno");
                        l.quantita = rs.getInt("quantita");
                        l.codiceIsbn = rs.getString("codiceIsbn");
                        elencoLibri.add(l);
                    }
                }
            }
            // menu per scelta utente
            // DA MODIFICARE INSERENDO TRY CATCH PER EVITARE CHE SI ROMPA
            System.out.println("*** SELEZIONA UNA VOCE DAL MENÙ ***\n");
            while (!exit) {
                System.out.println("1 per inserire un nuovo libro");
                System.out.println("2 per mostrare i libri presenti");
                System.out.println("3 per rimuovere un libro");
                System.out.println("4 per modificare i dati inseriti");
                System.out.println("5 per uscire dal menù");

                System.out.print("Inserisci il numero della tua selezione: ");
                scelta = sc.nextInt();
                sc.nextLine();

                switch (scelta) {
                    case 1:
                        insertLibro(sc);
                        break;
                    case 2:
                        showLibri();
                        break;
                        /*
                    case 3:
                        deleteLibro(sc);
                        break;
                    case 4:
                        updateLibro(sc);
                        break;
                         */
                    case 5:
                        System.out.println("Uscita dal menù.");
                        exit = true;
                        break;
                    default:
                        System.out.println("Scelta non valida. Riprova.");
                        break;
                }
            } //fine while
        } catch (SQLException e) {
            System.err.println("Si è verificato un errore." + e.getMessage());
        }
        sc.close();
    }

    private static void insertLibro(Scanner sc) {
        Libri nuovoLibro = new Libri();

        System.out.println("Inserisci i dati del nuovo libro:");

        System.out.print("Titolo: ");
        nuovoLibro.titolo = sc.nextLine();

        System.out.print("Autore: ");
        nuovoLibro.autore = sc.nextLine();

        System.out.print("ID Genere: ");
        nuovoLibro.idGenere = sc.nextInt();
        sc.nextLine();

        System.out.print("ID Genere 2 (premi invio per saltare): ");
        String idGenere2Input = sc.nextLine();
        if (idGenere2Input.isEmpty()) {
            nuovoLibro.idGenere2 = null;
        } else {
            nuovoLibro.idGenere2 = Integer.parseInt(idGenere2Input);
        }

        System.out.print("ID Casa Editrice: ");
        nuovoLibro.idCasaEd = sc.nextInt();
        sc.nextLine();

        System.out.print("Data (yyyy): ");
        nuovoLibro.anno = sc.nextInt();
        sc.nextLine();

        System.out.print("Quantità: ");
        nuovoLibro.quantita = sc.nextInt();
        sc.nextLine();

        System.out.print("Codice ISBN: ");
        nuovoLibro.codiceIsbn = sc.nextLine();

        elencoLibri.add(nuovoLibro);
        System.out.println("\nNuovo libro inserito: " + nuovoLibro + "\n");

        //DOBBIAMO INSERIRLO ANCHE NEL DB, NON SOLO NELL ARRAY LIST
    }

    private static void showLibri() {
        System.out.println("Libri presenti:");
        for (Libri libro : elencoLibri) {
            System.out.println(libro);
        }
    }

}