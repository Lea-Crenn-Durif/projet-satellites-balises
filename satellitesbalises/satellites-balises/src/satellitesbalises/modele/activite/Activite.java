package satellitesbalises.modele.activite;

import java.io.Serializable;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Ce que fait une balise en ce moment (patron État, décision D4) :
 * Collecte → Remontée → Attente → Transmission → Descente → Collecte (EX-5.1).
 *
 * <p>La balise confie chaque pas à son activité courante, et chaque activité décide elle-même
 * de l'activité suivante : {@code Balise} ne contient aucun {@code switch}. Les méthodes {@code sur…}
 * ne font rien par défaut : seules les activités concernées les redéfinissent.</p>
 */
public interface Activite extends Serializable {

    /** Le nom affiché à l'écran, par exemple « Collecte ». */
    String getNom();

    /** Ce que fait la balise pendant un pas, dans cette activité. */
    void agir(Balise balise, ContexteSimulation contexte);

    /** Vrai si un satellite peut commencer un transfert avec la balise (seulement en Attente). */
    default boolean accepteTransmission() {
        return false;
    }

    /** Un satellite commence à recevoir les mesures de la balise. */
    default void surTransfertDemarre(Balise balise, ContexteSimulation contexte) {
        // Rien à faire en dehors de l'attente.
    }

    /**
     * Le transfert est fini.
     *
     * @param complet vrai si toutes les mesures sont parties, faux s'il a été interrompu
     */
    default void surTransfertTermine(Balise balise, boolean complet, ContexteSimulation contexte) {
        // Rien à faire en dehors de la transmission.
    }
}
