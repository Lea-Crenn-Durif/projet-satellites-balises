package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Entite;
import satellitesbalises.modele.evenement.EvenementApparenceModifiee;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

/** Change l'apparence d'une entité et l'annonce (EX-2.9). Entité inconnue : commande ignorée et signalée. */
public class CommandeChangerApparence implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nom;
    private final Apparence apparence;

    public CommandeChangerApparence(String nom, Apparence apparence) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.apparence = Objects.requireNonNull(apparence, "apparence");
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Entite> entite = simulation.getMonde().trouver(nom);
        if (entite.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "apparence " + nom + " ignorée : entité inconnue"));
            return;
        }
        entite.get().changerApparence(apparence);
        simulation.envoyer(new EvenementApparenceModifiee(entite.get()));
    }
}
