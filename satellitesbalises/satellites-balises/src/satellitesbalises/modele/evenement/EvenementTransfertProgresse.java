package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.communication.Transfert;

/** Publié à chaque envoi de mesures d'un transfert (EX-6.5, EX-6.10). */
public class EvenementTransfertProgresse extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Transfert transfert;

    public EvenementTransfertProgresse(Transfert transfert) {
        super(transfert);
        this.transfert = transfert;
    }

    public Transfert getTransfert() {
        return transfert;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurTransferts) ecouteur).transfertProgresse(this);
    }
}
