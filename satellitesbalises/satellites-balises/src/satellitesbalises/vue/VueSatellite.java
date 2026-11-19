package satellitesbalises.vue;

import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.entite.Satellite;

/** Un satellite : une icône de satellite dans le ciel, grisée et marquée « (HS) » s'il est arrêté. */
public class VueSatellite extends VueEntite {

    private final Satellite satellite;

    public VueSatellite(Satellite satellite, ConvertisseurCoordonnees convertisseur) {
        super(satellite, convertisseur, "satellite.png");
        this.satellite = satellite;
    }

    /** Grisé s'il est hors service, en couleur sinon (EX-6.10). */
    @Override
    public void mettreAJourStatut() {
        griser(satellite.getStatut() == StatutSatellite.HORS_SERVICE);
    }
}
