/**
 * Programme contenant la classe permettant de jouer contre et en tant qu'humain et les méthodes nécessaires.
 * 
 * @author Cluet - Marchelli
 * 
 **/
package items;

public class HumanPlayer extends Player {

	/**
	 * Constructeur paramétré
	 *  
	 * @param name Nom du joueur
	 * @param num Numéro de joueur
	 * 
	 **/
	public HumanPlayer(String name,int num) {
		super(name,num,1);
	}

	/**
	 * Méthode appelée lorsque c'est au tour du joueur de jouer
	 * 
	 * @param g La partie actuelle
	 * 
	 **/
	public void play(GameEngine g) {
		// Méthode vide car le joueur choisit la colonne à l'aide de l'interface graphique
	}

}