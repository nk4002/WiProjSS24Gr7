package wiprojss24gr7.userhandling;

public class Ppa extends User{
	private String land;
	private String einrichtung;
	private User User;
	
	
	
	public Ppa(int PK, String vorname, String nachname, String land, String einrichtung) {
		super(PK, vorname, nachname);
		this.land = land;
		this.einrichtung = einrichtung;
		
	}
	
	@Override
	public String getClassName() {
        return "Ppa";
    }
	
	@Override
	public String showGreetings() {
		return "Eingeloggt als PPA der HFT Stuttgart";
	}

	public User getUser() {
        return User;
    }

    public void setUser(User user) {
        this.User = User;
    }

	@Override
	public String toString() {
		return "PPA [ppaId=" + PK + ", land=" + land + ", einrichtung=" + einrichtung + ", toString()="
				+ super.toString() + ", getVorname()=" + getVorname() + ", getNachname()=" + getNachname()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
}
