package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.entite.Entite;

public class EvenementEntiteAjoutee extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Entite entite;

    public EvenementEntiteAjoutee(Entite entite) {
        super(entite);
        this.entite = entite;
    }

    public Entite getEntite() {
        return entite;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurEntites) ecouteur).entiteAjoutee(this);
    }
}
