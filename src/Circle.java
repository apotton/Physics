import java.awt.Color;
import java.util.Random;

public class Circle {
    /**
     * Couleur du cercle
     */
    public Color couleur;

    /**
     * Position actuelle du cercle
     */
    public Position centre;

    /**
     * Position du cercle à l'intervalle de temps précédent
     */
    public Position centre_precedent;

    /**
     * Rayon du cercle
     */
    public double rayon;

    /**
     * Coordonnées du cercle dans le quadrillage
     */
    public int coordonnees[];

    /**
     * Index du cercle pour les repérer dans le quadrillage
     */
    public int index;

    /**
     * Le nombre de cercles ajoutés au total
     */
    public static int COUNT = 0;

    /**
     * Création d'un cercle étant données ses paramètres
     * 
     * @param rayon  Rayon du cercles
     * @param centre Position de son centre
     * @param col    Couleur du cercle
     */
    public Circle(int rayon, Position centre, Color col) {
        this.rayon = rayon;
        this.centre = centre;
        this.centre_precedent = centre;
        this.couleur = col;
        this.coordonnees = new int[2];
    }

    /**
     * Affiche la position du centre du cercle et son rayon
     */
    @Override
    public String toString() {
        String ret = "Centre :" + centre.toString() + " rayon: " + rayon;
        return ret;
    }

    /**
     * Génère un cercle aléatoire
     */
    public Circle() {
        // Déclaration des variables
        Random rand = new Random();
        Color coul;
        double rayon;
        Position centre_a;

        // Instanciation de la couleur
        if (AnimationMain.COULEUR_ALEATOIRE) {
            // Si la couleur est aléatoire, on génère des nombres aléatoires
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();

            coul = new Color(r, g, b);
        } else {
            // Sinon, la classe RGB la genère
            coul = RGB.RGBsuivante();
        }

        // Instanciation du rayon
        if (AnimationMain.RAYON_ALEATOIRE) {
            rayon = rand.nextDouble() * (AnimationMain.RAYON_MAX - AnimationMain.RAYON_MIN) + AnimationMain.RAYON_MIN;
        } else {
            rayon = AnimationMain.RAYON;
        }

        // Instanciation du centre
        Position centre = new Position(AnimationMain.WINDOW_SIZE / 2, AnimationMain.WINDOW_SIZE / 3);

        // Instanciation de la position précédente selon la vitesse
        if (AnimationMain.DIRECTION_ALEATOIRE) {
            centre_a = new Position(
                    AnimationMain.WINDOW_SIZE / 2 - (rand.nextDouble() - 0.5) * 2 * AnimationMain.VITESSE_INIT,
                    AnimationMain.WINDOW_SIZE / 3 - (rand.nextDouble()) * 2 * AnimationMain.VITESSE_INIT);
        } else {
            centre_a = new Position(AnimationMain.WINDOW_SIZE / 2 - AnimationMain.VITESSE_INIT,
                    AnimationMain.WINDOW_SIZE / 3 - 0.5 * AnimationMain.VITESSE_INIT);

        }

        // Déclaration et instanciation de la position du cercle sur le quadrillage
        int n_x;
        int n_y;

        if (AnimationMain.RAYON_ALEATOIRE) {
            n_x = (int) (centre.x / AnimationMain.RAYON_MAX) / 2 + 1;
            n_y = (int) (centre.y / AnimationMain.RAYON_MAX) / 2 + 1;
        } else {
            n_x = (int) centre.x / AnimationMain.RAYON / 2 + 1;
            n_y = (int) centre.y / AnimationMain.RAYON / 2 + 1;
        }

        // Passage des grandeurs au cercle créé
        this.coordonnees = new int[2];
        this.coordonnees[0] = n_x;
        this.coordonnees[1] = n_y;
        this.index = COUNT;
        this.couleur = coul;
        this.centre_precedent = centre_a;
        this.centre = centre;
        this.rayon = rayon;

        // On incrémente le compteur de cercles
        COUNT++;
    }

    /**
     * Change la position de deux cercles qui se superposent
     * 
     * @param cercle1 Le premier cercle
     * @param cercle2 Le second cercle
     */
    static public void reglerCollision(Circle cercle1, Circle cercle2) {
        // Si les cercles sont le même, il n'y a pas de problème
        if (cercle1 == cercle2) {
            return;
        }

        // Calcul de la distance entre les cercles, puis de la distance limite
        double distance_min = cercle1.rayon + cercle2.rayon;
        double distance = Position.distance(cercle1.centre, cercle2.centre);

        // Si les deux cercles ne se chevauchent pas, on quitte la fonction
        if (distance >= distance_min) {
            return;
        }

        // Calcul de la normale du choc
        Position normale = new Position(Position.deplacement(cercle1.centre, cercle2.centre));
        normale.normaliser();

        // Calcul des proportions de recul pour la conservation de la quantité de
        // mouvement (masse = rayon)
        double ratio1 = cercle1.rayon / distance_min;
        double ratio2 = cercle2.rayon / distance_min;
        double delta = (distance - distance_min) * 0.5;

        // Mise à jour de la position 1
        cercle1.centre.x += (ratio2 * delta) * normale.x;
        cercle1.centre.y += (ratio2 * delta) * normale.y;

        // Mise à jour de la position 2
        cercle2.centre.x -= (ratio1 * delta) * normale.x * 1;
        cercle2.centre.y -= (ratio1 * delta) * normale.y * 1;
    }
}
