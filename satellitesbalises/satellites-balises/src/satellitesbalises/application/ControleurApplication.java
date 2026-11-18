package satellitesbalises.application;

import java.util.Objects;

import satellitesbalises.vue.ActionsUtilisateur;


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
