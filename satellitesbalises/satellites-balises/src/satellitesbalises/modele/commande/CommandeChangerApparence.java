package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Entite;
import satellitesbalises.modele.evenement.EvenementApparenceModifiee;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.simulation.Simulation;

/**
 * Change l'apparence d'une entité et l'annonce (EX-2.9). Entité inconnue : commande ignorée et signalée.
 *
 * <p>On peut ne changer que la couleur ou que la taille : la partie non donnée ({@code null})
 * garde sa valeur actuelle au moment de l'exécution.</p>
 */
public class CommandeChangerApparence implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nom;
    private final String couleur;
    private final Integer taille;

    /** Change la couleur et la taille. */
    public CommandeChangerApparence(String nom, Apparence apparence) {
        this(nom, apparence.getCouleur(), apparence.getTaille());
    }

    /**
     * @param couleur la nouvelle couleur, ou {@code null} pour garder l'actuelle
     * @param taille  la nouvelle taille, ou {@code null} pour garder l'actuelle
     */
    public CommandeChangerApparence(String nom, String couleur, Integer taille) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.couleur = couleur;
        this.taille = taille;
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Entite> entite = simulation.getMonde().trouver(nom);
        if (entite.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    "apparence " + nom + " ignorée : entité inconnue"));
            return;
        }
        Apparence actuelle = entite.get().getApparence();
        entite.get().changerApparence(new Apparence(
                couleur != null ? couleur : actuelle.getCouleur(),
                taille != null ? taille : actuelle.getTaille()));
        simulation.envoyer(new EvenementApparenceModifiee(entite.get()));
    }
}
