package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.activite.ConditionRemontee;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

public class CommandeChangerConditionRemontee implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nomBalise;
    private final ConditionRemontee condition;

    public CommandeChangerConditionRemontee(String nomBalise, ConditionRemontee condition) {
        this.nomBalise = Objects.requireNonNull(nomBalise, "nomBalise");
        this.condition = Objects.requireNonNull(condition, "condition");
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Balise> balise = simulation.getMonde().trouverBalise(nomBalise);
        if (balise.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "remontee " + nomBalise + " ignorée : balise inconnue"));
            return;
        }
        balise.get().changerConditionRemontee(condition);
    }
}
