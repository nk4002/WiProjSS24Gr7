package wiprojss24gr7.userhandling;

public class Student extends User {
	private String studiengang;
	private String firma;
	private String thema;
	private int profID;
	private boolean aktiviert;
	
	public Student(int PK, String vorname, String nachname, String studiengang, String firma, String thema, int profID, boolean aktiviert) {
		super(PK, vorname, nachname);
		this.studiengang = studiengang;
		this.firma = firma;
		this.thema = thema;
		this.aktiviert = aktiviert;
		this.setProfID(profID);
	}
	
	@Override
	public String getClassName() {
        return "Student";
    }
	
	@Override
	public String toString() {
		return "Student [studiengang=" + studiengang + ", firma=" + firma + ", thema=" + thema + ", profID=" + profID
				+ ",\n PK=" + PK + ", getStudiengang()=" + getStudiengang() + ", getFirma()=" + getFirma()
				+ ",\n getThema()=" + getThema() + ", getProfID()=" + getProfID() + ", toString()=" + super.toString()
				+ ",\n getVorname()=" + getVorname() + ", getNachname()=" + getNachname() + ", showGreetings()="
				+ showGreetings() + ",\n getPK()=" + getPK() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}

	public String printUserDetails() {
	    StringBuilder userDetails = new StringBuilder();
	    userDetails.append("--------------------\n");
	    userDetails.append("Vorname: ").append(getVorname()).append("\n");
	    userDetails.append("Nachname: ").append(getNachname()).append("\n");
	    userDetails.append("Studiengang: ").append(studiengang).append("\n");
	    userDetails.append("Firma: ").append(firma).append("\n");
	    userDetails.append("Thema: ").append(thema).append("\n");
	    return userDetails.toString();
	}
	
	public String getStudiengang() {
		return studiengang;
	}

	public void setStudiengang(String studiengang) {
		this.studiengang = studiengang;
	}

	public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	public String getThema() {
		return thema;
	}

	public void setThema(String thema) {
		this.thema = thema;
	}

	public int getProfID() {
		return profID;
	}

	public void setProfID(int profID) {
		this.profID = profID;
	}

	@Override
	public boolean isAktiviert() {
		return aktiviert;
	}

	public void setAktiviert(boolean aktiviert) {
		this.aktiviert = aktiviert;
	}
	
}
