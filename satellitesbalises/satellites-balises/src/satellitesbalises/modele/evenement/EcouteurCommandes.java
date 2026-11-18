package satellitesbalises.modele.evenement;

@FunctionalInterface
public interface EcouteurCommandes {

    void commandeIgnoree(EvenementCommandeIgnoree evenement);
}
