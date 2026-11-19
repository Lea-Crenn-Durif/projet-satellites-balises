package satellitesbalises.modele.evenement;

/** Est prévenu quand le statut d'un satellite change (disponible, occupé, hors service). */
@FunctionalInterface
public interface EcouteurSatellites {

    void statutModifie(EvenementStatutModifie evenement);
}
