package wiprojss24gr7.userhandling;

public class Student extends User {
	private String studiengang;
	private String firma;
	private String thema;
	
	public Student(int PK, String vorname, String nachname, String studiengang, String firma, String thema) {
		super(PK, vorname, nachname);
		this.studiengang = studiengang;
		this.firma=firma;
		this.thema=thema;
	}

	@Override
	public String toString() {
		return "Student [mnr=" + PK + ", studiengang=" + studiengang + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
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
}
