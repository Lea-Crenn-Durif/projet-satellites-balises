package satellitesbalises.modele.simulation;

import java.io.Serializable;
import java.util.Objects;

import satellitesbalises.modele.commande.CommandeSimulation;

/**
 * Une commande datée : le pas où l'exécuter, et un numéro d'ordre qui départage
 * les commandes prévues pour le même pas (la première planifiée passe la première).
 */
public final class CommandePlanifiee implements Comparable<CommandePlanifiee>, Serializable {

    private static final long serialVersionUID = 1L;

    private final long pas;
    private final long ordre;
    private final CommandeSimulation commande;

    public CommandePlanifiee(long pas, long ordre, CommandeSimulation commande) {
        this.pas = pas;
        this.ordre = ordre;
        this.commande = Objects.requireNonNull(commande, "commande");
    }

    public long getPas() {
        return pas;
    }

    public long getOrdre() {
        return ordre;
    }

    public CommandeSimulation getCommande() {
        return commande;
    }

    /** Par pas croissant, puis dans l'ordre de planification. */
    @Override
    public int compareTo(CommandePlanifiee autre) {
        int parPas = Long.compare(pas, autre.pas);
        return parPas != 0 ? parPas : Long.compare(ordre, autre.ordre);
    }
}
