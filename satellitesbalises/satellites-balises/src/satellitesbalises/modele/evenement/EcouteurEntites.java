package satellitesbalises.modele.evenement;

public interface EcouteurEntites {

    void entiteAjoutee(EvenementEntiteAjoutee evenement);

    void entiteSupprimee(EvenementEntiteSupprimee evenement);

    void positionModifiee(EvenementPositionModifiee evenement);

    void apparenceModifiee(EvenementApparenceModifiee evenement);
}
