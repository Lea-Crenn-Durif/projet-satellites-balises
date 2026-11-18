package satellitesbalises.modele.activite;

import java.io.Serializable;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Ce que fait une balise en ce moment (patron État, décision D4) : collecte, remontée, attente,
 * transmission ou descente. La balise confie chaque pas à son activité courante.
 *
 * <p>Étape 4 : la collecte. Les autres activités et le passage de l'une à l'autre arrivent à l'étape 5.</p>
 */
public interface Activite extends Serializable {

    /** Le nom affiché à l'écran, par exemple « Collecte ». */
    String getNom();

    /** Ce que fait la balise pendant un pas, dans cette activité. */
    void agir(Balise balise, ContexteSimulation contexte);
}
