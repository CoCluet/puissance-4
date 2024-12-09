/**
 * Programme permettant de faire tourner le jeu.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import exceptions.BadColumnException;


/** 
 * La classe GameEngine est une classe qui hérite de JPanel et qui est le moteur du jeu. 
 * Elle affiche correctement la partie et s'actualise à chaque coup.  
 * */

public class GameEngine extends JPanel{
	// **************************************************
	// Attributs
	// **************************************************

	/** 
	 * listes des couleurs possibles pour les ronds de la grille
	 * */
	private Color[] couleurs= new Color[]{Color.white,Color.yellow,Color.red}; 
	/** 
	 variable correspondant au numéro de la colonne sur laquelle le joueur passe sa souris
	 * */
	private int previewJetonColumn=-1; 
	/** 
	 * variable de type Game qui désigne la partie à afficher sur le panel
	 * */
	private Game game; 
	/** 
	 * variable de type Player égal au joueur qui doit jouer (change à chaque tour)
	 * */
	private Player currentPlayer; 
	/** 
	 * variable booléenne qui indique si une partie est en cours
	 * */
	private boolean running=false; 
	/** 
	 * variable de type JLabel où seront affichés des messages informatifs
	 * */
	private JLabel informationLabel;
	/** 
	 * variable de type JLabel où seront affichés les messages d'erreur
	 * */
	private JLabel errorLabel;
	/** 
	 * variable de type TopPanel où seront affichés les noms des joueurs
	 * */
	private TopPanel topPanel=new TopPanel("","");
	/** 
	 * variable de type JPanel où est affiché la grille du jeu
	 * */
	private JPanel grille;
	/** 
	 * variable de type MainFrame correspondant à la fenêtre principale
	 * */
	private MainFrame frame;

	// **************************************************
	// Constructeur
	// **************************************************

	/** 
	 * Constructeur
	 * @param frame désigne la fenêtre dans laquelle le panel GameEngine se trouve
	 * */
	public GameEngine(MainFrame frame) {
		this.frame=frame;
		this.setPreferredSize(new Dimension(700,565));
		this.setBackground(Color.white);
		this.setLayout(new FlowLayout(FlowLayout.CENTER,500,0));
		initComponents();
	}
	/** 
	 * initComponents
	 * méthode qui initialise les composants du panel GameEngine et les positionne
	 * */
	public void initComponents() {
		informationLabel=new JLabel("",SwingConstants.CENTER);
		informationLabel.setFont(new Font("Caladea", Font.BOLD, 16));
		informationLabel.setPreferredSize(new Dimension(500,35));
		informationLabel.setForeground(Color.black);
		errorLabel=new JLabel();

		// création du panel représentant graphiquement la grille
		grille=new JPanel(){
			public void paintComponent(Graphics g) {
				Graphics2D g2d=(Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setStroke(new BasicStroke(2));
				g2d.setColor(Color.blue);
				g2d.fillRoundRect(5,5,530,460,30,30);
				g2d.setColor(Color.black);
				g2d.drawRoundRect(5,5,530,460,30,30);
				for (int i=0;i<Game.WIDTH;i++) {
					for (int j=0;j<Game.HEIGHT;j++) {
						if (previewJetonColumn==i && j==game.getTabJetons()[i]) g.setColor(Color.gray);
						else g2d.setColor(couleurs[game.getGrille()[j][i]]);
						g2d.fillOval(30+i*70,380-j*70,60,60);
						g2d.setColor(Color.black);
						g2d.drawOval(30+i*70,380-j*70,60,60);
					}
				}
				getParent().repaint();
			}
		};
		grille.addMouseListener(new GrilleListener1());
		grille.addMouseMotionListener(new GrilleListener2());
		grille.setPreferredSize(new Dimension(540,470));

		this.add(informationLabel);
		this.add(topPanel);
		this.add(grille);
		this.add(errorLabel);
	}

	/** 
	 * startGame
	 * débute une partie
	 * @param game est la partie qui doit être commencé
	 * */
	public void startGame(Game game) {
		this.game=game;
		this.currentPlayer= (game.getRound()%2==1) ? game.getPlayer1() : game.getPlayer2();
		this.running=true;
		this.currentPlayer.play(this);
		topPanel.updateNames(game.getPlayer1().getName(),game.getPlayer2().getName());
		printInformation("Tour "+game.getRound()+" - C'est à "+currentPlayer.getName()+" de jouer..");
	}

	/** 
	 * currentPlayerPlay
	 * méthode appelée lorsque le joueur qui doit jouer a fait son choix
	 * la méthode ajoute le jeton dans la colonne choisie, et affiche le gagnant si il y en a un
	 * */
	public void currentPlayerPlay(){
		try {
			game.addJeton(currentPlayer.getChoice(),currentPlayer);
		} catch(BadColumnException bce) {
			// traitement de l'exception
			printError(bce.toString()); 
			// si c'est l'ordi il retente la méthode play
			if (currentPlayer.getType()==0) {currentPlayer.play(this);} 
			// si c'est un humain on arrête la fonction en attendant qu'il reclique sur une colonne
			if (currentPlayer.getType()==1) {return;} 
		}
		cleanError();
		repaint();

		// on regarde si le joueur a gagné
		if (game.getWinner()!=null) {
			if (game.isSaved()) Session.removeGame(game);
			printInformation(game.getWinner().getName()+" a gagné la partie.");
			running=false;
		}
		// sinon c'est à l'autre joueur de jouer
		else {
			currentPlayer.setChoice(-1);
			currentPlayer = (currentPlayer==game.getPlayer1()) ? game.getPlayer2() : game.getPlayer1();
			printInformation("Tour "+game.getRound()+" - C'est au tour de "+currentPlayer.getName()+" de jouer.");
			currentPlayer.play(this);
			
		}
	}

	public Game getGame(){
		return this.game;
	}
	public void setGame(Game g){
		this.game=g;
	}

	public void stopRunning() {
		this.running=false;
		this.previewJetonColumn=-1;
		grille.repaint();
	}

	public void run() {
		this.running=true;
	}

	/** 
	 * printInformation
	 * affiche un message dans le label d'informations
	 * @param message désigne le message qui doit être affiché
	 * */
	public void printInformation(String message) {
		this.informationLabel.setText(message);
		this.repaint();
	}
	/** 
	 * printError
	 * affiche un message dans le label d'erreur
	 * @param message désigne le message qui doit être affiché
	 * */
	public void printError(String message) {
		this.errorLabel.setText(message);
		this.repaint();
	}
	/** 
	 * cleanError
	 * supprime le texte du label d'erreur 
	 * */
	public void cleanError(){
		this.errorLabel.setText("");
		this.repaint();
	}

	/** 
	 * classe TopPanel héritée de JPanel
	 * panel personnalisé contenant une zone rouge et une zone jaune, où seront affichés les noms des joueurs
	 * */
	class TopPanel extends JPanel {
		private String name1;
		private String name2;
		public TopPanel(String name1,String name2){
			this.name1=name1;
			this.name2=name2;
			this.setPreferredSize(new Dimension(700,41));
		}
		public void updateNames(String name1,String name2) {
			this.name1=name1;
			this.name2=name2;
			repaint();
		}
		public void paintComponent(Graphics g){
				Graphics2D g2d=(Graphics2D)g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setStroke(new BasicStroke(2));
				Color[] colors=new Color[] {Color.white,Color.yellow,Color.white,Color.red};
				int[] sizes=new int[] {200,150,150,200};
				int width=0;
				for (int i=0;i<4;i++) {
					g2d.setPaint(new GradientPaint(width,0,colors[i],width+sizes[i],0,colors[(i+1)%4]));
					g2d.fillRect(width,0,sizes[i],40);
					width+=sizes[i];
				}
				g2d.setPaint(new GradientPaint(0,0,Color.white,100,0,Color.black));
				g2d.drawLine(0,0,600,0);
				g2d.drawLine(0,40,600,40);
				g2d.setPaint(new GradientPaint(600,0,Color.black,700,0,Color.white));
				g2d.drawLine(600,0,700,0);
				g2d.drawLine(600,40,700,40);
				g2d.setColor(Color.black);
				Font font = new Font("Caladea", Font.BOLD, 16);
   				g2d.setFont(font);
				int width1 = (int) g2d.getFontMetrics(font).stringWidth(name1)/2;
				int width2 = (int) g2d.getFontMetrics(font).stringWidth(name2)/2;
				g2d.drawString(name1,170-width1,25);
				g2d.drawString(name2,530-width2,25);
			}
	}
	/** 
	 * classe GrilleListener1 qui implémente MouseListener
	 * listener qui change le choix de l'utilisateur lorsqu'il clique sur une colonne et apelle la méthode currentPlayerPlay
	 * */
	class GrilleListener1 implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e){
			if (running && currentPlayer.getType()==1) {
				currentPlayer.setChoice(previewJetonColumn);
				previewJetonColumn=-1;
				currentPlayerPlay();
			}
		}
		@Override
		public void mouseEntered(MouseEvent e){
		}
		@Override
		public void mouseExited(MouseEvent e){
		}
		@Override
		public void mousePressed(MouseEvent e){
		}
		@Override
		public void mouseReleased(MouseEvent e){
		}
	}
	/** 
	 * classe GrilleListener1 qui implémente MouseMotionListener
	 * affiche une pré-visualisation d'un jeton lorsque l'utilisateur passe sa souris sur une colonne
	 * */
	class GrilleListener2 implements MouseMotionListener {
		@Override
		public void mouseDragged(MouseEvent e) {
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			if (running && currentPlayer.getType()==1) {
				int newColumn=previewJetonColumn;
				if (e.getX()>30 && e.getX()<90) {
					newColumn=0;
				}
				else if (e.getX()>100 && e.getX()<160) {
					newColumn=1;
				}
				else if (e.getX()>170 && e.getX()<230) {
					newColumn=2;
				}
				else if (e.getX()>240 && e.getX()<300) {
					newColumn=3;
				}
				else if (e.getX()>310 && e.getX()<370) {
					newColumn=4;
				}
				else if (e.getX()>380 && e.getX()<440) {
					newColumn=5;
				}
				else if (e.getX()>450 && e.getX()<510) {
					newColumn=6;
				}
				if (previewJetonColumn!=newColumn) {
					previewJetonColumn=newColumn;
					repaint();
				}
			}
			else {
				previewJetonColumn=-1;
			}
		}
	}
}
