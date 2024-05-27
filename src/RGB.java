import java.awt.Color;

public class RGB {
    private static final int FULL = 255;
    private static final int MEDIUM = 127;
    private static final int NULL = 0;

    private static int posMax = 3;
    private static int currentColor[] = { FULL, 0, 0 };

    /**
     * Code foireux pour générer un arc-en-ciel connaissant la couleur précédente
     * 
     * @return La couleur suivante
     */
    public static Color RGBsuivante() {
        // Je n'y comprends rien mais ça fonctionne
        if (currentColor[(posMax - 1) % 3] == NULL) {
            if (currentColor[(posMax + 1) % 3] == MEDIUM) {
                currentColor[(posMax + 1) % 3] = FULL;
                posMax++;
            } else if (currentColor[(posMax + 1) % 3] == NULL) {
                currentColor[(posMax + 1) % 3] = MEDIUM;
            }
        } else {
            if (currentColor[(posMax - 1) % 3] == FULL) {
                currentColor[(posMax - 1) % 3] = MEDIUM;
            } else if (currentColor[(posMax - 1) % 3] == MEDIUM) {
                currentColor[(posMax - 1) % 3] = NULL;
            }
        }
        Color coul = new Color(currentColor[0], currentColor[1], currentColor[2]);
        return coul;
    }
}
