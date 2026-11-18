package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.simulation.Simulation;


public class EvenementCommandeIgnoree extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final String raison;

    public EvenementCommandeIgnoree(Simulation simulation, String raison) {
        super(simulation);
        this.raison = raison;
    }

    public String getRaison() {
        return raison;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurCommandes) ecouteur).commandeIgnoree(this);
    }
}
