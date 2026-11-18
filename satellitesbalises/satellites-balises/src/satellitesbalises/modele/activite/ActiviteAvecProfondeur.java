package satellitesbalises.modele.activite;

abstract class ActiviteAvecProfondeur implements Activite {

    private static final long serialVersionUID = 1L;

    private final double profondeurDeTravail;

    protected ActiviteAvecProfondeur(double profondeurDeTravail) {
        this.profondeurDeTravail = profondeurDeTravail;
    }

    public double getProfondeurDeTravail() {
        return profondeurDeTravail;
    }
}
