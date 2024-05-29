package wiprojss24gr7.userhandling;

public class Professor extends User {
	private String fakultaet;
	private String fachBereich;
	private String buero;
	
	public Professor(int PK, String vorname, String nachname, String fakultaet, String fachBereich, String buero) {
		super(PK, vorname, nachname);
		this.fakultaet = fakultaet;
		this.fachBereich = fachBereich;
		this.buero = buero;
	}

	@Override
	public String toString() {
		return "Professor [profId=" + PK + ", fakultaet=" + fakultaet + ", fachBereich=" + fachBereich + ", buero="
				+ buero + ",\n toString()=" + super.toString() + ", getVorname()=" + getVorname() + ", getNachname()="
				+ getNachname() + ",\n getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
	public String printUserDetails() {
	    StringBuilder userDetails = new StringBuilder();
	    userDetails.append("Professor Details:\n");
	    userDetails.append("-----------------\n");
	    userDetails.append("Vorname: ").append(getVorname()).append("\n");
	    userDetails.append("Nachname: ").append(getNachname()).append("\n");
	    userDetails.append("Fakultät: ").append(fakultaet).append("\n");
	    userDetails.append("Fachbereich: ").append(fachBereich).append("\n");
	    userDetails.append("Büro: ").append(buero).append("\n\n");
	    return userDetails.toString();
	}
}
