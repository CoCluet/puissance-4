/**
 * Programme lié aux exceptions BadColumnException, qui se produisent lorsque la colonne choisie par une méthode
 * est invalide.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package exceptions;

/**
 * Classe BadColumnException héritant de Exception
 * exception qui affiche un message personalisé
 **/

public class BadColumnException extends Exception {
	/**
	 * variable qui désigne la colonne ayant déclenchée l'exception
	 **/
	private int col;

	/**
	 * Constructeur
	 * @param col Numéro de colonne
	 **/
	public BadColumnException(int col) {
		this.col=col;
	}
	
	/**
	 * Renvoie un texte adapté lorsque l'exception se produit
	 * @return Raison de l'exception
	 **/
	public String toString() {
		return "Vous ne pouvez pas jouer sur la colonne "+(this.col+1)+".";
	}
}