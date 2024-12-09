/**
 * Programme contenant la classe qui permet de créer des boutons personnalisés.
 * 
 * @author Cluet - Marchelli
 * 
 **/
package items;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** 
 * classe Bouton qui hérite de JButton
 * sert à créer des boutons personnalisés, pour plus d'esthétique
 * */

public class Bouton extends JButton {
	// **************************************************
	// Constantes : listes de couleurs 
	// **************************************************
	public static Color[] ORANGE=new Color[]{new Color(255,200,100),new Color(240,150,0),new Color(190,120,0)};
	public static Color[] GREEN=new Color[]{new Color(114,245,131),new Color(0,185,40),new Color(0,160,35)};
	public static Color[] RED=new Color[]{new Color(251,108,112),new Color(237,28,35),new Color(190,15,23)};
	// **************************************************
	// Attributs
	// **************************************************
	
	/** 
	 * attributs définis dans le constructeur
	 * */
	private String text;
	private boolean over;
	private int longueur;
	private int largeur;
	private int fontSize;
	private int stroke=2;
	private int radius;
	/** 
	 * listes des couleurs du bouton
	 * */
	private Color[] colors;
	/** 
	 * première couleur du bouton
	 * */
	private Color backgroundColor1;
	/** 
	 * deuxième couleur du bouton
	 * */
	private Color backgroundColor2;
	/** 
	 * couleur du contour du bouton
	 * */
	private Color borderColor;

	// **************************************************
	// Constructeur
	// **************************************************

	/** 
	 * Constructeur
	 * @param str : texte affiché sur le bouton
	 * @param longueur : longueur du bouton
	 * @param hauteur : hauteur du bouton
	 * @param radius : rayon des arrondis des coins du bouton
	 * @param fontSize : taille de la police d'écriture du texte
	 * @param colors : liste des couleurs qui colorieront le bouton (prendre une variable statique)
	 * */
	public Bouton(String str,int longueur,int hauteur,int radius,int fontSize,Color[]colors) {
		this.text=str;
		this.colors=colors;
		this.radius=radius;
		this.fontSize=fontSize;

		backgroundColor1=colors[0];
		backgroundColor2=colors[1];
		borderColor=colors[2];

		this.setPreferredSize(new Dimension(longueur,hauteur));
		this.setContentAreaFilled(false);
		this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		this.addMouseListener(new MouseAdapter() {
			/** 
			 * lorsque l'utilisateur passe sa souris sur le bouton, on échange les deux couleurs du background
			 * */
            @Override
            public void mouseEntered(MouseEvent me) {
                backgroundColor1=colors[1];
				backgroundColor2=colors[0];
                stroke=3;
                over = true;
            }
            /** 
			 * lorsque l'utilisateur enlève sa souris du bouton, le bouton reprend son apect initial
			 * */
            @Override
            public void mouseExited(MouseEvent me) {
                backgroundColor1=colors[0];
				backgroundColor2=colors[1];
                stroke=2;
                over = false;
            }
            /** 
			 * lorsque l'utilisateur clique, le bouton devient uni
			 * */
            @Override
            public void mousePressed(MouseEvent me) {
                backgroundColor1=colors[1];
				backgroundColor2=colors[1];
				stroke=4;
            }
            /** 
			 * lorsque l'utilisateur relâche son clic, on recolorie le bouton
			 * */
            @Override
            public void mouseReleased(MouseEvent me) {
            	// si l'utilisateur a sa souris sur le bouton
                if (over) {
                    backgroundColor1=colors[1];
					backgroundColor2=colors[0];
                    stroke=3;
                } // si l'utilisateur a sa souris à l'extérieur de la zone du bouton
                else {
                    backgroundColor1=colors[0];
					backgroundColor2=colors[1];
                    stroke=2;
                }
            }
        });
	}

	// **************************************************
	// Méthode
	// **************************************************

	/** 
	 * méthode override paintComponent
	 * "dessine" sur le bouton selon l'état du bouton et les options choisies dans le constructeur
	 * */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d=(Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(0,0,0,30));
		g2d.fillRoundRect(4,4,this.getWidth()-8,this.getHeight()-8,20,20);
		g2d.setPaint(new GradientPaint(0,0,backgroundColor1,0,getHeight(),backgroundColor2));
		g2d.fillRoundRect(2,2,this.getWidth()-4,this.getHeight()-10,20,20);
		g2d.setStroke(new BasicStroke(stroke));
		g2d.setColor(borderColor);
		g2d.drawRoundRect(2,2,this.getWidth()-4,this.getHeight()-10,20,20);
		g2d.setColor(Color.white);
		Font font = new Font("Corbel", Font.BOLD, fontSize);
   		g2d.setFont(font);
		int width = (int) (getWidth()/2 - g2d.getFontMetrics(font).stringWidth(text)/2);
		int height= (int) (getHeight()/2 + g2d.getFontMetrics(font).getAscent()/4);
		g2d.drawString(text,width,height);
	}
}
