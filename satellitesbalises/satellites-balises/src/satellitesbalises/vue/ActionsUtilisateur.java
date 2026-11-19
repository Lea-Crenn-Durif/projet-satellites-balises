package satellitesbalises.vue;

/**
 * Ce que l'utilisateur peut demander depuis l'écran.
 *
 * <p>Défini dans la vue et implémenté par l'application (inversion de dépendance) : la vue
 * n'a pas besoin de savoir comment le temps, le langage ou la sauvegarde fonctionnent.
 * Étapes 1 et 7 : pause, vitesse, programme. La sauvegarde arrive à l'étape 8.</p>
 */
public interface ActionsUtilisateur {

    /** Met la simulation en pause si elle tourne, la relance si elle est en pause. */
    void basculerPause();

    /** Change la durée d'un pas, en millisecondes. */
    void changerVitesse(int millisecondesParPas);

    /**
     * Analyse et exécute un programme du langage de commandes (étape 7).
     *
     * @return le compte rendu à montrer à l'utilisateur : commandes planifiées, ou erreurs avec leur ligne
     */
    String executerProgramme(String texte);
}
