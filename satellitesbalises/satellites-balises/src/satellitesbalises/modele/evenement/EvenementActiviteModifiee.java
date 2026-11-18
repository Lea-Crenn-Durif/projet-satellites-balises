package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.entite.Balise;

public class EvenementActiviteModifiee extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Balise balise;

    public EvenementActiviteModifiee(Balise balise) {
        super(balise);
        this.balise = balise;
    }

    public Balise getBalise() {
        return balise;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurBalises) ecouteur).activiteModifiee(this);
    }
}
