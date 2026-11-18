package satellitesbalises.vue;

import satellitesbalises.modele.entite.Balise;

public class VueBalise extends VueEntite {

    public VueBalise(Balise balise, ConvertisseurCoordonnees convertisseur) {
        super(balise, convertisseur, "balise.png");
    }
}
