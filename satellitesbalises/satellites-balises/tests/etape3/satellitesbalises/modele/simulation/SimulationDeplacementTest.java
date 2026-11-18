package satellitesbalises.modele.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.commande.CommandeChangerDeplacement;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.deplacement.DeplacementHorizontal;
import satellitesbalises.modele.deplacement.DeplacementOrbital;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EcouteurEntites;
import satellitesbalises.modele.evenement.EvenementApparenceModifiee;
import satellitesbalises.modele.evenement.EvenementEntiteAjoutee;
import satellitesbalises.modele.evenement.EvenementEntiteSupprimee;
import satellitesbalises.modele.evenement.EvenementPositionModifiee;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;

/** Étape 3 — EX-3.9, EX-3.10 : les entités bougent dans la simulation et le font savoir. */
class SimulationDeplacementTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final EventHandler eventHandler = new EventHandler();
    private Simulation simulation;

    @BeforeEach
    void creerSimulation() {
        simulation = new Simulation(new Monde(geometrie), (g, p, pas) -> 0.0, eventHandler);
    }

    @Test
    void satelliteOrbital_avanceAChaquePas() {
        Satellite s1 = fabrique.nouveauSatellite("s1").en(new Position(10, 70))
                .avecDeplacement(new DeplacementOrbital(3)).construire();
        simulation.ajouter(s1);

        simulation.avancerDUnPas();
        simulation.avancerDUnPas();

        assertEquals(new Position(16, 70), s1.getPosition());
    }

    @Test
    void changerDeplacement_appliqueAuPasSuivant() {
        Balise b1 = fabrique.nouvelleBalise("b1").en(new Position(50, -30)).construire(); // immobile par défaut
        simulation.ajouter(b1);
        simulation.planifier(new CommandeChangerDeplacement("b1", new DeplacementHorizontal(2)), 2);

        simulation.avancerDUnPas();
        assertEquals(50, b1.getPosition().getX(), 1e-9, "encore immobile au pas 1");

        simulation.avancerDUnPas();
        assertEquals(52, b1.getPosition().getX(), 1e-9, "le nouveau déplacement agit dès le pas 2");
    }

    @Test
    void agir_publiePositionModifiee() {
        Satellite s1 = fabrique.nouveauSatellite("s1").en(new Position(10, 70))
                .avecDeplacement(new DeplacementOrbital(3)).construire();
        simulation.ajouter(s1);
        EcouteurDePositions ecouteur = new EcouteurDePositions();
        eventHandler.registerListener(EvenementPositionModifiee.class, ecouteur);

        simulation.avancerDUnPas();

        assertEquals(List.of("s1"), ecouteur.noms);
    }

    @Test
    void entiteImmobile_neDeclenchePasDEvenementInutile() {
        simulation.ajouter(fabrique.nouveauSatellite("s1").en(new Position(10, 70)).construire());
        EcouteurDePositions ecouteur = new EcouteurDePositions();
        eventHandler.registerListener(EvenementPositionModifiee.class, ecouteur);

        simulation.avancerDUnPas();

        assertTrue(ecouteur.noms.isEmpty(), "une position inchangée n'est pas annoncée");
    }

    private static final class EcouteurDePositions implements EcouteurEntites {
        private final List<String> noms = new ArrayList<>();

        @Override
        public void positionModifiee(EvenementPositionModifiee e) {
            noms.add(e.getEntite().getNom());
        }

        @Override
        public void entiteAjoutee(EvenementEntiteAjoutee e) {
        }

        @Override
        public void entiteSupprimee(EvenementEntiteSupprimee e) {
        }

        @Override
        public void apparenceModifiee(EvenementApparenceModifiee e) {
        }
    }
}
