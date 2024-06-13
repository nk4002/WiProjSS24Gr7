package wiprojss24gr7.userhandling;

import java.sql.Blob;
import java.sql.Timestamp;

public class Document {
    private int documentId;
    private Blob document;
    private int MNr;
    private String documentType;
    private String fileType;
    private Timestamp timeStamp;

    public Document() {
    }

    public Document(int documentId, Blob document, int MNr, String documentType, String fileType, Timestamp timeStamp) {
        this.documentId = documentId;
        this.document = document;
        this.MNr = MNr;
        this.documentType = documentType;
        this.fileType = fileType;
        this.timeStamp = timeStamp;
    }

    // Getter und Setter.
    public int getDokumentId() {
        return documentId;
    }

    public void setDokumentId(int dokumentId) {
        this.documentId = dokumentId;
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
        return fileType;
    }

    public void setDateiTyp(String dateiTyp) {
        this.fileType = dateiTyp;
    }

    public Timestamp getZeitStempel() {
        return timeStamp;
    }

    public void setZeitStempel(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

	@Override
	public String toString() {
		return "Document [documentId=" + documentId + ", document=" + document + ", MNr=" + MNr + ", documentType="
				+ documentType + ", fileType=" + fileType + ", timeStamp=" + timeStamp + "]";
	}
}

