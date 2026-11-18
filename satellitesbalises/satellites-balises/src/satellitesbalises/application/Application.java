package satellitesbalises.application;

import javax.swing.SwingUtilities;

import eventHandler.EventHandler;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.deplacement.DeplacementHorizontal;
import satellitesbalises.modele.deplacement.DeplacementOrbital;
import satellitesbalises.modele.deplacement.DeplacementSinusoidal;
import satellitesbalises.modele.deplacement.DeplacementVertical;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.mesure.Environnement;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.Simulation;
import satellitesbalises.vue.VueSimulation;


public final class Application {

    private static final GeometrieMonde GEOMETRIE = new GeometrieMonde(800, 300, 300);
    private static final int MILLISECONDES_PAR_PAS = 100;

    private static final Environnement ENVIRONNEMENT_NEUTRE = (grandeur, position, pas) -> 0.0;

    private Application() {
        // Classe utilitaire : pas d'instance.
    }

    public static void main(String[] arguments) {
        SwingUtilities.invokeLater(Application::demarrer);
    }

    private static void demarrer() {
        EventHandler eventHandler = new EventHandler();
        Simulation simulation = new Simulation(new Monde(GEOMETRIE), ENVIRONNEMENT_NEUTRE, eventHandler);

        BoucleTempsReel boucle = new BoucleTempsReel(simulation, MILLISECONDES_PAR_PAS);
        VueSimulation vue = new VueSimulation(GEOMETRIE, new ControleurApplication(boucle));
        vue.abonner(eventHandler);
        ajouterEntitesDeDemonstration(simulation);

        vue.ouvrir();
        boucle.demarrer();
    }


    private static void ajouterEntitesDeDemonstration(Simulation simulation) {
        FabriqueEntites fabrique = new FabriqueEntites(GEOMETRIE);
        simulation.ajouter(fabrique.nouveauSatellite("s1").en(new Position(120, 230))
                .avecDeplacement(new DeplacementOrbital(2)).construire());
        simulation.ajouter(fabrique.nouveauSatellite("s2").en(new Position(400, 260)).avecPortee(40)
                .avecDeplacement(new DeplacementOrbital(1)).construire());
        simulation.ajouter(fabrique.nouveauSatellite("s3").en(new Position(680, 210))
                .avecApparence(new Apparence("rouge", 40)).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b1").en(new Position(100, -80))
                .avecDeplacement(new DeplacementHorizontal(2)).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b2").en(new Position(300, -180)).avecMemoire(30)
                .avecDeplacement(new DeplacementVertical(2, 40, 260)).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b3").en(new Position(520, -120))
                .avecDeplacement(new DeplacementSinusoidal(1, 40, 120, 150))
                .avecApparence(new Apparence("orange", 40)).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b4").en(new Position(700, -240)).construire());
    }
}
