package satellitesbalises.modele.commande;

import java.util.Objects;

import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

public class CommandeSupprimer implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nom;

    public CommandeSupprimer(String nom) {
        this.nom = Objects.requireNonNull(nom, "nom");
    }

    @Override
    public void executer(Simulation simulation) {
        if (simulation.getMonde().trouver(nom).isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "supprimer " + nom + " ignorée : entité inconnue"));
            return;
        }
        simulation.supprimer(nom);
    }
}
