package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.communication.Transfert;

/** Publié quand un transfert s'arrête : complet (toutes les mesures sont parties) ou rompu (EX-6.7, EX-6.8). */
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

    /** Vrai si toutes les mesures sont parties ; faux si le transfert a été rompu. */
    public boolean estComplet() {
        return complet;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurTransferts) ecouteur).transfertTermine(this);
    }
}
