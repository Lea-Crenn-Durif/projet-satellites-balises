package satellitesbalises.modele.creation;

import java.util.Objects;

import satellitesbalises.modele.activite.ConditionRemontee;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.mesure.Instrument;

/** Les trois catalogues du projet, réunis pour être passés ensemble (au langage, par exemple). */
public final class Catalogues {

    private final Catalogue<Deplacement> deplacements;
    private final Catalogue<Instrument> instruments;
    private final Catalogue<ConditionRemontee> conditionsRemontee;

    public Catalogues(Catalogue<Deplacement> deplacements, Catalogue<Instrument> instruments,
                      Catalogue<ConditionRemontee> conditionsRemontee) {
        this.deplacements = Objects.requireNonNull(deplacements, "deplacements");
        this.instruments = Objects.requireNonNull(instruments, "instruments");
        this.conditionsRemontee = Objects.requireNonNull(conditionsRemontee, "conditionsRemontee");
    }

    public Catalogue<Deplacement> getDeplacements() {
        return deplacements;
    }

    public Catalogue<Instrument> getInstruments() {
        return instruments;
    }

    public Catalogue<ConditionRemontee> getConditionsRemontee() {
        return conditionsRemontee;
    }
}
