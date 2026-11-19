package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.entite.Balise;

/** Publié quand le contenu de la mémoire d'une balise a changé (EX-4.9). */
public class EvenementMemoireModifiee extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final Balise balise;

    public EvenementMemoireModifiee(Balise balise) {
        super(balise);
        this.balise = balise;
    }

    public Balise getBalise() {
        return balise;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurBalises) ecouteur).memoireModifiee(this);
    }
}
