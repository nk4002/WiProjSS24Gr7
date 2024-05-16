package wiprojss24gr7.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import wiprojss24gr7.database.DatabaseManager;
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
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    

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
        cardPpa = new JPanel();

        //Füge Karten zu cardPanel hinzu
        cardsPanel.add(cardLogIn, "CardLogIn");
        cardsPanel.add(cardStudent, "CardStudent");
        cardsPanel.add(cardProfessor, "CardProfessor");
        cardsPanel.add(cardPpa, "CardPpa");
        
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
        loginButton.addActionListener(e -> Controller.handleLogin(e, cardLayout, cardsPanel, usernameField, passwordField));
        
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


        //cardsPane werden auf contentPane gelegt
        contentPane.add(cardsPanel, BorderLayout.CENTER);
        
        
    }
	private class Controller {
		
		//Methode nimmt Text aus den Feldern Ruft getRole() in DBManager auf und übergibt den returnten String switchCard()
		public static void handleLogin(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel, JTextField usernameField, JPasswordField passwordField) {
			char[] passwordChars = passwordField.getPassword();
			String password = new String(passwordChars);
			String cardName = DatabaseManager.getRole(usernameField.getText(), password);
			switchCard(cardLayout, cardsPanel, cardName);
		}
		
		public static void handleLogout(ActionEvent e, CardLayout cardLayout, JPanel cardsPanel) {
			User.setLoggedInuser(null);
			switchCard(cardLayout, cardsPanel, "CardLogIn");
		}
		
		//Methode Geht zu Karte deren Name als String übergeben wurde 
		public static void switchCard(CardLayout cardLayout, JPanel cardsPanel, String cardName) {
            cardLayout.show(cardsPanel, cardName);
        }
    }
}
