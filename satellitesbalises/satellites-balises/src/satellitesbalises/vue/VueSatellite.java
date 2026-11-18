package satellitesbalises.vue;

import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.entite.Satellite;

public class VueSatellite extends VueEntite {

    private final Satellite satellite;

    public VueSatellite(Satellite satellite, ConvertisseurCoordonnees convertisseur) {
        super(satellite, convertisseur, "satellite.png");
        this.satellite = satellite;
    }

    @Override
    public void mettreAJourStatut() {
        griser(satellite.getStatut() == StatutSatellite.HORS_SERVICE);
    }
}
