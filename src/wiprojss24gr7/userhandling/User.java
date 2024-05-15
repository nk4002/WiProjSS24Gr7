package wiprojss24gr7.userhandling;

import java.util.ArrayList;
import java.util.List;

public class User {
	protected int PK;
	private String Vorname;
	private String Nachname;
	protected static User loggedInUser;
	
	
	public User(int PK, String vorname, String nachname) {
		this.PK=PK;
		this.Vorname = vorname;
		this.Nachname = nachname;
	}

	@Override
	public String toString() {
		return "User [Vorname=" + Vorname + ", Nachname=" + Nachname + "]";
	}

	public static User getLoggedInuser() {
		return loggedInUser;
	}

	public static void setLoggedInuser(User loggedInUser) {
		User.loggedInUser = loggedInUser;
	}
	public static void logout() {
        loggedInUser = null;
    }
	public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
	
	public String getVorname() {
		return Vorname;
	}

	public void setVorname(String vorname) {
		Vorname = vorname;
	}

	public String getNachname() {
		return Nachname;
	}

	public void setNachname(String nachname) {
		Nachname = nachname;
	}
	public String showGreetings() {
		return "Angemeldet Als: " + loggedInUser.getVorname() +" "+ loggedInUser.getNachname();
	}

	public int getPK() {
		return PK;
	}
	
}

