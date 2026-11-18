package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.entite.Balise;

public class EvenementEquipementModifie extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Balise balise;

    public EvenementEquipementModifie(Balise balise) {
        super(balise);
        this.balise = balise;
    }

    public Balise getBalise() {
        return balise;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurBalises) ecouteur).equipementModifie(this);
    }
}
