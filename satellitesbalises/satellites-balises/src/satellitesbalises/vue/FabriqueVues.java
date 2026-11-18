package satellitesbalises.vue;

import java.util.Objects;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Entite;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.entite.VisiteurEntite;

public class FabriqueVues implements VisiteurEntite {

    private final ConvertisseurCoordonnees convertisseur;
    private VueEntite vueCreee;

    public FabriqueVues(ConvertisseurCoordonnees convertisseur) {
        this.convertisseur = Objects.requireNonNull(convertisseur, "convertisseur");
    }

    public VueEntite creerVue(Entite entite) {
        entite.accepter(this);
        return vueCreee;
    }

    @Override
    public void visiter(Satellite satellite) {
        vueCreee = new VueSatellite(satellite, convertisseur);
    }

    @Override
    public void visiter(Balise balise) {
        vueCreee = new VueBalise(balise, convertisseur);
    }
}
