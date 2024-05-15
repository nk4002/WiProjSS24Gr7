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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import wiprojss24gr7.database.DatabaseManager;


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
        cardStudent = new JPanel();
        cardProfessor = new JPanel();
        cardPpa = new JPanel();

        //Füge Karten zu cardPanel hinzu
        cardsPanel.add(cardLogIn, "CardLogIn");
        cardsPanel.add(cardStudent, "CardStudent");
        cardsPanel.add(cardProfessor, "CardProfessor");
        cardsPanel.add(cardPpa, "CardPpa");
        
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
		
		//Methode Geht zu Karte deren Name als String übergeben wurde 
		public static void switchCard(CardLayout cardLayout, JPanel cardsPanel, String cardName) {
            cardLayout.show(cardsPanel, cardName);
        }
    }
}
