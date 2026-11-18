package satellitesbalises.vue;

import satellitesbalises.modele.entite.Balise;

/** Une balise : une icône jaune dans la mer. Son activité et sa mémoire sont dans le panneau d'observation. */
public class VueBalise extends VueEntite {

    public VueBalise(Balise balise, ConvertisseurCoordonnees convertisseur) {
        super(balise, convertisseur, "balise.png");
    }
}
