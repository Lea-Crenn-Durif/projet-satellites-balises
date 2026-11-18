package satellitesbalises.modele.creation;

import java.util.Objects;

import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.monde.Zone;


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

    static void verifierStrictementPositif(String description, String parametre, double valeur) {
        if (valeur <= 0) {
            throw new ErreurConfiguration(description + " : " + parametre + " doit être strictement positif ("
                    + valeur + ")");
        }
    }
}
