package satellitesbalises.vue;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

import javax.imageio.ImageIO;

import nicellipse.component.NiImage;
import nicellipse.component.NiLabel;
import nicellipse.component.NiSpace;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Entite;

public abstract class VueEntite {

    private static final Color FOND_DU_NOM = new Color(255, 255, 255, 200);

    private final Entite entite;
    private final ConvertisseurCoordonnees convertisseur;
    private final Image imageOriginale;
    protected final NiImage icone;
    protected final NiLabel nom = new NiLabel();

    protected VueEntite(Entite entite, ConvertisseurCoordonnees convertisseur, String fichierImage) {
        this.entite = Objects.requireNonNull(entite, "entite");
        this.convertisseur = Objects.requireNonNull(convertisseur, "convertisseur");
        this.imageOriginale = chargerImage(fichierImage);
        this.icone = new NiImage(imageOriginale);
        nom.setText(entite.getNom());
        nom.setOpaque(true);
        nom.setBackground(FOND_DU_NOM);
        appliquerApparence();
    }

    void afficherDans(NiSpace espace) {
        espace.add(nom, 0);
        espace.add(icone, 0);
    }

    void retirerDe(NiSpace espace) {
        espace.remove(icone);
        espace.remove(nom);
    }

    public void mettreAJourPosition() {
        placer();
    }

    public void mettreAJourApparence() {
        appliquerApparence();
    }

    private void placer() {
        Point centre = convertisseur.versEcran(entite.getPosition());
        int demiTaille = icone.getWidth() / 2;
        icone.setLocation(centre.x - demiTaille, centre.y - demiTaille);
        nom.setLocation(centre.x + demiTaille, centre.y - demiTaille);
    }

    private void appliquerApparence() {
        Apparence apparence = entite.getApparence();
        int taille = Math.max(1, apparence.getTaille());
        icone.setImage(imageOriginale.getScaledInstance(taille, taille, Image.SCALE_SMOOTH));
        nom.setForeground(Couleurs.depuisNom(apparence.getCouleur()));
        placer();
    }

    private static Image chargerImage(String fichier) {
        URL adresse = VueEntite.class.getResource("images/" + fichier);
        if (adresse == null) {
            throw new IllegalStateException("Image introuvable : images/" + fichier);
        }
        try {
            return ImageIO.read(adresse);
        } catch (IOException erreur) {
            throw new UncheckedIOException("Image illisible : images/" + fichier, erreur);
        }
    }
}
