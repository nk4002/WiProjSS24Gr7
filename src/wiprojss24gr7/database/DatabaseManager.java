package wiprojss24gr7.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;


import wiprojss24gr7.service.UserService;
import wiprojss24gr7.userhandling.Document;
import wiprojss24gr7.userhandling.Ppa;
import wiprojss24gr7.userhandling.Professor;
import wiprojss24gr7.userhandling.Student;
import wiprojss24gr7.userhandling.User;
import wiprojss24gr7.util.SecureUtil;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://3.69.96.96:80/";
    private static final String DB_NAME = "db7";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USERNAME = "db7";
    private static final String PASSWORD = "!db7.seo24?SS1";
    
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());

    private static Connection connection;

    private DatabaseManager() {
    	
    }

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        if (connection == null || connection.isClosed()) {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    //Geht Durch einzelne Tabellen uns returned Die Rolle als String damit man im MainFrame die richtige Karte gezeigt wird
    public static String getRole(String username, String password) {
        String hashedPassword;
        try {
            hashedPassword = SecureUtil.hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println(hashedPassword);
        String studentQuery = "SELECT MNr, Firma, Thema, Aktiviert FROM studenten WHERE Benutzername = ? AND Passwort = ?";
        String professorQuery = "SELECT ProfID FROM professoren WHERE Benutzername = ? AND Passwort = ?";
        String ppaQuery = "SELECT PPAID FROM ppa WHERE Benutzername = ? AND Passwort = ?";

        try (Connection conn = getConnection()) {
            try (PreparedStatement statementStudent = conn.prepareStatement(studentQuery);
                 PreparedStatement statementProfessor = conn.prepareStatement(professorQuery);
                 PreparedStatement statementPPA = conn.prepareStatement(ppaQuery)) {

                statementStudent.setString(1, username);
                statementStudent.setString(2, hashedPassword);
                statementProfessor.setString(1, username);
                statementProfessor.setString(2, hashedPassword);
                statementPPA.setString(1, username);
                statementPPA.setString(2, hashedPassword);

                try (ResultSet rsStudent = statementStudent.executeQuery()) {
                    if (rsStudent.next()) {
                        int mNr = rsStudent.getInt("MNr");
                        String firma = rsStudent.getString("Firma");
                        String thema = rsStudent.getString("Thema");
                        boolean aktiviert = rsStudent.getBoolean("Aktiviert");
                        logIn(mNr, "studenten");

                        if (isEmpty(firma) && isEmpty(thema)) {
                            logIn(mNr, "studenten");
                            return "CardStudentErstanmeldung"; //Student ohne Firma und Thema
                        } else if (!aktiviert) { //aktiviert == false
                            return "nicht Aktiviert";
                        } else {
                            logIn(mNr, "studenten");
                            return "CardStudent"; //Student aktiviert und mit Thema u. Firma.
                        }
                    }
                }

                try (ResultSet rsProfessor = statementProfessor.executeQuery()) {
                    if (rsProfessor.next()) {
                        int ProfID = rsProfessor.getInt("ProfID");
                        logIn(ProfID, "professoren");
                        return "CardProfessor";
                    }
                }

                try (ResultSet rsPPA = statementPPA.executeQuery()) {
                    if (rsPPA.next()) {
                        int ppaId = rsPPA.getInt("PPAID");
                        logIn(ppaId, "ppa");
                        return "CardPpa";
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Setzt den eingeloggten Nutzer
    public static void logIn(int PK, String userType) {
        logger.info("Versucht logIn mit PK: " + PK);
        try (Connection conn = getConnection()) {
            String query = switch (userType) {
                case "studenten" -> "SELECT * FROM studenten WHERE MNr = ?";
                case "professoren" -> "SELECT * FROM professoren WHERE ProfID = ?";
                case "ppa" -> "SELECT * FROM ppa WHERE PPAID = ?";
                default -> null;
            };

            if (query == null) return;

            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, PK);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        User loggedInUser = switch (userType) {
                            case "studenten" -> new Student(PK, rs.getString("Vorname"), rs.getString("Name"),
                                    rs.getString("Studiengang"), rs.getString("Firma"), rs.getString("Thema"),
                                    rs.getInt("ProfID"), rs.getBoolean("Aktiviert"));
                            case "professoren" -> new Professor(PK, rs.getString("Name"), rs.getString("Vorname"),
                                    rs.getString("Fakultaet"), rs.getString("Fachbereich"), rs.getString("Buero"));
                            case "ppa" -> new Ppa(PK, null, null, rs.getString("Land"), rs.getString("Einrichtung"));
                            default -> null;
                        };

                        if (loggedInUser != null) {
                            User.setLoggedInuser(loggedInUser);
                        }
                    } else {
                        logger.warning("Benutzer nicht gefunden");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Error während logging in: " + e.getMessage());
        }
    }

    //Geändert am 16.06 switch + check für listModel 
    public static List<String> getUsers(String tableName, boolean noTutor) {
        List<String> users = new ArrayList<>();
        String sql;
        
        switch (tableName) {
            case "studenten" -> sql = noTutor ? 
                "SELECT MNr, Vorname, Name, Aktiviert FROM studenten WHERE ProfID IS NULL AND Aktiviert = 1" : 
                "SELECT MNr, Vorname, Name, Aktiviert FROM studenten";
            case "professoren" -> sql = "SELECT ProfID, Vorname, Name FROM professoren";
            case "meinestudenten" -> {
                int profId = User.getLoggedInuser().getPK();
                sql = "SELECT MNr, Vorname, Name, Aktiviert FROM studenten WHERE ProfID = " + profId;
            }
            default -> throw new IllegalArgumentException("Ungültiger Tabellen Name: " + tableName);
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("Vorname") + " " + rs.getString("Name");

                switch (tableName) {
                    case "studenten" -> {
                        int mnr = rs.getInt("MNr");
                        if (noTutor) {
                            UserService.addSLNoTutor(mnr);
                        } else {
                            if (!rs.getBoolean("Aktiviert")) {
                                userName += " (Nicht Aktiviert!)";
                            }
                            UserService.addSL(mnr);
                        }
                    }
                    case "professoren" -> UserService.addPL(rs.getInt("ProfID"));
                    case "meinestudenten" -> {
                        int mnr = rs.getInt("MNr");
                        UserService.addSLMyStudents(mnr);
                    }
                }

                users.add(userName);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return users;
    }


    //Utilitäre Methode um Dopplung zu vermeiden.
    //Platzhalter
    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    //Geht durch Tabelle Studenten und setzt Stud. mit passender MNr auf Aktiviert
    public static void activateStudent() throws ClassNotFoundException {
        int mNr = User.getSelectedUser().getPK();
        String sql = "UPDATE studenten SET Aktiviert = true WHERE MNr = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mNr);
            int rowsUpdated = pstmt.executeUpdate();
            
            if (rowsUpdated > 0) {
                logger.info("Student erfolgreich Aktiviert MNr: " + mNr);
            } else {
                logger.warning("Kein Student mit angegebener MNr: " + mNr);
            }
        } catch (SQLException e) {
            logger.severe("Fehler bei Aktivierung: " + e.getMessage());
        }
    }

    public static User getUserByPk(int pk) throws ClassNotFoundException, SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmtStudent = conn.prepareStatement("SELECT * FROM studenten WHERE MNr = ?");
             PreparedStatement pstmtProfessor = conn.prepareStatement("SELECT * FROM professoren WHERE ProfID = ?")) {

            pstmtStudent.setInt(1, pk);
            try (ResultSet rsStudent = pstmtStudent.executeQuery()) {
                if (rsStudent.next()) {
                    return new Student(pk, rsStudent.getString("Vorname"), rsStudent.getString("Name"),
                            rsStudent.getString("Studiengang"), rsStudent.getString("Firma"),
                            rsStudent.getString("Thema"), rsStudent.getInt("ProfID"),
                            rsStudent.getBoolean("Aktiviert"));
                }
            }

            pstmtProfessor.setInt(1, pk);
            try (ResultSet rsProfessor = pstmtProfessor.executeQuery()) {
                if (rsProfessor.next()) {
                    return new Professor(pk, rsProfessor.getString("Vorname"), rsProfessor.getString("Name"),
                            rsProfessor.getString("Fakultaet"), rsProfessor.getString("FachBereich"),
                            rsProfessor.getString("Buero"));
                }
            }
        }
        return null;
    }
    
    //Sucht nach Student mit MNr und setzt ProfID auf Parameter.
    public static void setProfID(int profId, int MNr) {
        String updateQuery = "UPDATE studenten SET ProfID = ? WHERE Mnr = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            
            pstmt.setInt(1, profId);
            pstmt.setInt(2, MNr);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("ProfID erfolgreich in DB gesetzt: " + MNr);
            } else {
                logger.warning("Kein Student in DB gefunden: " + MNr);
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Genereller Fehler bei Setzung ProfID: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Neue Methode, um die Professor-Daten abzurufen
    public static List<String[]> getProfessorData() {
        List<String[]> professorData = new ArrayList<>();
        String sql = "SELECT CONCAT(Vorname, ' ', Name) AS Vollname, ProfID FROM professoren";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String vollname = rs.getString("Vollname");
                String profId = rs.getString("ProfID");
                professorData.add(new String[]{vollname, profId});
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Error fetching professor data: " + e.getMessage());
            e.printStackTrace();
        }

        return professorData;
    }
    
    // Methode um Firma und Thema zu speichern
    public static void saveStudentData(User user, String company, String topic) {
        String query = "UPDATE studenten SET Firma = ?, Thema = ? WHERE MNr = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, company);
            stmt.setString(2, topic);
            stmt.setInt(3, user.getPK()); 
            stmt.executeUpdate(); 
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Student> getAllInactiveStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT MNr, Vorname, Name, Studiengang, Firma, Thema, ProfID, Aktiviert FROM studenten WHERE ProfID IS NULL And Aktiviert = 1";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("MNr"),
                    rs.getString("Vorname"),
                    rs.getString("Name"),
                    rs.getString("Studiengang"),
                    rs.getString("Firma"),
                    rs.getString("Thema"),
                    rs.getInt("ProfID"),
                    rs.getBoolean("Aktiviert")
                );
                students.add(student);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return students;
    }
    
    public static void assignStudentToProfessor(int studentMNr, int professorId) {
        String updateQuery = "UPDATE studenten SET Aktiviert = true, ProfID = ? WHERE MNr = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setInt(1, professorId);
            pstmt.setInt(2, studentMNr);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Student erfolgreich zugewiesen. MNr: " + studentMNr);
            } else {
                logger.warning("Kein Student in der Datenbank gefunden mit der MNr: " + studentMNr);
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Fehler beim Zuweisen des Studenten: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static List<Student> getProfessorStudents(int profId) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT MNr, Vorname, Name, Studiengang, Firma, Thema, ProfID, Aktiviert FROM studenten WHERE ProfID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, profId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                        rs.getInt("MNr"),
                        rs.getString("Vorname"),
                        rs.getString("Name"),
                        rs.getString("Studiengang"),
                        rs.getString("Firma"),
                        rs.getString("Thema"),
                        rs.getInt("ProfID"),
                        rs.getBoolean("Aktiviert")
                    );
                    students.add(student);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return students;
    }
    
    //Methode sucht nach einem Dokument anhand des Typs und der Nutzer-ID des ausgewählten Nutzers.
    public static Optional<Document> getDocumentByTypeAndUser(String documentType) {
        int userId = User.getSelectedUser().getPK();
        String query = "SELECT * FROM dokumente WHERE DokumentTyp = ? AND MNr = ?";
        try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, documentType);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                Document document = new Document(
                    rs.getInt("dokumentid"),
                    rs.getBlob("Dokument"),
                    rs.getInt("MNr"),
                    rs.getString("DokumentTyp"),
                    rs.getString("DateiTyp"),
                    rs.getTimestamp("Zeitstempel")
                );
                logger.info("Dokument gefunden mit Typ: " + documentType + " für Benutzer: " + userId);
                return Optional.of(document);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Fehler beim Abrufen des Dokuments mit Typ: " + documentType + " für Benutzer: " + userId + ". Ausnahme: " + e.getMessage());
        }
        logger.warning("Dokument nicht gefunden mit Typ: " + documentType + " für Benutzer: " + userId);
        return Optional.empty();
    }

    //Methode Speichert Dokument in DB bei Überschneidung mit bereits
    //vorhandenem Paar aus MNr und DokumentTyp wird das Existierende Element Überschrieben.
    public static boolean saveDocumentToDatabase(Document document) throws ClassNotFoundException {
        String selectSql = "SELECT COUNT(*) FROM dokumente WHERE Mnr = ? AND DokumentTyp = ?";
        String updateSql = "UPDATE dokumente SET Dokument = ?, DateiTyp = ?, Zeitstempel = ? WHERE Mnr = ? AND DokumentTyp = ?";
        String insertSql = "INSERT INTO dokumente (Mnr, DokumentTyp, Dokument, DateiTyp, Zeitstempel) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement selectPs = connection.prepareStatement(selectSql);
             PreparedStatement updatePs = connection.prepareStatement(updateSql);
             PreparedStatement insertPs = connection.prepareStatement(insertSql)) {

            selectPs.setInt(1, document.getMNr());
            selectPs.setString(2, document.getDocumentType());

            ResultSet resultSet = selectPs.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count > 0) {
                    updatePs.setBlob(1, document.getDocument());
                    updatePs.setString(2, document.getDateiTyp());
                    updatePs.setTimestamp(3, document.getZeitStempel());
                    updatePs.setInt(4, document.getMNr());
                    updatePs.setString(5, document.getDocumentType());

                    int rowsUpdated = updatePs.executeUpdate();
                    return rowsUpdated > 0;
                } else {
                    insertPs.setInt(1, document.getMNr());
                    insertPs.setString(2, document.getDocumentType());
                    insertPs.setBlob(3, document.getDocument());
                    insertPs.setString(4, document.getDateiTyp());
                    insertPs.setTimestamp(5, document.getZeitStempel());

                    int rowsInserted = insertPs.executeUpdate();
                    return rowsInserted > 0;
                }
            }
        } catch (SQLException ex) {
            logger.severe("Fehler beim Speichern des Dokuments in der Datenbank: " + ex.getMessage());
        }
        return false;
    }

    //Methode zum Erstellen eines Blob aus einem Byte-Array.
    public static Blob createBlob(byte[] data) throws SQLException, ClassNotFoundException {
        try (Connection connection = getConnection()) {
            //Erstellen eines Blob-Objekts und Schreiben der Daten aus einem Byte-Array.
            Blob blob = connection.createBlob();
            blob.setBytes(1, data);
            return blob;
        }
    }
    
    public static String getProfessorName(int profID) throws ClassNotFoundException {
        String professorenName = "";

        String sql = "SELECT Name, Vorname FROM professoren WHERE ProfID = ?";

        try (
            Connection verbindung = getConnection();
            PreparedStatement pstmt = verbindung.prepareStatement(sql);
        ) {
            pstmt.setInt(1, profID);

            ResultSet ergebnis = pstmt.executeQuery();

            if (ergebnis.next()) {
                String name = ergebnis.getString("Vorname");
                String vorname = ergebnis.getString("Name");
                professorenName = vorname + " " + name;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return professorenName;
    }

    //returned die Anzahl an Dokumenten mit der gelichen MNr um die Progress Bar richtig zu füllen
    public static int countAssignedDocuments(int studentenMnr) throws ClassNotFoundException {
        int anzahl = 0;
        String sql = "SELECT COUNT(*) FROM dokumente WHERE Mnr = ?";

        try (Connection verbindung = getConnection();
             PreparedStatement ps = verbindung.prepareStatement(sql)) {
            ps.setInt(1, studentenMnr);
            ResultSet ergebnis = ps.executeQuery();
            if (ergebnis.next()) {
                anzahl = ergebnis.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return anzahl;
    }
}

