package satellitesbalises.modele.evenement;

@FunctionalInterface
public interface EcouteurTemps {

    void pasTermine(EvenementPasTermine evenement);
}
