package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;

/** Remonter quand la mémoire est pleine. C'est la condition par défaut. */
public class ConditionMemoirePleine implements ConditionRemontee {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean estRemplie(Balise balise, long dureeCollecte) {
        return balise.getMemoire().estPleine();
    }
}
