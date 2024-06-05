import model.Libri;
import model.Utente;

import javax.xml.stream.events.Comment;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<Libri> elencoLibri = new ArrayList<Libri>();
    private static String url = "jdbc:mysql://127.0.0.1:3306/Libreria";

    public static void main(String[] args) {
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
                try (ResultSet rs = ps.executeQuery()) {    //il ResultSet mi consente di scorrere il risultato della SELECT una riga alla volta
                    while (rs.next()) {
                        l = new Libri();
                        l.id = rs.getInt("id");
                        l.titolo = rs.getString("titolo");
                        l.autore = rs.getString("autore");
                        l.idGenere = rs.getInt("id_genere");
                        l.idGenere2 = rs.getInt("id_genere2");
                        l.idCasaEd = rs.getInt("id_casa_editrice");
                        l.anno = rs.getInt("data_pubblicazione");
                        l.quantita = rs.getInt("quantita");
                        l.codiceIsbn = rs.getString("codice");
                        elencoLibri.add(l);
                    }
                }
            }

            // menu per scelta utente
            // DA MODIFICARE INSERENDO TRY CATCH PER EVITARE CHE SI ROMPA
            System.out.println("*** SELEZIONA UNA VOCE DAL MENÙ PRINCIPALE ***\n");
            while (!exit) {
                System.out.println("1 per il menù libri");
                System.out.println("2 per il menù utenti");
                System.out.println("3 per il menù magazzino");
                System.out.println("4 per fare una ricerca");
                System.out.println("5 per reportistica prestiti");
                System.out.println("6 per uscire");

                System.out.print("Inserisci il numero della tua selezione: ");
                scelta = sc.nextInt();
                sc.nextLine();

                if (scelta == 1) {
                    menuLibri(sc);
                } else if (scelta == 2) {
                    menuUtenti(sc);
                } /* else if (scelta == 3) {
                    menuMagazz(sc);
                } else if (scelta == 4) {
                    menuRicerca(sc);
                } else if (scelta == 5) {
                    menuReportistica(sc);
                } */ else if (scelta == 6) {
                    System.out.println("Uscita dal menù.");
                    exit = true;
                } else {
                    System.out.println("Scelta non valida. Riprova.");
                }
            } //fine while
        } catch (SQLException e) {
            System.err.println("Si è verificato un errore." + e.getMessage());
        }
        sc.close();
    }

    // menu libri
    private static void menuLibri(Scanner sc) {
            // menu per scelta utente x libro
            boolean exit = false;
            int scelta;

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

                if (scelta == 1) {
                    insertLibro(sc);
                } else if (scelta == 2) {
                    showLibri();
                } else if (scelta == 3) {
                    deleteLibro(sc);
                } else if (scelta == 4) {
                    updateLibro(sc);
                } else if (scelta == 5) {
                    System.out.println("Uscita dal menù.");
                    exit = true;
                } else {
                    System.out.println("Scelta non valida. Riprova.");
                }
            }
    }

    // menu utenti

    private static void menuUtenti(Scanner sc) {
        boolean exit = false;
        while (!exit) {
            System.out.println("*** MENU UTENTI ***");
            System.out.println("1. Inserisci un nuovo utente");
            System.out.println("2. Visualizza tutti gli utenti");
            System.out.println("3. Autentica utente");
            System.out.println("4. Mostra prestiti utente");
            System.out.println("5. Torna al menu principale");

            System.out.print("Inserisci il numero della tua selezione: ");
            int scelta = sc.nextInt();
            sc.nextLine();

            switch (scelta) {
                case 1:
                    Utente.insertUtente(sc);
                    break;
                case 2:
                    Utente.showUtente();
                    break;
                case 3:
                    Utente.authenticateUser(sc);
                    break;
                case 4:
                    System.out.print("Inserisci l'ID dell'utente: ");
                    int userId = sc.nextInt();
                    sc.nextLine();
                    Utente.showLoanHistory(userId);
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Selezione non valida, riprova.");
            }
        }
    }

    /* da fare:

        private static void menuMagazzino(Scanner sc) {
        // Implementa la logica del menu del magazzino
    }

    private static void menuRicerca(Scanner sc) {
        // Implementa la logica del menu di ricerca
    }

    private static void menuPrestiti(Scanner sc) {
        // Implementa la logica del menu dei prestiti
    }

     */


    private static void insertLibro(Scanner sc) {

        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
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
                nuovoLibro.idGenere2 = 0;
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

            // Verifica se i valori id_genere e id_genere2 esistono nella tabella generi
            if (isValidGenere(conn, nuovoLibro.idGenere) && (nuovoLibro.idGenere2 == 0 || isValidGenere(conn, nuovoLibro.idGenere2))) {
                String sql = "INSERT INTO libri (titolo, autore, id_genere, id_genere2, id_casa_editrice, data_pubblicazione, quantita, codice) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, nuovoLibro.titolo);
                    pstmt.setString(2, nuovoLibro.autore);
                    pstmt.setInt(3, nuovoLibro.idGenere);
                    if (nuovoLibro.idGenere2 == 0) {
                        pstmt.setNull(4, java.sql.Types.INTEGER);
                    } else {
                        pstmt.setInt(4, nuovoLibro.idGenere2);
                    }
                    pstmt.setInt(5, nuovoLibro.idCasaEd);
                    pstmt.setInt(6, nuovoLibro.anno);
                    pstmt.setInt(7, nuovoLibro.quantita);
                    pstmt.setString(8, nuovoLibro.codiceIsbn);
                    pstmt.executeUpdate();
                    elencoLibri.add(nuovoLibro); // Aggiungi il nuovo libro alla lista locale
                    System.out.println("Nuovo libro inserito: " + nuovoLibro);
                }
            } else {
                System.out.println("ID Genere o ID Genere 2 non validi.");
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del libro: " + e.getMessage());
        }
    }

    private static boolean isValidGenere(Connection conn, int idGenere) {
        String sql = "SELECT COUNT(*) FROM generi WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idGenere);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la verifica del genere: " + e.getMessage());
        }
        return false;
    }

    private static void showLibri() {
        System.out.println("Libri presenti:");
        for (Libri libro : elencoLibri) {
            System.out.println(libro);
        }
    }

    private static void deleteLibro(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
            System.out.print("Inserisci l'ID del libro da cancellare: ");
            int id = sc.nextInt();
            sc.nextLine();

            // Verifica se l'ID del libro esiste nella lista elencoLibri
            boolean libroTrovato = false;
            for (Libri libro : elencoLibri) {
                if (libro.id == id) {
                    libroTrovato = true;
                    break;
                }
            }

            if (!libroTrovato) {
                System.out.println("Nessun libro trovato con ID " + id);
                return;
            }

            String sql = "DELETE FROM libri WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    elencoLibri.removeIf(libro -> libro.id == id);
                    System.out.println("Libro con ID " + id + " cancellato.");
                } else {
                    System.out.println("Nessun libro trovato con ID " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante la cancellazione del libro: " + e.getMessage());
        }
    }

    private static void updateLibro(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
            System.out.print("Inserisci l'ID del libro da modificare: ");
            int id = sc.nextInt();
            sc.nextLine();

            for (Libri libro : elencoLibri) {
                if (libro.id == id) {
                    System.out.println("Inserisci i nuovi dati del libro (lascia vuoto per mantenere il valore attuale):");

                    System.out.print("Titolo (" + libro.titolo + "): ");
                    String nuovoTitolo = sc.nextLine();
                    if (!nuovoTitolo.isEmpty()) {
                        libro.titolo = nuovoTitolo;
                    }

                    System.out.print("Autore (" + libro.autore + "): ");
                    String nuovoAutore = sc.nextLine();
                    if (!nuovoAutore.isEmpty()) {
                        libro.autore = nuovoAutore;
                    }

                    System.out.print("ID Genere (" + libro.idGenere + "): ");
                    String nuovoIdGenere = sc.nextLine();
                    if (!nuovoIdGenere.isEmpty()) {
                        libro.idGenere = Integer.parseInt(nuovoIdGenere);
                    }

                    System.out.print("ID Genere 2 (" + libro.idGenere2 + "): ");
                    String nuovoIdGenere2 = sc.nextLine();
                    if (!nuovoIdGenere2.isEmpty()) {
                        libro.idGenere2 = Integer.parseInt(nuovoIdGenere2);
                    } else {
                        libro.idGenere2 = 0; // Usa 0 per rappresentare l'assenza di un valore valido
                    }

                    System.out.print("ID Casa Editrice (" + libro.idCasaEd + "): ");
                    String nuovoIdCasaEd = sc.nextLine();
                    if (!nuovoIdCasaEd.isEmpty()) {
                        libro.idCasaEd = Integer.parseInt(nuovoIdCasaEd);
                    }

                    System.out.print("Anno (" + libro.anno + "): ");
                    String nuovoAnno = sc.nextLine();
                    if (!nuovoAnno.isEmpty()) {
                        libro.anno = Integer.parseInt(nuovoAnno);
                    }

                    System.out.print("Quantità (" + libro.quantita + "): ");
                    String nuovaQuantita = sc.nextLine();
                    if (!nuovaQuantita.isEmpty()) {
                        libro.quantita = Integer.parseInt(nuovaQuantita);
                    }

                    System.out.print("Codice ISBN (" + libro.codiceIsbn + "): ");
                    String nuovoCodiceIsbn = sc.nextLine();
                    if (!nuovoCodiceIsbn.isEmpty()) {
                        libro.codiceIsbn = nuovoCodiceIsbn;
                    }

                    // Verifica se i valori id_genere e id_genere2 esistono nella tabella generi
                    if (isValidGenere(conn, libro.idGenere) && (libro.idGenere2 == 0 || isValidGenere(conn, libro.idGenere2))) {
                        String sql = "UPDATE libri SET titolo = ?, autore = ?, id_genere = ?, id_genere2 = ?, id_casa_editrice = ?, data_pubblicazione = ?, quantita = ?, codice = ? WHERE id = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, libro.titolo);
                            pstmt.setString(2, libro.autore);
                            pstmt.setInt(3, libro.idGenere);
                            if (libro.idGenere2 == 0) {
                                pstmt.setNull(4, java.sql.Types.INTEGER);
                            } else {
                                pstmt.setInt(4, libro.idGenere2);
                            }
                            pstmt.setInt(5, libro.idCasaEd);
                            pstmt.setInt(6, libro.anno);
                            pstmt.setInt(7, libro.quantita);
                            pstmt.setString(8, libro.codiceIsbn);
                            pstmt.setInt(9, id);
                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Libro con ID " + id + " aggiornato.");
                            } else {
                                System.out.println("Nessun libro trovato con ID " + id);
                            }
                        }
                    } else {
                        System.out.println("ID Genere o ID Genere 2 non validi.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del libro: " + e.getMessage());
        }
    }
}
