import java.awt.Color;

public class Scene {
    /**
     * Tableau contenant tous les cercles
     */
    public Circle objets[] = new Circle[AnimationMain.NOMBRE_PARTICULES];

    /**
     * Cercle dans lequel la scène est contenue
     */
    public Circle contrainte;

    /**
     * Dimension du grillage
     */
    public static int DIM;

    /**
     * Nombre maximal de cercles par carreaux
     */
    public static int TAILLE;

    /**
     * Grillage utilisé pour contenir les cercles afin de résoudre les collisions
     */
    public static int quadrillage[][][];

    /**
     * Nombre de cercles ajoutés à la scène
     */
    public static int NOMBRE_CERCLES_AJOUTES = 0;

    public Scene() {
        // Création de l'enveloppe du milieu
        Position centre = new Position(AnimationMain.WINDOW_SIZE / 2, AnimationMain.WINDOW_SIZE / 2);
        this.contrainte = new Circle(AnimationMain.WINDOW_SIZE / 2, centre, Color.BLACK);

        // Création du quadrillage de la scène
        if (AnimationMain.RAYON_ALEATOIRE) {
            DIM = (AnimationMain.WINDOW_SIZE / AnimationMain.RAYON_MAX) / 2 + 3;
            TAILLE = 2 * (int) Math.pow(AnimationMain.RAYON_MAX / AnimationMain.RAYON_MIN, 2);
            quadrillage = new int[DIM][DIM][TAILLE];
        } else {
            TAILLE = 4;
            DIM = (AnimationMain.WINDOW_SIZE / AnimationMain.RAYON) / 2 + 3;
            quadrillage = new int[DIM][DIM][TAILLE];
        }

        // Initialisation du quadrillage à -1
        for (int x = 0; x < DIM; x++) {
            for (int y = 0; y < DIM; y++) {
                for (int i = 0; i < TAILLE; i++) {
                    quadrillage[x][y][i] = -1;
                }
            }
        }

        for (int j = 0; j < AnimationMain.NOMBRE_PARTICULES; j++) {
            // Création en amont du cercle et ajout à la scène
            Circle cercle = new Circle();
            objets[cercle.index] = cercle;
        }
    }

    /**
     * Ajoute un cercle dans la scène
     */
    public void ajouterCercleAleatoire() {
        // On vérifie qu'on ajoute pas trop de cercles
        if (NOMBRE_CERCLES_AJOUTES >= AnimationMain.NOMBRE_PARTICULES) {
            return;
        }

        // Récupération du cercle
        Circle cercle = this.objets[NOMBRE_CERCLES_AJOUTES];
        NOMBRE_CERCLES_AJOUTES++;

        // Obtention des coordonnées pour le quadrilalge
        int x = cercle.coordonnees[0];
        int y = cercle.coordonnees[1];

        // Recherche d'une place où stocker le cercle
        int i = 0;
        while ((i < TAILLE) && (quadrillage[x][y][i] != -1)) {
            i++;
        }

        if (i < TAILLE) { // On a trouvé une position vide
            quadrillage[x][y][i] = cercle.index;
        } else { // Sinon c'est pas normal
            System.out.println("C'est trop");
        }
    }

    /**
     * Met à jour la disposition des cercles sur le quadrillage afin de savoir où
     * ils sont
     */
    public void majCercles() {
        for (int i = 0; i < NOMBRE_CERCLES_AJOUTES; i++) {
            Circle cercle = this.objets[i];

            // On récupère les anciennes coordonnées sur le quadrillage
            int a_x = (int) cercle.coordonnees[0];
            int a_y = (int) cercle.coordonnees[1];

            // On crée des nouvelles coordonnées par des calculs savants
            int n_x;
            int n_y;

            // Voilà les calculs savants
            if (AnimationMain.RAYON_ALEATOIRE) {
                n_x = (int) (cercle.centre.x / AnimationMain.RAYON_MAX) / 2 + 1;
                n_y = (int) (cercle.centre.y / AnimationMain.RAYON_MAX) / 2 + 1;
            } else {
                n_x = (int) cercle.centre.x / AnimationMain.RAYON / 2 + 1;
                n_y = (int) cercle.centre.y / AnimationMain.RAYON / 2 + 1;
            }

            // Si les coordonnées ne sont pas les mêmes, on ne fait rien
            if ((a_x == n_x) && (a_y == n_y)) {
                continue;
            }

            // On cherche l'indice du cercle à ses anciennes coordonnées
            int j = 0;
            while ((j < TAILLE) && (quadrillage[a_x][a_y][j] != cercle.index)) {
                j++;
            }
            if (j < TAILLE) {
                quadrillage[a_x][a_y][j] = -1;
            } else {
                System.out.println("Problème pour l'index" + cercle.index);
            }

            // On met à jour les coordonnées internes au cercle
            cercle.coordonnees[0] = n_x;
            cercle.coordonnees[1] = n_y;

            // On ajoute l'indice du cercle au bon endroit
            int h = 0;
            while ((h < TAILLE) && (quadrillage[n_x][n_y][h] != -1)) {
                h++;
            }
            if (h < TAILLE) {
                quadrillage[n_x][n_y][h] = cercle.index;
            } else {
                System.out.println("Problème 2 pour l'index" + cercle.index);
            }
        }
    }

    /**
     * Ajoute au cercle son déplacement par inertie et la gravité, par intégration
     * de Verlet
     * 
     * @param dt Intervalle de temps
     */
    public void acceleration(double dt) {
        for (int i = 0; i < NOMBRE_CERCLES_AJOUTES; i++) {
            Circle circle = this.objets[i];
            // Différence entre la position actuelle et la position précédente
            Position deplacement = Position.deplacement(circle.centre_precedent, circle.centre);

            // La position actuelle devient la précédente, et on crée un nouvel objet pour
            // effacer les effets de bord
            circle.centre_precedent = new Position(circle.centre);

            // Mise à jour des positions selon la méthode d'intégration de Verlet
            circle.centre.x += deplacement.x;
            circle.centre.y += deplacement.y + AnimationMain.GRAVITE * dt * dt;
        }
    }

    /**
     * Remet en place les cercles qui dépassent du cadre
     */
    public void contrainte() {
        for (int i = 0; i < NOMBRE_CERCLES_AJOUTES; i++) {
            Circle circle = objets[i];

            // Calcul de la distance entre le cercle et le centre de la scène
            double dist = Position.distance(circle.centre, this.contrainte.centre);

            // Si il n'y a pas de choc, on passe au cercle suivant
            if (dist <= this.contrainte.rayon - circle.rayon) {
                continue;
            }

            // Calcul du vecteur normal au choc
            double X = (this.contrainte.centre.x - circle.centre.x) / dist;
            double Y = (this.contrainte.centre.y - circle.centre.y) / dist;

            // Correction de la position selon ce vecteur
            circle.centre.x = this.contrainte.centre.x - X * (this.contrainte.rayon - circle.rayon);
            circle.centre.y = this.contrainte.centre.y - Y * (this.contrainte.rayon - circle.rayon);

            // Codage du rebond

            // Calcul du vecteur d'arrivée
            Position k = new Position(Position.deplacement(circle.centre_precedent, circle.centre));

            // Calcul du vecteur normal au choc
            Position rayon = new Position(Position.deplacement(contrainte.centre, circle.centre));

            // Si le rebond se fait en haut du cercle, on évite de calculer quoi que ce soit
            // parce que ça fait tourner des trucs bizarres
            if (rayon.y < 0) {
                continue;
            }

            // Mise à jour de la position
            circle.centre_precedent = new Position(circle.centre);

            // Calcul du produit scalaire normalisé dans la formule d'une réflexion
            double prod = Position.produitScalaire(rayon, k);
            prod /= Math.pow(rayon.distanceToOrigin(), 2);

            k.x -= 2 * k.x * prod;
            k.y -= 2 * k.y * prod;

            // Ajout des coordonnées du rebond
            circle.centre.x += k.x;
            circle.centre.y += k.y;
        }
    }

    /**
     * Fonction naïve de calculs de collision, en O(n²)
     */
    public void collision() {
        // Itération sur tous les cercles
        for (int i = 1; i < AnimationMain.NOMBRE_PARTICULES; i++) {
            for (int j = 0; j < i; j++) {
                Circle.reglerCollision(objets[i], objets[j]);
            }
        }
    }

    /**
     * Fonction plus maline, qui utilise le quadrillage en O(n)
     */
    public void collision2() {
        for (int j = 0; j < NOMBRE_CERCLES_AJOUTES; j++) {
            Circle cercle = objets[j];
            // On récupère les coordonnées du cercle sur le quadrillage
            int x = cercle.coordonnees[0];
            int y = cercle.coordonnees[1];

            // On cherche des potentielles collisions dans les cases d'à côté
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int i = 0;
                    while (i < TAILLE) {
                        // Index du deuxième cercle à vérifier
                        int indexCercle2 = quadrillage[x + dx][y + dy][i];

                        if (indexCercle2 != -1) {
                            Circle.reglerCollision(cercle, objets[indexCercle2]);
                        }
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Mise à jour de la scène pendant un temps dt
     * 
     * @param dt L'intervalle de temps
     */
    public void maj(double dt) {
        acceleration(dt);
        contrainte();
        majCercles();
        collision2();
    }
}
