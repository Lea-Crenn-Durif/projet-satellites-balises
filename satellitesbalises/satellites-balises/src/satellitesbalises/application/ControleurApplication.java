package satellitesbalises.application;

import java.util.Objects;

import satellitesbalises.vue.ActionsUtilisateur;

/**
 * Reçoit les demandes de l'utilisateur et les transmet à qui sait les traiter.
 *
 * <p>Étape 1 : la pause et la vitesse, confiées à la boucle temps réel. Le langage (étape 7)
 * et la sauvegarde (étape 8) s'ajouteront ici.</p>
 */
class ControleurApplication implements ActionsUtilisateur {

    private final BoucleTempsReel boucle;

    ControleurApplication(BoucleTempsReel boucle) {
        this.boucle = Objects.requireNonNull(boucle, "boucle");
    }

    @Override
    public void basculerPause() {
        if (boucle.estEnPause()) {
            boucle.demarrer();
        } else {
            boucle.mettreEnPause();
        }
    }

    @Override
    public void changerVitesse(int millisecondesParPas) {
        boucle.changerVitesse(millisecondesParPas);
    }
}
