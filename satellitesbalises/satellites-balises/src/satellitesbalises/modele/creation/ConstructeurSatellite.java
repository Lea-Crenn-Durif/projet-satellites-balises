package satellitesbalises.modele.creation;

import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.deplacement.DeplacementImmobile;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.monde.Zone;


public class ConstructeurSatellite {

    private final String nom;
    private final FabriqueEntites fabrique;
    private Position position;
    private Deplacement deplacement = new DeplacementImmobile();
    private double portee = 30;
    private int debit = 2;
    private Apparence apparence = Apparence.parDefaut();

    ConstructeurSatellite(String nom, FabriqueEntites fabrique) {
        this.nom = nom;
        this.fabrique = fabrique;
    }

    public ConstructeurSatellite en(Position position) {
        this.position = position;
        return this;
    }

    public ConstructeurSatellite avecDeplacement(Deplacement deplacement) {
        this.deplacement = deplacement;
        return this;
    }

    public ConstructeurSatellite avecPortee(double portee) {
        this.portee = portee;
        return this;
    }

    public ConstructeurSatellite avecDebit(int debit) {
        this.debit = debit;
        return this;
    }

    public ConstructeurSatellite avecApparence(Apparence apparence) {
        this.apparence = apparence;
        return this;
    }

    /** @throws ErreurConfiguration si la position manque, n'est pas dans le ciel, ou si portée ou débit ≤ 0 */
    public Satellite construire() {
        String description = "Satellite " + nom;
        fabrique.verifierPlacement(description, position, Zone.CIEL);
        FabriqueEntites.verifierStrictementPositif(description, "la portée", portee);
        FabriqueEntites.verifierStrictementPositif(description, "le débit", debit);
        return new Satellite(nom, position, deplacement, apparence, portee, debit);
    }
}
