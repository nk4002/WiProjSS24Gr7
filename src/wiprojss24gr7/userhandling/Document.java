package wiprojss24gr7.userhandling;

import java.sql.Blob;
import java.sql.Timestamp;

public class Document {
	private int dokumentId;
	private Blob document;
	private int MNr;
	private String documentType;
	private String dateiTyp;
	private Timestamp zeitStempel;
	
	public Document() {
    }

    public Document(int dokumentId, Blob document, int MNr, String documentType, String dateiTyp, Timestamp zeitStempel) {
        this.dokumentId = dokumentId;
        this.document = document;
        this.MNr = MNr;
        this.documentType = documentType;
        this.dateiTyp = dateiTyp;
        this.zeitStempel = zeitStempel;
    }

	public int getDokumentId() {
		return dokumentId;
	}


	public Blob getDocument() {
		return document;
	}

	public int getMNr() {
		return MNr;
	}

	public String getDocumentType() {
		return documentType;
	}

	public String getDateiTyp() {
		return dateiTyp;
	}

	public Timestamp getZeitStempel() {
		return zeitStempel;
	}

	public void setZeitStempel(Timestamp zeitStempel) {
		this.zeitStempel = zeitStempel;
	}
    
	
}
