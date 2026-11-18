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

public class Satellite extends Entite {

    private static final long serialVersionUID = 1L;

    private static final Set<String> PROPRIETES = Set.of("portee", "debit");

    private double portee;
    private int debit;
    private StatutSatellite statut = StatutSatellite.DISPONIBLE;
    private final ArchiveSatellite archive = new ArchiveSatellite();

    public Satellite(String nom, Position position, Deplacement deplacement, Apparence apparence,
                     double portee, int debit) {
        super(nom, position, deplacement, apparence);
        this.portee = portee;
        this.debit = debit;
    }

    public double getPortee() {
        return portee;
    }

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

    public void reserver() {
        if (statut == StatutSatellite.DISPONIBLE) {
            statut = StatutSatellite.OCCUPE;
        }
    }

    public void liberer() {
        if (statut == StatutSatellite.OCCUPE) {
            statut = StatutSatellite.DISPONIBLE;
        }
    }

    public void arreter() {
        statut = StatutSatellite.HORS_SERVICE;
    }

    public void demarrer() {
        if (statut == StatutSatellite.HORS_SERVICE) {
            statut = StatutSatellite.DISPONIBLE;
        }
    }

    public boolean couvre(Position position) {
        return getPosition().ecartHorizontal(position) <= portee;
    }

    public void recevoir(String nomBalise, List<Mesure> mesures) {
        archive.ajouter(nomBalise, mesures);
    }

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

    @Override
    public void agir(ContexteSimulation contexte) {
        seDeplacer(contexte);
    }

    @Override
    public void accepter(VisiteurEntite visiteur) {
        visiteur.visiter(this);
    }
}
