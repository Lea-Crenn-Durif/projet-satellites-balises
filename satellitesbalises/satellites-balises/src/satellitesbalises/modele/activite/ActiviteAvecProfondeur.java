package satellitesbalises.modele.activite;

/**
 * Le point commun des activités du voyage vers la surface et retour : elles retiennent
 * la profondeur de travail, pour que la descente ramène la balise là où elle collectait (EX-5.6).
 */
abstract class ActiviteAvecProfondeur implements Activite {

    private static final long serialVersionUID = 1L;

    private final double profondeurDeTravail;

    protected ActiviteAvecProfondeur(double profondeurDeTravail) {
        this.profondeurDeTravail = profondeurDeTravail;
    }

    /** L'ordonnée (négative) où la balise collectait avant de remonter. */
    public double getProfondeurDeTravail() {
        return profondeurDeTravail;
    }
}
