package wiprojss24gr7.userhandling;

import java.util.ArrayList;
import java.util.List;

public class User {
	protected int PK;
	private String Vorname;
	private String Nachname;
	protected static User loggedInUser;
	
	private static User selectedUser;
	private static User selectedProf;
	
	
	public User(int PK, String vorname, String nachname) {
		this.PK=PK;
		this.Vorname = vorname;
		this.Nachname = nachname;
	}
	
	public String getClassName() {
        return null;
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

	public static User getSelectedUser() {
		return selectedUser;
	}

	public static void setSelectedUser(User selectedUser) {
		User.selectedUser = selectedUser;
	}

	public static User getSelectedProf() {
		return selectedProf;
	}

	public static void setSelectedProf(User selectedProf) {
		User.selectedProf = selectedProf;
	}
	
	public String printUserDetails() {
		return null;
	}

	public boolean isAktiviert() {
		return false;
	}
	
}

