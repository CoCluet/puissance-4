/**
 * Programme permettant de créer un panel correspondant au menu du jeu.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import java.awt.BorderLayout;
import javax.swing.border.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.*;

public class MainPanel extends JPanel {
	// **************************************************
	// Constante
	// **************************************************
	public static int WEST_WIDTH=300; 

	// **************************************************
	// Attributs
	// **************************************************
	private MainFrame frame;
	//private GridBagConstraints gbd;
	private JPanel westBox;
	private JPanel centerBox;
	private Bouton btn_newGame;
	private Bouton btn_continueGame;
	private Bouton btn_memberArea;
	private Bouton btn_menu;
	private JLabel title;
	private JPanel containerCenterBox;

	// **************************************************
	// Constructeur
	// **************************************************

	/** 
	 * constructeur
	 * @param frame désigne la fenêtre principale
	 * */

	public MainPanel(MainFrame frame) {
		this.frame=frame;
		this.setBackground(new Color(150,220,235));
		initComponents();
	}

	// **************************************************
	// Méthodes
	// **************************************************

	/** 
	 * initComponents
	 * initialise et place les différents composants
	 * */

	public void initComponents() {
		// utilisation d'un SpringLayout pour centrer le panel horizontalement et verticalement
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		// bordure du contenaire 
		Border border = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black);

		// création du conteneur principal
		JPanel container=new JPanel();
		container.setPreferredSize(new Dimension(900,640));
		container.setBackground(Color.blue);
		container.setBorder(border);
		container.setLayout(new BorderLayout());

		layout.putConstraint(SpringLayout.VERTICAL_CENTER, container,0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, container,0, SpringLayout.HORIZONTAL_CENTER, this);
		this.add(container);

		// panel de gauche (où sont disposés les boutons)
		westBox=new JPanel();
		westBox.setBackground(new Color(220,220,220));
		westBox.setPreferredSize(new Dimension(MainPanel.WEST_WIDTH,0));
		westBox.setLayout(new FlowLayout(FlowLayout.CENTER,0,50));
		westBox.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.black));

		// panel central qui comporte un titre et un contenu qui varie selon les catégories
		centerBox=new JPanel();
		centerBox.setBackground(Color.white);
		centerBox.setLayout(new BorderLayout());
		centerBox.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

		container.add(westBox,BorderLayout.WEST);
		container.add(centerBox,BorderLayout.CENTER);

		// panel contenant les 4 boutons 
		JPanel buttonsBox=new JPanel();
		buttonsBox.setLayout(new GridLayout(4,1,0,10));
		buttonsBox.setOpaque(false);
		westBox.add(buttonsBox);

		btn_newGame=new Bouton("Nouvelle partie",210,60,10,17,Bouton.ORANGE);
		btn_newGame.addActionListener(new BoutonNewGameListener());
		buttonsBox.add(btn_newGame);

		btn_continueGame=new Bouton("Reprendre une partie",210,60,10,17,Bouton.ORANGE);
		btn_continueGame.addActionListener(new BoutonContinueGameListener());
		buttonsBox.add(btn_continueGame);

		btn_memberArea=new Bouton("Espace membre",210,60,10,17,Bouton.ORANGE);
		btn_memberArea.addActionListener(new BoutonMemberAreaListener());
		buttonsBox.add(btn_memberArea);

		btn_menu=new Bouton("Menu principal",210,60,10,17,Bouton.ORANGE);
		btn_menu.addActionListener(new BoutonMenuListener());
		buttonsBox.add(btn_menu);

		// panel contenant le titre (pour le box central)
		JPanel panelTitle = new JPanel();
		panelTitle.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));
		panelTitle.setBackground(new Color(255,165,10));
		panelTitle.setPreferredSize(new Dimension(0,80));

		title = new JLabel("NOUVELLE PARTIE",SwingConstants.CENTER);
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Corbel", Font.BOLD, 42));
		panelTitle.add(title);

		centerBox.add(panelTitle,BorderLayout.NORTH);

		// contenu du box central 
		containerCenterBox=new JPanel();
		containerCenterBox.setBackground(Color.white);
		containerCenterBox.setLayout(new FlowLayout(FlowLayout.CENTER,5,15));
		centerBox.add(containerCenterBox,BorderLayout.CENTER);

		// on ajoute un panel de type "PanelNewGame"
		PanelNewGame panel=new PanelNewGame(frame);
		panel.setPreferredSize(new Dimension(550,490));
		putInContainer(panel);
	}
	/** 
	 * changeTitle
	 * change le titre du box principal
	 * @param newTitle désigne le titre à afficher
	 * */
	public void changeTitle(String newTitle){
		title.setText(newTitle);
		repaint();
	}
	/** 
	 * putInContainer
	 * remplace le panel du box central par un nouveau panel 
	 * @param panel est le panel à placer
	 * */
	public void putInContainer(JPanel panel){
		containerCenterBox.removeAll();
		containerCenterBox.add(panel);
		validate();
	}

	// **************************************************
	// Classes (Listener)
	// **************************************************

	/** 
	 * classe BoutonNewGameListener qui implémente ActionListener
	 * listener du bouton "nouvelle partie"
	 * change le titre et le contenu du conteneur principal par un panel de type "PanelNewGame"
	 * */
	class BoutonNewGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeTitle("NOUVELLE PARTIE");
			PanelNewGame panel=new PanelNewGame(frame);
			panel.setPreferredSize(new Dimension(550,490));
			putInContainer(panel);
		}
	}
	/** 
	 * classe BoutonContinueGameListener qui implémente ActionListener
	 * listener du bouton "continuer une partie"
	 * change le titre et le contenu du conteneur principal par un panel de type "PanelContinueGame"
	 * */
	class BoutonContinueGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeTitle("CONTINUER UNE PARTIE");
			PanelContinueGame panel=new PanelContinueGame(frame);
			panel.setPreferredSize(new Dimension(550,490));
			putInContainer(panel);
		}
	}
	/** 
	 * classe BoutonMemberAreaListener qui implémente ActionListener
	 * listener du bouton "espace membre"
	 * change le titre et le contenu du conteneur principal par un panel de type "PanelMemberArea"
	 * */
	class BoutonMemberAreaListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeTitle("ESPACE MEMBRE");
			PanelMemberArea panel=new PanelMemberArea(frame);
			panel.setPreferredSize(new Dimension(550,490));
			putInContainer(panel);
		}
	}
	/** 
	 * classe BoutonPlayListener qui implémente ActionListener
	 * listener du bouton "menu principal"
	 * change le contenu de la fenêtre par un panel de type "Menu"
	 * */
	class BoutonMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setContentPane(new Menu(frame));
			frame.validate();
		}
	}

}