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
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private JPanel cardLogIn;
    private JPanel cardStudent;
    private JPanel cardProfessor;
    private JPanel cardPpa;
    private JPanel cardStudentErstanmeldung;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JProgressBar progressBarStudent;
    DefaultListModel<String> studentListPpaModel;
    DefaultListModel<String> studentListModelTab2Ppa;
    DefaultListModel<String> professorListModelTab2;
    private static JLabel loginLabel, studentLabel, profLabel, ppaLabel;

  public static void main(String[] args) {
    	
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
        setBounds(100, 100, 1000, 800);
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
        
        /////////////////////////////////////////////////////////
        //Code zu Login Panel
        /////////////////////////////////////////////////////////
        
        //Komponenten von cardLogIn Panel werden hinzugefügt
        
        cardLogIn.setLayout(new GridBagLayout());

        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        gbcLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcLabel.anchor = GridBagConstraints.NORTH;
        gbcLabel.insets = new Insets(20, 5, 25, 5); 
        loginLabel = new JLabel("Melden sie sich mit Ihren Benutzerdaten an.", SwingConstants.CENTER); 
        cardLogIn.add(loginLabel, gbcLabel);
        loginLabel.setPreferredSize(new Dimension(300, loginLabel.getPreferredSize().height));

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


        loginButton.addActionListener(e -> Controller.handleLogin(e, cardLayout, cardsPanel, usernameField, passwordField, studentListPpaModel, professorListModelTab2, studentListModelTab2Ppa));

        /////////////////////////////////////////////////////////
        //Code zu StudentErstanmeldung Panel
        /////////////////////////////////////////////////////////

        //Top Panel für Button und Label werden mit GridBagLayout recht und links platziert
		JPanel topPanelSE = new JPanel(new GridBagLayout());
		GridBagConstraints gbcTop = new GridBagConstraints();
		gbcTop.insets = new Insets(5, 5, 5, 5);

		//Student Label
		gbcTop.gridx = 0;
		gbcTop.gridy = 0;
		gbcTop.anchor = GridBagConstraints.WEST;
		JLabel studentELabel = new JLabel("Student Label");
		topPanelSE.add(studentELabel, gbcTop);

		//Abmelden Button
		gbcTop.gridx = 1;
		gbcTop.gridy = 0;
		gbcTop.weightx = 1.0; // Rückt den Button nach rechts ein
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

		//Unternehmen Feld
		gbcCenter.gridx = 0;
		gbcCenter.gridy = 0;
		gbcCenter.anchor = GridBagConstraints.WEST;
		JLabel companyLabel = new JLabel("Unternehmen:");
		centerPanel.add(companyLabel, gbcCenter);

		gbcCenter.gridx = 1;
		gbcCenter.gridy = 0;
		gbcCenter.gridwidth = 2;
		JTextField companyField = new JTextField(20);
		centerPanel.add(companyField, gbcCenter);

		//Thema Feld
		gbcCenter.gridx = 0;
		gbcCenter.gridy = 1;
		gbcCenter.gridwidth = 1;
		gbcCenter.anchor = GridBagConstraints.NORTHWEST;
		JLabel topicLabel = new JLabel("Thema:");
		centerPanel.add(topicLabel, gbcCenter);

		gbcCenter.gridx = 1;
		gbcCenter.gridy = 1;
		gbcCenter.gridwidth = 2;
		gbcCenter.fill = GridBagConstraints.BOTH;
		JTextArea topicField = new JTextArea(5, 20);
		topicField.setLineWrap(true);
		topicField.setWrapStyleWord(true);
		JScrollPane topicScrollPane = new JScrollPane(topicField);
		centerPanel.add(topicScrollPane, gbcCenter);

		// bestätigen Button
		gbcCenter.gridx = 0;
		gbcCenter.gridy = 2;
		gbcCenter.gridwidth = 3;
		gbcCenter.anchor = GridBagConstraints.CENTER;
		JButton confirmButton = new JButton("Stammdaten bestätigen");
		confirmButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        Controller.handleConfirm(companyField, topicField);
		    }
		});
		centerPanel.add(confirmButton, gbcCenter);

		cardStudentErstanmeldung.add(centerPanel, BorderLayout.CENTER);

        /////////////////////////////////////////////////////////
        // Code zu Student Panel
        /////////////////////////////////////////////////////////

		JPanel topPanelS = new JPanel(new BorderLayout());

        studentLabel = new JLabel("Student Label");
        topPanelS.add(studentLabel, BorderLayout.WEST);

		JButton abmeldenS = new JButton("Abmelden");
		abmeldenS.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
		topPanelS.add(abmeldenS, BorderLayout.EAST);

		cardStudent.add(topPanelS, BorderLayout.NORTH);

		JPanel centerPanelS = new JPanel(new GridBagLayout());
		GridBagConstraints gbcCenter1 = new GridBagConstraints();
		gbcCenter1.insets = new Insets(5, 5, 5, 5);
		gbcCenter1.fill = GridBagConstraints.HORIZONTAL;

		// Name und Betreuer Tabelle
		String[][] data = {{"Student 1", "Betreuer 1"}};
		String[] columnNames = {"Dein Name", "Dein Betreuer"};
		JTable nameTable = new JTable(data, columnNames);
		nameTable.setFont(new Font("Arial", Font.PLAIN, 14));
		nameTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		nameTable.setRowHeight(25);

		gbcCenter1.gridx = 0;
		gbcCenter1.gridy = 0;
		gbcCenter1.gridwidth = 2;
		centerPanelS.add(nameTable.getTableHeader(), gbcCenter1);

		gbcCenter1.gridy = 1;
		centerPanelS.add(nameTable, gbcCenter1);

		gbcCenter1.gridwidth = 1;
		gbcCenter1.gridx = 0;
		gbcCenter1.gridy = 2;
		gbcCenter1.anchor = GridBagConstraints.WEST;

		JPanel checkBoxPanel = new JPanel(new GridLayout(4, 1));

		JComboBox<String> optionsComboBoxStud = Controller.createOptionsComboBox();
		
		checkBoxPanel.add(optionsComboBoxStud);

		centerPanelS.add(checkBoxPanel, gbcCenter1);

		// Dokument Hochladen Button
		gbcCenter1.gridx = 0;
		gbcCenter1.gridy = 3;
		gbcCenter1.gridwidth = 2;
		gbcCenter1.anchor = GridBagConstraints.CENTER;

		JButton uploadButton = new JButton("Dokument hochladen");
		uploadButton.addActionListener(e -> Controller.handleUpload(optionsComboBoxStud));
		

		uploadButton.setPreferredSize(new Dimension(200, 40));
		centerPanelS.add(uploadButton, gbcCenter1);

		cardStudent.add(centerPanelS, BorderLayout.CENTER);

        /////////////////////////////////////////////////////////
        // Code zu Professor Panel
        /////////////////////////////////////////////////////////

        JPanel topPanelP = new JPanel(new BorderLayout());

        profLabel = new JLabel("Professor Label");
        topPanelP.add(profLabel, BorderLayout.WEST);

        JButton abmeldenP = new JButton("Abmelden");
        abmeldenP.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelP.add(abmeldenP, BorderLayout.EAST);

        cardProfessor.add(topPanelP, BorderLayout.NORTH);

        JTabbedPane tabbedPaneP = new JTabbedPane();
        Controller.tabSwitchListener(tabbedPaneP);

        // Tab 1 mit einer Tabelle
        JPanel tab1Panel = new JPanel(new BorderLayout());
        String[] spaltenNamen = {"Vorname und Name", "MNr", "Firma", "Thema"};

        DefaultTableModel tableModel = new DefaultTableModel(spaltenNamen, 0);
        JTable tabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        tab1Panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPaneP.addTab("Studentenliste", tab1Panel);

        // Tab 2 mit einer Tabelle für betreute Studenten
        JPanel tab2Panel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel2 = new DefaultTableModel(spaltenNamen, 0);
        JTable tabelle2 = new JTable(tableModel2);
        JScrollPane scrollPane2 = new JScrollPane(tabelle2);
        tab2Panel.add(scrollPane2, BorderLayout.CENTER);

        tabbedPaneP.addTab("Meine Betreuungen", tab2Panel);

        cardProfessor.add(tabbedPaneP, BorderLayout.CENTER);

        // Hinzufügen des Zuweisen-Buttons
        JButton zuweisenButton = new JButton("Student betreuen");
        zuweisenButton.addActionListener(e -> Controller.handleAssignButton(tabelle, tableModel));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(zuweisenButton);
        tab1Panel.add(buttonPanel, BorderLayout.SOUTH);

        cardProfessor.add(tabbedPaneP, BorderLayout.CENTER);

        // Daten für die Tabellen laden
        Controller.loadInactiveStudentDataIntoTable(tableModel);
        Controller.loadInactiveStudentDataIntoTable(tableModel2);

        /////////////////////////////////////////////////////////
        // Code zu Ppa Panel
        /////////////////////////////////////////////////////////

        //Geändert am 01.07 Gui Style + fix von Textfeld Größe
        //Top Panel für Button und Label werden mit BorderLayout recht und links platziert.
        JPanel topPanelPpa = new JPanel(new BorderLayout());
        topPanelPpa.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ppaLabel = new JLabel("Ppa Label");
        ppaLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanelPpa.add(ppaLabel, BorderLayout.WEST);

        JButton abmeldenPpa = new JButton("Abmelden");
        abmeldenPpa.setPreferredSize(new Dimension(120, 30));
        topPanelPpa.add(abmeldenPpa, BorderLayout.EAST);

        cardPpa.add(topPanelPpa, BorderLayout.NORTH);

        JTabbedPane tabbedPanePpa = new JTabbedPane();
        tabbedPanePpa.setFont(new Font("Arial", Font.PLAIN, 12));

        //Tab 1: Studentenverwaltung
        studentListPpaModel = new DefaultListModel<>();
        JList<String> studentListPpa = new JList<>(studentListPpaModel);
        studentListPpa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentListPpa.setFont(new Font("Arial", Font.PLAIN, 12));
        studentListPpa.setBorder(BorderFactory.createTitledBorder("Studenten"));
        studentListPpa.setPreferredSize(new Dimension(250, 300));//Ich Setze die größe einfach direkt selber Swing ist ein Mülleimer sonst.

        JTextArea textAreaStudentPpa = new JTextArea();
        textAreaStudentPpa.setEditable(false);
        textAreaStudentPpa.setFont(new Font("Arial", Font.PLAIN, 12));
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
        progressBarStudent.setString("Beispiel");
        progressBarStudent.setPreferredSize(new Dimension(200, 20));

        JComboBox<String> optionsComboBoxPpa = Controller.createOptionsComboBox();
        buttonPanelPpa.add(optionsComboBoxPpa);
        
        JButton downloadButton = new JButton("Download Doc");
        downloadButton.addActionListener(e -> Controller.handleDownloadButtonClick(optionsComboBoxPpa));
        JButton buttonAktivieren = new JButton("Aktivieren");

        optionsComboBoxPpa.setPreferredSize(new Dimension(160, 25));
        downloadButton.setPreferredSize(new Dimension(120, 25));
        buttonAktivieren.setPreferredSize(new Dimension(120, 25));

        buttonPanelPpa.add(progressBarStudent);
        buttonPanelPpa.add(optionsComboBoxPpa);
        buttonPanelPpa.add(downloadButton);
        buttonPanelPpa.add(buttonAktivieren);

        buttonAktivieren.addActionListener(e -> {
            Student selectedStudent = (Student) Student.getSelectedUser();
            if (selectedStudent.isAktiviert()) {
                return;
            } else {
                selectedStudent.setAktiviert(true);
                try {
                    DatabaseManager.activateStudent();
                    Controller.updateLists(studentListPpaModel, professorListModelTab2, studentListModelTab2Ppa);
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
        //Tab 2: Betreuerverwaltung
        studentListModelTab2Ppa = new DefaultListModel<>();
        JList<String> studentListTab2Ppa = new JList<>(studentListModelTab2Ppa);
        studentListTab2Ppa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentListTab2Ppa.setFont(new Font("Arial", Font.PLAIN, 12));
        studentListTab2Ppa.setBorder(BorderFactory.createTitledBorder("Studenten ohne Betreuer"));
        studentListTab2Ppa.setPreferredSize(new Dimension(150, 300));//Selbiges hier.

        JTextArea studentDetailTextAreaTab2 = new JTextArea();
        studentDetailTextAreaTab2.setEditable(false);
        studentDetailTextAreaTab2.setFont(new Font("Arial", Font.PLAIN, 12));
        studentDetailTextAreaTab2.setBorder(BorderFactory.createTitledBorder("Student Details"));
        JScrollPane studentDetailScrollPaneTab2Ppa = new JScrollPane(studentDetailTextAreaTab2);
        studentDetailScrollPaneTab2Ppa.setPreferredSize(new Dimension(300, 150));//Hier besonders wichtig weil sonst das Textfeld nicht die richtige Größe hat.

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
        professorListTab2Ppa.setFont(new Font("Arial", Font.PLAIN, 12));
        professorListTab2Ppa.setBorder(BorderFactory.createTitledBorder("Professoren"));
        professorListTab2Ppa.setPreferredSize(new Dimension(150, 300));//Selbiges hier.

        JTextArea professorDetailTextAreaTab2 = new JTextArea();
        professorDetailTextAreaTab2.setEditable(false);
        professorDetailTextAreaTab2.setFont(new Font("Arial", Font.PLAIN, 12));
        professorDetailTextAreaTab2.setBorder(BorderFactory.createTitledBorder("Professor Details"));
        JScrollPane professorDetailScrollPaneTab2Ppa = new JScrollPane(professorDetailTextAreaTab2);
        professorDetailScrollPaneTab2Ppa.setPreferredSize(new Dimension(300, 150));//Selbiges hier.
        
        professorListTab2Ppa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Controller.handleMouseClick(professorListTab2Ppa, professorDetailTextAreaTab2, "professorList");
                }
            }
        });

        JButton zuweiseButton = new JButton("Zuweisen");
        zuweiseButton.setPreferredSize(new Dimension(120, 25));
        
        zuweiseButton.addActionListener(e -> {
            Controller.setBetreuer();
            Controller.updateLists(studentListPpaModel, professorListModelTab2, studentListModelTab2Ppa);
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

        

        tabbedPaneP.addTab("Studentenliste", tab1Panel);
        cardProfessor.add(tabbedPaneP, BorderLayout.CENTER);

        contentPane.add(cardsPanel, BorderLayout.CENTER);
    }

    private static class Controller {

        private static final Logger logger = Logger.getLogger(MainFrame.class.getName());

        @SafeVarargs
        public static void handleLogin(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel, JTextField usernameField, JPasswordField passwordField, DefaultListModel<String>... models) {
            String cardName = authenticateUser(usernameField.getText(), new String(passwordField.getPassword()));
            clearFields(usernameField, passwordField);
            if (cardName != null && cardName.equals("nicht Aktiviert")) {
                loginLabel.setText("Account noch nicht Aktiviert");
            }
            switchCard(cardLayout, cardsPanel, cardName, models);
        }

        private static String authenticateUser(String username, String password) {
            String role = DatabaseManager.getRole(username, password);
            if (role == null) {
                loginLabel.setText("Ungültiger Nutzername oder Passwort.");
            } else {
                showGreetings();
                logger.log(Level.INFO, "User Authentifiziert: {0}", username);
                return role;
            }
            return null;
        }

        public static void handleLogout(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel) {
            User.setLoggedInuser(null);
            switchCard(cardLayout, cardsPanel, "CardLogIn");
            DatabaseManager.closeConnection();
            UserService.delSL();
        }

        @SafeVarargs
		private static void switchCard(CardLayout cardLayout, JPanel cardsPanel, String cardName, DefaultListModel<String>... models) {
            cardLayout.show(cardsPanel, cardName);
            if (models != null && models.length == 3) {
                controlPopulateList(models[0], models[1], models[2]);
            }
        }

        @SafeVarargs
		private static void controlPopulateList(DefaultListModel<String>... models) {
            if (User.getLoggedInuser() instanceof Ppa) {
                if (models != null && models.length == 3) {
                    populateUserList(models[0], "studenten", false);
                    populateUserList(models[1], "professoren", false);
                    populateUserList(models[2], "studenten", true);
                }
            }
        }

        private static void populateUserList(DefaultListModel<String> listModel, String tableName, boolean noTutor) {
            UserService.populateUserList(listModel, tableName, noTutor);
            if (noTutor) {
                logger.log(Level.INFO, "Liste Studenten ohne Betreuer gefüllt: {0}", tableName);
            } else {
                logger.log(Level.INFO, "Modell gefüllt: {0}", tableName);
            }
        }

        private static void clearFields(JTextField usernameField, JPasswordField passwordField) {
            usernameField.setText("");
            passwordField.setText("");
            logger.log(Level.INFO, "Login Felder geleert.");
        }

        public static void handleMouseClick(JList<String> list, JTextArea textArea, String listIdentifier) {
            String result;
            try {
                result = setSelectedUser(list, listIdentifier);
                textArea.setText(result);
                logger.log(Level.INFO, "Listenelement gewählt: {0}", result);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        public static String setSelectedUser(JList<String> studentList, String listIdentifier) throws ClassNotFoundException, SQLException {
            int selectedIndex = studentList.getSelectedIndex();
            if (selectedIndex != -1) {
                int selectedPk = getSelectedPk(selectedIndex, listIdentifier);
                if (selectedPk != -1) {
                    User selectedUser = DatabaseManager.getUserByPk(selectedPk);
                    if ("professorList".equals(listIdentifier)) {
                        User.setSelectedProf(selectedUser);
                    } else {
                        User.setSelectedUser(selectedUser);
                    }
                    logger.log(Level.INFO, "Selected User: {0}", selectedUser);
                    if(User.getSelectedProf() != null)logger.log(Level.INFO, "Selected Prof: {0}", User.getSelectedProf().toString());
                    return selectedUser.printUserDetails();
                }
            }
            return null;
        }

        private static int getSelectedPk(int selectedIndex, String listIdentifier) {
            return switch (listIdentifier) {
			    case "studentList" -> UserService.getSLIndex(selectedIndex);
			    case "studentListNoTutor" -> UserService.getSLNoTutorIndex(selectedIndex);
			    case "professorList" -> UserService.getPLIndex(selectedIndex);
			    default -> {
			        logger.log(Level.WARNING, "Unbekannte Liste: {0}", listIdentifier);
			        yield -1;
			    }
			};
        }

        private static void updateLists(DefaultListModel<String> studentListPpaModel, DefaultListModel<String> professorListModelTab2, DefaultListModel<String> studentListModelTab2Ppa) {
        	Controller.controlPopulateList(studentListPpaModel, professorListModelTab2, studentListModelTab2Ppa);
        }
        
        public static void clearTextAreas(JTextArea... textAreas) {
            for (JTextArea textArea : textAreas) {
                textArea.setText("");
            }
        }
                
        //Setzt Selektierten Nutzer bei tab switch auf null um Probleme bei zuteilung zu vermeiden.
        public static void tabSwitchListener(JTabbedPane tabbedPane) {
            tabbedPane.addChangeListener(e -> {
                int selectedIndex = tabbedPane.getSelectedIndex();
                var selectedTabTitle = tabbedPane.getTitleAt(selectedIndex);
                User.setSelectedUser(null);
                logger.log(Level.INFO, "User geleert.");
                
            });
        }
        
        public static void showGreetings() {
            String role = User.getLoggedInuser().getClassName();

            switch (role) {
                case "Student" -> studentLabel.setText(User.getLoggedInuser().showGreetings());
                case "Professor" -> profLabel.setText(((Professor) User.getLoggedInuser()).showGreetings() + " (ProfID: " + ((Professor) User.getLoggedInuser()).getPK() + ")");
                case "Ppa" -> ppaLabel.setText(User.getLoggedInuser().showGreetings());
                default -> logger.log(Level.SEVERE, "Unbekannte Rolle");
            }
        }
        
        //Setzt ProfID bei selektiertem User und in DB.
        public static void setBetreuer() {
            User selectedProf = User.getSelectedProf();
            User selectedStudent = (Student) User.getSelectedUser();
            
            if (selectedProf == null || selectedStudent == null) {
                logger.log(Level.WARNING, "Keine Auswahl getroffen.");
                return;
            }
            
            int profId = selectedProf.getPK();
            int MNr = selectedStudent.getPK();
            ((Student) selectedStudent).setProfID(profId);
            
            DatabaseManager.setProfID(profId, MNr);
			logger.log(Level.INFO, "Zuweisung erfolgreich.");
        }
     
	    private static void handleConfirm(JTextField companyField, JTextArea topicField) {
	        String company = companyField.getText();
	        String topic = topicField.getText();
	
	        System.out.println("Confirm button clicked");
	        System.out.println("Company: " + company);
	        System.out.println("Topic: " + topic);
	
	        if (company.isEmpty() || topic.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "Bitte füllen Sie alle Felder aus", "Fehler",
	                    JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	
	        //Daten in DB gespeichert
	        try {
	            DatabaseManager.saveStudentData(User.getLoggedInuser(), company, topic);
	            JOptionPane.showMessageDialog(null, "Stammdaten erfolgreich gespeichert", "Erfolg",
	                    JOptionPane.INFORMATION_MESSAGE);
	            
	        } catch (SQLException e) {
	            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, e);
	            JOptionPane.showMessageDialog(null, "Fehler beim Speichern der Daten", "Fehler",
	                    JOptionPane.ERROR_MESSAGE);
	        }
	    }
      
	    private static void loadInactiveStudentDataIntoTable(DefaultTableModel tableModel) {
	        List<Student> students = DatabaseManager.getAllInactiveStudents();
	        for (Student student : students) {
	            Object[] rowData = {
	                student.getVorname() + " " + student.getNachname(),
	                student.getPK(),
	                student.getFirma(),
	                student.getThema()
	            };
	            tableModel.addRow(rowData);
	        }
	    }
	
	    private static void handleAssignButton(JTable table, DefaultTableModel tableModel) throws HeadlessException {
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(contentPane, "Bitte wählen Sie einen Studenten aus der Liste aus.", "Keine Auswahl", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	
	        int studentMNr = (int) tableModel.getValueAt(selectedRow, 1); // Annahme: MNr ist in der zweiten Spalte
	        int professorId = User.getLoggedInuser().getPK();
	
	        DatabaseManager.assignStudentToProfessor(studentMNr, professorId);
	        
	        // Aktualisieren der Tabelle
	        tableModel.setRowCount(0); // Leeren der Tabelle
	        loadInactiveStudentDataIntoTable(tableModel); // Neuladen der Tabelle mit den aktuellen Daten
	    }
    
	    private static void loadProfessorStudentDataIntoTable(DefaultTableModel tableModel) {
	        List<Student> students = DatabaseManager.getProfessorStudents(User.getLoggedInuser().getPK());
	        for (Student student : students) {
	            Object[] rowData = {
	                student.getVorname() + " " + student.getNachname(),
	                student.getPK(),
	                student.getFirma(),
	                student.getThema()
	            };
	            tableModel.addRow(rowData);
	        }
	    }
	    
	    private static JComboBox<String> createOptionsComboBox() {
	        String[] options = {"Besuchs Bericht", "Studierenden Bericht", "Tätigkeitsnachweis", "Vortrag Bericht"};
	        return new JComboBox<>(options);
	    }
	    
	    private static void handleDownloadButtonClick(JComboBox<String> optionsComboBox) {
	        try {
	            String documentType = getSelectedOption(optionsComboBox);
	            logger.info("Option ausgewählt. Dokumenttyp: " + documentType);
	            DocumentService.downloadDocument(documentType);
	        } catch (Exception e) {
	            logger.severe("Fehler beim Behandeln der ausgewählten Option: " + e.getMessage());
	        }
	    }

	    private static String getSelectedOption(JComboBox<String> optionsComboBox) {
	        int selectedIndex = optionsComboBox.getSelectedIndex();
	        int optionNumber = selectedIndex + 1;
	        return getDocType(optionNumber);//Rückgabe des ausgewählten Dokumenttyps.
	    }

	    // Methode zur Zuordnung einer Dokumentnummer zu einem Dokumenttyp.
	    public static String getDocType(int documentNr) {
	        return switch (documentNr) {
	            case 1 -> "BesucherBericht";
	            case 2 -> "StudBericht";
	            case 3 -> "TaetigkeitsNw";
	            case 4 -> "VortrBericht";
	            default -> null;
	        };
	    }

	    private static void handleUpload(JComboBox<String> optionsComboBox) {
	        File file = Controller.chooseFile();//Dateiauswahl über Dateiauswahlpopup.
	        if (file != null) {
	            String documentType = getSelectedOption(optionsComboBox);
	            try {
	                DocumentService.uploadDocument(file, documentType);
	            } catch (ClassNotFoundException | SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }

	    // Methode zur Auswahl einer Datei über den Dateiauswahldialog
	    public static File chooseFile() {
	        JFileChooser fileChooser = new JFileChooser();
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	            return fileChooser.getSelectedFile();//Rückgabe der ausgewählten Datei
	        } else {
	            return null;
	        }
	    }
	}
}






