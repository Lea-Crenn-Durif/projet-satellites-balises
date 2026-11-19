/**
 * L'analyse du texte (étape 7c) : le lexer et le parser générés par ANTLR, puis la construction
 * de l'arbre abstrait.
 *
 * <h2>Modifier la grammaire</h2>
 * <ol>
 *   <li>Modifier {@code SatellitesBalises.g4} (dans ce dossier).</li>
 *   <li>Avec le greffon IntelliJ « ANTLR v4 » : clic droit sur la grammaire → <i>Configure ANTLR…</i>
 *       → dossier de sortie : le dossier {@code src} du module ; paquetage :
 *       {@code satellitesbalises.langage.analyse.genere} ; cocher <i>generate parse tree visitor</i>,
 *       décocher <i>generate parse tree listener</i>. Puis clic droit → <i>Generate ANTLR Recognizer</i>.</li>
 *   <li>Mettre à jour {@link satellitesbalises.langage.analyse.ConstructeurArbre} si une règle a changé,
 *       puis relancer les tests de l'étape 7.</li>
 * </ol>
 * <p>La version d'ANTLR qui génère doit être celle de la bibliothèque {@code antlr4-runtime} (4.13.2).</p>
 */
package satellitesbalises.langage.analyse;
