package wiprojss24gr7.database;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
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
        String studentQuery = "SELECT MNr, Firma, Thema FROM studenten WHERE Benutzername = ? AND Passwort = ?";
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
                        logIn(mNr, "studenten");

                        if ((firma == null || firma.isEmpty()) && (thema == null || thema.isEmpty())) {
                        	logIn(mNr, "studenten");
                        	return "CardStudentErstanmeldung"; // Student found but Firma and Thema are empty
                        } else {
                            logIn(mNr, "studenten");
                            return "CardStudent"; // Student found with Firma and Thema
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
                                    rs.getInt("ProfID"));
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
    
    public static List<String> getUsers(String tableName) {
        List<String> users = new ArrayList<>();

        String sql = switch (tableName) {
            case "studenten" -> "SELECT MNr, Vorname, Name FROM studenten";
            case "professoren" -> "SELECT ProfID, Vorname, Name FROM professoren";
            default -> throw new IllegalArgumentException("Invalid table name: " + tableName);
        };

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("Vorname") + " " + rs.getString("Name");
                users.add(userName);
                if ("studenten".equals(tableName)) {
                    int mnr = rs.getInt("MNr");
                    UserService.addSL(mnr);
                } else if ("professoren".equals(tableName)) {
                    int profId = rs.getInt("ProfID");
                    UserService.addPL(profId);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return users;
    }
 
    public static List<String> getStudentenWithNoTutor() {
        List<String> users = new ArrayList<>();

        String sql = "SELECT MNr, Vorname, Name FROM studenten WHERE ProfID IS NULL";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("Vorname") + " " + rs.getString("Name");
                users.add(userName);
                int mnr = rs.getInt("MNr");
                UserService.addSLNoTutor(mnr);
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("Error fetching studenten with no tutor: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
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
                            rsStudent.getString("Thema"), rsStudent.getInt("ProfID"));
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
}
