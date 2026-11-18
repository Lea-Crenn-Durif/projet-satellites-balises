package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;

public class ConditionMemoirePleine implements ConditionRemontee {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean estRemplie(Balise balise, long dureeCollecte) {
        return balise.getMemoire().estPleine();
    }
}
