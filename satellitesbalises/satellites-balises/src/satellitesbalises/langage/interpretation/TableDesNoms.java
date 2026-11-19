package satellitesbalises.langage.interpretation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import satellitesbalises.langage.arbre.TypeEntite;
import satellitesbalises.modele.monde.Monde;

/**
 * Les entités connues pendant l'interprétation d'un programme, avec leur type (EX-7.8).
 *
 * <p>Elle part des entités déjà dans la simulation, puis suit le programme ligne à ligne :
 * une entité créée plus haut est connue plus bas, une entité supprimée ne l'est plus.</p>
 */
class TableDesNoms {

    private final Map<String, TypeEntite> noms = new HashMap<>();

    /** Une table qui connaît déjà les satellites et les balises du monde. */
    static TableDesNoms depuis(Monde monde) {
        TableDesNoms table = new TableDesNoms();
        monde.getSatellites().forEach(satellite -> table.declarer(satellite.getNom(), TypeEntite.SATELLITE));
        monde.getBalises().forEach(balise -> table.declarer(balise.getNom(), TypeEntite.BALISE));
        return table;
    }

    void declarer(String nom, TypeEntite type) {
        noms.put(nom, type);
    }

    void oublier(String nom) {
        noms.remove(nom);
    }

    /** Le type de l'entité, ou rien si aucune entité ne porte ce nom. */
    Optional<TypeEntite> typeDe(String nom) {
        return Optional.ofNullable(noms.get(nom));
    }
}
