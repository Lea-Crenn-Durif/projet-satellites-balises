package satellitesbalises.modele.creation;

import satellitesbalises.modele.activite.ConditionMemoirePleine;
import satellitesbalises.modele.activite.ConditionRemontee;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.deplacement.DeplacementHorizontal;
import satellitesbalises.modele.deplacement.DeplacementImmobile;
import satellitesbalises.modele.deplacement.DeplacementOrbital;
import satellitesbalises.modele.deplacement.DeplacementSinusoidal;
import satellitesbalises.modele.deplacement.DeplacementVertical;
import satellitesbalises.modele.mesure.Instrument;


public final class CataloguesParDefaut {

    private CataloguesParDefaut() {
        // Classe utilitaire : pas d'instance.
    }

    public static Catalogues creer() {
        return new Catalogues(deplacements(), instruments(), conditionsRemontee());
    }

    private static Catalogue<Deplacement> deplacements() {
        Catalogue<Deplacement> catalogue = new Catalogue<>();
        catalogue.enregistrer("immobile", p -> new DeplacementImmobile());
        catalogue.enregistrer("horizontal", p -> new DeplacementHorizontal(p.nombre("vitesse", 1)));
        catalogue.enregistrer("vertical", p -> new DeplacementVertical(
                p.nombre("vitesse", 1), p.nombre("profondeurMin", 10), p.nombre("profondeurMax", 100)));
        catalogue.enregistrer("sinusoidal", p -> new DeplacementSinusoidal(
                p.nombre("vitesse", 1), p.nombre("amplitude", 10), p.nombre("periode", 50), p.nombre("profondeur", 50)));
        catalogue.enregistrer("orbital", p -> new DeplacementOrbital(p.nombre("vitesse", 1)));
        catalogue.enregistrer("geostationnaire", p -> new DeplacementImmobile());
        return catalogue;
    }

    private static Catalogue<Instrument> instruments() {
        return new Catalogue<>();
    }

    private static Catalogue<ConditionRemontee> conditionsRemontee() {
        Catalogue<ConditionRemontee> catalogue = new Catalogue<>();
        catalogue.enregistrer("memoirePleine", p -> new ConditionMemoirePleine());
        return catalogue;
    }
}
