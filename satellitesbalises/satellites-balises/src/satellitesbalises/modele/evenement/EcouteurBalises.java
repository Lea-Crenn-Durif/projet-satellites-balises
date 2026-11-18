package satellitesbalises.modele.evenement;

/** Est prévenu de ce qui change à l'intérieur d'une balise : activité, mémoire, instruments. */
public interface EcouteurBalises {

    void activiteModifiee(EvenementActiviteModifiee evenement);

    void memoireModifiee(EvenementMemoireModifiee evenement);

    void equipementModifie(EvenementEquipementModifie evenement);
}
