package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.evenement.EvenementEquipementModifie;
import satellitesbalises.modele.simulation.Simulation;

public class CommandeDesequiper implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nomBalise;
    private final String nomInstrument;

    public CommandeDesequiper(String nomBalise, String nomInstrument) {
        this.nomBalise = Objects.requireNonNull(nomBalise, "nomBalise");
        this.nomInstrument = Objects.requireNonNull(nomInstrument, "nomInstrument");
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Balise> balise = simulation.getMonde().trouverBalise(nomBalise);
        if (balise.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "desequiper " + nomBalise + " ignorée : balise inconnue"));
            return;
        }
        if (balise.get().desequiper(nomInstrument).isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "desequiper " + nomBalise + " ignorée : pas d'instrument « " + nomInstrument + " »"));
            return;
        }
        simulation.envoyer(new EvenementEquipementModifie(balise.get()));
    }
}
