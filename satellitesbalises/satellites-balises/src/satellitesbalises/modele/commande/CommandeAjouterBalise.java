package satellitesbalises.modele.commande;

import java.util.Objects;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

/** Ajoute une balise au monde. Si son nom est déjà pris, la commande est ignorée et on le signale. */
public class CommandeAjouterBalise implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final Balise balise;

    public CommandeAjouterBalise(Balise balise) {
        this.balise = Objects.requireNonNull(balise, "balise");
    }

    @Override
    public void executer(Simulation simulation) {
        try {
            simulation.ajouter(balise);
        } catch (ErreurConfiguration erreur) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation, erreur.getMessage()));
        }
    }
}
