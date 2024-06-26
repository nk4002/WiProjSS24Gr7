package wiprojss24gr7.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import wiprojss24gr7.database.DatabaseManager;
import wiprojss24gr7.service.DocumentService;
import wiprojss24gr7.service.UserService;
import wiprojss24gr7.userhandling.Ppa;
import wiprojss24gr7.userhandling.Professor;
import wiprojss24gr7.userhandling.Student;
import wiprojss24gr7.userhandling.User;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static JPanel contentPane;
    private static CardLayout cardLayout;
    private static JPanel cardsPanel;
    private JPanel cardLogIn;
    private JPanel cardStudent;
    private JPanel cardProfessor;
    private JPanel cardPpa;
    private JPanel cardStudentErstanmeldung;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private static JProgressBar progressBarStudent, progressBarS, progressBarPpa;
    static DefaultListModel<String> studentListPpaModel;
    static DefaultListModel<String> studentListModelNoTutor;
    static DefaultListModel<String> studentListModelProfNoTutor;
    static DefaultListModel<String> professorListModelTab2;
    static DefaultListModel<String> studentListModelProfMyStudents;
    private static JLabel loginLabel, studentErstLabel, studentBetreuerLabel, studentLabel, profLabel, ppaLabel;

  public static void main(String[] args) {
    	
	    Controller.setUIFont(new FontUIResource(new Font("Arial", Font.PLAIN, 20)));
	  
	  	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainFrame() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 800);
        setMinimumSize(new Dimension(500, 400));
        setTitle("Campus Code BPS Verwaltung");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        cardLogIn = new JPanel();
        cardStudent = new JPanel(new BorderLayout());
        cardProfessor = new JPanel(new BorderLayout());
        cardPpa = new JPanel(new BorderLayout());
        cardStudentErstanmeldung = new JPanel(new BorderLayout());

        cardsPanel.add(cardLogIn, "CardLogIn");
        cardsPanel.add(cardStudent, "CardStudent");
        cardsPanel.add(cardProfessor, "CardProfessor");
        cardsPanel.add(cardPpa, "CardPpa");
        cardsPanel.add(cardStudentErstanmeldung, "CardStudentErstanmeldung");
        
        TitledBorder titledBorderFortschritt = BorderFactory.createTitledBorder("Fortschritt");
        
        /////////////////////////////////////////////////////////
        //Code zu Login Panel
        /////////////////////////////////////////////////////////
        
        cardLogIn.setLayout(new GridBagLayout());

        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        gbcLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcLabel.anchor = GridBagConstraints.NORTH;
        gbcLabel.insets = new Insets(20, 5, 25, 5);
        loginLabel = new JLabel("Melden Sie sich mit Ihren Benutzerdaten an.", SwingConstants.CENTER);
        cardLogIn.add(loginLabel, gbcLabel);
        loginLabel.setPreferredSize(new Dimension(390, loginLabel.getPreferredSize().height));

        GridBagConstraints gbcUsername = new GridBagConstraints();
        gbcUsername.gridx = 0;
        gbcUsername.gridy = 1;
        gbcUsername.fill = GridBagConstraints.HORIZONTAL;
        gbcUsername.anchor = GridBagConstraints.NORTH;
        gbcUsername.insets = new Insets(5, 5, 5, 5);
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        cardLogIn.add(usernameField, gbcUsername);

        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.gridx = 0;
        gbcPassword.gridy = 2;
        gbcPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcPassword.anchor = GridBagConstraints.NORTH;
        gbcPassword.insets = new Insets(5, 5, 5, 5);
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        cardLogIn.add(passwordField, gbcPassword);

        GridBagConstraints gbcLoginButton = new GridBagConstraints();
        gbcLoginButton.gridx = 0;
        gbcLoginButton.gridy = 3;
        gbcLoginButton.fill = GridBagConstraints.HORIZONTAL;
        gbcLoginButton.anchor = GridBagConstraints.NORTH;
        gbcLoginButton.insets = new Insets(5, 5, 5, 5);
        loginButton = new JButton("Login");
        cardLogIn.add(loginButton, gbcLoginButton);

        loginButton.addActionListener(e -> Controller.handleLogin(e, cardLayout, cardsPanel, usernameField, passwordField));

		/////////////////////////////////////////////////////////
		//Code zu StudentErstanmeldung Panel
		/////////////////////////////////////////////////////////
		
     //Top Panel für Button und Label werden mit GridBagLayout recht und links platziert
        JPanel topPanelSE = new JPanel(new GridBagLayout());
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.insets = new Insets(5, 5, 5, 5);

        // Student Erstanmeldung Label
        studentErstLabel = new JLabel("Student Erstanmeldung");
        gbcTop.gridx = 0; 
        gbcTop.gridy = 0;
        gbcTop.anchor = GridBagConstraints.WEST; 
        topPanelSE.add(studentErstLabel, gbcTop);

        JPanel spacer = new JPanel();
        gbcTop.gridx = 1; 
        gbcTop.weightx = 1.0; 
        gbcTop.fill = GridBagConstraints.HORIZONTAL; 
        topPanelSE.add(spacer, gbcTop);

        // Info Button
        gbcTop.gridx = 2;
        gbcTop.weightx = 0.0;
        gbcTop.anchor = GridBagConstraints.EAST;
        JButton infoButtonErst = new JButton("Info");
        infoButtonErst.addActionListener(e -> {
            String message = "Willkommen zur Erstanmeldung für Studenten.\n\n"
                           + "Folgende Schritte sind erforderlich:\n"
                           + "- Geben Sie Ihre persönlichen Informationen ein.\n"
                           + "- Wählen Sie Ihre Studiengänge und Kurse aus.\n"
                           + "- Bestätigen Sie Ihre Angaben und schließen Sie die Anmeldung ab.";
            
            JOptionPane.showMessageDialog(contentPane,
                    message,
                    "Erstanmeldung Student",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        topPanelSE.add(infoButtonErst, gbcTop);

        // Abmelden Button
        gbcTop.gridx = 3;
        gbcTop.anchor = GridBagConstraints.EAST; 
        JButton abmeldenSE = new JButton("Abmelden");
        abmeldenSE.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelSE.add(abmeldenSE, gbcTop);

        cardStudentErstanmeldung.add(topPanelSE, BorderLayout.NORTH);

        //Center Panel für Unternehmen und Themenfeld
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.insets = new Insets(5, 5, 5, 5);
        gbcCenter.fill = GridBagConstraints.HORIZONTAL;

        //Unternehmen Panel
        JPanel companyPanel = new JPanel(new GridBagLayout());
        companyPanel.setBorder(BorderFactory.createTitledBorder("Unternehmen"));
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.gridwidth = 3;
        centerPanel.add(companyPanel, gbcCenter);

        GridBagConstraints gbcCompany = new GridBagConstraints();
        gbcCompany.insets = new Insets(5, 5, 5, 5);
        gbcCompany.gridx = 0;
        gbcCompany.gridy = 0;
        gbcCompany.gridwidth = 2;
        JTextField companyField = new JTextField(25);
        companyPanel.add(companyField, gbcCompany);

        //Thema Panel
        JPanel topicPanel = new JPanel(new GridBagLayout());
        topicPanel.setBorder(BorderFactory.createTitledBorder("Thema"));
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 1;
        gbcCenter.gridwidth = 3;
        centerPanel.add(topicPanel, gbcCenter);

        GridBagConstraints gbcTopic = new GridBagConstraints();
        gbcTopic.insets = new Insets(5, 5, 5, 5);
        gbcTopic.gridx = 0;
        gbcTopic.gridy = 0;
        gbcTopic.gridwidth = 2;
        gbcTopic.fill = GridBagConstraints.BOTH;
        JTextArea topicField = new JTextArea(5, 25);
        topicField.setLineWrap(true);
        topicField.setWrapStyleWord(true);
        JScrollPane topicScrollPane = new JScrollPane(topicField);
        topicPanel.add(topicScrollPane, gbcTopic);

        //Bestätigen Button
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 2;
        gbcCenter.gridwidth = 3;
        gbcCenter.anchor = GridBagConstraints.CENTER;
        JButton confirmButton = new JButton("Stammdaten bestätigen");
        confirmButton.addActionListener(e -> Controller.handleConfirm(companyField, topicField));
        centerPanel.add(confirmButton, gbcCenter);

        cardStudentErstanmeldung.add(centerPanel, BorderLayout.CENTER);


        /////////////////////////////////////////////////////////
        // Code zu Student Panel
        /////////////////////////////////////////////////////////

        JPanel topPanelS = new JPanel(new BorderLayout());

        studentLabel = new JLabel("Student Label");
        topPanelS.add(studentLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton infoButton = new JButton("Info");
        infoButton.addActionListener(e -> {
            // Create and show the info popup dialog
            String message = "Wählen Sie das hochzuladende Dokument mithilfe des\n"
                           + "Dropdown-Menüs aus. Bei Anklicken des Buttons öffnet sich ein\n"
                           + "Explorer. Wählen Sie die hochzuladende Datei aus.";
            
            JOptionPane.showMessageDialog(contentPane,
                    message,
                    "Hilfe",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(infoButton);

        JButton abmeldenS = new JButton("Abmelden");
        abmeldenS.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        buttonPanel.add(abmeldenS);

        topPanelS.add(buttonPanel, BorderLayout.EAST);

        cardProfessor.add(topPanelS, BorderLayout.NORTH);

        cardStudent.add(topPanelS, BorderLayout.NORTH);

        JPanel centerPanelS = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentBetreuerLabel = new JLabel("Dein Name und Betreuer");
        titlePanel.add(studentBetreuerLabel);

        centerPanelS.add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel label = new JLabel("Dokument Hochladen");
        contentPanel.add(label, gbc);

        gbc.gridy++;

        String optionsS[] = {"Studierenden Bericht", "Tätigkeitsnachweis", "Vortrag Bericht"};
        JComboBox<String> optionsComboBoxStud = new JComboBox<>(optionsS);
        Dimension comboBoxDimension = new Dimension(300, optionsComboBoxStud.getPreferredSize().height);
        optionsComboBoxStud.setPreferredSize(comboBoxDimension);
        contentPanel.add(optionsComboBoxStud, gbc);

        gbc.gridy++;

        JButton uploadButton = new JButton("Dokument hochladen: ");
        uploadButton.setPreferredSize(comboBoxDimension);
        uploadButton.addActionListener(e -> Controller.handleUpload(optionsComboBoxStud));
        contentPanel.add(uploadButton, gbc);

        gbc.gridy++;

        progressBarS = new JProgressBar();
        progressBarS.setStringPainted(true);
        progressBarS.setPreferredSize(new Dimension(350, 40));
        progressBarS.setBorder(titledBorderFortschritt);
        contentPanel.add(progressBarS, gbc);

        centerPanelS.add(contentPanel, BorderLayout.CENTER);

        cardStudent.add(centerPanelS, BorderLayout.CENTER);

        /////////////////////////////////////////////////////////
        // Code zu Professor Panel
        /////////////////////////////////////////////////////////

        JPanel topPanelP = new JPanel(new BorderLayout());

        profLabel = new JLabel("Professor Label");
        topPanelP.add(profLabel, BorderLayout.WEST);

        // Create a panel for the buttons on the right
        JPanel buttonPanelP = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Info Button
        JButton infoButtonProfessor = new JButton("Info");
        infoButtonProfessor.addActionListener(e -> {
            String message = "Willkommen zur Professor-Verwaltung.\n\n"
                           + "Hier können Sie folgende Aufgaben ausführen:\n"
                           + "- Verwalten Sie die Ihnen zugewiesenen Studenten.\n"
                           + "- Überprüfen Sie den Fortschritt und die Aktivitäten Ihrer Studenten.\n"
                           + "- Laden Sie erforderliche Dokumente über das Dropdown-Menü herunter.";
            
            JOptionPane.showMessageDialog(topPanelP,
                    message,
                    "Professor-Verwaltung",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanelP.add(infoButtonProfessor);

        // Abmelden Button
        JButton abmeldenP = new JButton("Abmelden");
        abmeldenP.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        buttonPanelP.add(abmeldenP);

        topPanelP.add(buttonPanelP, BorderLayout.EAST);

        cardProfessor.add(topPanelP, BorderLayout.NORTH);

        JTabbedPane tabbedPaneP = new JTabbedPane();
        tabbedPaneP.setFont(new Font("Arial", Font.PLAIN, 18)); 
        tabbedPaneP.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        Controller.tabSwitchListener(tabbedPaneP);

        //Tab 1 
        JPanel tab1PanelProf = new JPanel(new BorderLayout());

        studentListModelProfNoTutor = new DefaultListModel<>();
        JList<String> studentListProfNoTutor = new JList<>(studentListModelProfNoTutor);
        studentListProfNoTutor.setBorder(BorderFactory.createTitledBorder("Studentenliste"));
        studentListProfNoTutor.setPreferredSize(new Dimension(250, 400)); 
        JScrollPane listScrollPaneProf = new JScrollPane(studentListProfNoTutor);
        tab1PanelProf.add(listScrollPaneProf, BorderLayout.WEST);
        


        JTextArea textAreaProfNoTutor = new JTextArea();
        textAreaProfNoTutor.setBorder(BorderFactory.createTitledBorder("Details"));
        JScrollPane textScrollPane1 = new JScrollPane(textAreaProfNoTutor);
        tab1PanelProf.add(textScrollPane1, BorderLayout.CENTER);
        
        studentListProfNoTutor.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 1) {
	                Controller.handleMouseClick(studentListProfNoTutor, textAreaProfNoTutor, "studentListNoTutor");
	            }
	        }
	    });

        JPanel bottomPanel1 = new JPanel(new BorderLayout());

        JButton übernehmenButton = new JButton("Student Übernehmen");
        
        übernehmenButton.addActionListener(e -> {
            Controller.setBetreuer();
            Controller.updateLists(studentListModelProfNoTutor, studentListModelProfMyStudents);
        });
        
        bottomPanel1.add(übernehmenButton, BorderLayout.CENTER);
        
        tab1PanelProf.add(bottomPanel1, BorderLayout.SOUTH);
        tabbedPaneP.addTab("Studentenliste", tab1PanelProf);

        //Tab 2
        JPanel tab2PanelProf = new JPanel(new BorderLayout());

	     studentListModelProfMyStudents = new DefaultListModel<>();
	     JList<String> myStudentsListProf = new JList<>(studentListModelProfMyStudents);
	     myStudentsListProf.setBorder(BorderFactory.createTitledBorder("Meine Betreuungen"));
	     myStudentsListProf.setPreferredSize(new Dimension(250, 400));
	     JScrollPane listScrollPane2 = new JScrollPane(myStudentsListProf);
	     tab2PanelProf.add(listScrollPane2, BorderLayout.WEST);
	
	     JTextArea textAreaProfMyStudents = new JTextArea();
	     textAreaProfMyStudents.setBorder(BorderFactory.createTitledBorder("Details"));
	     JScrollPane textScrollPane2 = new JScrollPane(textAreaProfMyStudents);
	     tab2PanelProf.add(textScrollPane2, BorderLayout.CENTER);
	
	     JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	
	     progressBarPpa = new JProgressBar();
	     progressBarPpa.setPreferredSize(new Dimension(350, 40));
	     progressBarPpa.setStringPainted(true);
	     progressBarPpa.setBorder(titledBorderFortschritt);
	     bottomPanel.add(progressBarPpa);
	     
	     JComboBox<String> comboBoxProf = Controller.createOptionsComboBox();
	     bottomPanel.add(comboBoxProf);
	     
	     myStudentsListProf.addMouseListener(new MouseAdapter() {
	         @Override
	         public void mouseClicked(MouseEvent e) {
	             if (e.getClickCount() == 1) {
	                 Controller.handleMouseClick(myStudentsListProf, textAreaProfMyStudents, "professorListMyStudents");
	             }
	         }
	     });
	
	     JComboBox<String> comboBoxUtil = Controller.createOptionsComboBox();
	     
	     // Buttons
	     JButton downloadButtonProf = new JButton("Download Dokument");
	     downloadButtonProf.addActionListener(e -> Controller.handleDownloadButtonClick(comboBoxProf));
	     JButton uploadButtonP = new JButton("Upload Besucherbericht");
	     uploadButtonP.addActionListener(e -> Controller.handleUpload(comboBoxUtil));
	     bottomPanel.add(downloadButtonProf);
	     bottomPanel.add(uploadButtonP);
	
	     tab2PanelProf.add(bottomPanel, BorderLayout.SOUTH);
	
	     tabbedPaneP.addTab("Meine Betreuungen", tab2PanelProf);
	
        cardProfessor.add(tabbedPaneP, BorderLayout.CENTER);

	    //////////////////////////////////////////////////////////////////////////////////
        //Code zu PPA
        //////////////////////////////////////////////////////////////////////////////////
        
        JPanel topPanelPpa = new JPanel(new BorderLayout());
        topPanelPpa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ppaLabel = new JLabel("Ppa Label");
        topPanelPpa.add(ppaLabel, BorderLayout.WEST);

        JPanel buttonPanelPpaTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton infoButtonPpa = new JButton("Info");
        infoButtonPpa.addActionListener(e -> {
            String message = "Von hier aus können Sie folgende Aktionen durchführen:\n\n"
                           + "- Überprüfen Sie den Fortschritt und die Aktivierung von Studenten.\n"
                           + "- Weisen Sie Studenten bestimmten Professoren zu.\n"
                           + "- Verwenden Sie das Dropdown-Menü, um verschiedene Dokumententypen auszuwählen.\n"
                           + "- Klicken Sie auf den 'Download' Button, um das ausgewählte Dokument herunterzuladen.";
            
            JOptionPane.showMessageDialog(topPanelPpa,
                    message,
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanelPpaTop.add(infoButtonPpa);

        JButton abmeldenPpa = new JButton("Abmelden");
        abmeldenPpa.setPreferredSize(new Dimension(140, 40)); // Set preferred size
        abmeldenPpa.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        buttonPanelPpaTop.add(abmeldenPpa);

        topPanelPpa.add(buttonPanelPpaTop, BorderLayout.EAST);
	
	    cardPpa.add(topPanelPpa, BorderLayout.NORTH);
	
	    JTabbedPane tabbedPanePpa = new JTabbedPane();
	    tabbedPanePpa.setFont(new Font("Arial", Font.PLAIN, 18)); 
	    tabbedPanePpa.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	
	    // Tab 1: Studentenverwaltung
	    studentListPpaModel = new DefaultListModel<>();
	    JList<String> studentListPpa = new JList<>(studentListPpaModel);
	    studentListPpa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    studentListPpa.setBorder(BorderFactory.createTitledBorder("Studenten"));
	    studentListPpa.setPreferredSize(new Dimension(300, 400)); 
	
	    JTextArea textAreaStudentPpa = new JTextArea();
	    textAreaStudentPpa.setEditable(false);
	    textAreaStudentPpa.setBorder(BorderFactory.createTitledBorder("Student Details"));
	    JScrollPane scrollPaneStudentPpa = new JScrollPane(textAreaStudentPpa);
	
	    studentListPpa.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 1) {
	                Controller.handleMouseClick(studentListPpa, textAreaStudentPpa, "studentList");
	            }
	        }
	    });
	
	    JPanel buttonPanelPpa = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    progressBarStudent = new JProgressBar(0, 100);
	    progressBarStudent.setStringPainted(true);
	    progressBarStudent.setString("");
	    progressBarStudent.setBorder(titledBorderFortschritt);
	    progressBarStudent.setPreferredSize(new Dimension(350, 40)); 
	
	    JComboBox<String> optionsComboBoxPpa = Controller.createOptionsComboBox();
	    buttonPanelPpa.add(optionsComboBoxPpa);
	
	    JButton downloadButton = new JButton("Download Doc");
	    downloadButton.addActionListener(e -> Controller.handleDownloadButtonClick(optionsComboBoxPpa));
	    JButton buttonAktivieren = new JButton("Aktivieren");
	
	    optionsComboBoxPpa.setPreferredSize(new Dimension(200, 35)); 
	    downloadButton.setPreferredSize(new Dimension(160, 35)); 
	    buttonAktivieren.setPreferredSize(new Dimension(160, 35)); 
	
	    buttonPanelPpa.add(progressBarStudent);
	    buttonPanelPpa.add(optionsComboBoxPpa);
	    buttonPanelPpa.add(downloadButton);
	    buttonPanelPpa.add(buttonAktivieren);
	
	    buttonAktivieren.addActionListener(e -> {
	        Student selectedStudent = (Student) Student.getSelectedUser();
	        if (selectedStudent.isAktiviert() || selectedStudent.getFirma() == null || selectedStudent.getFirma().isEmpty()) {
	        	JOptionPane.showMessageDialog(null, "Studenten ohne Eintragung Firma und Thema können nicht aktiviert werden.", "Fehler", JOptionPane.WARNING_MESSAGE);
	        	return;
	        } else {
	             selectedStudent.setAktiviert(true);
	            try {
	                DatabaseManager.activateStudent();
	                Controller.updateLists(studentListPpaModel, professorListModelTab2, studentListModelNoTutor);
	            } catch (ClassNotFoundException e1) {
	                e1.printStackTrace();
	            }
	        }
	    });
	
	    JPanel tabPanelPpa = new JPanel(new BorderLayout(10, 10));
	    tabPanelPpa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    tabPanelPpa.add(new JScrollPane(studentListPpa), BorderLayout.WEST);
	    tabPanelPpa.add(scrollPaneStudentPpa, BorderLayout.CENTER);
	    tabPanelPpa.add(buttonPanelPpa, BorderLayout.SOUTH);
	
	    tabbedPanePpa.addTab("Studentenverwaltung", tabPanelPpa);
	    Controller.tabSwitchListener(tabbedPanePpa);
	
	    // Tab 2: Betreuerverwaltung
	    studentListModelNoTutor = new DefaultListModel<>();
	    JList<String> studentListTab2Ppa = new JList<>(studentListModelNoTutor);
	    studentListTab2Ppa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    studentListTab2Ppa.setBorder(BorderFactory.createTitledBorder("Studenten ohne Betreuer"));
	    studentListTab2Ppa.setPreferredSize(new Dimension(250, 400)); 
	
	    JTextArea studentDetailTextAreaTab2 = new JTextArea();
	    studentDetailTextAreaTab2.setEditable(false);
	    studentDetailTextAreaTab2.setBorder(BorderFactory.createTitledBorder("Student Details"));
	    JScrollPane studentDetailScrollPaneTab2Ppa = new JScrollPane(studentDetailTextAreaTab2);
	    studentDetailScrollPaneTab2Ppa.setPreferredSize(new Dimension(350, 200)); 
	
	    studentListTab2Ppa.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 1) {
	                Controller.handleMouseClick(studentListTab2Ppa, studentDetailTextAreaTab2, "studentListNoTutor");
	            }
	        }
	    });
	
	    professorListModelTab2 = new DefaultListModel<>();
	    JList<String> professorListTab2Ppa = new JList<>(professorListModelTab2);
	    professorListTab2Ppa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    professorListTab2Ppa.setBorder(BorderFactory.createTitledBorder("Professoren"));
	    professorListTab2Ppa.setPreferredSize(new Dimension(200, 400)); 
	     
	    JTextArea professorDetailTextAreaTab2 = new JTextArea();
	    professorDetailTextAreaTab2.setEditable(false);
	    professorDetailTextAreaTab2.setBorder(BorderFactory.createTitledBorder("Professor Details"));
	    JScrollPane professorDetailScrollPaneTab2Ppa = new JScrollPane(professorDetailTextAreaTab2);
	    professorDetailScrollPaneTab2Ppa.setPreferredSize(new Dimension(350, 200)); 
	
	    professorListTab2Ppa.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 1) {
	                Controller.handleMouseClick(professorListTab2Ppa, professorDetailTextAreaTab2, "professorList");
	            }
	        }
	    });
	
	    JButton zuweiseButton = new JButton("Zuweisen");
	    zuweiseButton.setPreferredSize(new Dimension(140, 35)); 
	
	    zuweiseButton.addActionListener(e -> {
	        Controller.setBetreuer();
	        Controller.updateLists(studentListPpaModel, professorListModelTab2, studentListModelNoTutor);
	    });
	
	    JPanel studentPanelTab2Ppa = new JPanel(new BorderLayout(10, 10));
	    studentPanelTab2Ppa.add(new JScrollPane(studentListTab2Ppa), BorderLayout.CENTER);
	    studentPanelTab2Ppa.add(studentDetailScrollPaneTab2Ppa, BorderLayout.SOUTH);
	
	    JPanel professorPanelTab2Ppa = new JPanel(new BorderLayout(10, 10));
	    professorPanelTab2Ppa.add(new JScrollPane(professorListTab2Ppa), BorderLayout.CENTER);
	    professorPanelTab2Ppa.add(professorDetailScrollPaneTab2Ppa, BorderLayout.SOUTH);
	
	    JPanel mainPanelTab2Ppa = new JPanel(new GridLayout(1, 2, 10, 10));
	    mainPanelTab2Ppa.add(studentPanelTab2Ppa);
	    mainPanelTab2Ppa.add(professorPanelTab2Ppa);
	
	    JPanel tabPanel2Ppa = new JPanel(new BorderLayout(10, 10));
	    tabPanel2Ppa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    tabPanel2Ppa.add(mainPanelTab2Ppa, BorderLayout.CENTER);
	    tabPanel2Ppa.add(zuweiseButton, BorderLayout.SOUTH);
	
	    tabbedPanePpa.addTab("Betreuerverwaltung", tabPanel2Ppa);
	
	    abmeldenPpa.addActionListener(e -> {
	        Controller.handleLogout(e, cardLayout, cardsPanel);
	        Controller.clearTextAreas(textAreaStudentPpa, professorDetailTextAreaTab2, studentDetailTextAreaTab2);
	    });
	
	    cardPpa.add(tabbedPanePpa, BorderLayout.CENTER);
	
	    tabbedPaneP.addTab("Studentenliste", tab1PanelProf);
	    cardProfessor.add(tabbedPaneP, BorderLayout.CENTER);
     
        contentPane.add(cardsPanel, BorderLayout.CENTER);
    }

    private static class Controller {
    	
    	//War zum Debuggen in Fertigem Programm nicht Nötig
        //private static final Logger logger = Logger.getLogger(MainFrame.class.getName());

    	//Setzt alle Fonts für alle Objekte (Stack Overflow regelt)
    	public static void setUIFont(FontUIResource font) {
            UIDefaults defaults = UIManager.getDefaults();
            for (Object key : defaults.keySet()) {
                if (key instanceof String && ((String) key).endsWith(".font")) {
                    defaults.put(key, font);
                }
            }
        }
    	
    	//Methode regelt alle Wichtigen Dinge beim Login Checkt in DB nach Nutzer und Zeigt Angemessene KArte.
        public static void handleLogin(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel, JTextField usernameField, JPasswordField passwordField) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            usernameField.setText("");
            passwordField.setText("");

            String role = DatabaseManager.getRole(username, password);
            if (role == null) {
                loginLabel.setText("Ungültiger Nutzername oder Passwort.");
                return;
            }
            
            showGreetings();
            
            if ("nicht Aktiviert".equals(role)) {
                loginLabel.setText("Account noch nicht Aktiviert");
            } else {
                cardLayout.show(cardsPanel, role);
                User loggedInUser = User.getLoggedInuser();
                if (loggedInUser instanceof Ppa) {
                    controlPopulateList(studentListPpaModel, professorListModelTab2, studentListModelNoTutor);
                } else if (loggedInUser instanceof Professor) {
                    controlPopulateList(studentListModelProfNoTutor, studentListModelProfMyStudents);
                } else if (loggedInUser instanceof Student) {
                    updateProgressBars();
                }
            }
        }

        @SafeVarargs
        private static void controlPopulateList(DefaultListModel<String>... models) {
            User loggedInUser = User.getLoggedInuser();
            if (loggedInUser instanceof Ppa) {
                UserService.populateUserList(models[0], "studenten", false);
                UserService.populateUserList(models[1], "professoren", false);
                UserService.populateUserList(models[2], "studenten", true);
            } else if (loggedInUser instanceof Professor) {
                UserService.populateUserList(models[0], "studenten", true);
                UserService.populateUserList(models[1], "meinestudenten", false);
            }
        }

        //Setzt eingeloggten Nutzer auf Null wechselt Karte und schließt Verbindung zu DB.
        public static void handleLogout(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel) {
            User.setLoggedInuser(null);
            cardLayout.show(cardsPanel, "CardLogIn");
            DatabaseManager.closeConnection();
            UserService.delAll();
            resetProgressBar(progressBarStudent);
            resetProgressBar(progressBarPpa);
            resetProgressBar(progressBarS);
        }

        //Setzt Selektierten Nutzer
        public static void handleMouseClick(JList<String> list, JTextArea textArea, String listIdentifier) {
            try {
                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex != -1) {
                    int selectedPk = switch (listIdentifier) {
                        case "studentList" -> UserService.getSLIndex(selectedIndex);
                        case "studentListNoTutor" -> UserService.getSLNoTutorIndex(selectedIndex);
                        case "professorList" -> UserService.getPLIndex(selectedIndex);
                        case "professorListMyStudents" -> UserService.getSLMyStudentsIndex(selectedIndex);
                        default -> -1;
                    };
                    
                    if (selectedPk != -1) {
                        User selectedUser = DatabaseManager.getUserByPk(selectedPk);
                        if ("professorList".equals(listIdentifier)) {
                            User.setSelectedProf(selectedUser);
                        } else {
                            User.setSelectedUser(selectedUser);
                        }
                        String userDetails = selectedUser.printUserDetails();
                        textArea.setText(userDetails);
                        updateProgressBars();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @SafeVarargs
        public static void updateLists(DefaultListModel<String>... listModels) {
            UserService.delAll();
            controlPopulateList(listModels);
        }

        public static void clearTextAreas(JTextArea... textAreas) {
            for (JTextArea textArea : textAreas) {
                textArea.setText("");
            }
        }

        public static void tabSwitchListener(JTabbedPane tabbedPane) {
            tabbedPane.addChangeListener(e -> User.setSelectedUser(null));
        }

        //Setzt Label Oben rechts Auf Angemeldet als: Nutzer Name
        public static void showGreetings() {
            User loggedInUser = User.getLoggedInuser();
            String role = loggedInUser.getClassName();
            switch (role) {
                case "Student" -> {
                    String greetings = loggedInUser.showGreetings() + " (MNr: " + ((Student) loggedInUser).getPK() + ")";
                    studentLabel.setText(greetings);
                    studentErstLabel.setText(greetings);
                    try {
                        String professorName = DatabaseManager.getProfessorName(((Student) loggedInUser).getProfID());
                        studentBetreuerLabel.setText(professorName.isEmpty() ? "Dein Betreuer: Noch kein Betreuer vorhanden." : "Dein Betreuer: " + professorName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                case "Professor" -> profLabel.setText(loggedInUser.showGreetings() + " (ProfID: " + ((Professor) loggedInUser).getPK() + ")");
                case "Ppa" -> ppaLabel.setText(loggedInUser.showGreetings());
            }
        }

        public static void setBetreuer() {
            User loggedInUser = User.getLoggedInuser();
            if (loggedInUser instanceof Professor || loggedInUser instanceof Ppa) {
                User selectedProf = loggedInUser instanceof Ppa ? User.getSelectedProf() : loggedInUser;
                User selectedStudent = User.getSelectedUser();
                if (selectedProf != null && selectedStudent != null) {
                    int profId = selectedProf.getPK();
                    int studentMnr = selectedStudent.getPK();
                    ((Student) selectedStudent).setProfID(profId);
                    DatabaseManager.setProfID(profId, studentMnr);
                }
            }
        }

        public static void handleConfirm(JTextField companyField, JTextArea topicField) {
            String company = companyField.getText();
            String topic = topicField.getText();
            if (company.isEmpty() || topic.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DatabaseManager.saveStudentData(User.getLoggedInuser(), company, topic);
			JOptionPane.showMessageDialog(null, "Stammdaten erfolgreich gespeichert", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
			cardLayout.show(cardsPanel, "CardLogIn");
        }

        public static JComboBox<String> createOptionsComboBox() {
            return new JComboBox<>(new String[]{"Besuchs Bericht", "Studierenden Bericht", "Tätigkeitsnachweis", "Vortrag Bericht"});
        }

        public static void handleDownloadButtonClick(JComboBox<String> optionsComboBox) {
            try {
                String documentType = getDocType(optionsComboBox.getSelectedIndex() + (User.getLoggedInuser() instanceof Student ? 2 : 1));
                DocumentService.downloadDocument(documentType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Öffnet Dateisucher.
        public static void handleUpload(JComboBox<String> optionsComboBox) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String documentType = getDocType(optionsComboBox.getSelectedIndex() + (User.getLoggedInuser() instanceof Student ? 2 : 1));
                if (file.getName().toLowerCase().endsWith(".pdf")) {
                    try {
                        DocumentService.uploadDocument(file, documentType);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nur PDF's erlaubt.", "File Upload Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private static String getDocType(int documentNr) {
            return switch (documentNr) {
                case 1 -> "BesucherBericht";
                case 2 -> "StudBericht";
                case 3 -> "TaetigkeitsNw";
                case 4 -> "VortrBericht";
                default -> null;
            };
        }

        public static void updateProgressBars() {
            User loggedInUser = User.getLoggedInuser();
            int returnValue = 0;
            try {
                if (loggedInUser instanceof Ppa || loggedInUser instanceof Professor) {
                    returnValue = DatabaseManager.countAssignedDocuments(User.getSelectedUser().getPK());
                    updateProgress(progressBarStudent, returnValue);
                    updateProgress(progressBarPpa, returnValue);
                } else {
                    returnValue = DatabaseManager.countAssignedDocuments(loggedInUser.getPK());
                    updateProgress(progressBarS, returnValue);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private static void updateProgress(JProgressBar progressBar, int fortschritt) {
            progressBar.setValue(fortschritt * 25); 
            progressBar.setString(fortschritt + "/4");
        }
        
        private static void resetProgressBar(JProgressBar progressBar) {
            progressBar.setValue(0);
            progressBar.setString("0/4");
        }
   }
}






