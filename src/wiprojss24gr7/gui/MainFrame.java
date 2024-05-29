package wiprojss24gr7.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import wiprojss24gr7.database.DatabaseManager;
import wiprojss24gr7.service.UserService;
import wiprojss24gr7.userhandling.Ppa;
import wiprojss24gr7.userhandling.Student;
import wiprojss24gr7.userhandling.User;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
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
    

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//controller = new Controller();
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        //Erstelle Karten
        cardLogIn = new JPanel();
        cardStudent = new JPanel(new BorderLayout());
        cardProfessor = new JPanel(new BorderLayout());
        cardPpa = new JPanel(new BorderLayout());
        cardStudentErstanmeldung = new JPanel(new BorderLayout());

        //Füge Karten zu cardPanel hinzu
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
        
        //Username Feld
        GridBagConstraints gbcUsername = new GridBagConstraints();
        gbcUsername.gridx = 0;
        gbcUsername.gridy = 0;
        gbcUsername.fill = GridBagConstraints.HORIZONTAL;
        gbcUsername.anchor = GridBagConstraints.NORTH; 

        gbcUsername.insets = new Insets(5, 5, 5, 5);
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        cardLogIn.add(usernameField, gbcUsername);

        //Password Feld
        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.gridx = 0;
        gbcPassword.gridy = 1;
        gbcPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcPassword.anchor = GridBagConstraints.NORTH; 
        gbcPassword.insets = new Insets(5, 5, 5, 5);
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        cardLogIn.add(passwordField, gbcPassword);

        //Login Feld
        GridBagConstraints gbcLoginButton = new GridBagConstraints();
        gbcLoginButton.gridx = 0;
        gbcLoginButton.gridy = 2;
        gbcLoginButton.fill = GridBagConstraints.HORIZONTAL;
        gbcLoginButton.anchor = GridBagConstraints.NORTH; 
        gbcLoginButton.insets = new Insets(5, 5, 5, 5);
        loginButton = new JButton("Login");
        cardLogIn.add(loginButton, gbcLoginButton);
        loginButton.addActionListener(e -> Controller.handleLogin(e, cardLayout, cardsPanel, usernameField, passwordField, studentListPpaModel, professorListModelTab2, studentListModelTab2Ppa));
        
        /////////////////////////////////////////////////////////
        //Code zu Student Panel
        /////////////////////////////////////////////////////////
        
        //Top Panel für Button und Label werden mit borderLayout.Himmelsrichtung recht und links platziert
        JPanel topPanelS = new JPanel(new BorderLayout());

        JLabel studentLabel = new JLabel("Student Label");
        topPanelS.add(studentLabel, BorderLayout.WEST);

        JButton abmeldenS = new JButton("Abmelden");
        abmeldenS.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelS.add(abmeldenS, BorderLayout.EAST);

        cardStudent.add(topPanelS, BorderLayout.NORTH);

        //JTabbedPane wird im Zentrum platziert
        JTabbedPane tabbedPaneS = new JTabbedPane();
        tabbedPaneS.addTab("Tab 1", new JPanel());
        tabbedPaneS.addTab("Tab 2", new JPanel());
        tabbedPaneS.addTab("Tab 3", new JPanel());
        cardStudent.add(tabbedPaneS, BorderLayout.CENTER);

        /////////////////////////////////////////////////////////
        //Code zu Professor Panel
        /////////////////////////////////////////////////////////
        
        //Top Panel für Button und Label werden mit borderLayout.Himmelsrichtung recht und links platziert
        JPanel topPanelP = new JPanel(new BorderLayout());

        JLabel ProfLabel = new JLabel("Professor Label");
        topPanelP.add(ProfLabel, BorderLayout.WEST);

        JButton abmeldenP = new JButton("Abmelden");
        abmeldenP.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelP.add(abmeldenP, BorderLayout.EAST);

        cardProfessor.add(topPanelP, BorderLayout.NORTH);

        //JTabbedPane wird im Zentrum platziert
        JTabbedPane tabbedPaneP = new JTabbedPane();
        tabbedPaneP.addTab("Tab 1", new JPanel());
        tabbedPaneP.addTab("Tab 2", new JPanel());
        tabbedPaneP.addTab("Tab 3", new JPanel());
        cardProfessor.add(tabbedPaneP, BorderLayout.CENTER);
        
        /////////////////////////////////////////////////////////
        //Code zu Ppa Panel
        /////////////////////////////////////////////////////////
        
        //Top Panel für Button und Label werden mit borderLayout.Himmelsrichtung recht und links platziert
        JPanel topPanelPpa = new JPanel(new BorderLayout());

        JLabel PpaLabel = new JLabel("Ppa Label");
        topPanelPpa.add(PpaLabel, BorderLayout.WEST);

        JButton abmeldenPpa = new JButton("Abmelden");
        abmeldenPpa.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelPpa.add(abmeldenPpa, BorderLayout.EAST);

        cardPpa.add(topPanelPpa, BorderLayout.NORTH);

        //JTabbedPane wird im Zentrum platziert
        JTabbedPane tabbedPanePpa = new JTabbedPane();
        
        //StudentenListe in Tab 1
        studentListPpaModel = new DefaultListModel<>();
        JList<String> studentListPpa = new JList<>(studentListPpaModel);
        studentListPpa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //Textbereich um Studenteninfo anzuzeigen
        JTextArea textAreaStudentPpa = new JTextArea();
        textAreaStudentPpa.setEditable(false);
        JScrollPane scrollPaneStudentPpa = new JScrollPane(textAreaStudentPpa);
        
        studentListPpa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Handle single-click, change as needed
                    Controller.handleMouseClick(studentListPpa, textAreaStudentPpa, "studentList");
                }
            }
        });
        
        /////////////////////////////////////////////////////////
        //Code zu StudentErstanmeldung Panel
        /////////////////////////////////////////////////////////
        
        //Top Panel für Button und Label werden mit borderLayout.Himmelsrichtung recht und links platziert
        JPanel topPanelSE = new JPanel(new BorderLayout());

        JLabel studentELabel = new JLabel("Student Label");
        topPanelS.add(studentELabel, BorderLayout.WEST);

        JButton abmeldenSE = new JButton("Abmelden");
        abmeldenS.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelS.add(abmeldenS, BorderLayout.EAST);

        cardStudentErstanmeldung.add(topPanelS, BorderLayout.NORTH);

        //Unternehmen Feld
        JTextField companyField = new JTextField(20);
        cardStudentErstanmeldung.add(companyField, BorderLayout.CENTER);
        
        //Themen Feld
        JTextArea topicField = new JTextArea(5, 20);
        cardStudentErstanmeldung.add(topicField, BorderLayout.CENTER);
        
        
        
        
        
        
        
        //Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        progressBarStudent = new JProgressBar(0, 100);
        progressBarStudent.setStringPainted(true);
        progressBarStudent.setString("Beispiel");  //Platzhalter
        JButton Button1Ppa = new JButton("Button 1");	//Platzhalter
        JButton button2Ppa = new JButton("Button 2");	//Platzhalter
        buttonPanel.add(progressBarStudent);
        buttonPanel.add(Button1Ppa);
        buttonPanel.add(button2Ppa);
        
        //Panel für Liste/Textbox
        JPanel tabPanelPpa = new JPanel(new BorderLayout());
        tabPanelPpa.add(new JScrollPane(studentListPpa), BorderLayout.WEST);
        tabPanelPpa.add(scrollPaneStudentPpa, BorderLayout.CENTER);
        tabPanelPpa.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPanePpa.addTab("Studentenverwaltung", tabPanelPpa);
        
        //Studenten/Professoren Zuteilung Tab 2 Listen + Button zum Zuteilen
        studentListModelTab2Ppa = new DefaultListModel<>();
        JList<String> studentListTab2Ppa = new JList<>(studentListModelTab2Ppa);
        studentListTab2Ppa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JTextArea studentDetailTextAreaTab2 = new JTextArea();
        studentDetailTextAreaTab2.setEditable(false);
        JScrollPane studentDetailScrollPaneTab2Ppa = new JScrollPane(studentDetailTextAreaTab2);
        
        studentListTab2Ppa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Handle single-click, change as needed
                    Controller.handleMouseClick(studentListTab2Ppa, studentDetailTextAreaTab2, "studentListNoTutor");
                }
            }
        });
        
        professorListModelTab2 = new DefaultListModel<>();
        JList<String> professorListTab2Ppa = new JList<>(professorListModelTab2);
        professorListTab2Ppa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JTextArea professorDetailTextAreaTab2 = new JTextArea();
        professorDetailTextAreaTab2.setEditable(false);
        JScrollPane professorDetailScrollPaneTab2Ppa = new JScrollPane(professorDetailTextAreaTab2);
        
        professorListTab2Ppa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Handle single-click, change as needed
                    Controller.handleMouseClick(professorListTab2Ppa, professorDetailTextAreaTab2, "professorList");
                }
            }
        });
        
        JButton zuweiseButton = new JButton("Zuweisen");
        
        JPanel studentPanelTab2Ppa = new JPanel(new BorderLayout());
        studentPanelTab2Ppa.add(new JScrollPane(studentListTab2Ppa), BorderLayout.CENTER);
        studentPanelTab2Ppa.add(studentDetailScrollPaneTab2Ppa, BorderLayout.SOUTH);
        
        JPanel professorPanelTab2Ppa = new JPanel(new BorderLayout());
        professorPanelTab2Ppa.add(new JScrollPane(professorListTab2Ppa), BorderLayout.CENTER);
        professorPanelTab2Ppa.add(professorDetailScrollPaneTab2Ppa, BorderLayout.SOUTH);
        
        JPanel mainPanelTab2Ppa = new JPanel(new GridLayout(1, 2));
        mainPanelTab2Ppa.add(studentPanelTab2Ppa);
        mainPanelTab2Ppa.add(professorPanelTab2Ppa);
        
        JPanel tabPanel2Ppa = new JPanel(new BorderLayout());
        tabPanel2Ppa.add(mainPanelTab2Ppa, BorderLayout.CENTER);
        tabPanel2Ppa.add(zuweiseButton, BorderLayout.SOUTH);
        
        tabbedPanePpa.addTab("Betreuerverwaltung", tabPanel2Ppa);
        
        cardPpa.add(tabbedPanePpa, BorderLayout.CENTER);

        //cardsPane werden auf contentPane gelegt
        contentPane.add(cardsPanel, BorderLayout.CENTER);
    }
	
	private class Controller {
		
	
		private static final Logger logger = Logger.getLogger(MainFrame.class.getName());

	    //logIn
		public static void handleLogin(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel, JTextField usernameField, JPasswordField passwordField, DefaultListModel<String> modelA, DefaultListModel<String> modelB, DefaultListModel<String> modelC) {
	        String cardName = authenticateUser(usernameField.getText(), new String(passwordField.getPassword()));
	        switchCard(cardLayout, cardsPanel, cardName, modelA, modelB, modelC);
	        clearFields(usernameField, passwordField);
	    }

	    //logOut
		public static void handleLogout(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel) {
	        User.setLoggedInuser(null);
	        switchCard(cardLayout, cardsPanel, "CardLogIn", null, null, null);
	        UserService.delSL();
	    }

	    //Authentifiziert Nutzer
		private static String authenticateUser(String username, String password) {
	        String role = DatabaseManager.getRole(username, password);
	        logger.log(Level.INFO, "User authenticated: {0}", username);
	        return role;
	    }
	    
	    //Wechselt Karten
	    private static void switchCard(CardLayout cardLayout, JPanel cardsPanel, String cardName, DefaultListModel<String> modelA, DefaultListModel<String> modelB, DefaultListModel<String> modelC) {
	        cardLayout.show(cardsPanel, cardName);
	        if (modelA != null && modelB != null && modelC != null) {
	            controlPopulateList(modelA, modelB, modelC);
	        }
	    }

	    //Steuert wie welche Listen gefüllt werden
	    private static void controlPopulateList(DefaultListModel<String> modelA, DefaultListModel<String> modelB, DefaultListModel<String> modelC) {
	        if (User.getLoggedInuser() instanceof Ppa) {
	            populateFullList(modelA, "studenten");
	            populateFullList(modelB, "professoren");
	            populateStudentNoTutorList(modelC, "studenten");
	        }
	    }

	    //Füllt Liste mit allen Tabellenelementen
	    private static void populateFullList(DefaultListModel<String> listModel, String tableName) {
	        UserService.populateUserList(listModel, tableName);
	        logger.log(Level.INFO, "Populated list model: {0}", tableName);
	    }

	    //Steht im namen lol wie oben
	    private static void populateStudentNoTutorList(DefaultListModel<String> listModel, String tableName) {
	        UserService.populateStudentNoTutorList(listModel, tableName);
	        logger.log(Level.INFO, "Populated student list model without tutor: {0}", tableName);
	    }

	    //Leert der Anmeldedatenfelder
	    private static void clearFields(JTextField usernameField, JPasswordField passwordField) {
	        usernameField.setText("");
	        passwordField.setText("");
	        logger.log(Level.INFO, "login felder geleert.");
	    }

	    //Stuert was bei Klick passiert
	    public static void handleMouseClick(JList<String> list, JTextArea textArea, String listIdentifier)  {
	        String result;
			try {
				result = setSelectedUser(list, listIdentifier);
				textArea.setText(result);
		        logger.log(Level.INFO, "Listenelement gewählt: {0}", result);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	    //Setzt ausgewählten User
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
	                return selectedUser.toString();
	            }
	        }
	        return null;
	    }

	    private static int getSelectedPk(int selectedIndex, String listIdentifier) throws ClassNotFoundException, SQLException {
	        switch (listIdentifier) {
	            case "studentList":
	                return UserService.getSLIndex(selectedIndex);
	            case "studentListNoTutor":
	                return UserService.getSLNoTutorIndex(selectedIndex);
	            case "professorList":
	                return UserService.getPLIndex(selectedIndex);
	            default:
	                logger.log(Level.WARNING, "Unbekannte Liste: {0}", listIdentifier);
	                return -1;
	        }
	    }
	}
}
