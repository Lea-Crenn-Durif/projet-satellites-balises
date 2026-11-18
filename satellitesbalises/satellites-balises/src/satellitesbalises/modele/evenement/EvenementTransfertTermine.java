package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.communication.Transfert;

public class EvenementTransfertTermine extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Transfert transfert;
    private final boolean complet;

    public EvenementTransfertTermine(Transfert transfert, boolean complet) {
        super(transfert);
        this.transfert = transfert;
        this.complet = complet;
    }

    public Transfert getTransfert() {
        return transfert;
    }

    public boolean estComplet() {
        return complet;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurTransferts) ecouteur).transfertTermine(this);
    }
}
