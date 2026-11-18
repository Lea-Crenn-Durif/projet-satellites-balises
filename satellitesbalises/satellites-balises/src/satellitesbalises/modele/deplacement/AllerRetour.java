package satellitesbalises.modele.deplacement;

import java.io.Serializable;


final class AllerRetour implements Serializable {

    private static final long serialVersionUID = 1L;

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
