package satellitesbalises.modele.monde;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Entite;
import satellitesbalises.modele.entite.Satellite;

public class Monde implements Serializable {

    private static final long serialVersionUID = 1L;

    private final GeometrieMonde geometrie;
    private final Map<String, Satellite> satellites = new LinkedHashMap<>();
    private final Map<String, Balise> balises = new LinkedHashMap<>();

    public Monde(GeometrieMonde geometrie) {
        this.geometrie = Objects.requireNonNull(geometrie, "geometrie");
    }

    public GeometrieMonde getGeometrie() {
        return geometrie;
    }

    public void ajouter(Satellite satellite) {
        verifierNomLibre(satellite.getNom());
        satellites.put(satellite.getNom(), satellite);
    }

    public void ajouter(Balise balise) {
        verifierNomLibre(balise.getNom());
        balises.put(balise.getNom(), balise);
    }

    public Optional<Entite> retirer(String nom) {
        Satellite satellite = satellites.remove(nom);
        if (satellite != null) {
            return Optional.of(satellite);
        }
        return Optional.ofNullable(balises.remove(nom));
    }

    public Optional<Entite> trouver(String nom) {
        Optional<Entite> satellite = trouverSatellite(nom).map(Entite.class::cast);
        return satellite.isPresent() ? satellite : trouverBalise(nom).map(Entite.class::cast);
    }

    public Optional<Satellite> trouverSatellite(String nom) {
        return Optional.ofNullable(satellites.get(nom));
    }

    public Optional<Balise> trouverBalise(String nom) {
        return Optional.ofNullable(balises.get(nom));
    }

    public Collection<Satellite> getSatellites() {
        return Collections.unmodifiableCollection(satellites.values());
    }

    public Collection<Balise> getBalises() {
        return Collections.unmodifiableCollection(balises.values());
    }

    private void verifierNomLibre(String nom) {
        if (satellites.containsKey(nom) || balises.containsKey(nom)) {
            throw new ErreurConfiguration("Le nom « " + nom + " » est déjà utilisé");
        }
    }
}
