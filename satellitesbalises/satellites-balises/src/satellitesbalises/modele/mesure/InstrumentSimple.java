package satellitesbalises.modele.mesure;

import java.util.List;
import java.util.Objects;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/** Un instrument qui mesure une seule grandeur : thermomètre, salinomètre, turbidimètre. */
public class InstrumentSimple implements Instrument {

    private static final long serialVersionUID = 1L;

    private final String nom;
    private final Grandeur grandeur;

    public InstrumentSimple(String nom, Grandeur grandeur) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.grandeur = Objects.requireNonNull(grandeur, "grandeur");
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public List<Grandeur> getGrandeurs() {
        return List.of(grandeur);
    }

    @Override
    public Mesure mesurer(Position position, ContexteSimulation contexte) {
        double valeur = contexte.getEnvironnement().valeur(grandeur, position, contexte.getPas());
        return new Mesure(nom, contexte.getPas(), position, List.of(new ValeurMesuree(grandeur, valeur)));
    }
}
