package satellitesbalises.vue;

import satellitesbalises.modele.entite.Satellite;

/** Un satellite : une icône de satellite grise dans le ciel. */
public class VueSatellite extends VueEntite {

    public VueSatellite(Satellite satellite, ConvertisseurCoordonnees convertisseur) {
        super(satellite, convertisseur, "satellite.png");
    }
}
