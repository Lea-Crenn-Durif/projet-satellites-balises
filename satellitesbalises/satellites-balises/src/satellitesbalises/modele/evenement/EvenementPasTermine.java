package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.simulation.Simulation;

/** Publié à la fin de chaque pas, une fois que tout ce qui devait se passer pendant ce pas est fait. */
public class EvenementPasTermine extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final long pas;

    public EvenementPasTermine(Simulation simulation, long pas) {
        super(simulation);
        this.pas = pas;
    }

    /** Numéro du pas qui vient de se terminer. */
    public long getPas() {
        return pas;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurTemps) ecouteur).pasTermine(this);
    }
}
