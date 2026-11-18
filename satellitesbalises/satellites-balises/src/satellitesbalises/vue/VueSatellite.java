package satellitesbalises.vue;

import satellitesbalises.modele.entite.Satellite;

public class VueSatellite extends VueEntite {

    public VueSatellite(Satellite satellite, ConvertisseurCoordonnees convertisseur) {
        super(satellite, convertisseur, "satellite.png");
    }
}
