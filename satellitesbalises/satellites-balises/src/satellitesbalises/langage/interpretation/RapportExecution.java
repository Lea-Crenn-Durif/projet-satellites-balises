package satellitesbalises.langage.interpretation;

import java.util.List;

import satellitesbalises.langage.arbre.ErreurProgramme;

/**
 * Le compte rendu d'un programme : combien de commandes ont été planifiées, ou pourquoi aucune
 * ne l'a été (EX-7.7 : tout ou rien).
 */
public final class RapportExecution {

    private final int nombreDeCommandes;
    private final List<ErreurProgramme> erreurs;

    public RapportExecution(int nombreDeCommandes, List<ErreurProgramme> erreurs) {
        this.nombreDeCommandes = nombreDeCommandes;
        this.erreurs = List.copyOf(erreurs);
    }

    /** Vrai si le programme ne contenait aucune erreur : ses commandes sont planifiées. */
    public boolean estReussi() {
        return erreurs.isEmpty();
    }

    /** Nombre de commandes planifiées ; 0 si le programme contenait une erreur. */
    public int getNombreDeCommandes() {
        return nombreDeCommandes;
    }

    /** Toutes les erreurs du programme, dans l'ordre des lignes. */
    public List<ErreurProgramme> getErreurs() {
        return erreurs;
    }

    @Override
    public String toString() {
        return estReussi() ? nombreDeCommandes + " commandes planifiées" : erreurs.size() + " erreur(s) : " + erreurs;
    }
}
