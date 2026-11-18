package satellitesbalises.langage.arbre;

import java.util.List;


public final class ProgrammesExemples {

    private ProgrammesExemples() {
    }

    // ---- petits raccourcis pour écrire les arbres lisiblement ----

    public static Propriete nombre(String nom, double valeur) {
        return new Propriete(nom, new ValeurNombre(valeur));
    }

    public static Propriete texte(String nom, String valeur) {
        return new Propriete(nom, new ValeurTexte(valeur));
    }

    public static AppelComportement appel(String nom, Propriete... parametres) {
        return new AppelComportement(nom, List.of(parametres));
    }

    /** Exemple 1 : construction d'une campagne simple (exemples/01-construction.sb). */
    public static Programme construction() {
        return new Programme(List.of(
                new InstructionCreer(4, TypeEntite.SATELLITE, "s1", 0, 200, List.of(nombre("portee", 40), nombre("debit", 3))),
                new InstructionComportement(5, "s1", Aspect.DEPLACEMENT, appel("orbital", nombre("vitesse", 2))),
                new InstructionCreer(7, TypeEntite.SATELLITE, "s2", 600, 250, List.of()),
                new InstructionComportement(8, "s2", Aspect.DEPLACEMENT, appel("geostationnaire")),
                new InstructionCreer(10, TypeEntite.BALISE, "b1", 120, -80, List.of(nombre("memoire", 30), nombre("vitesseVerticale", 2))),
                new InstructionComportement(11, "b1", Aspect.DEPLACEMENT,
                        appel("sinusoidal", nombre("amplitude", 15), nombre("periode", 60), nombre("vitesse", 1), nombre("profondeur", 80))),
                new InstructionEquiper(12, "b1", List.of(appel("temperature"), appel("salinite"))),
                new InstructionCreer(14, TypeEntite.BALISE, "b2", 400, -150, List.of()),
                new InstructionComportement(15, "b2", Aspect.DEPLACEMENT, appel("horizontal", nombre("vitesse", 2))),
                new InstructionEquiper(16, "b2", List.of(appel("courantometre")))));
    }

    /** Exemple 2 : évolution dans le temps. */
    public static Programme evolution() {
        return new Programme(List.of(
                new InstructionCreer(3, TypeEntite.BALISE, "b1", 200, -100, List.of(nombre("memoire", 20))),
                new InstructionComportement(4, "b1", Aspect.DEPLACEMENT,
                        appel("vertical", nombre("vitesse", 3), nombre("profondeurMin", 40), nombre("profondeurMax", 200))),
                new InstructionEquiper(5, "b1", List.of(appel("temperature"))),
                new InstructionAttendre(7, 100),
                new InstructionComportement(8, "b1", Aspect.DEPLACEMENT, appel("horizontal", nombre("vitesse", 1))),
                new InstructionAttendre(10, 100),
                new InstructionConfigurer(11, "b1", List.of(nombre("memoire", 40))),
                new InstructionComportement(12, "b1", Aspect.REMONTEE, appel("duree", nombre("pas", 150)))));
    }

    /** Exemple 3 : équipement et apparence. */
    public static Programme equipement() {
        return new Programme(List.of(
                new InstructionCreer(3, TypeEntite.BALISE, "b1", 300, -60, List.of()),
                new InstructionEquiper(4, "b1", List.of(appel("temperature"), appel("salinite"), appel("courantometre"), appel("turbidite"))),
                new InstructionApparence(5, "b1", List.of(texte("couleur", "orange"), nombre("taille", 14))),
                new InstructionAttendre(7, 50),
                new InstructionDesequiper(8, "b1", "turbidite"),
                new InstructionApparence(9, "b1", List.of(texte("couleur", "rouge")))));
    }

    /** Exemple 4 : communication et panne (exemples/04-communication.sb). */
    public static Programme communication() {
        return new Programme(List.of(
                new InstructionCreer(3, TypeEntite.SATELLITE, "s1", 100, 200, List.of(nombre("portee", 60), nombre("debit", 2))),
                new InstructionComportement(4, "s1", Aspect.DEPLACEMENT, appel("orbital", nombre("vitesse", 3))),
                new InstructionCreer(6, TypeEntite.BALISE, "b1", 150, -40, List.of(nombre("memoire", 15), nombre("vitesseVerticale", 4))),
                new InstructionEquiper(7, "b1", List.of(appel("temperature"))),
                new InstructionAttendre(9, 200),
                new InstructionAgir(10, "s1", Ordre.ARRETER),
                new InstructionAttendre(11, 100),
                new InstructionAgir(12, "s1", Ordre.DEMARRER),
                new InstructionAgir(13, "b1", Ordre.REMONTER)));
    }
}
