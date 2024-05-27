import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimationMain extends JPanel implements ActionListener {
	/// Paramètres de la simulation
	static final int WINDOW_SIZE = 800; // Taille de la fenêtre
	static final int DUREE_IMAGES = 10; // Durée d'affichage de chaque image

	static final int SUBSTEPS = 5; // Nombre de calculations par image

	static final int NOMBRE_PARTICULES = 2000; // Nombre de particules
	static final int IMAGES_ENTRE_PARTICULES = 1; // Nombre d'images avant d'ajouter une particule

	static final boolean RAYON_ALEATOIRE = true; // Génération de particules à rayon aléatoire
	static final int RAYON_MAX = 10; // Rayon maximal des particules générées
	static final int RAYON_MIN = 5; // Rayon minimal
	static final int RAYON = 5; // Rayon tout court, si il n'est pas aléatoire

	static final boolean COULEUR_ALEATOIRE = false; // Génération de couleur aléatoire, arc-en-ciel sinon

	static final int VITESSE_INIT = 5; // Vitesse initiale des particules
	static final boolean DIRECTION_ALEATOIRE = false; // Spawn en direction aléatoire
	static final int GRAVITE = 8; // Gravité qui s'applique aux particules

	Timer timer; // Element Java pour les animations
	public Scene scene = new Scene(); // Objet contenant tous les cercles
	static int compteur; // Compteur de frames
	static int nbParticules = 0; // Nombre de particules ajoutées
	public long temps = System.nanoTime(); // Heure système en nanosecondes
	double fps = 0; // Nombre de fps

	void paint_scene(Graphics2D g) {
		// Peindre l'arrière plan pour que rien ne dépasse
		g.setColor(g.getBackground());
		g.fillRect((int) (scene.contrainte.centre.x - scene.contrainte.rayon),
				(int) (scene.contrainte.centre.y - scene.contrainte.rayon), (int) scene.contrainte.rayon * 3,
				(int) scene.contrainte.rayon * 3);

		// Peindre le centre
		g.setColor(Color.WHITE);
		g.fillOval((int) (scene.contrainte.centre.x - scene.contrainte.rayon),
				(int) (scene.contrainte.centre.y - scene.contrainte.rayon), (int) scene.contrainte.rayon * 2,
				(int) scene.contrainte.rayon * 2);

		// Peindre l'extérieur de la scène
		g.setColor(Color.BLACK);
		g.drawOval((int) (scene.contrainte.centre.x - scene.contrainte.rayon),
				(int) (scene.contrainte.centre.y - scene.contrainte.rayon), (int) scene.contrainte.rayon * 2,
				(int) scene.contrainte.rayon * 2);

		// Peindre chaque cercle
		for (int i = 0; i < Scene.NOMBRE_CERCLES_AJOUTES; i++) {
			Circle cercle = scene.objets[i];
			g.setColor(cercle.couleur);
			g.fillOval((int) (cercle.centre.x - cercle.rayon), (int) (cercle.centre.y - cercle.rayon),
					(int) cercle.rayon * 2, (int) cercle.rayon * 2);
		}

		// Affichage des stats
		g.setColor(Color.BLACK);
		g.drawString(Double.toString(Double.valueOf(String.valueOf(((int) (fps * 1000)))) / 1000), 0, 10);
		g.drawString(String.valueOf(Scene.NOMBRE_CERCLES_AJOUTES), 0, 25);
	}

	/**
	 * Initialisation du minuteur
	 */
	public AnimationMain() {
		timer = new Timer(DUREE_IMAGES, this);
		timer.start();
	}

	public void paint(Graphics g) {
		// Pour calculer les fps, on fait une moyenne toutes les cinq images
		if (compteur % 5 == 1) {
			temps = System.nanoTime();
		}

		// Render de la scène
		Graphics2D g2d = (Graphics2D) g;
		paint_scene(g2d);

		// Réalisation de plusieurs updates
		for (int i = 0; i < SUBSTEPS; i++) {
			scene.maj(0.1 / SUBSTEPS);
		}

		compteur++;

		// Ajout de particules
		if ((compteur % IMAGES_ENTRE_PARTICULES == 0) && (Scene.NOMBRE_CERCLES_AJOUTES < NOMBRE_PARTICULES)) {
			nbParticules++;
			scene.ajouterCercleAleatoire();
		}

		// Calcul de la moyenne des fps sur cinq images
		if (compteur % 5 == 0) {
			temps = System.nanoTime() - temps;
			fps = 5 / ((double) temps / Math.pow(10, 9));
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Univers"); // Création de la fenêtre
		frame.add(new AnimationMain()); // Ajout de l'animation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Arrêt du programme à la fermeture de la fenêtre
		frame.setSize(WINDOW_SIZE, WINDOW_SIZE + 50); // Taille de la fenêtre
		frame.setLocationRelativeTo(null); // Position de la fenêtre
		frame.setVisible(true); // Afficher la fenêtre
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}
}