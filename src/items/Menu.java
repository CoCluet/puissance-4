/**
 * Programme permettant de créer un panel correspondant au menu principal.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;


/** 
 * classe Menu qui hérite de JPanel
 * représente un menu avec deux boutons (jouer et quitter) 
 * */

public class Menu extends JPanel {
	// **************************************************
	// Attribut
	// **************************************************

	/** 
	 * variable de type JFrame correspondant à la fenêtre principale
	 * */
	private MainFrame frame;

	// **************************************************
	// Constructeur 
	// **************************************************

	/** 
	 * constructeur
	 * @param frame désigne la fenêtre courante
	 * */
	public Menu(MainFrame frame) {
		this.frame=frame;
		this.setBackground(new Color(150,220,235));
		initComponents();
	}

	// **************************************************
	// Méthodes
	// **************************************************

	/** 
	 * initComponents
	 * initialise et place les composants dans le panel
	 * */
	public void initComponents(){
		// utilisation d'un SpringLayout pour centrer le panel horizontalement et verticalement
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER,500,10));
		buttonsPanel.setPreferredSize(new Dimension(400,210));

		Bouton playButton=new Bouton("JOUER",340,100,20,28,Bouton.GREEN);
		playButton.addActionListener(new BoutonPlayListener());

		Bouton exitButton=new Bouton("QUITTER",220,80,20,20,Bouton.ORANGE);
		exitButton.addActionListener(new BoutonExitListener());

		buttonsPanel.add(playButton);
		buttonsPanel.add(exitButton);

		layout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonsPanel,0, SpringLayout.VERTICAL_CENTER, this);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, buttonsPanel,0, SpringLayout.HORIZONTAL_CENTER, this);

		this.add(buttonsPanel);
	}

	// **************************************************
	// Classes (Listener)
	// **************************************************

	/** 
	 * classe BoutonPlayListener qui implémente ActionListener
	 * listener du bouton "jouer"
	 * change le contenu principal par un nouveau panel de type "MainPanel"
	 * */
	class BoutonPlayListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setContentPane(new MainPanel(frame));
			frame.validate();
		}
	}
	/** 
	 * classe BoutonExitListener qui implémente ActionListener
	 * listener du bouton "quitter"
	 * ferme la fenêtre
	 * */
	class BoutonExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
}