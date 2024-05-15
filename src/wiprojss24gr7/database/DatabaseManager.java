package wiprojss24gr7.database;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            return null; // Error
        }
        
        System.out.println(hashedPassword);
        String studentQuery = "SELECT MNr FROM studenten WHERE Benutzername = ? AND Passwort = ?";
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
                        logIn(mNr, "studenten");
                        return "CardStudent"; // Student gefunden
                    }
                }

                try (ResultSet rsProfessor = statementProfessor.executeQuery()) {
                    if (rsProfessor.next()) {
                    	int ProfID = rsProfessor.getInt("ProfID");
                    	logIn(ProfID, "professoren"); // Platzhalter
                        return "CardProfessor"; // Professor gefunden
                    }
                }

                try (ResultSet rsPPA = statementPPA.executeQuery()) {
                    if (rsPPA.next()) {
                    	int ppaId = rsPPA.getInt("PPAID");
                    	logIn(ppaId, "ppa"); // Platzhalter
                        return "CardPpa"; // PPA gefunden
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null; // keinen Treffer erzielt
    }
    
    
    public static void logIn(int PK, String userType) {
    	System.out.println(PK);
    	try (Connection conn = getConnection()) {
    	    String query;
    	    //Erstes Switch um query zu setzen
    	    switch (userType) {
    	        case "studenten":
    	            query = "SELECT * FROM studenten WHERE MNr = ?";
    	            break;
    	        case "professoren":
    	            query = "SELECT * FROM professoren WHERE ProfID = ?";
    	            break;
    	        case "ppa":
    	            query = "SELECT * FROM ppa WHERE PPAID = ?";
    	            break;
    	        default:
    	            System.out.println("Unbekannter Benutzertyp");
    	            return;
    	    }
    	    
    	    //Zweites Switch um eingeloggten User zu setzen
    	    try (PreparedStatement statement = conn.prepareStatement(query)) {
    	        statement.setInt(1, PK);
    	        try (ResultSet rs = statement.executeQuery()) {
    	            if (rs.next()) {
    	                switch (userType) {
    	                    case "studenten":
    	                        // Erstellt Student
    	                        Student student = new Student(
    	                            PK,
    	                            rs.getString("Vorname"),
    	                            rs.getString("Name"),
    	                            rs.getString("Studiengang"),
    	                            rs.getString("Firma"),
    	                            rs.getString("Thema")
    	                        );
    	                        User.setLoggedInuser(student);
    	                        System.out.println("Student eingeloggt: " + student.toString());
    	                        break;
    	                    case "professoren":
    	                        // Erstellt Professor
    	                        Professor professor = new Professor(
    	                            PK,
    	                            rs.getString("Name"),
    	                            rs.getString("Vorname"),
    	                            rs.getString("Fakultaet"),
    	                            rs.getString("Fachbereich"),
    	                            rs.getString("Buero")
    	                        );
    	                        User.setLoggedInuser(professor);
    	                        System.out.println("Professor eingeloggt: " + professor.toString());
    	                        break;
    	                    case "ppa":
    	                        // erstellt ppa
    	                        Ppa ppa = new Ppa(
    	                            PK,
    	                            null,
    	                            null,
    	                            rs.getString("Land"),
    	                            rs.getString("Einrichtung")
    	                        );	
    	                        User.setLoggedInuser(ppa);
    	                        System.out.println("PPA eingeloggt");
    	                        break;
    	                }
    	            } else {
    	                System.out.println("Benutzer nicht gefunden");
    	            }
    	        }
    	    }
    	} catch (SQLException | ClassNotFoundException e) {
    	    e.printStackTrace();
    	}

    }
    
}
