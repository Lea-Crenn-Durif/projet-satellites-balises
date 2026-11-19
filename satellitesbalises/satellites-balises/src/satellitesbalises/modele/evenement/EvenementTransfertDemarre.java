package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.communication.Transfert;

/** Publié quand un transfert commence (EX-6.10). */
public class EvenementTransfertDemarre extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Transfert transfert;

    public EvenementTransfertDemarre(Transfert transfert) {
        super(transfert);
        this.transfert = transfert;
    }

    public Transfert getTransfert() {
        return transfert;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurTransferts) ecouteur).transfertDemarre(this);
    }
}
