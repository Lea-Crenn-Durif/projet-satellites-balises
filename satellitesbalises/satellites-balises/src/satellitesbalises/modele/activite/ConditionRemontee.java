package satellitesbalises.modele.activite;

import java.io.Serializable;

import satellitesbalises.modele.entite.Balise;

/** Dit quand une balise en collecte doit remonter à la surface (patron Stratégie). */
@FunctionalInterface
public interface ConditionRemontee extends Serializable {

    /**
     * @param balise        la balise en collecte
     * @param dureeCollecte depuis combien de pas elle collecte (le pas courant compte)
     */
    boolean estRemplie(Balise balise, long dureeCollecte);
}
