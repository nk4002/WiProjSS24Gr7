package wiprojss24gr7.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import wiprojss24gr7.database.DatabaseManager;
import wiprojss24gr7.gui.MainFrame;
import wiprojss24gr7.userhandling.Document;

public class DocumentService {
    
    private static final Logger logger = Logger.getLogger(DocumentService.class.getName());
    
    //Methode lädt ein Dokument mit der angegebenen ID herunter und speichert es im Download-Ordner des Benutzers.
    //Sollte meines Wissens OS-unabhängig sein, wenn nicht, dann krachts wie Sau.
    public static void downloadDocument(int dokumentId) {
        String benutzerVerzeichnis = System.getProperty("user.home");
        String downloadsOrdnerPfad = benutzerVerzeichnis + File.separator + "Downloads";
        File downloadsOrdner = new File(downloadsOrdnerPfad);
        if (!downloadsOrdner.exists()) {
            if (!downloadsOrdner.mkdirs()) {
                logger.warning("Konnte Downloads-Ordner nicht erstellen.");
                return;
            }
        }
        String dateiPfad = downloadsOrdnerPfad + File.separator + "Beispiel.pdf";
        //Optional<Document> repräsentiert potenziell leeres Dokument aus der DB.
        Optional<Document> optionalDocument = DatabaseManager.getDocumentById(dokumentId);
        optionalDocument.ifPresent(dokument -> {
            try {
                byte[] byteArray = toByteArray(dokument.getDocument());
                try (FileOutputStream fos = new FileOutputStream(dateiPfad)) {
                    fos.write(byteArray);
                    logger.info("Dokument erfolgreich heruntergeladen und gespeichert: " + dateiPfad);
                    //Popup-Fenster mit dem Download-Pfad wird angezeigt.
                    JOptionPane.showMessageDialog(null, "Dokument erfolgreich heruntergeladen und gespeichert unter:\n" + dateiPfad, "Download Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    logger.severe("Fehler beim Schreiben des Dokuments in die Datei: " + ex.getMessage());
                }
            } catch (SQLException | IOException ex) {
                logger.severe("Fehler beim Konvertieren des Dokuments: " + ex.getMessage());
            }
        });
        optionalDocument.orElseGet(() -> {
            logger.warning("Dokument mit ID " + dokumentId + " nicht gefunden.");
            //Popup-Fenster wird angezeigt, wenn das Dokument nicht gefunden wurde.
            JOptionPane.showMessageDialog(null, "Dokument mit ID " + dokumentId + " nicht gefunden.", "Dokument nicht gefunden", JOptionPane.WARNING_MESSAGE);
            return null;
        });
    }
   
    //Konvertiert einen Blob in einen Byte-Array.
    public static byte[] toByteArray(Blob blob) throws SQLException, IOException {
        try (InputStream is = blob.getBinaryStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytesRead=0;

            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }
            
            logger.info("Blob erfolgreich in Byte-Array konvertiert. Gelesene Bytes: " + totalBytesRead);

            return baos.toByteArray();
        }
    }
}

