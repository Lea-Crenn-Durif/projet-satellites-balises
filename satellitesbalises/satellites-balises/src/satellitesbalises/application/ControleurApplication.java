package satellitesbalises.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import satellitesbalises.langage.analyse.AnalyseurProgramme;
import satellitesbalises.langage.analyse.ErreurSyntaxe;
import satellitesbalises.langage.arbre.ErreurProgramme;
import satellitesbalises.langage.interpretation.InterpreteurProgramme;
import satellitesbalises.langage.interpretation.RapportExecution;
import satellitesbalises.modele.simulation.Simulation;
import satellitesbalises.vue.ActionsUtilisateur;

/**
 * Reçoit les demandes de l'utilisateur et les transmet à qui sait les traiter :
 * la boucle temps réel pour la pause et la vitesse, l'analyseur et l'interpréteur pour les programmes.
 *
 * <p>Tout se passe sur le fil d'affichage Swing, comme les pas de la minuterie : un programme est
 * donc toujours exécuté entre deux pas. La sauvegarde (étape 8) s'ajoutera ici.</p>
 */
class ControleurApplication implements ActionsUtilisateur {

    private final BoucleTempsReel boucle;
    private final Simulation simulation;
    private final AnalyseurProgramme analyseur;
    private final InterpreteurProgramme interpreteur;

    ControleurApplication(BoucleTempsReel boucle, Simulation simulation,
                          AnalyseurProgramme analyseur, InterpreteurProgramme interpreteur) {
        this.boucle = Objects.requireNonNull(boucle, "boucle");
        this.simulation = Objects.requireNonNull(simulation, "simulation");
        this.analyseur = Objects.requireNonNull(analyseur, "analyseur");
        this.interpreteur = Objects.requireNonNull(interpreteur, "interpreteur");
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

    @Override
    public String executerProgramme(String texte) {
        long premierPas = simulation.getPasCourant() + 1;
        try {
            RapportExecution rapport = interpreteur.interpreter(analyseur.analyser(texte), simulation);
            if (rapport.estReussi()) {
                return "Programme accepté : " + rapport.getNombreDeCommandes()
                        + " commande(s) planifiée(s) à partir du pas " + premierPas + ".";
            }
            return refus(rapport.getErreurs());
        } catch (ErreurSyntaxe erreur) {
            return refus(erreur.getErreurs());
        }
    }

    /** Tout ou rien (EX-7.7) : on liste toutes les erreurs et on rappelle que rien n'a été planifié. */
    private static String refus(List<ErreurProgramme> erreurs) {
        return "Programme refusé, rien n'a été planifié :\n" + erreurs.stream()
                .map(erreur -> "  " + erreur)
                .collect(Collectors.joining("\n"));
    }
}
