/**
 * Programme contenant les méthodes principales liées aux joueurs.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

public abstract class Player {

	// **************************************************
	// Attributs privés
	// **************************************************
	private String name;
	private int numPlayer; // joueur 1 ou 2
	private int typePlayer; // 0 : ordinateur, 1 : humain
	private int choice=-1; // choix de la colonne

	/**
	 * Constructeur paramétré
	 *  
	 * @param name Nom du joueur
	 * @param num Numéro de joueur
	 * @param type Type de joueur (ordinateur ou humain)
	 * 
	 **/
	public Player(String name,int num,int type){
		this.name=name;
		this.numPlayer=num;
		this.typePlayer=type;
	}

	// **************************************************
	// Accesseurs
	// **************************************************

	/**
	 * Retourne le nom du joueur
	 *  
	 * @return Nom du joueur
	 **/
	public String getName() {
		return this.name;
	}

	/**
	 * Retourne le numéro du joueur
	 *  
	 * @return Numéro du joueur
	 **/
	public int getNum() {
		return this.numPlayer;
	}

	/**
	 * Retourne le type du joueur (ordinateur ou humain)
	 *  
	 * @return Type du joueur (ordinateur ou humain)
	 **/
	public int getType() {
		return this.typePlayer;
	}

	/**
	 * Retourne le choix effectué par le joueur
	 *  
	 * @return Choix du joueur
	 **/
	public int getChoice(){
		return this.choice;
	}

	/**
	 * Permet de choisir le prochain coup
	 *  
	 * @param n Numéro de la colonne choisie pour le prochain coup
	 **/
	public void setChoice(int n){
		this.choice=n;
	}

	/**
	 * Méthode utilisée pour jouer un coup.
	 * 
	 * @param g le moteur de jeu de la partie actuelle
	 * 
	 **/
	public abstract void play(GameEngine g) ;
}