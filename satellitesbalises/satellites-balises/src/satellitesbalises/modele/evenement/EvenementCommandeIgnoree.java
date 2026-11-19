package satellitesbalises.modele.evenement;

import eventHandler.AbstractEvent;
import satellitesbalises.modele.simulation.Simulation;

/**
 * Publié quand une commande prévue ne peut pas s'appliquer, par exemple parce que son entité
 * n'existe plus (EX-7.12). La simulation continue.
 *
 * <p>Version minimale (étape 1) : les commandes qui le publient arrivent aux étapes suivantes.</p>
 */
public class EvenementCommandeIgnoree extends AbstractEvent {

    private static final long serialVersionUID = 1L;

    private final String raison;

    public EvenementCommandeIgnoree(Simulation simulation, String raison) {
        super(simulation);
        this.raison = raison;
    }

    /** Pourquoi la commande a été ignorée, en clair pour l'utilisateur. */
    public String getRaison() {
        return raison;
    }

    @Override
    public void sendTo(Object ecouteur) {
        ((EcouteurCommandes) ecouteur).commandeIgnoree(this);
    }
}
