package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;

public class ConditionDuree implements ConditionRemontee {

    private static final long serialVersionUID = 1L;

    private final long dureeMax;

    public ConditionDuree(long dureeMax) {
        this.dureeMax = dureeMax;
    }

    @Override
    public boolean estRemplie(Balise balise, long dureeCollecte) {
        return dureeCollecte >= dureeMax;
    }
}
