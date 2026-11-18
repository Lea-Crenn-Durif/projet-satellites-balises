package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.entite.Entite;

public class EvenementEntiteSupprimee extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Entite entite;

    public EvenementEntiteSupprimee(Entite entite) {
        super(entite);
        this.entite = entite;
    }

    public Entite getEntite() {
        return entite;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurEntites) ecouteur).entiteSupprimee(this);
    }
}
