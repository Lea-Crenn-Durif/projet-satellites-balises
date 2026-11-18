package satellitesbalises.vue;

/**
 * Ce que l'utilisateur peut demander depuis l'écran.
 *
 * <p>Défini dans la vue et implémenté par l'application (inversion de dépendance) : la vue
 * n'a pas besoin de savoir comment le temps, le langage ou la sauvegarde fonctionnent.
 * Étape 1 : pause et vitesse. Les autres actions arrivent avec leurs étapes.</p>
 */
public interface ActionsUtilisateur {

    /** Met la simulation en pause si elle tourne, la relance si elle est en pause. */
    void basculerPause();

    /** Change la durée d'un pas, en millisecondes. */
    void changerVitesse(int millisecondesParPas);
}
