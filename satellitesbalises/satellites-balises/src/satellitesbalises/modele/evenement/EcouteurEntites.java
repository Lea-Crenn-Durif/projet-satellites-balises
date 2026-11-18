package satellitesbalises.modele.evenement;

/** Est prévenu de la vie des entités : arrivée, départ, déplacement, changement d'apparence. */
public interface EcouteurEntites {

    void entiteAjoutee(EvenementEntiteAjoutee evenement);

    void entiteSupprimee(EvenementEntiteSupprimee evenement);

    void positionModifiee(EvenementPositionModifiee evenement);

    void apparenceModifiee(EvenementApparenceModifiee evenement);
}
