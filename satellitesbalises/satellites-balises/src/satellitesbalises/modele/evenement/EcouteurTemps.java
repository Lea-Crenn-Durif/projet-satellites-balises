package satellitesbalises.modele.evenement;

/** Est prévenu à la fin de chaque pas de simulation. */
@FunctionalInterface
public interface EcouteurTemps {

    void pasTermine(EvenementPasTermine evenement);
}
