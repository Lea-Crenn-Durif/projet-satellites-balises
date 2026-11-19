package satellitesbalises.modele.entite;

import java.util.List;
import java.util.Set;

import satellitesbalises.modele.communication.ArchiveSatellite;
import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Un satellite, dans le ciel. Il reçoit les mesures des balises qui sont à sa portée,
 * au rythme de son débit, et les range dans son archive (EX-6.1 à EX-6.11).
 *
 * <p>Il ne connaît pas les balises : c'est le gestionnaire de communications qui les met en relation.</p>
 */
public class Satellite extends Entite {

    private static final long serialVersionUID = 1L;

    /** Les caractéristiques qu'on peut régler à la création ou avec « configurer » (exemple : portee = 40 ; debit = 3). */
    public static final Set<String> PROPRIETES = Set.of("portee", "debit");

    private double portee;
    private int debit;
    private StatutSatellite statut = StatutSatellite.DISPONIBLE;
    private final ArchiveSatellite archive = new ArchiveSatellite();

    /** Utiliser {@code FabriqueEntites.nouveauSatellite(nom)}, qui vérifie les valeurs. */
    public Satellite(String nom, Position position, Deplacement deplacement, Apparence apparence,
                     double portee, int debit) {
        super(nom, position, deplacement, apparence);
        this.portee = portee;
        this.debit = debit;
    }

    /** Distance horizontale jusqu'à laquelle il peut communiquer avec une balise. */
    public double getPortee() {
        return portee;
    }

    /** Nombre de mesures reçues par pas pendant un transfert. */
    public int getDebit() {
        return debit;
    }

    public StatutSatellite getStatut() {
        return statut;
    }

    public boolean estDisponible() {
        return statut == StatutSatellite.DISPONIBLE;
    }

    public ArchiveSatellite getArchive() {
        return archive;
    }

    /** Un transfert commence avec lui : il devient OCCUPE (EX-6.4). */
    public void reserver() {
        if (statut == StatutSatellite.DISPONIBLE) {
            statut = StatutSatellite.OCCUPE;
        }
    }

    /** Son transfert est fini : il redevient DISPONIBLE, sauf s'il a été arrêté entre-temps (EX-6.9). */
    public void liberer() {
        if (statut == StatutSatellite.OCCUPE) {
            statut = StatutSatellite.DISPONIBLE;
        }
    }

    /** L'utilisateur l'arrête : HORS_SERVICE (EX-6.1). Un transfert en cours sera rompu. */
    public void arreter() {
        statut = StatutSatellite.HORS_SERVICE;
    }

    /** L'utilisateur le redémarre : DISPONIBLE (EX-6.1). */
    public void demarrer() {
        if (statut == StatutSatellite.HORS_SERVICE) {
            statut = StatutSatellite.DISPONIBLE;
        }
    }

    /**
     * Vrai si la position est à sa portée : |x satellite − x position| ≤ portée (EX-6.2).
     * L'écart se mesure dans le monde, sans faire le tour par le bord (EX-6.11).
     */
    public boolean couvre(Position position) {
        return getPosition().ecartHorizontal(position) <= portee;
    }

    /** Range des mesures reçues d'une balise (EX-6.6). */
    public void recevoir(String nomBalise, List<Mesure> mesures) {
        archive.ajouter(nomBalise, mesures);
    }

    /** Propriétés : « portee » et « debit », strictement positives. */
    @Override
    public void configurer(Parametres parametres) {
        for (String nom : parametres.getValeurs().keySet()) {
            if (!PROPRIETES.contains(nom)) {
                throw new ErreurConfiguration("Satellite " + getNom() + " : propriété inconnue « " + nom + " »");
            }
        }
        double nouvellePortee = parametres.nombre("portee", portee);
        int nouveauDebit = parametres.entier("debit", debit);
        if (nouvellePortee <= 0 || nouveauDebit <= 0) {
            throw new ErreurConfiguration("Satellite " + getNom() + " : la portée et le débit doivent être > 0");
        }
        portee = nouvellePortee;
        debit = nouveauDebit;
    }

    /** Étape 3 : il se déplace. La communication avec les balises arrive à l'étape 6. */
    @Override
    public void agir(ContexteSimulation contexte) {
        seDeplacer(contexte);
    }

    @Override
    public void accepter(VisiteurEntite visiteur) {
        visiteur.visiter(this);
    }
}
