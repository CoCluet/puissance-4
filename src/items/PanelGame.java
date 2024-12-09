/**
 * Programme permettant de créer un panel qui permet d'afficher une prtie de puissance 4.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import javax.swing.border.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.*;



/** 
 * classe PanelGame héritant de JPanel
 * crée le panel relatif au jeu, contenant la grille du jeu et des boutons
 * */
public class PanelGame extends JPanel {
	// **************************************************
	// Attribut
	// **************************************************
	private MainFrame frame;

	// **************************************************
	// Constructeur
	// **************************************************

	/** 
	 * constructeur
	 * @param frame désigne la fenêtre principale
	 * @param game désigne la partie qui sera affichée
	 * */
	public PanelGame(MainFrame frame,Game game) {
		this.frame=frame;
		this.setBackground(new Color(150,220,235));
		initComponents(game);
	}

	// **************************************************
	// Méthode
	// **************************************************

	/** 
	 * initComponents
	 * initialise et place les différents composants
	 * @param game désigne la partie qui sera affichée
	 * */	

	public void initComponents(Game game) {
		// ajout d'un SpringLayout pour centrer le panel au milieu de la fenêtre
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		// bordure
		Border border = BorderFactory.createMatteBorder(2, 2, 3, 2, Color.black);

		// conteneur principal
		JPanel container=new JPanel();
		container.setPreferredSize(new Dimension(900,640));
		container.setBackground(Color.white);
		container.setBorder(border);
		container.setLayout(new FlowLayout(FlowLayout.CENTER,500,0));

		layout.putConstraint(SpringLayout.VERTICAL_CENTER, container,0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, container,0, SpringLayout.HORIZONTAL_CENTER, this);
		this.add(container);

		// texte qui indique le pseudo de la personne connecté 
		JLabel label=new JLabel();
		label.setForeground(Color.black);
		label.setFont(new Font("Caladea", Font.BOLD, 15));
		if (frame.getAccount()==null) {
			label.setText("Mode invité.");
		}
		else {
			label.setText("Compte : "+frame.getAccount());
		}

		// création et ajout du moteur du jeu
		GameEngine gameEngine=new GameEngine(new MainFrame());
		gameEngine.startGame(game);
		container.add(gameEngine);
		// panel contenant les 2 ou 3 boutons
		JPanel boutons=new JPanel();
		boutons.setLayout(new FlowLayout(FlowLayout.CENTER,12,0));
		boutons.setOpaque(false);
		// bouton enregistrer (visible seulement si un compte est connecté)
		Bouton btn_save=new Bouton("Enregistrer",160,40,10,14,Bouton.GREEN);
		btn_save.addActionListener(new BtnSaveListener(gameEngine));
		if (frame.getAccount()==null) btn_save.setVisible(false);
		// bouton recommencer
		Bouton btn_restart=new Bouton("Recommencer",160,40,10,14,Bouton.ORANGE);
		btn_restart.addActionListener(new BtnRestartListener(gameEngine));
		// bouton quitter
		Bouton btn_exit=new Bouton("Quitter",160,40,10,14,Bouton.RED);
		btn_exit.addActionListener(new BtnExitListener(gameEngine));

		boutons.add(btn_save);
		boutons.add(btn_restart);
		boutons.add(btn_exit);

		container.add(boutons);
		container.add(label);
	}

	// **************************************************
	// Classes (Listener)
	// **************************************************

	/** 
	 * classe BtnSaveListener implémentant ActionListener
	 * listener du bouton "enregistrer"
	 * */

	class BtnSaveListener implements ActionListener {
		private Game game;

		public BtnSaveListener(GameEngine gameEngine){
			this.game=gameEngine.getGame();
		}
		public void actionPerformed(ActionEvent e) {
			game.updateData();
			Session.updateGame(game);
		}
	}

	/** 
	 * classe BtnRestartListener implémentant ActionListener
	 * listener du bouton "enregistrer"
	 * */

	class BtnRestartListener implements ActionListener {
		private Player player1;
		private Player player2;
		private Game game;

		public BtnRestartListener(GameEngine gameEngine){
			this.game=gameEngine.getGame();
			this.player1=gameEngine.getGame().getPlayer1();
			this.player2=gameEngine.getGame().getPlayer2();
		}
		public void actionPerformed(ActionEvent e) {
			if (game.getWinner()!=null || game.getRound()<3) {
				frame.setContentPane(new PanelGame(frame,new Game(frame.getAccount(),player1,player2)));
				frame.validate();
			}
			else {
				Object[] options = {"Recommencer", "Annuler"};
				int n = JOptionPane.showOptionDialog(frame,"Voulez-vous vraiment recommencer ?","Puissance 4",
				    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				    null, options,options[0]);
				if (n==0) {
					frame.setContentPane(new PanelGame(frame,new Game(frame.getAccount(),player1,player2)));
					frame.validate();
				}
			}
		}
	}

	/** 
	 * classe BtnExitListener implémentant ActionListener
	 * listener du bouton "quitter"
	 * */

	class BtnExitListener implements ActionListener {
		private Game game;

		public BtnExitListener(GameEngine gameEngine){
			this.game=gameEngine.getGame();
		}
		public void actionPerformed(ActionEvent e) {
			if (game.getWinner()!=null) {
				frame.setContentPane(new MainPanel(frame));
				frame.validate();
			}
			else if (frame.getAccount()==null || game.isSaved()){
				Object[] options = {"Quitter", "Annuler"};
				int n = JOptionPane.showOptionDialog(frame,"Voulez-vous vraiment quitter ?","Puissance 4",
				    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				    null, options,options[0]);
				if (n==0) {
					frame.setContentPane(new MainPanel(frame));
					frame.validate();
				}
			}
			else {
				Object[] options = {"Enregistrer","Ne pas enregistrer","Annuler"};
		        int n = JOptionPane.showOptionDialog(frame, "Voulez-vous enregistrer la partie ?","Puissance 4",
				    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				    null,options,options[2]);
		        if (n==0) {
		        	game.updateData();
		        	Session.updateGame(game);
		        }
		        if (n==0 || n==1) {
		        	frame.setContentPane(new MainPanel(frame));
					frame.validate();
	        	}
			}
		}
	}
}