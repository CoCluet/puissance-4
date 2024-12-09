/**
 * Programme contenant les méthodes de base pour jouer une partie.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import java.text.DateFormat;
import java.util.Date;
import exceptions.BadColumnException;

/**
 * classe Game
 * permet de créer des parties de puissance 4, sur lesquelles on peut réaliser différentes actions
 **/

public class Game {

	// **************************************************
	// Constantes 
	// **************************************************
	public static int WIDTH=7; // longueur de la grille
	public static int HEIGHT=6; // hauteur de la grille

	// **************************************************
	// Attributs privés
	// **************************************************
	private String data; // chaîne de caractère qui contient toutes les données de la partie 
	private String hostUsername; // nom d'utilisateur du joueur connecté (= null si aucun joueur connecté)
	private boolean isSaved;
	private String id; // identifiant de la partie (date)

	private String strGrille; // grille d'état de la partie sous forme de chaîne de caractères
	private int round; // numéro du tour (changement de tour à chaque jeton posé)
	private Player player1; // joueur 1 (celui qui joue en premier)
	private Player player2; // joueur 2

	private int[] tabJetons; // tableau qui contient le nombre de jetons dans chaque colonne de la grille
	private int[][] grille; // grille contenant les jetons 

	private Player winner=null; // gagnant de la partie (=null si pas de gagnant)

	// **************************************************
	// Constructeurs
	// **************************************************
	/**
	 * Constructeur pour créer une nouvelle partie
	 *  
	 * @param username Nom du joueur qui crée la partie
	 * @param player1 Joueur 1
	 * @param player2 Joueur 2
	 **/ 
	public Game(String username,Player player1,Player player2){
		Date date = new Date();
	    DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);
	    this.id=dateFormat.format(date);
	    this.round=1;
	    this.player1=player1;
	    this.player2=player2;
	    this.tabJetons=new int[Game.WIDTH];
	    this.grille=new int[Game.HEIGHT][Game.WIDTH];
		this.hostUsername=username;
		if (hostUsername!=null) updateData();
	}

	/**
	 * Constructeur pour recréer une partie déjà commencée
	 *  
	 * @param username Nom du joueur qui recrée la partie
	 * @param id Identifiant de la partie
	 * @param strGrille Grille au moment où la partie a été arrêtée
	 * @param round Tour au moment où la partie a été arrêtée
	 * @param player1 Joueur 1
	 * @param player2 Joueur 2
	 * 
	 **/ 
	public Game(String username,String id,String strGrille,int round,Player player1,Player player2) {
		this.hostUsername=username;
		this.player1=player1;
		this.player2=player2;
		this.id=id;
		this.strGrille=strGrille;
		this.grille=transformIntGrille(strGrille);
		this.round=round;
		initTabJetons();
		updateData();
	}

	// **************************************************
	// Méthodes
	// **************************************************
	/**
	 * Actualise les données de la partie
	 **/
	public void updateData() {
		updateStrGrille();
		data="";
		data+=hostUsername+";";
		data+=id+";";
		data+=strGrille+";";
		data+=round+";";
		data+=player1.getType()+":"+player1.getName()+";";
		data+=player2.getType()+":"+player2.getName();
		isSaved=true;
	}

	/**
	 * Actualise la chaîne de caractères correspondant à la grille
	 **/
	public void updateStrGrille(){
		this.strGrille="";
		for (int[]col : this.grille) {
			for (int b : col) {
				this.strGrille+=b;
			}
			this.strGrille+=":";
		}
	}

	/**
	 * Initialise le tableau des jetons à partir d'une grille
	 **/
	public void initTabJetons(){
		this.tabJetons=new int[Game.WIDTH];
		for (int i=0;i<Game.WIDTH;i++) {
			for (int j=0;j<Game.HEIGHT;j++) {
				if (grille[j][i]==1 || grille[j][i]==2) {
					this.tabJetons[i]+=1;
				}
			}
		}
	}

	/**
	 * Prend en entrée une grille en chaîne de caractères et renvoie une grille sous forme d'un tableau d'entiers
	 * 
	 * @param strGrille Grille sous forme de chaîne de caractères
	 * @return Grille sous forme de tableau d'entiers
	 **/
	public int[][] transformIntGrille(String strGrille) {
		// on crée la grille à partir de la chaîne de caractères
		String[] lignes=strGrille.split(":");
		String[][] tabGrilleStr=new String[6][7];
		int[][] tabGrille=new int[6][7];
		this.tabJetons=new int[7];
		for (int i=0;i<6;i++) {
			tabGrilleStr[i]=lignes[i].split("");
		}
		for (int i=0;i<6;i++) {
			for (int j=0;j<7;j++){
				tabGrille[i][j]=Integer.parseInt(tabGrilleStr[i][j]);
				if (tabGrille[i][j]>0) this.tabJetons[j]+=1;
			}
		}
		return tabGrille;
	}

	/**
	 * Ajoute un jeton dans la grille (crée une exception en cas de colonne invalide)
	 * 
	 * @param numColumn Numéro de la colonne choisie
	 * @param player Joueur possédant le jeton ajouté
	 **/
	public void addJeton(int numColumn,Player player) throws BadColumnException {
		if (this.tabJetons[numColumn]==6 || numColumn<0 || numColumn>6) {
			throw new BadColumnException(numColumn);
		}
		else {
			this.grille[tabJetons[numColumn]][numColumn]=player.getNum();
			this.tabJetons[numColumn]+=1;
			this.round+=1;
			if (checkWin(numColumn,player.getNum())) {
				this.winner=player;
			}
		}
		isSaved=false;
	}

	/**
	 * Vérifie si le joueur a gagné
	 * 
	 * @param column Numéro de la colonne choisie
	 * @param num Joueur possédant le dernier jeton ajouté
	 * @return Vrai en cas de victoire, faux sinon
	 **/
  	public boolean checkWin(int column,int num) {
  		// on définit (x,y) les coordonnées du dernier jeton posé
  		// puis on regarde dans les directions autour de ce jeton pour voir si il y en a 4 alignés
		int x=column;
		int y=Math.max(tabJetons[column]-1,0);

		// vérification sur la ligne y
		int suite=0;
		for(int i=0;i<Game.WIDTH;i++){
			suite=(grille[y][i]==num) ? suite+1 : 0;
			if (suite==4) return true;
		}
		// vérification sur la colomne x
		suite=0;
		for(int j=0;j<Game.HEIGHT;j++){
			suite=(grille[j][x]==num) ? suite+1 : 0;
			if (suite==4) return true;
		}
		// diagonale /
		suite=0;
		int min=Math.min(x,y);
		int max=Math.min(Game.WIDTH-1-x,Game.HEIGHT-1-y);
		for (int k=0;k<min+max+1;k++){
			suite=(grille[y-min+k][x-min+k]==num) ? suite+1 : 0;
			if (suite==4) return true;
		}
		// diagonale \
		suite=0;
		min=Math.min(Game.WIDTH-1-x,y);
		max=Math.min(x,Game.HEIGHT-1-y);
		for (int k=0;k<min+max+1;k++){
			suite=(grille[y-min+k][x+min-k]==num) ? suite+1 : 0;
			if (suite==4) return true;
		}
		return false;
	}

	/**
	 * Copie une partie 
	 * 
	 * @param game Partie à copier
	 **/
	public void copyGame(Game game) {
		int[][] copyGrille=new int[Game.HEIGHT][Game.WIDTH];
		int[] copyTabJetons=new int[Game.WIDTH];
		for (int x=0;x<Game.WIDTH;x++){
			for (int y=0;y<Game.HEIGHT;y++) {
				copyGrille[y][x]=game.getGrille()[y][x];
			}
			copyTabJetons[x]=game.getTabJetons()[x];
		}
		this.grille=copyGrille;
		this.tabJetons=copyTabJetons;
	}

	// **************************************************
	// Accesseurs et modificateurs
	// **************************************************

	/**
	 * Renvoie le gagnant
	 * 
	 * @return Gagnant
	 **/
	public Player getWinner(){
		return this.winner;
	}

	/**
	 * Modifie le gagnant
	 * 
	 * @param player Gagnant
	 **/
	public void setWinner(Player player){
		this.winner=player;
	}

	/**
	 * Renvoie la grille sous forme de tableau d'entiers
	 * 
	 * @return Grille (tableau d'entiers)
	 **/
	public int[][] getGrille(){
		return this.grille;
	}

	/**
	 * Renvoie la grille sous forme de chaîne de caractères
	 * 
	 * @return Grille (chaîne de caractères)
	 **/
	public String getStrGrille() {
		return strGrille;
	}

	/**
	 * Renvoie le tableau contenant le nombre de jetons par colonne
	 * 
	 * @return Tableau du nombre de jetons par colonne
	 **/
	public int[] getTabJetons(){
		return this.tabJetons;
	}

	/**
	 * Renvoie les données de la partie
	 * 
	 * @return Données de la partie
	 **/
	public String getData() {
		return this.data;
	}

	/**
	 * Renvoie le tour actuel
	 * 
	 * @return Tour actuel
	 **/
	public int getRound() {
		return this.round;
	}

	/**
	 * Renvoie le joueur 1
	 * 
	 * @return Joueur 1
	 **/
	public Player getPlayer1() {
		return this.player1;
	}

	/**
	 * Renvoie le joueur 2
	 * 
	 * @return Joueur 2
	 **/
	public Player getPlayer2() {
		return this.player2;
	}

	/**
	 * Renvoie l'état de la partie (sauvegardée ou non)
	 * 
	 * @return Vrai si la partie est sauvegardée, faux sinon
	 **/
	public boolean isSaved(){
		return this.isSaved;
	}
}