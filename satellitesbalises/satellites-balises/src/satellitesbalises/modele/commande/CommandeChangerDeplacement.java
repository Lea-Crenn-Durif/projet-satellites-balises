package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.entite.Entite;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

/**
 * Change le déplacement d'une entité ; il s'applique dès le pas où la commande s'exécute (EX-3.9).
 * Entité inconnue : commande ignorée et signalée.
 */
public class CommandeChangerDeplacement implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nom;
    private final Deplacement deplacement;

    public CommandeChangerDeplacement(String nom, Deplacement deplacement) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.deplacement = Objects.requireNonNull(deplacement, "deplacement");
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Entite> entite = simulation.getMonde().trouver(nom);
        if (entite.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "deplacement " + nom + " ignorée : entité inconnue"));
            return;
        }
        entite.get().changerDeplacement(deplacement);
    }
}
