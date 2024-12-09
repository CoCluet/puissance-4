/**
 * Programme contenant la classe permettant de jouer contre un ordinateur et les méthodes nécessaires.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import java.util.ArrayList;
import exceptions.BadColumnException;

public class ComputerPlayer extends Player {

	/** 
	 * Constructeur permettant de créer un joueur ordinateur
	 * 
	 * @param num Numéro de joueur
	 **/
	public ComputerPlayer(int num) {
		super("Computer",num,0);
	}

	// **************************************************
	// Méthodes
	// **************************************************

	/** 
	 * Méthode appelée lorsque c'est au tour du joueur de jouer
	 * @param g le moteur de jeu de la partie actuelle
	 **/
	public void play(GameEngine g) {
		g.stopRunning();
		int column=minimax(g.getGame(),4,true)[0];
		setChoice(column);
		g.currentPlayerPlay();
		g.run();
	}

	/** 
	 * Méthode contenant le code le l'algorithme minimax, permettant à l'ordinateur de trouver les meilleurs coups
	 * selon certains paramètres.
	 * 
	 * @param game Partie actuelle
	 * @param depth Profondeur de l'arbre théorique de jeu, i.e. combien de coups d'avance l'ordinateur prévoit.
	 * @param isMaximizingPlayer Vrai si on doit supposer lors de l'appel qu'on prend le meilleur coup pour l'ordi, faux sinon
	 * 
	 * @return Tableau contenant les valeurs calculées par l'algorithme
	 **/
	public int[] minimax(Game game,int depth,boolean isMaximizingPlayer) {
		Player opponent=(this.getNum()==1) ? game.getPlayer2() : game.getPlayer1();

		int score;
		int column;

		int[] values=new int[2];

		if (depth==0) {
			values[1]=evaluateGame(game,this)-evaluateGame(game,opponent);
			return values;
		}

		if (isLeaf(game)) {
			for (int col=0;col<Game.WIDTH;col++) {
				if (game.checkWin(col,this.getNum())) {
					values[1]=Integer.MAX_VALUE;
					return values;
				}
				if (game.checkWin(col,opponent.getNum())) {
					values[1]=Integer.MIN_VALUE;
					return values;
				}
			} 
			values[1]=0; // la grille est pleine -> match nul
			return values;
		}

		if (isMaximizingPlayer) {
			score=Integer.MIN_VALUE;
			column=getBestMove(game,this);

			Game testGame=new Game(null,game.getPlayer1(),game.getPlayer2());

			for (int col=0;col<Game.WIDTH;col++) {
				testGame.copyGame(game);
				try {
					testGame.addJeton(col,this);
				}
				catch (BadColumnException bce) {
					continue;
				}
				// on évalue le coup
				int testScore=minimax(testGame,depth-1,false)[1];
				// si le score obtenu est plus élevé, la variable score prend sa valeur
				if (testScore>score) {
					score=testScore;
					column=col;
				}
				if (testScore==Integer.MAX_VALUE) break;
			}
			values[0]=column;
			values[1]=score;
			return values;
		}
		else {
			score=Integer.MAX_VALUE;
			column=getBestMove(game,this);

			Game testGame=new Game(null,game.getPlayer1(),game.getPlayer2());

			for (int col=0;col<Game.WIDTH;col++) {
				testGame.copyGame(game);
				try {
					testGame.addJeton(col,opponent);
				}
				catch (BadColumnException bce) {
					continue;
				}
				// on évalue le coup
				int testScore=minimax(testGame,depth-1,true)[1];
				// si le score obtenu est plus élevé, la variable score prend sa valeur
				if (testScore<score) {
					score=testScore;
					column=col;
				}
				if (testScore==Integer.MIN_VALUE) break;
			}
			values[0]=column;
			values[1]=score;
			return values;
		}
	}

	/** 
	 * Détermine si l'état d'une partie est finale (i.e. si un des 2 joueurs a gagné ou si la grille est pleine)
	 * 
	 * @param game Etat de la partie traitée
	 * @return Vrai si l'état de la partie est final, faux sinon.
	 **/
	public boolean isLeaf(Game game) {
		boolean isLeaf=false;
		for (int col=0;col<Game.WIDTH;col++) {
			if (game.checkWin(col,1) || game.checkWin(col,2) || game.getRound()==43) {
				isLeaf=true;
			}
		}
		return isLeaf;
	}

	/** 
	 * Détermine le meilleur coup possible 
	 * 
	 * @param game Partie actuelle
	 * @param player Joueur pour lequel on détermine le meilleur coup
	 * @return Numéro de colonne du meilleur coup possible
	 **/
    public int getBestMove(Game game,Player player) {
		int column=0;
		int score=0;
		// on va évaluer chaque mouvement possible et renvoyer celui qui a le score le plus haut
		for (int col=0;col<Game.WIDTH;col++) {
			Game testGame=new Game(null,game.getPlayer1(),game.getPlayer2());
			testGame.copyGame(game);
			try {
				testGame.addJeton(col,player);
			}
			catch (BadColumnException bce) {
				continue;
			}
			int testScore=evaluateGame(testGame,player);
			if (testScore>score) {
				score=testScore;
				column=col;
			}
		}
		return column;
    }

	/** 
	 * Evalue un état de la partie en combinant les méthodes d'évaluation qui suivent
	 * 
	 * @param game Etat de la partie
	 * @param player Joueur pour lequel on évalue l'état
	 * @return Score de l'état évalué
	 **/
	public int evaluateGame(Game game,Player player) {
		int score=0;
		score+=evaluateRows(game,player);
		score+=evaluateColumns(game,player);
		score+=evaluateDiagonales(game,player);
		return score;
	}

	/** 
	 * Renvoie un score à un moment de la partie. Utilisé dans les méthodes d'évaluation suivantes
	 * pour évaluer chaque ligne, colonne, ou diagonale.
	 * 
	 * @param game Etat de la partie
	 * @param player Joueur pour lequel on évalue l'état
	 * @return Score de l'état évalué
	 **/
	public int evaluateList(int[] list,Player player){
		int score=0;
		int suiteCases=0;
		int suiteJetons=0;

		for (int o : list) {
			if (o==0) { // case vide
				suiteCases+=1;
			}
			else if (o==player.getNum()) { // jeton du joueur
				suiteCases+=1;
				suiteJetons+=1;
			}
			else { // jeton adverse
				if (suiteCases>=4) {
					if (suiteJetons==1) score+=5;
					if (suiteJetons==2) score+=30;
					if (suiteJetons==3) score+=100;
					if (suiteJetons>3) score+=10000;
				}
				suiteCases=0;
				suiteJetons=0;
			}
		}
		if (suiteCases>=4) {
			if (suiteJetons==1) score+=5;
			if (suiteJetons==2) score+=30;
			if (suiteJetons==3) score+=100;
			if (suiteJetons>3) score+=10000;
		}
		return score;
	}

	/** 
	 * Evalue l'état de la partie en prenant en compte les alignements horizontaux possibles.
	 * 
	 * @param game Etat de la partie
	 * @param player Joueur pour lequel on évalue l'état
	 * @return Score (lignes)
	 **/
	 public int evaluateRows(Game game,Player player) {
		int score=0;
		int[] tabJetons=game.getTabJetons();
		int max=0;
		for (int i : tabJetons) if (i>max) max=i;

		for (int line=0;line<max;line++) {
			score+=evaluateList(game.getGrille()[line],player);
		}
		return score;
	} 

	/** 
	 * Evalue l'état de la partie en prenant en compte les alignements verticaux possibles.
	 * 
	 * @param game Etat de la partie
	 * @param player Joueur pour lequel on évalue l'état
	 * @return Score (colonnes)
	 **/
	public int evaluateColumns(Game game,Player player) {
		int score=0;
		for (int x=0;x<Game.WIDTH;x++) {
			int[] column=new int[6];
			for (int y=0;y<Game.HEIGHT;y++) {
				column[y]=game.getGrille()[y][x];
			}
			score+=evaluateList(column,player);
		}
		return score;
	} 

	/** 
	 * Evalue l'état de la partie en prenant en compte les alignements diagonaux possibles.
	 * 
	 * @param game Etat de la partie
	 * @param player Joueur pour lequel on évalue l'état
	 * @return Score (diagonales)
	 **/
	public int evaluateDiagonales(Game game,Player player) {
		int score=0;
		ArrayList<int[]> diags=new ArrayList<int[]>(); // liste contenant les 12 diagonales (sous forme de tableau)
		for (int i=4;i<=6;i++) {
			for (int k=0;k<4;k++) { // 4 diagonales de 4 cases, 4 de 5 cases, 4 de 6 cases
				diags.add(new int[i]);
			}
		}
		for (int i=0;i<4;i++) {
			diags.get(0)[i]=game.getGrille()[2+i][i];
			diags.get(1)[i]=game.getGrille()[i][3+i];
			diags.get(2)[i]=game.getGrille()[3-i][i];
			diags.get(3)[i]=game.getGrille()[5-i][3+i];
		}
		for (int i=0;i<5;i++) {
			diags.get(4)[i]=game.getGrille()[1+i][i];
			diags.get(5)[i]=game.getGrille()[i][2+i];
			diags.get(6)[i]=game.getGrille()[4-i][i];
			diags.get(7)[i]=game.getGrille()[5-i][2+i];
		}
		for (int i=0;i<6;i++) {
			diags.get(8)[i]=game.getGrille()[i][i];
			diags.get(9)[i]=game.getGrille()[i][1+i];
			diags.get(10)[i]=game.getGrille()[5-i][i];
			diags.get(11)[i]=game.getGrille()[5-i][1+i];
		}
		for (int[] diag : diags) {
			score+=evaluateList(diag,player);
		}
		return score;
	} 
}