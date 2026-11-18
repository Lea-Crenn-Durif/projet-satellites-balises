package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

public class CommandeForcerRemontee implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nomBalise;

    public CommandeForcerRemontee(String nomBalise) {
        this.nomBalise = Objects.requireNonNull(nomBalise, "nomBalise");
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Balise> balise = simulation.getMonde().trouverBalise(nomBalise);
        if (balise.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "remonter " + nomBalise + " ignorée : balise inconnue"));
            return;
        }
        balise.get().demanderRemontee();
    }
}
