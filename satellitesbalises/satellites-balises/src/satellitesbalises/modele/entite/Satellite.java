package satellitesbalises.modele.entite;

import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;


public class Satellite extends Entite {

    private static final long serialVersionUID = 1L;

    private final double portee;
    private final int debit;
    private StatutSatellite statut = StatutSatellite.DISPONIBLE;

    public Satellite(String nom, Position position, Deplacement deplacement, Apparence apparence,
                     double portee, int debit) {
        super(nom, position, deplacement, apparence);
        this.portee = portee;
        this.debit = debit;
    }

    public double getPortee() {
        return portee;
    }

    public int getDebit() {
        return debit;
    }

    public StatutSatellite getStatut() {
        return statut;
    }

    public boolean estDisponible() {
        return statut == StatutSatellite.DISPONIBLE;
    }

    @Override
    public void agir(ContexteSimulation contexte) {
        seDeplacer(contexte);
    }

    @Override
    public void accepter(VisiteurEntite visiteur) {
        visiteur.visiter(this);
    }
}
