package satellitesbalises.modele.evenement;

public interface EcouteurTransferts {

    void transfertDemarre(EvenementTransfertDemarre evenement);

    void transfertProgresse(EvenementTransfertProgresse evenement);

    void transfertTermine(EvenementTransfertTermine evenement);
}
