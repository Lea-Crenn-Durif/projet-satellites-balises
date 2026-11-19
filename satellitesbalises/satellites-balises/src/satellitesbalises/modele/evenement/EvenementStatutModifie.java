package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.entite.Satellite;

/** Publié quand un satellite change de statut (EX-6.1, EX-6.10). */
public class EvenementStatutModifie extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Satellite satellite;

    public EvenementStatutModifie(Satellite satellite) {
        super(satellite);
        this.satellite = satellite;
    }

    public Satellite getSatellite() {
        return satellite;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurSatellites) ecouteur).statutModifie(this);
    }
}
