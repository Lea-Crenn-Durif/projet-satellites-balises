package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.simulation.Simulation;

public class EvenementPasTermine extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final long pas;

    public EvenementPasTermine(Simulation simulation, long pas) {
        super(simulation);
        this.pas = pas;
    }

    public long getPas() {
        return pas;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurTemps) ecouteur).pasTermine(this);
    }
}
