/**
 * Programme permettant de créer un panel dans lequel on peut commencer une nouvelle partie.
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
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class PanelNewGame extends JPanel {
	// **************************************************
	// Attributs
	// **************************************************
	private MainFrame frame;
	private JLabel errorMessage;
	private FieldName fieldName1;
	private FieldName fieldName2;
	private PanelChoice panelChoiceOpposant;
	private PanelChoice panelChoiceColor;
	private Bouton btn_commencer;

	// **************************************************
	// Constructeur
	// **************************************************

	/** 
	 * constructeur
	 * @param frame désigne la fenêtre courante
	 * */
	public PanelNewGame(MainFrame frame) {
		this.frame=frame;
		this.setBackground(new Color(247,247,247));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,500,5));
		initComponents();
	}

	// **************************************************
	// Méthodes
	// **************************************************

	/** 
	 * initComponents
	 * initialise et place les composants du panel
	 * */
	public void initComponents() {
		JLabel informationMessage=new JLabel("",SwingConstants.CENTER);
		informationMessage.setForeground(Color.black);
		informationMessage.setFont(new Font("Sans-serif", Font.BOLD, 14));
		informationMessage.setPreferredSize(new Dimension(400,20));
		if (frame.getAccount()==null) informationMessage.setText("Mode invité");
		else informationMessage.setText("Connecté en tant que : "+frame.getAccount());

		errorMessage=new JLabel("",SwingConstants.CENTER);
		errorMessage.setForeground(Color.red);
		errorMessage.setFont(new Font("Sans-serif", Font.BOLD, 14));
		errorMessage.setPreferredSize(new Dimension(400,30));
		
		panelChoiceOpposant=new PanelChoice("Choix de l'adversaire : ",
			"../images/icone_robot.png","Ordinateur",
			"../images/icone_humain.png","Autre joueur",
			"L'ordinateur est programmé pour jouer intelligemment.");

		panelChoiceColor=new PanelChoice("Choix de la couleur : ",
			"../images/jaune.png","Jaune",
			"../images/rouge.png","Rouge",
			"Le joueur qui a les jetons jaunes joue en premier.");

		fieldName1=new FieldName();
		fieldName2=new FieldName();
		fieldName1.setVisible(false);
		fieldName2.setVisible(false);

		JPanel panelNames=new JPanel();
		panelNames.setLayout(new FlowLayout(FlowLayout.CENTER,15,10));
		panelNames.setPreferredSize(new Dimension(500,40));
		panelNames.setOpaque(false);
		panelNames.add(fieldName1);
		panelNames.add(fieldName2);

		btn_commencer=new Bouton("COMMENCER",200,65,25,16,Bouton.GREEN);
		btn_commencer.addActionListener(new BoutonCommencerListener());

		this.add(informationMessage);
		this.add(panelChoiceOpposant);
		this.add(panelChoiceColor);
		this.add(panelNames);
		this.add(errorMessage);
		this.add(btn_commencer);
	}

	/** 
	 * updateComponents
	 * actualise les champs de texte pour le/les pseudos selon les choix de l'utilisateur
	 * */
	public void updateComponents() {
		if (panelChoiceOpposant.getChoice()==1) { // partie contre ordinateur 
			fieldName2.setVisible(false);
			if (frame.getAccount()!=null) fieldName1.setVisible(false); // si un compte est connecté on ne demande pas de choisir de nom
			else { // si aucun compte n'est connecté il faut choisir un pseudo
				fieldName1.setVisible(true);
				fieldName1.setText("Choisissez votre pseudo : ");
			} 
		}
		else { // partie contre un autre joueur
			fieldName1.setVisible(true);
			if (frame.getAccount()!=null) { // si compte connecté on demande le nom de l'autre joueur
				fieldName1.setText("Pseudo de l'autre joueur : ");
				fieldName2.setVisible(false);
			}
			else { // sinon on demande les deux noms
				fieldName2.setVisible(true);
				fieldName1.setText("Votre pseudo : ");
				fieldName2.setText("Pseudo de l'adversaire : ");
			}
		}
		validate();
	}
	/** 
	 * printError
	 * affiche un message d'erreur dans le label d'erreur
	 * */
	public void printError(){
		errorMessage.setText("Veuillez choisir les options de la partie.");
		repaint();
	}

	// **************************************************
	// Classes
	// **************************************************

	/** 
	 * classe BoutonCommencerListener qui implémente ActionListener
	 * listener du bouton "commencer"
	 * vérifie que l'utilisateur a bien fait un choix
	 * créé une partie de type Game selon les choix de l'utilisateur
	 * remplace le contenu de la fenetre par un panel de type "PanelGame" avec la partie associée
	 * */
	class BoutonCommencerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (panelChoiceOpposant.getChoice()==0||panelChoiceColor.getChoice()==0){
				printError();
			}
			else{
				Game game;
				int choice1=panelChoiceOpposant.getChoice();
				int choice2=panelChoiceColor.getChoice();

				if (choice1==1) { // partie contre ordinateur 
					String name=(frame.getAccount()==null) ? fieldName1.getName() : frame.getAccount();
					if (choice2==1) { // le joueur choisi la couleur jaune
						game=new Game(frame.getAccount(),new HumanPlayer(name,1),new ComputerPlayer(2));
					}
					else { // le joueur choisi la couleur rouge
						game=new Game(frame.getAccount(),new ComputerPlayer(1),new HumanPlayer(name,2));
					}
				}
				else { // partie contre un autre humain
					Player player1,player2;
					if (frame.getAccount()==null) { // aucun compte connecté, parties entre deux joueurs
						player1=new HumanPlayer(fieldName1.getName(),1);
						player2=new HumanPlayer(fieldName2.getName(),2);
					}
					else { // le joueur est connecté et affronte un ami
						if (choice2==1) { // le joueur connecté a choisi la couleur jaune
							player1=new HumanPlayer(frame.getAccount(),1);
							player2=new HumanPlayer(fieldName1.getName(),2);
						}
						else { // le joueur connecté a choisi la couleur rouge
							player1=new HumanPlayer(fieldName1.getName(),1);
							player2=new HumanPlayer(frame.getAccount(),2);
						}
					}
					game=new Game(frame.getAccount(),player1,player2);
				}
				frame.setContentPane(new PanelGame(frame,game));
				frame.validate();
			}
		}
	}

	/** 
	 * classe FieldName héritée de JPanel
	 * permet de créer un "ensemble" label - textField
	 * */
	class FieldName extends JPanel {
		private JTextField field;
		private JLabel label;

		public FieldName() {
			this.setLayout(new FlowLayout());
			this.setOpaque(false);
			label=new JLabel();
			label.setFont(new Font("Sans-serif", Font.BOLD, 12));
			label.setForeground(Color.black);
			this.add(label);
			field=new JTextField("");
			field.setPreferredSize(new Dimension(80,18));
			field.setFont(new Font("Arial", Font.BOLD, 12));
			this.add(field);
		}
		// renvoie le nom tapé par l'utilisateur dans le champ de texte
		public String getName() {
			return field.getText();
		}
		// change le texte du label devant le champ de texte
		public void setText(String text) {
			label.setText(text);
			repaint();
		}
	}

	/** 
	 * classe PanelChoice héritée de JPanel
	 * permet de créer une ligne contenant : un texte descriptif, deux boutons image, 
	 * */
	class PanelChoice extends JPanel {
		private int choice=0;
		private JLabel mainLabel;
		private JLabel textImg1;
		private JLabel textImg2;
		private JLabel comment;
		private BoutonImage btn1;
		private BoutonImage btn2;
		/** 
		 * constructeur
		 * @param mainText : description 
		 * @param img1 : chemin vers l'image du premier bouton
		 * @param text1 : description du premier bouton
		 * @param img2 : chemin vers l'image du deuxième bouton
		 * @param text2 : description du deuxième bouton
		 * @param textUnder : commentaire en dessous 
		 * */
		public PanelChoice(String mainText,String img1,String text1,String img2,String text2,String textUnder) {
			this.setOpaque(false);
			initComponents(mainText,img1,text1,img2,text2,textUnder);
			putComponents();
		}
		/**
		 * initComponents
		 * initialise les composants
		 * */
		public void initComponents(String mainText,String img1,String text1,String img2,String text2,String textUnder) {
			mainLabel=new JLabel(mainText,SwingConstants.RIGHT);
			mainLabel.setForeground(Color.black);
			mainLabel.setFont(new Font("Corbel", Font.BOLD, 24));
			mainLabel.setPreferredSize(new Dimension(250,50));

			textImg1=new JLabel(text1);
			textImg1.setForeground(Color.black);
			textImg1.setFont(new Font("Corbel", Font.BOLD, 15));

			textImg2=new JLabel(text2);
			textImg2.setForeground(Color.black);
			textImg2.setFont(new Font("Corbel", Font.BOLD, 15));

			comment=new JLabel(textUnder);
			comment.setForeground(Color.black);
			comment.setFont(new Font("Corbel", Font.BOLD, 15));

			btn1=new BoutonImage(img1);
			btn2=new BoutonImage(img2);

			// on ajoute les actions listener qui change la valeur de l'attribut choice et l'apparence du bouton

			btn1.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					choice=1;
					btn1.setSelect(true);
					btn2.setSelect(false);
			  	} 
			});
			btn2.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					choice=2;
					btn1.setSelect(false);
					btn2.setSelect(true);
			  	} 
			});
		}
		/**
		 * putComponents
		 * place les composants
		 * */
		public void putComponents() {
			// utilisation d'un gridbaglayout pour placer très précisément les composants
			this.setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = 0;
			gc.gridy = 0;
			gc.insets = new Insets(10,10, 0, 10);
			gc.anchor=GridBagConstraints.LINE_END;
			this.add(mainLabel,gc);
			gc.gridx=1;
			gc.anchor=GridBagConstraints.CENTER;
			this.add(btn1,gc);
			gc.gridx=2;
			gc.insets = new Insets(10,0, 0, 50);
			this.add(btn2,gc);
			gc.gridx=1;
			gc.gridy=1;
			gc.insets = new Insets(0,0, 0, 0);
			this.add(textImg1,gc);
			gc.gridx=2;
			gc.gridy=1;
			gc.insets = new Insets(0,0, 0, 50);
			this.add(textImg2,gc);
			gc.gridx=0;
			gc.gridy=2;
			gc.gridwidth=3;
			gc.insets = new Insets(10,0, 12, 0);
			this.add(comment,gc);
		}

		public int getChoice(){
			return choice;
		}
	}
	/**
	 * classe BoutonImage héritée de JButton
	 * sert à créer des boutons avec une image à l'intérieur, et dont l'apparence change lorsuqu'ils sont sélectionnés
	 * */
	class BoutonImage extends JButton {
		private BufferedImage img;
		private Color borderColor=Color.black;
		private int borderStroke=2;

		public BoutonImage(String fichier) {
			try {
				this.img = ImageIO.read(getClass().getResource(fichier));
			} catch(IOException ioe) {
				System.out.println(ioe);
			}
			this.setPreferredSize(new Dimension(80,80));
			this.setContentAreaFilled(false);
			this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		}

		public void setSelect(boolean b){
			if (b) {
				borderColor=new Color(255,165,10);
				borderStroke=3;
			}
			else {
				borderColor=Color.black;
				borderStroke=2;
			}
			this.repaint();
			updateComponents();
		}

		public void paintComponent(Graphics g){
			Graphics2D g2d=(Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setStroke(new BasicStroke(borderStroke));
			g2d.setColor(Color.white);
			g2d.fillRoundRect(2,2,getWidth()-4,getHeight()-4,25,25);
			g2d.setColor(borderColor);
			g2d.drawRoundRect(2,2,getWidth()-4,getHeight()-4,25,25);
			g2d.drawImage(img, 5, 5, null);
		}
	}
}