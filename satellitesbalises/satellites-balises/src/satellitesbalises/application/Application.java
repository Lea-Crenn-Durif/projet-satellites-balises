package satellitesbalises.application;

import javax.swing.SwingUtilities;

import eventHandler.EventHandler;
import satellitesbalises.modele.activite.ConditionDuree;
import satellitesbalises.modele.commande.CommandeChangerStatut;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.deplacement.DeplacementHorizontal;
import satellitesbalises.modele.deplacement.DeplacementOrbital;
import satellitesbalises.modele.deplacement.DeplacementSinusoidal;
import satellitesbalises.modele.deplacement.DeplacementVertical;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.InstrumentCourantometre;
import satellitesbalises.modele.mesure.InstrumentSimple;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.Simulation;
import satellitesbalises.vue.VueSimulation;

public final class Application {

    private static final GeometrieMonde GEOMETRIE = new GeometrieMonde(800, 300, 300);
    private static final int MILLISECONDES_PAR_PAS = 100;

    private Application() {
        // Classe utilitaire : pas d'instance.
    }

    public static void main(String[] arguments) {
        SwingUtilities.invokeLater(Application::demarrer);
    }

    private static void demarrer() {
        EventHandler eventHandler = new EventHandler();
        Simulation simulation = new Simulation(new Monde(GEOMETRIE), EnvironnementSimule.parDefaut(), eventHandler);

        BoucleTempsReel boucle = new BoucleTempsReel(simulation, MILLISECONDES_PAR_PAS);
        VueSimulation vue = new VueSimulation(GEOMETRIE, new ControleurApplication(boucle));
        vue.abonner(eventHandler);
        ajouterEntitesDeDemonstration(simulation);

        vue.ouvrir();
        boucle.demarrer();
    }

    private static void ajouterEntitesDeDemonstration(Simulation simulation) {
        FabriqueEntites fabrique = new FabriqueEntites(GEOMETRIE);
        simulation.ajouter(fabrique.nouveauSatellite("s1").en(new Position(120, 230)).avecDebit(1)
                .avecDeplacement(new DeplacementOrbital(4)).construire());
        simulation.ajouter(fabrique.nouveauSatellite("s2").en(new Position(400, 260)).avecPortee(40)
                .avecDeplacement(new DeplacementOrbital(1)).construire());
        simulation.ajouter(fabrique.nouveauSatellite("s3").en(new Position(680, 210))
                .avecApparence(new Apparence("rouge", 40)).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b1").en(new Position(100, -80)).avecVitesseVerticale(3)
                .avecDeplacement(new DeplacementHorizontal(2))
                .equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE),
                        new InstrumentSimple("salinite", Grandeur.SALINITE)).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b2").en(new Position(300, -180)).avecMemoire(10).avecVitesseVerticale(4)
                .avecDeplacement(new DeplacementVertical(2, 40, 260))
                .equipeeDe(new InstrumentCourantometre()).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b3").en(new Position(520, -120)).avecMemoire(20)
                .avecDeplacement(new DeplacementSinusoidal(1, 40, 120, 150))
                .equipeeDe(new InstrumentSimple("turbidite", Grandeur.TURBIDITE),
                        new InstrumentSimple("temperature", Grandeur.TEMPERATURE))
                .avecApparence(new Apparence("orange", 40)).construire());
        simulation.ajouter(fabrique.nouvelleBalise("b4").en(new Position(700, -240)).avecVitesseVerticale(3)
                .remontantQuand(new ConditionDuree(60)).construire());
        simulation.planifier(new CommandeChangerStatut("s2", false), 150);
        simulation.planifier(new CommandeChangerStatut("s2", true), 300);
    }
}
