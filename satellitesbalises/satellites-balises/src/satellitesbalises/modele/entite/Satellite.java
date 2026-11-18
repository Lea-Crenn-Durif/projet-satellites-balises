package satellitesbalises.modele.entite;

import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Un satellite, dans le ciel. Il recevra les mesures des balises qui sont à sa portée,
 * au rythme de son débit (étape 6).
 */
public class Satellite extends Entite {

    private static final long serialVersionUID = 1L;

    private final double portee;
    private final int debit;
    private StatutSatellite statut = StatutSatellite.DISPONIBLE;

    /** Utiliser {@code FabriqueEntites.nouveauSatellite(nom)}, qui vérifie les valeurs. */
    public Satellite(String nom, Position position, Deplacement deplacement, Apparence apparence,
                     double portee, int debit) {
        super(nom, position, deplacement, apparence);
        this.portee = portee;
        this.debit = debit;
    }

    /** Distance horizontale jusqu'à laquelle il peut communiquer avec une balise. */
    public double getPortee() {
        return portee;
    }

    /** Nombre de mesures reçues par pas pendant un transfert. */
    public int getDebit() {
        return debit;
    }

    public StatutSatellite getStatut() {
        return statut;
    }

    public boolean estDisponible() {
        return statut == StatutSatellite.DISPONIBLE;
    }

    /** Étape 3 : il se déplace. La communication avec les balises arrive à l'étape 6. */
    @Override
    public void agir(ContexteSimulation contexte) {
        seDeplacer(contexte);
    }

    @Override
    public void accepter(VisiteurEntite visiteur) {
        visiteur.visiter(this);
    }
}
