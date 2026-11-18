package satellitesbalises.modele.activite;

import java.io.Serializable;

public interface Activite extends Serializable {

    /** Le nom affiché à l'écran, par exemple « Collecte ». */
    String getNom();
}
