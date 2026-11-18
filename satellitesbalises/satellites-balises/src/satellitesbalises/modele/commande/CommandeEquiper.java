package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.evenement.EvenementEquipementModifie;
import satellitesbalises.modele.mesure.Instrument;
import satellitesbalises.modele.simulation.Simulation;

public class CommandeEquiper implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nomBalise;
    private final Instrument instrument;

    public CommandeEquiper(String nomBalise, Instrument instrument) {
        this.nomBalise = Objects.requireNonNull(nomBalise, "nomBalise");
        this.instrument = Objects.requireNonNull(instrument, "instrument");
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Balise> balise = simulation.getMonde().trouverBalise(nomBalise);
        if (balise.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "equiper " + nomBalise + " ignorée : balise inconnue"));
            return;
        }
        balise.get().equiper(instrument);
        simulation.envoyer(new EvenementEquipementModifie(balise.get()));
    }
}
