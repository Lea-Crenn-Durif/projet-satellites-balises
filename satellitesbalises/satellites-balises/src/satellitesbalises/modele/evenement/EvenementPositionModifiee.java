package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.entite.Entite;

/** Publié quand une entité a changé de position (à partir de l'étape 3). */
public class EvenementPositionModifiee extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Entite entite;

    public EvenementPositionModifiee(Entite entite) {
        super(entite);
        this.entite = entite;
    }

    public Entite getEntite() {
        return entite;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurEntites) ecouteur).positionModifiee(this);
    }
}
