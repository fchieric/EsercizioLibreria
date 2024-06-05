package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Utente {

    private static String url = "jdbc:mysql://127.0.0.1:3306/Libreria";
    private static ArrayList<Utente> elencoUtenti = new ArrayList<>();
    private int id;
    private String nome, cognome, email, telefono;
    private Date data_registrazione;

    // Constructor with parameters
    public Utente(String nome, String cognome, String email, String telefono, Date data_registrazione) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.data_registrazione = data_registrazione;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getData_registrazione() {
        return data_registrazione;
    }

    public void setData_registrazione(Date data_registrazione) {
        this.data_registrazione = data_registrazione;
    }

    public static void insertUtente(Scanner sc) {
        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
            System.out.println("Inserisci i dati del nuovo utente:");

            System.out.print("Nome: ");
            String nome = sc.nextLine();

            System.out.print("Cognome: ");
            String cognome = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            System.out.print("Telefono: ");
            String telefono = sc.nextLine();

            // For the registration date, you can use the current date
            Date dataRegistrazione = new Date();

            // Create a new Utente object using the collected data
            Utente nuovoUtente = new Utente(nome, cognome, email, telefono, dataRegistrazione);

            // Now proceed with the database operations
            String sql = "INSERT INTO Utente (nome, cognome, email, telefono, data_registrazione) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nuovoUtente.getNome());
                pstmt.setString(2, nuovoUtente.getCognome());
                pstmt.setString(3, nuovoUtente.getEmail());
                pstmt.setString(4, nuovoUtente.getTelefono());
                pstmt.setDate(5, new java.sql.Date(nuovoUtente.getData_registrazione().getTime()));
                pstmt.executeUpdate();
                System.out.println("Nuovo utente inserito: " + nuovoUtente);
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento dell'utente: " + e.getMessage());
        }
    }

    public static void showUtente() {
        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
            String sql = "SELECT * FROM Utente";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nome: " + rs.getString("nome"));
                    System.out.println("Cognome: " + rs.getString("cognome"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Telefono: " + rs.getString("telefono"));
                    System.out.println("Data di registrazione: " + rs.getDate("data_registrazione"));
                    System.out.println("------------");
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero degli utenti: " + e.getMessage());
        }
    }

    public static boolean authenticateUser(Scanner sc) {
        System.out.print("Inserisci l'email: ");
        String email = sc.nextLine();

        System.out.print("Inserisci il telefono: ");
        String telefono = sc.nextLine();

        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
            String sql = "SELECT * FROM Utente WHERE email = ? AND telefono = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, telefono);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Autenticazione riuscita.");
                        return true;
                    } else {
                        System.out.println("Autenticazione fallita. Email o telefono non corretti.");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante l'autenticazione: " + e.getMessage());
            return false;
        }
    }

    public static void showLoanHistory(int userId) {
        try (Connection conn = DriverManager.getConnection(url, "fabi", "1234")) {
            String sql = "SELECT * FROM Prestiti WHERE id_utente = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        System.out.println("ID Prestito: " + rs.getInt("id"));
                        System.out.println("ID Libro: " + rs.getInt("id_libro"));
                        System.out.println("Data Prestito: " + rs.getDate("data_prestito"));
                        System.out.println("Data Restituzione: " + rs.getDate("data_restituzione"));
                        System.out.println("------------");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero dello storico dei prestiti: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", data_registrazione=" + data_registrazione +
                '}';
    }
}