package satellitesbalises.modele.activite;

import java.util.Objects;

import satellitesbalises.modele.entite.Balise;

public class ConditionOu implements ConditionRemontee {

    private static final long serialVersionUID = 1L;

    private final ConditionRemontee premiere;
    private final ConditionRemontee seconde;

    public ConditionOu(ConditionRemontee premiere, ConditionRemontee seconde) {
        this.premiere = Objects.requireNonNull(premiere, "premiere");
        this.seconde = Objects.requireNonNull(seconde, "seconde");
    }

    @Override
    public boolean estRemplie(Balise balise, long dureeCollecte) {
        return premiere.estRemplie(balise, dureeCollecte) || seconde.estRemplie(balise, dureeCollecte);
    }
}
