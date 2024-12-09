/**
 * Programme permettant de gérer les sessions.
 * 
 * @author Cluet - Marchelli
 * 
 **/

package items;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.net.URI;
import java.net.URISyntaxException;

public class Session {

	// **************************************************
	// Attributs 
	// **************************************************
	public static Path LOGIN_FILE; // contient la liste des comptes
	public static Path GAMES_FILE; // contient la liste des parties

	// **************************************************
	// Méthodes publiques
	// **************************************************

	/**
	 * Ajoute un nouveau compte à la liste des comptes
	 * 
	 * @param username Pseudonyme du joueur
	 * @param password Mot de passe
	 **/
	public static void initializeFiles() {
		Session object=new Session();
		URI uri1,uri2;
		try {
			uri1=object.getClass().getResource("../text_files/login.myp4.txt").toURI();
			uri2=object.getClass().getResource("../text_files/games.myp4.txt").toURI();
			Session.LOGIN_FILE=Paths.get(uri1);
			Session.LOGIN_FILE=Paths.get(uri2);
		}
		catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

	public static void addAccount(String username,String password){
		try (BufferedWriter w=Files.newBufferedWriter(Session.LOGIN_FILE,
			StandardOpenOption.CREATE,StandardOpenOption.APPEND)) {
			w.write(username+":"+password);
			w.newLine();
		} catch (IOException ioe) {
			System.out.println("exception dans addAccount : "+ioe);
		}
	}

	/**
	 * Vérifie les identifiants.
	 * 
	 * @param username Pseudonyme du joueur
	 * @param password Mot de passe
	 * @return Vrai si les identifiants sont corrects, faux sinon
	 **/
	public static boolean checkAccount(String username,String password) {
		try (BufferedReader reader = Files.newBufferedReader(Session.LOGIN_FILE)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] id=line.split(":");
				if (username.equals(id[0])&&password.equals(id[1])) return true;
			}
		} catch (IOException ioe) {
			System.out.println("exception dans checkAccount : "+ioe);
			return false;
		}
		return false;
	}

	/**
	 * Vérifie si le pseudonyme est disponible.
	 * 
	 * @param username Pseudonyme choisi
	 * @return Vrai si le pseudonyme n'est pas pris, faux sinon
	 **/
	public static boolean checkUsername(String username){
		try (BufferedReader reader = Files.newBufferedReader(Session.LOGIN_FILE)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String usernameLine=line.split(":")[0];
				if (username.equals(usernameLine)) return false;
			}
		} catch (IOException ioe) {
			System.out.println("exception dans checkUsername : "+ioe);
			return false;
		}
		return true;
	}

	/**
	 * Ajoute une partie à la liste des parties.
	 * 
	 * @param game Partie en question
	 **/
	public static void addGame(Game game) {
		try (BufferedWriter w=Files.newBufferedWriter(Session.GAMES_FILE,
			StandardOpenOption.CREATE,StandardOpenOption.APPEND)) {
			w.write(game.getData());
			w.newLine();
		} catch (IOException ioe) {
			System.out.println("exception dans addGame : "+ioe);
		}
	}

	/**
	 * actualise les données d'une partie (remplacement des anciennes données par les nouvelles)
	 * et ajoute la partie à la liste des parties si elle n'existe pas
	 * 
	 * @param game Partie en question
	 **/
	public static void updateGame(Game game){
		String idGame=game.getData().split(";")[1];
		ArrayList<String> gamesList=new ArrayList<String>();
		boolean gameExists=false;
		try (BufferedReader reader = Files.newBufferedReader(Session.GAMES_FILE)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String idLine=line.split(";")[1];
				if (idGame.equals(idLine)) {
					gamesList.add(game.getData());
					gameExists=true;
				}
				else {
					gamesList.add(line);
				}
			}
		} catch (IOException ioe) {
			System.out.println("exception dans updateGame : "+ioe);
		}
		try {
			Files.write(Session.GAMES_FILE, gamesList, StandardOpenOption.TRUNCATE_EXISTING);
		} catch(IOException ioe) {
			System.out.println("exception dans updateGame : "+ioe);
		}
		if (!gameExists) Session.addGame(game);
	}

	/**
	 * Supprime une partie de la liste des parties
	 * 
	 * @param game Partie à supprimer
	 **/
	// supprime une partie de la liste des parties
	public static void removeGame(Game game){
		String idGame=game.getData().split(";")[1];
		ArrayList<String> gamesList=new ArrayList<String>();

		try (BufferedReader reader = Files.newBufferedReader(Session.GAMES_FILE)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String idLine=line.split(";")[1];
				if (!idGame.equals(idLine)) {
					gamesList.add(line);
				}
			}
		} catch (IOException ioe) {
			System.out.println("exception dans removeGame : "+ioe);
		}

		try {
			Files.write(Session.GAMES_FILE, gamesList, StandardOpenOption.TRUNCATE_EXISTING);
		} catch(IOException ioe) {
			System.out.println("exception dans removeGame : "+ioe);
		}
	}

	/**
	 *Renvoie la liste des parties enregistrées d'un utilisateur
	 * 
	 * @param username Pseudo de l'utilisateur dont on veut voir les parties
	 * @return Liste des parties enregistrées de l'utilisateur
	 **/
	public static ArrayList<String> getListGames(String username){
		ArrayList<String> gamesList=new ArrayList<String>();

		try (BufferedReader reader = Files.newBufferedReader(Session.GAMES_FILE)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String usernameLine=line.split(";")[0];
				if (usernameLine.equals(username)) {
					gamesList.add(line);
				}
			}
		} catch (IOException ioe) {
			System.out.println("exception dans updateGame : "+ioe);
		}
		return gamesList;
	}

}