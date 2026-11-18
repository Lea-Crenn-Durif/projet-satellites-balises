package satellitesbalises.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nicellipse.component.NiEllipse;
import nicellipse.component.NiSpace;
import satellitesbalises.modele.communication.Transfert;
import satellitesbalises.modele.monde.Position;

public final class VueTransfert {

    private static final Color ORANGE = new Color(255, 170, 0);
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private static final int[] DIAMETRES = {30, 50, 70};

    private final Transfert transfert;
    private final ConvertisseurCoordonnees convertisseur;
    private final List<NiEllipse> ondesSatellite = new ArrayList<>();
    private final List<NiEllipse> ondesBalise = new ArrayList<>();

    public VueTransfert(Transfert transfert, ConvertisseurCoordonnees convertisseur) {
        this.transfert = Objects.requireNonNull(transfert, "transfert");
        this.convertisseur = Objects.requireNonNull(convertisseur, "convertisseur");
        for (int diametre : DIAMETRES) {
            ondesSatellite.add(onde(diametre));
            ondesBalise.add(onde(diametre));
        }
        mettreAJour();
    }

    void afficherDans(NiSpace espace) {
        ondesSatellite.forEach(onde -> espace.add(onde, 0));
        ondesBalise.forEach(onde -> espace.add(onde, 0));
    }

    void retirerDe(NiSpace espace) {
        ondesSatellite.forEach(espace::remove);
        ondesBalise.forEach(espace::remove);
    }

    public void mettreAJour() {
        centrer(ondesSatellite, transfert.getSatellite().getPosition());
        centrer(ondesBalise, transfert.getBalise().getPosition());
    }

    private void centrer(List<NiEllipse> ondes, Position position) {
        Point centre = convertisseur.versEcran(position);
        ondes.forEach(onde -> onde.setCenter(centre));
    }

    private static NiEllipse onde(int diametre) {
        NiEllipse onde = new NiEllipse();
        onde.setDimension(new Dimension(diametre, diametre));
        onde.setBackground(TRANSPARENT);
        onde.setBorderColor(ORANGE);
        onde.setStrokeWidth(1.5f);
        return onde;
    }
}
