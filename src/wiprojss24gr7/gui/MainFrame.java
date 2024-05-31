package wiprojss24gr7.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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

    public static void main(String[] args) {
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
        setBounds(100, 100, 800, 600);
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

        GridBagConstraints gbcUsername = new GridBagConstraints();
        gbcUsername.gridx = 0;
        gbcUsername.gridy = 0;
        gbcUsername.fill = GridBagConstraints.HORIZONTAL;
        gbcUsername.anchor = GridBagConstraints.NORTH;
        gbcUsername.insets = new Insets(5, 5, 5, 5);
        usernameField = new JTextField(20);
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        cardLogIn.add(usernameField, gbcUsername);

        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.gridx = 0;
        gbcPassword.gridy = 1;
        gbcPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcPassword.anchor = GridBagConstraints.NORTH;
        gbcPassword.insets = new Insets(5, 5, 5, 5);
        passwordField = new JPasswordField(20);
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        cardLogIn.add(passwordField, gbcPassword);

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
		gbcTop.weightx = 1.0; // Pushes the button to the right
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

		cardStudentErstanmeldung.add(centerPanel, BorderLayout.CENTER);

        /////////////////////////////////////////////////////////
        // Code zu Student Panel
        /////////////////////////////////////////////////////////

        JPanel topPanelS = new JPanel(new BorderLayout());

        JLabel studentLabel = new JLabel("Student Label");
        topPanelS.add(studentLabel, BorderLayout.WEST);

        JButton abmeldenS = new JButton("Abmelden");
        abmeldenS.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelS.add(abmeldenS, BorderLayout.EAST);

        cardStudent.add(topPanelS, BorderLayout.NORTH);

        JTabbedPane tabbedPaneS = new JTabbedPane();
        tabbedPaneS.addTab("Tab 1", new JPanel());
        tabbedPaneS.addTab("Tab 2", new JPanel());
        tabbedPaneS.addTab("Tab 3", new JPanel());
        cardStudent.add(tabbedPaneS, BorderLayout.CENTER);

        /////////////////////////////////////////////////////////
        // Code zu Professor Panel
        /////////////////////////////////////////////////////////

        JPanel topPanelP = new JPanel(new BorderLayout());

        JLabel ProfLabel = new JLabel("Professor Label");
        topPanelP.add(ProfLabel, BorderLayout.WEST);

        JButton abmeldenP = new JButton("Abmelden");
        abmeldenP.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelP.add(abmeldenP, BorderLayout.EAST);

        cardProfessor.add(topPanelP, BorderLayout.NORTH);

        JTabbedPane tabbedPaneP = new JTabbedPane();
        
        // Tab 1 mit einer Tabelle
        JPanel tab1Panel = new JPanel(new BorderLayout());
        String[] spaltenNamen = {"Vorname und Name", "ProfID", "Firma", "Thema"};
        
        DefaultTableModel tableModel = new DefaultTableModel(spaltenNamen, 0);
        JTable tabelle = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelle);
        tab1Panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPaneP.addTab("Tab 1", tab1Panel);
        tabbedPaneP.addTab("Tab 2", new JPanel());
        tabbedPaneP.addTab("Tab 3", new JPanel());
        cardProfessor.add(tabbedPaneP, BorderLayout.CENTER);

        loadDataIntoTable(tableModel);

        /////////////////////////////////////////////////////////
        // Code zu Ppa Panel
        /////////////////////////////////////////////////////////

        JPanel topPanelPpa = new JPanel(new BorderLayout());

        JLabel PpaLabel = new JLabel("Ppa Label");
        topPanelPpa.add(PpaLabel, BorderLayout.WEST);

        JButton abmeldenPpa = new JButton("Abmelden");
        abmeldenPpa.addActionListener(e -> Controller.handleLogout(e, cardLayout, cardsPanel));
        topPanelPpa.add(abmeldenPpa, BorderLayout.EAST);

        cardPpa.add(topPanelPpa, BorderLayout.NORTH);

        JTabbedPane tabbedPanePpa = new JTabbedPane();

        studentListPpaModel = new DefaultListModel<>();
        JList<String> studentListPpa = new JList<>(studentListPpaModel);
        studentListPpa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTextArea textAreaStudentPpa = new JTextArea();
        textAreaStudentPpa.setEditable(false);
        JScrollPane scrollPaneStudentPpa = new JScrollPane(textAreaStudentPpa);

        studentListPpa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    Controller.handleMouseClick(studentListPpa, textAreaStudentPpa, "studentList");
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JProgressBar progressBarStudent = new JProgressBar(0, 100);
        progressBarStudent.setStringPainted(true);
        progressBarStudent.setString("Beispiel");
        JButton button1Ppa = new JButton("Button 1");
        JButton button2Ppa = new JButton("Button 2");
        JButton buttonAktivieren = new JButton("Aktivieren");

        buttonPanel.add(progressBarStudent);
        buttonPanel.add(button1Ppa);
        buttonPanel.add(button2Ppa);
        buttonPanel.add(buttonAktivieren);

        //Aktiviert Student und lädt Listen neu um Änderung zu reflektieren
        buttonAktivieren.addActionListener(e -> {
            Student selectedStudent = (Student) Student.getSelectedUser();
            if (selectedStudent.isAktiviert()) {
                return;
            } else {
                selectedStudent.setAktiviert(true);
                try {
                    DatabaseManager.activateStudent();
                    Controller.controlPopulateList(studentListPpaModel, professorListModelTab2, studentListModelTab2Ppa);
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JPanel tabPanelPpa = new JPanel(new BorderLayout());
        tabPanelPpa.add(new JScrollPane(studentListPpa), BorderLayout.WEST);
        tabPanelPpa.add(scrollPaneStudentPpa, BorderLayout.CENTER);
        tabPanelPpa.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPanePpa.addTab("Studentenverwaltung", tabPanelPpa);

        studentListModelTab2Ppa = new DefaultListModel<>();
        JList<String> studentListTab2Ppa = new JList<>(studentListModelTab2Ppa);
        studentListTab2Ppa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTextArea studentDetailTextAreaTab2 = new JTextArea();
        studentDetailTextAreaTab2.setEditable(false);
        JScrollPane studentDetailScrollPaneTab2Ppa = new JScrollPane(studentDetailTextAreaTab2);


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

        JTextArea professorDetailTextAreaTab2 = new JTextArea();
        professorDetailTextAreaTab2.setEditable(false);
        JScrollPane professorDetailScrollPaneTab2Ppa = new JScrollPane(professorDetailTextAreaTab2);

        professorListTab2Ppa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
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

        contentPane.add(cardsPanel, BorderLayout.CENTER);
    }

    ///////////////////////////////////////////////////////////////////////////
    private void loadDataIntoTable(DefaultTableModel tableModel) {
        List<String[]> data = DatabaseManager.getProfessorData(); 
        for (String[] row : data) {
            tableModel.addRow(row);
        }
    }

    private static class Controller {

        private static final Logger logger = Logger.getLogger(MainFrame.class.getName());

        public static void handleLogin(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel, JTextField usernameField, JPasswordField passwordField, DefaultListModel<String> modelA, DefaultListModel<String> modelB, DefaultListModel<String> modelC) {
            String cardName = authenticateUser(usernameField.getText(), new String(passwordField.getPassword()));
            switchCard(cardLayout, cardsPanel, cardName, modelA, modelB, modelC);
            clearFields(usernameField, passwordField);
        }

        public static void handleLogout(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel) {
            User.setLoggedInuser(null);
            switchCard(cardLayout, cardsPanel, "CardLogIn", null, null, null);
            UserService.delSL();
        }

        private static String authenticateUser(String username, String password) {
            String role = DatabaseManager.getRole(username, password);
            logger.log(Level.INFO, "User authenticated: {0}", username);
            return role;
        }

        private static void switchCard(CardLayout cardLayout, JPanel cardsPanel, String cardName, DefaultListModel<String> modelA, DefaultListModel<String> modelB, DefaultListModel<String> modelC) {
            cardLayout.show(cardsPanel, cardName);
            if (modelA != null && modelB != null && modelC != null) {
                controlPopulateList(modelA, modelB, modelC);
            }
        }

        private static void controlPopulateList(DefaultListModel<String> modelA, DefaultListModel<String> modelB, DefaultListModel<String> modelC) {
            if (User.getLoggedInuser() instanceof Ppa) {
                populateFullList(modelA, "studenten");
                populateFullList(modelB, "professoren");
                populateStudentNoTutorList(modelC, "studenten");
            }
        }

        private static void populateFullList(DefaultListModel<String> listModel, String tableName) {
            UserService.populateUserList(listModel, tableName);
            logger.log(Level.INFO, "Populated list model: {0}", tableName);
        }

        private static void populateStudentNoTutorList(DefaultListModel<String> listModel, String tableName) {
            UserService.populateStudentNoTutorList(listModel, tableName);
            logger.log(Level.INFO, "Populated student list model without tutor: {0}", tableName);
        }

        private static void clearFields(JTextField usernameField, JPasswordField passwordField) {
            usernameField.setText("");
            passwordField.setText("");
            logger.log(Level.INFO, "login felder geleert.");
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
                    return selectedUser.printUserDetails();
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
