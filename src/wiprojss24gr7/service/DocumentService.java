package wiprojss24gr7.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import wiprojss24gr7.database.DatabaseManager;
import wiprojss24gr7.gui.MainFrame;
import wiprojss24gr7.userhandling.Document;
import wiprojss24gr7.userhandling.Student;
import wiprojss24gr7.userhandling.User;

public class DocumentService {
    
    private static final Logger logger = Logger.getLogger(DocumentService.class.getName());
    
	 // Methode lädt ein Dokument mit der angegebenen ID herunter und speichert es im Download-Ordner des Benutzers.
	 // Sollte meines Wissens OS-unabhängig sein, wenn nicht, dann krachts wie Sau.
	 public static void downloadDocument(String documentType) {
	     // Der Pfad zum Download-Ordner des Benutzers wird erstellt.
	     String downloadsFolderPath = System.getProperty("user.home") + File.separator + "Downloads";
	     File downloadsFolder = new File(downloadsFolderPath);
	     // Falls der Ordner nicht existiert, wird er erstellt.
	     if (!downloadsFolder.exists() && !downloadsFolder.mkdirs()) {
	         logger.warning("Konnte Downloads-Ordner nicht erstellen.");
	         return;
	     }
	     // Dynamische Namensgebung für heruntergeladene Dokumente.
	     String filePath = downloadsFolderPath + File.separator + User.getSelectedUser().getNachname() + User.getSelectedUser().getVorname() + documentType + ".pdf";
	
	     Optional<Document> optionalDocument = DatabaseManager.getDocumentByTypeAndUser(documentType);
	     optionalDocument.ifPresent(document -> {
	         try (FileOutputStream fos = new FileOutputStream(filePath)) {
	             //Das Dokument wird in die Datei geschrieben.
	             fos.write(toByteArray(document.getDocument()));
	             logger.info("Dokument erfolgreich heruntergeladen und gespeichert: " + filePath);
	             //Erfolg Popup
	             JOptionPane.showMessageDialog(null, "Dokument erfolgreich heruntergeladen und gespeichert unter:\n" + filePath, "Download Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
	         } catch (IOException | SQLException ex) {
	             logger.severe("Fehler beim Schreiben des Dokuments in die Datei: " + ex.getMessage());
	         }
	     });
	     optionalDocument.orElseGet(() -> {
	         //Misserfolg
	         logger.warning("Kein Dokument dieser Art bei User Vorhanden Vorhanden");
	         JOptionPane.showMessageDialog(null, "Kein Dokument dieser Art bei "+ User.getSelectedUser().getVorname() +
	        		 " " + User.getSelectedUser().getNachname() + " Vorhanden Vorhanden", "Dokument nicht gefunden", JOptionPane.WARNING_MESSAGE);
	         return null;
	     });
	 }
	 
	 //Methode zum Hochladen eines Dokuments.
	 public static void uploadDocument(File file, String documentType) throws ClassNotFoundException, SQLException {
	     if (file != null) {
	         try (FileInputStream fis = new FileInputStream(file)) {
	             //Datei wrid in Byte-Array gelesen.
	             byte[] fileData = new byte[(int) file.length()];
	             fis.read(fileData);

	             //Byte-Array wird in Blob konvertiert.
	             Blob documentBlob = DatabaseManager.createBlob(fileData);

	             //Check ob Eingeloggter User Student oder Prof
	             int userPK;
	             if (User.getLoggedInuser() instanceof Student) {
	                 userPK = User.getLoggedInuser().getPK();
	             } else {
	                 userPK = User.getSelectedUser().getPK();
	             }
	             
	             Document document = new Document(
	            		    0, // Unwichtig da PK automatisch Inkrementiert wird.
	            		    documentBlob,
	            		    userPK,
	            		    documentType,
	            		    getFileExtension(file),
	            		    new Timestamp(System.currentTimeMillis())
	             );
	             
	             // Document-Objekt in die Datenbank speichern
	             if (DatabaseManager.saveDocumentToDatabase(document)) {
	                 // Erfolgsmeldung anzeigen
	                 JOptionPane.showMessageDialog(null, "Dokument erfolgreich hochgeladen und gespeichert.", "Upload Erfolgreich", JOptionPane.INFORMATION_MESSAGE);
	                 logger.info("Dokument erfolgreich hochgeladen und gespeichert: " + file.getAbsolutePath());
	             } else {
	                 JOptionPane.showMessageDialog(null, "Fehler beim Hochladen des Dokuments.", "Upload Fehler", JOptionPane.ERROR_MESSAGE);
	                 logger.severe("Fehler beim Hochladen des Dokuments: " + file.getAbsolutePath());
	             }
	         } catch (IOException ex) {
	             logger.severe("Fehler beim Lesen der Datei: " + ex.getMessage());
	             JOptionPane.showMessageDialog(null, "Fehler beim Lesen der Datei.", "Upload Fehler", JOptionPane.ERROR_MESSAGE);
	         }
	     } else {
	         logger.info("Dateiauswahl abgebrochen.");
	     }
	 }

	 //Hilfsmethode zur Ermittlung der Dateierweiterung(.pdf etc).
	 private static String getFileExtension(File file) {
	     String fileName = file.getName();
	     int lastIndexOfDot = fileName.lastIndexOf('.');
	     if (lastIndexOfDot == -1) {
	         return "";
	     }
	     return fileName.substring(lastIndexOfDot + 1);
	 }

	
	 // Konvertiert einen Blob in ein Byte-Array.
	 public static byte[] toByteArray(Blob blob) throws SQLException, IOException {
	     try (InputStream is = blob.getBinaryStream(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	         byte[] buffer = new byte[1024];
	         int bytesRead;
	         while ((bytesRead = is.read(buffer)) != -1) {
	             baos.write(buffer, 0, bytesRead);
	         }
	         logger.info("Blob erfolgreich in Byte-Array konvertiert. Gelesene Bytes: " + baos.size());
	         return baos.toByteArray();
	     }
	 }

}

