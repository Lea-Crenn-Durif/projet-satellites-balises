package satellitesbalises.modele.deplacement;

import java.io.Serializable;

/**
 * Avance une coordonnée entre deux bornes, en faisant demi-tour à chaque borne.
 *
 * <p>Arrivée sur une borne, la coordonnée s'y arrête exactement (elle ne la dépasse jamais),
 * puis repart dans l'autre sens au pas suivant. Chaque déplacement a son propre aller-retour,
 * car il retient le sens courant.</p>
 */
final class AllerRetour implements Serializable {

    private static final long serialVersionUID = 1L;

    /** +1 : vers les valeurs croissantes ; −1 : vers les valeurs décroissantes. */
    private int sens;

    AllerRetour(int sensInitial) {
        this.sens = sensInitial;
    }

    double avancer(double valeur, double vitesse, double minimum, double maximum) {
        double suivante = valeur + sens * vitesse;
        if (suivante >= maximum) {
            sens = -1;
            return maximum;
        }
        if (suivante <= minimum) {
            sens = 1;
            return minimum;
        }
        return suivante;
    }
}
