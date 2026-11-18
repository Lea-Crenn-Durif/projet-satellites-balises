package satellitesbalises.modele.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import satellitesbalises.modele.mesure.Mesure;

public class ArchiveSatellite implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, List<Mesure>> recues = new LinkedHashMap<>();

    public void ajouter(String nomBalise, List<Mesure> mesures) {
        recues.computeIfAbsent(nomBalise, nom -> new ArrayList<>()).addAll(mesures);
    }

    public int getNombreMesures() {
        return recues.values().stream().mapToInt(List::size).sum();
    }

    public List<Mesure> getMesuresDe(String nomBalise) {
        return Collections.unmodifiableList(recues.getOrDefault(nomBalise, List.of()));
    }
}
