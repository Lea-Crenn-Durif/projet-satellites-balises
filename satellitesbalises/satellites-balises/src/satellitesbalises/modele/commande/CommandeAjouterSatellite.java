package satellitesbalises.modele.commande;

import java.util.Objects;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

/** Ajoute un satellite au monde. Si son nom est déjà pris, la commande est ignorée et on le signale. */
public class CommandeAjouterSatellite implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final Satellite satellite;

    public CommandeAjouterSatellite(Satellite satellite) {
        this.satellite = Objects.requireNonNull(satellite, "satellite");
    }

    @Override
    public void executer(Simulation simulation) {
        try {
            simulation.ajouter(satellite);
        } catch (ErreurConfiguration erreur) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation, erreur.getMessage()));
        }
    }
}
