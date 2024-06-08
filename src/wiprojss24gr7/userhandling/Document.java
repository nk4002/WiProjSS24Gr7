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

    // Getters and setters
    public int getDokumentId() {
        return dokumentId;
    }

    public void setDokumentId(int dokumentId) {
        this.dokumentId = dokumentId;
    }

    public Blob getDocument() {
        return document;
    }

    public void setDocument(Blob document) {
        this.document = document;
    }

    public int getMNr() {
        return MNr;
    }

    public void setMNr(int MNr) {
        this.MNr = MNr;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDateiTyp() {
        return dateiTyp;
    }

    public void setDateiTyp(String dateiTyp) {
        this.dateiTyp = dateiTyp;
    }

    public Timestamp getZeitStempel() {
        return zeitStempel;
    }

    public void setZeitStempel(Timestamp zeitStempel) {
        this.zeitStempel = zeitStempel;
    }

	@Override
	public String toString() {
		return "Document [dokumentId=" + dokumentId + ", document=" + document + ", MNr=" + MNr + ", documentType="
				+ documentType + ", dateiTyp=" + dateiTyp + ", zeitStempel=" + zeitStempel + "]";
	}
    
}

