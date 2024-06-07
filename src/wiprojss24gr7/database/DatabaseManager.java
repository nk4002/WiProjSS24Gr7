package wiprojss24gr7.database;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import wiprojss24gr7.service.UserService;
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

    public static void logIn(int PK, String userType) {
        logger.info("Versucht logIn mit PK: " + PK);
        try (Connection conn = getConnection()) {
            String query = switch (userType) {
                case "studenten" -> "SELECT * FROM studenten WHERE MNr = ?";
                case "professoren" -> "SELECT * FROM professoren WHERE ProfID = ?";
                case "ppa" -> "SELECT * FROM ppa WHERE PPAID = ?";
                default -> {
                    logger.warning("Unbekannter Benutzertyp: " + userType);
                    yield null;
                }
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
                            logger.info(userType.substring(0, 1).toUpperCase() + userType.substring(1) + " eingeloggt: " + loggedInUser.toString());
                        }
                    } else {
                        logger.warning("Benutzer nicht gefunden");
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Error während logging in: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Geändert am 29.05 von if else auf switch + check ob Aktiviert oder nicht.
    public static List<String> getUsers(String tableName, boolean noTutor) {
        List<String> users = new ArrayList<>();
        String sql = switch (tableName) {
            case "studenten" -> noTutor ? 
                "SELECT MNr, Vorname, Name, Aktiviert FROM studenten WHERE ProfID IS NULL AND Aktiviert = 0" : 
                "SELECT MNr, Vorname, Name, Aktiviert FROM studenten";
            case "professoren" -> "SELECT ProfID, Vorname, Name FROM professoren";
            default -> throw new IllegalArgumentException("Ungültiger Tabellen Name: " + tableName);
        };

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("Vorname") + " " + rs.getString("Name");

                if ("studenten".equals(tableName)) {
                    int mnr = rs.getInt("MNr");
                    if (noTutor) {
                        UserService.addSLNoTutor(mnr);
                    } else {
                        if (!rs.getBoolean("Aktiviert")) {
                            userName += " (Nicht Aktiviert!)";
                        }
                        UserService.addSL(mnr);
                    }
                } else if ("professoren".equals(tableName)) {
                    UserService.addPL(rs.getInt("ProfID"));
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
    public static void saveStudentData(User user, String company, String topic) throws SQLException {
        String query = "UPDATE studenten SET Firma = ?, Thema = ? WHERE MNr = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, company);
            stmt.setString(2, topic);
            stmt.setInt(3, user.getPK()); // PK = MNr
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.log(Level.INFO, "Studentendaten erfolgreich für Benutzer mit PK: {0} gespeichert", user.getPK());
            } else {
                logger.log(Level.WARNING, "Kein Studenteneintrag gefunden für Benutzer mit PK: {0}", user.getPK());
            }
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Datenbanktreiber nicht gefunden", e);
            throw new SQLException("Datenbanktreiber konnte nicht geladen werden", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Fehler beim Speichern der Studentendaten", e);
            throw e;
        }
    }


    public static List<Student> getAllInactiveStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT MNr, Vorname, Name, Studiengang, Firma, Thema, ProfID, Aktiviert FROM studenten WHERE Aktiviert = false";

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
}