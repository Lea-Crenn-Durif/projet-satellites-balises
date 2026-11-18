package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.entite.Entite;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

public class CommandeConfigurer implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nom;
    private final Parametres parametres;

    public CommandeConfigurer(String nom, Parametres parametres) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.parametres = Objects.requireNonNull(parametres, "parametres");
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Entite> entite = simulation.getMonde().trouver(nom);
        if (entite.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "configurer " + nom + " ignorée : entité inconnue"));
            return;
        }
        try {
            entite.get().configurer(parametres);
        } catch (ErreurConfiguration erreur) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "configurer " + nom + " ignorée : " + erreur.getMessage()));
        }
    }
}
