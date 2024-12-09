/**
 * Programme permettant de créer un panel dans lequel on peut reprendre une partie commencée.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class PanelContinueGame extends JPanel {
	// **************************************************
	// Attributs
	// **************************************************
	private Game game=null;
	private MainFrame frame;
	private JPanel gamesList;
	private JLabel errorLabel;
	private Bouton btn_start;
	private CaseGame selectedGame=null;

	// **************************************************
	// Constructeur
	// **************************************************

	/** 
	 * constructeur
	 * @param frame désigne la fenêtre courante
	 * */

	public PanelContinueGame(MainFrame frame) {
		this.frame=frame;
		this.setBackground(new Color(247,247,247));
		this.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		this.setLayout(new FlowLayout(FlowLayout.CENTER,500,10));
		if (frame.getAccount()!=null) initComponents(frame.getAccount());
		else initErrorComponents();
	}
	// **************************************************
	// Méthodes
	// **************************************************

	/** 
	 * initComponents
	 * initialise et place les composants lorsqu'un joueur est connecté
	 * @param username désigne le nom du joueur connecté
	 * */
	public void initComponents(String username) {
		JLabel description=new JLabel("Cliquez sur la partie que vous souhaitez reprendre.");
		description.setFont(new Font("Corbel", Font.BOLD, 16));
		this.add(description);

		JPanel panelTitle=new JPanel();
		panelTitle.setPreferredSize(new Dimension(450,30));
		panelTitle.setBackground(new Color(61,217,89));
		panelTitle.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(90,90,90)));

		JLabel title=new JLabel("Parties de "+username,SwingConstants.CENTER);
		title.setForeground(Color.white);
		title.setFont(new Font("Corbel", Font.BOLD, 18));
		panelTitle.add(title);

		gamesList=new JPanel();
		gamesList.setLayout(new FlowLayout(FlowLayout.CENTER,500,0));
		gamesList.setPreferredSize(new Dimension(454,350));
		gamesList.setBorder(BorderFactory.createMatteBorder(3, 2, 2, 2, new Color(90,90,90)));
		gamesList.setBackground(new Color(245,245,245));

		gamesList.add(panelTitle);
		ArrayList<String> strGamesList=Session.getListGames(username);
		for (int i=0;i<strGamesList.size();i++) {
			gamesList.add(new CaseGame(strGamesList.get(i),i%2,i+1));
		}
		this.add(gamesList);

		btn_start=new Bouton("Reprendre cette partie",240,50,20,17,Bouton.GREEN);
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (game!=null) {
					frame.setContentPane(new PanelGame(frame,game));
					frame.validate();
				}
				else {
					errorLabel.setVisible(true);
				}
			}
		});
		this.add(btn_start);

		errorLabel=new JLabel("Vous n'avez pas sélectionné de partie.");
		errorLabel.setFont(new Font("Corbel", Font.BOLD, 16));
		errorLabel.setForeground(Color.red);
		errorLabel.setVisible(false);
		this.add(errorLabel);
	}

	/** 
	 * initErrorComponents
	 * initiliase les composants lorsqu'aucun utilisateur n'est connecté
	 * */
	public void initErrorComponents() {
		JLabel label=new JLabel("Il faut être connecté pour pouvoir reprendre une partie.");
		label.setForeground(Color.black);
		label.setFont(new Font("Corbel", Font.BOLD, 18));
		this.add(label);
	}

	// **************************************************
	// Classes
	// **************************************************


	/** 
	 * Classe CaseGame héritée de JButton et implémentant ActionListener
	 * permet de créer des "boutons" où seront affichées les parties
	 * chaque objet de type CaseGame stocke une partie 
	 * */
	class CaseGame extends JButton implements ActionListener {
		public static Color[] COLORS=new Color[] {new Color(225,225,225),Color.white};
		private int numGame;
		private String id;
		private String grille;
		private int round;
		private Player player1;
		private Player player2;
		private String opposant;
		private Color color;
		private boolean isSelected=false;

		public CaseGame(String data,int color,int numGame) {
			this.addActionListener(this);
			this.setPreferredSize(new Dimension(450,25));
			this.numGame=numGame;
			this.color=CaseGame.COLORS[color];
			String[] datas=data.split(";");
			this.id=datas[1];
			this.grille=datas[2];
			this.round=Integer.parseInt(datas[3]);
			String[] strPlayer1=datas[4].split(":");
			this.player1=(strPlayer1[0].equals("1")) ? new HumanPlayer(strPlayer1[1],1) : new ComputerPlayer(1);
			String[] strPlayer2=datas[5].split(":");
			this.player2=(strPlayer2[0].equals("1")) ? new HumanPlayer(strPlayer2[1],2) : new ComputerPlayer(2);
			this.opposant=(player1.getName()==frame.getAccount()) ? player2.getName() : player1.getName();
		}

		public void paintComponent(Graphics g){
			Graphics2D g2d=(Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Color backgroundColor=(isSelected) ? new Color(150,220,240) : color; 
			g2d.setColor(backgroundColor);
			g2d.fillRect(0,0,getWidth(),getHeight());
			g2d.setColor(Color.black);
			g2d.drawString(numGame+")  Partie contre "+opposant+" - Tour "+round,6,17);
			g2d.drawString(id,330,17);		
		}
		public void isSelect(boolean b) {
			isSelected=b;
			repaint();
		}

		public void actionPerformed(ActionEvent e) {
			game=new Game(frame.getAccount(),id,grille,round,player1,player2);
			if (selectedGame!=null) selectedGame.isSelect(false);
			this.isSelect(true);
			selectedGame=this;
		}
	}
}