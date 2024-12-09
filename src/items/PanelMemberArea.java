/**
 * Programme permettant de créer un panel dans lequel on peut se connecter ou s'inscrire.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.*;


public class PanelMemberArea extends JPanel {
	// **************************************************
	// Attributs
	// **************************************************
	private JPanel container;
	private JPanel accountConnected;
	private MenuButton tabConnexion;
	private MenuButton tabInscription;
	private JPanel box;
	private InputPanel fieldConnexion;
	private InputPanel fieldInscription;
	private JLabel titleBox;
	private JLabel informationLabel;
	private MainFrame frame;

	// **************************************************
	// Constructeur
	// **************************************************

	/** 
	 * constructeur
	 * @param frame désigne la fenêtre courante
	 * le contenu du panel n'est pas le même selon si un compte est connecté ou non
	 * */

	public PanelMemberArea(MainFrame frame) {
		this.frame=frame;
		this.setBackground(new Color(247,247,247));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,500,10));
		initComponents(); // initialisation des composants du panel
		if (frame.getAccount()==null) putComponents();
		else connexion(frame.getAccount());
	}

	// **************************************************
	// Méthodes
	// **************************************************

	/** 
	 * initComponents
	 * initialise et place les différents composants
	 * */

	public void initComponents() {
		// texte explicatif
		JLabel line1=new JLabel("Avoir un compte permet de pouvoir enregistrer ses parties,");
		line1.setFont(new Font("Corbel", Font.BOLD, 16));
		JLabel line2=new JLabel("afin de les reprendre plus tard. ");
		line2.setFont(new Font("Corbel", Font.BOLD, 16));
		this.add(line1);
		this.add(line2);
		// conteneur principal 
		container=new JPanel();
		container.setPreferredSize(new Dimension(460,390));
		container.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(90,90,90)));
		// panel qui s'affiche lorsqu'un utilisateur est connecté
		accountConnected=new JPanel();
		accountConnected.setPreferredSize(new Dimension(400,200));
		accountConnected.setLayout(new FlowLayout(FlowLayout.CENTER,500,40));
		// onglets de connexion et d'inscription
		tabConnexion=new MenuButton("Connexion",false);
		tabConnexion.isSelect(true);
		tabInscription=new MenuButton("Inscription",true);
		// panel qui contiendra le formulaire d'inscription ou de connexion
		box=new JPanel();
		box.setPreferredSize(new Dimension(460,360));
		box.setBackground(Color.white);
		box.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		box.setLayout(new FlowLayout(FlowLayout.CENTER,300,30));
		// titre du box
		titleBox = new JLabel("Connexion",SwingConstants.CENTER);
		titleBox.setForeground(Color.BLACK);
		titleBox.setFont(new Font("Corbel", Font.BOLD, 20));
		box.add(titleBox);
		// label où seront affichés les messages informatifs
		informationLabel=new JLabel();
		informationLabel.setFont(new Font("Corbel", Font.BOLD, 16));
		// champs de connexion et d'inscription
		fieldConnexion=new InputPanel("Se connecter");
		fieldInscription=new InputPanel("S'inscrire");
		// on ajoute l'onglet connexion (par défault)
		box.add(fieldConnexion);
		// ajout des listener sur les boutons connexion et inscription
		fieldConnexion.getButton().addActionListener(new ConnexionListener());
		fieldInscription.getButton().addActionListener(new InscriptionListener());
		// ajout des listener sur les onglets connexion et inscription
		tabConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabConnexion.isSelect(true);
				tabInscription.isSelect(false);
				titleBox.setText("Connexion");
				box.remove(fieldInscription);
				box.remove(informationLabel);
				fieldConnexion.reset();
				box.add(fieldConnexion);
				validate();
				repaint();
			}
		});
		tabInscription.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabConnexion.isSelect(false);
				tabInscription.isSelect(true);
				titleBox.setText("Inscription");
				box.remove(fieldConnexion);
				box.remove(informationLabel);
				fieldInscription.reset();
				box.add(fieldInscription);
				validate();
				repaint();
			}
		});
		// ajout du container dans le panel
		this.add(container);
	}

	/** 
	 * putComponents
	 * place les composants dans le container
	 * */
	public void putComponents(){
		container.removeAll();
		// utilisation d'un SpringLayout pour avoir une disposition très précise
		SpringLayout layout=new SpringLayout();
		container.setLayout(layout);
		layout.putConstraint(SpringLayout.WEST, tabConnexion,0, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.NORTH, tabConnexion,0, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.EAST, tabInscription,0, SpringLayout.EAST, container);
		layout.putConstraint(SpringLayout.NORTH, tabInscription,0, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.EAST, tabConnexion,0, SpringLayout.WEST, tabInscription);
		layout.putConstraint(SpringLayout.SOUTH, box,0, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.WEST, box,0, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.EAST, box,0, SpringLayout.EAST, container);

		container.add(tabConnexion);
		container.add(tabInscription);
		container.add(box);
		validate();
		repaint();
	}

	/** 
	 * printMessage
	 * affiche un message dans le label d'information
	 * @param messgae désigne le message à afficher
	 * @param color désigne la couleur du texte à afficher
	 * */
	public void printMessage(String message,Color color){
		informationLabel.setForeground(color);
		informationLabel.setText(message);
		box.add(informationLabel);
		validate();
		repaint();
	}

	/** 
	 * connexion
	 * méthode appelée lorsqu'un utilisateur se connecte
	 * change le contenu du "container" (enlève les champs d'inscription/de connexion et affiche un bouton de déconnexion)
	 * @param username désigne le pseudo du joueur connecté
	 * */
	public void connexion(String username) {
		container.removeAll();
		container.setLayout(new FlowLayout(FlowLayout.CENTER,0,100));
		JLabel label=new JLabel("Connecté en tant que : "+frame.getAccount(),SwingConstants.CENTER);
		label.setFont(new Font("Corbel", Font.BOLD, 20));
		label.setForeground(Color.black);
		Bouton btn_deconnexion=new Bouton("Déconnexion",180,50,15,16,Bouton.RED);
		btn_deconnexion.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				putComponents();
				frame.disconnectAccount();
				printMessage("Déconnexion réussie avec succès.",Color.red);
			}
		});
		accountConnected.removeAll();
		accountConnected.add(label);
		accountConnected.add(btn_deconnexion);
		container.add(accountConnected);
		validate();
		repaint();
	}

	// **************************************************
	// Classes
	// **************************************************

	/** 
	 * classe ConnexionListener implémentant ActionListener
	 * listener du bouton connexion
	 * */

	class ConnexionListener implements ActionListener {
		/** 
		 * méthode Ovveride actionPerformed
		 * vérifie que tous les champs sont bien remplis
		 * vérifie que le pseudo et mot de passe sont corrects
		 * si c'est le cas, connecte le joueur
		 * */
		@Override
		public void actionPerformed(ActionEvent e) {
			String username=fieldConnexion.getUsername();
			String password=fieldConnexion.getPassword();
			if (username.length()>0 && password.length()>0){
				if (Session.checkAccount(username,password)) {
					frame.connectAccount(username);
					connexion(username);
					fieldConnexion.reset();
				}
				else {
					printMessage("Pseudo ou mot de passe inccorect.",Color.red);
				}
			}
			else {
				printMessage("Veuillez renseigner tous les champs.",Color.red);
			}
		}
	}

	/** 
	 * classe InscriptionListener implémentant ActionListener
	 * listener du bouton inscription
	 * */

	class InscriptionListener implements ActionListener {
		/** 
		 * méthode Override actioNperformed
		 * vérifie que tous les champs sont remplis
		 * vérifie que le pseudo est disponible
		 * si tout est bon, inscris le joueur
		 * */
		@Override
		public void actionPerformed(ActionEvent e) {
			String username=fieldInscription.getUsername();
			String password=fieldInscription.getPassword();
			if (username.length()>0 && password.length()>0){
				if (Session.checkUsername(username)) {
					Session.addAccount(username,password);
					printMessage("Inscription réussie ! Vous pouvez vous connecter.",Color.black);
					fieldInscription.reset();
				}
				else {
					printMessage("Ce pseudo est déjà utilisé.",Color.red);
				}
			}
			else {
				printMessage("Veuillez renseigner tous les champs.",Color.red);
			}
		}
	}

	/** 
	 * classe InputPanel héritant de JPanel
	 * permet de créer des formulaires d'inscription/de connexion
	 * contient un champ de texte pour le pseudo, un pour le mot de passe et un bouton
	 * */
	class InputPanel extends JPanel {
		private JLabel usernameLabel;
		private JLabel passwordLabel;
		private JTextField usernameField;
		private JPasswordField passwordField;
		private Bouton btn;

		public InputPanel(String text){
			this.setPreferredSize(new Dimension(350,180));
			initComponents(text);
			putComponents();
		}
		/** 
		 * initComponents
		 * initialise les composants
		 * @param text correspond au texte qui sera affiché sur le bouton
		 * */
		public void initComponents(String text){
			usernameLabel = new JLabel("Nom d'utilisateur : ");
			usernameLabel.setForeground(Color.black);
			usernameLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));

			usernameField=new JTextField("");
			usernameField.setPreferredSize(new Dimension(120,25));
			usernameField.setFont(new Font("Sans-serif", Font.BOLD, 16));

			passwordLabel = new JLabel("Mot de passe : ");
			passwordLabel.setForeground(Color.black);
			passwordLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));

			passwordField = new JPasswordField("");
			passwordField.setPreferredSize(new Dimension(120,25));
			passwordField.setFont(new Font("Sans-serif", Font.BOLD, 16));
			passwordField.setEchoChar('*');

			btn=new Bouton(text,180,45,25,16,Bouton.GREEN);
		}

		/** 
		 * putComponents
		 * place les composants
		 * */
		public void putComponents(){
			// utilisation d'un SpringLayout pour placer correctemment les différents composants
			SpringLayout layout=new SpringLayout();
			this.setLayout(layout);
			layout.putConstraint(SpringLayout.WEST, usernameLabel,30, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, usernameLabel,30, SpringLayout.NORTH, this);
			layout.putConstraint(SpringLayout.NORTH, usernameField,30, SpringLayout.NORTH, this);
			layout.putConstraint(SpringLayout.WEST, usernameField,5, SpringLayout.EAST, usernameLabel);
			layout.putConstraint(SpringLayout.WEST, passwordField,0, SpringLayout.WEST, usernameField);
			layout.putConstraint(SpringLayout.NORTH, passwordField,15, SpringLayout.SOUTH, usernameField);
			layout.putConstraint(SpringLayout.NORTH, passwordLabel,18, SpringLayout.SOUTH, usernameLabel);
			layout.putConstraint(SpringLayout.EAST, passwordLabel,0, SpringLayout.EAST, usernameLabel);
			layout.putConstraint(SpringLayout.WEST, btn,85, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, btn,30, SpringLayout.SOUTH, passwordLabel);

			this.add(usernameLabel);
			this.add(usernameField);
			this.add(passwordLabel);
			this.add(passwordField);
			this.add(btn);
		}
		public Bouton getButton(){
			return this.btn;
		}
		public String getPassword(){
			char[] pf = passwordField.getPassword();
			String value = new String(pf);
			return value;
		}
		public String getUsername(){
			return usernameField.getText();
		}

		public void reset(){
			usernameField.setText("");
			passwordField.setText("");
		}
	}
	/** 
	 * classe MenuButton héritant de JButton
	 * permet des créer des boutons personnalisés représentant des onglets
	 * */
	class MenuButton extends JButton {
		private boolean isSelected=false;
		private String text;
		//private int stroke=3;
		private boolean line;

		public MenuButton(String text,boolean b) {
			this.text=text;
			this.line=b;
			this.setPreferredSize(new Dimension(230,40));
			this.setContentAreaFilled(false);
			this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		}
		/** 
		 * méthode override paintComponent
		 * colorie le bouton selon si il est sélectionné ou non
		 * */
		@Override
		public void paintComponent(Graphics g){
				Graphics2D g2d=(Graphics2D)g;
				g2d.setStroke(new BasicStroke(3));
				Color color=(isSelected) ? Color.white : new Color(225,225,225);
				g2d.setColor(color);
				g2d.fillRect(0,0,getWidth(),getHeight());
				g2d.setColor(new Color(90,90,90));
				if (line) g2d.drawLine(0, 0, 0, getHeight());
				if (!isSelected) g2d.drawLine(0, getHeight(), getWidth(), getHeight());
				g2d.setColor(Color.black);
				g2d.setFont(new Font("Corbel", Font.BOLD, 15));
				g2d.drawString(text,76,24);			
			}
		/** 
		 * méthode isSelect
		 * change l'état du bouton (attribut isSelected)
		 * */
		public void isSelect(boolean b){
			if (b){
				isSelected=true;
			}
			else{
				isSelected=false;
			}
			repaint();
		}
	}
}