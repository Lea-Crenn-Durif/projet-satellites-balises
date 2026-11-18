package satellitesbalises.modele.evenement;

public interface EcouteurBalises {

    void activiteModifiee(EvenementActiviteModifiee evenement);

    void memoireModifiee(EvenementMemoireModifiee evenement);

    void equipementModifie(EvenementEquipementModifie evenement);
}
