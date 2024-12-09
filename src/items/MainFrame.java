/**
 * Programme permettant de créer une fenêtre personnalisée.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import javax.swing.JFrame;

/** 
 * classe MainFrame héritée de JFrame
 * correspond à la fenêtre principale du jeu
 * */

public class MainFrame extends JFrame {
	public String account;
	/** 
	 * constructeur qui paramètre correctement la fenêtre
	 * */
	public MainFrame() {
		Session.initializeFiles();
		this.account=null;
		this.setTitle("Jeu Puissance 4");
		this.setSize(1080,720);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// on met un Panel de type "Menu" comme contentPane de la fenêtre
		this.setContentPane(new Menu(this));
	}

	/** 
	 * update
	 * actualise la fenêtre
	 * */
	public void update(){
		validate();
		repaint();
	}
	/** 
	 * getAccount
	 * renvoie le nom du compte connecté si il y en a un et null sinon
	 * */
	public String getAccount(){
		return this.account;
	}
	/** 
	 * connectAccount
	 * connecte un compte à la fenêtre
	 * */
	public void connectAccount(String name){
		this.account=name;
	}
	/** 
	 * disconnectAccount
	 * déconnecte le compte connecté à la fenêtre
	 * */
	public void disconnectAccount(){
		this.account=null;
	}
}