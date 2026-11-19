package satellitesbalises.modele.evenement;

/** Est prévenu de la vie des transferts : début, chaque envoi, fin. */
public interface EcouteurTransferts {

    void transfertDemarre(EvenementTransfertDemarre evenement);

    void transfertProgresse(EvenementTransfertProgresse evenement);

    void transfertTermine(EvenementTransfertTermine evenement);
}
