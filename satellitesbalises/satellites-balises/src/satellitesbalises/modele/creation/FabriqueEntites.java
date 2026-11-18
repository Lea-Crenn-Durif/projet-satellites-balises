package satellitesbalises.modele.creation;

import java.util.Objects;

import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.monde.Zone;

/**
 * Le point d'entrée pour créer des entités (décision D3) : elle donne un monteur par entité,
 * et vérifie pour lui que l'entité est bien placée.
 *
 * <pre>
 * Satellite s1 = fabrique.nouveauSatellite("s1").en(new Position(10, 200)).avecPortee(40).construire();
 * </pre>
 */
public class FabriqueEntites {

    private final GeometrieMonde geometrie;

    public FabriqueEntites(GeometrieMonde geometrie) {
        this.geometrie = Objects.requireNonNull(geometrie, "geometrie");
    }

    public ConstructeurSatellite nouveauSatellite(String nom) {
        return new ConstructeurSatellite(nom, this);
    }

    public ConstructeurBalise nouvelleBalise(String nom) {
        return new ConstructeurBalise(nom, this);
    }

    /**
     * Refuse une position absente, hors du monde, ou dans la mauvaise zone (EX-2.3).
     *
     * @param description par exemple « Satellite s1 », pour un message clair
     */
    void verifierPlacement(String description, Position position, Zone zoneAttendue) {
        if (position == null) {
            throw new ErreurConfiguration(description + " : la position est obligatoire");
        }
        if (!geometrie.contient(position)) {
            throw new ErreurConfiguration(description + " : la position " + position + " est hors du monde");
        }
        if (geometrie.zoneDe(position) != zoneAttendue) {
            throw new ErreurConfiguration(description + " : la position " + position
                    + " n'est pas dans la zone " + zoneAttendue);
        }
    }

    /** Refuse une valeur nulle ou négative (EX-2.3). */
    static void verifierStrictementPositif(String description, String parametre, double valeur) {
        if (valeur <= 0) {
            throw new ErreurConfiguration(description + " : " + parametre + " doit être strictement positif ("
                    + valeur + ")");
        }
    }
}
