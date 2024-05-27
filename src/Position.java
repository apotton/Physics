/**
 * Simple classe représentant une position dans un repère cartésien
 */
public class Position {

    public double x;
    public double y;

    /**
     * Crée une position
     * 
     * @param x0 L'abscisse
     * @param y0 L'ordonnée
     */
    public Position(double x0, double y0) {
        this.x = x0;
        this.y = y0;
    }

    /**
     * Duplique une position
     * 
     * @param p0 La position initiale
     */
    public Position(Position p0) {
        this.x = p0.x;
        this.y = p0.y;
    }

    /**
     * Calcule la norme d'un vecteur
     * 
     * @return La norme euclidienne
     */
    public double distanceToOrigin() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    /**
     * Calcul de la distance entre deux positions
     * 
     * @param p1 La première position
     * @param p2 La deuxième position
     * @return La distance entre les deux
     */
    static public double distance(Position p1, Position p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    /**
     * Affiche l'abscisse et l'ordonnée
     */
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * Calcule la différence entre deux positions
     * 
     * @param ancienne La première position
     * @param actuelle La deuxième position
     * @return actuelle-ancienne
     */
    public static Position deplacement(Position ancienne, Position actuelle) {
        return new Position(actuelle.x - ancienne.x, actuelle.y - ancienne.y);
    }

    // Calcul du produit scalaire euclidien entre deux vecteurs
    /**
     * Calcule le produit scalaire de deux vecteurs
     * 
     * @param p1 Le premier vecteur
     * @param p2 Le deuxième vecteur
     * @return Le produit scalaire euclidien entre les deux
     */
    public static double produitScalaire(Position p1, Position p2) {
        return p1.x * p2.x + p1.y + p2.y;
    }

    /**
     * Normalise un vecteur
     */
    public void normaliser() {
        double norme = this.distanceToOrigin();
        this.x /= norme;
        this.y /= norme;
    }
}
