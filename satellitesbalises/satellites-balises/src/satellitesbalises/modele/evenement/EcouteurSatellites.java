package satellitesbalises.modele.evenement;

@FunctionalInterface
public interface EcouteurSatellites {

    void statutModifie(EvenementStatutModifie evenement);
}
