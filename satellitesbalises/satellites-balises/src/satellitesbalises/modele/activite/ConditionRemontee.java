package satellitesbalises.modele.activite;

import java.io.Serializable;

import satellitesbalises.modele.entite.Balise;

@FunctionalInterface
public interface ConditionRemontee extends Serializable {

    boolean estRemplie(Balise balise, long dureeCollecte);
}
