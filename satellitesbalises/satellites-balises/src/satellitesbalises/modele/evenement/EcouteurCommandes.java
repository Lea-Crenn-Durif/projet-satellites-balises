package satellitesbalises.modele.evenement;

/** Est prévenu quand une commande n'a pas pu être appliquée. */
@FunctionalInterface
public interface EcouteurCommandes {

    void commandeIgnoree(EvenementCommandeIgnoree evenement);
}
