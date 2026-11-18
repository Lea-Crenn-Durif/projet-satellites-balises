package satellitesbalises.modele.communication;

/** L'état d'un satellite vis-à-vis des communications (EX-6.1). */
public enum StatutSatellite {
    /** Peut recevoir les mesures d'une balise. */
    DISPONIBLE,
    /** Reçoit déjà les mesures d'une balise. */
    OCCUPE,
    /** Arrêté par l'utilisateur. */
    HORS_SERVICE
}
