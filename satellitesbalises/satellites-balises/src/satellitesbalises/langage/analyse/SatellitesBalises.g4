// Grammaire du langage de commandes « Satellites et balises » (étape 7c, décision D10).
//
// Une instruction par ligne. Les lignes vides et les commentaires (-- jusqu'à la fin de la ligne)
// sont ignorés. Les noms de déplacements, d'instruments et de conditions ne sont PAS des mots-clés :
// ce sont des NOM, résolus ensuite par les catalogues du modèle. Ajouter un instrument ne change
// donc pas cette grammaire (EX-7.9).
//
// Le code Java est généré dans le paquetage satellitesbalises.langage.analyse.genere
// (voir le mode d'emploi dans langage/analyse/package-info.java).
grammar SatellitesBalises;

// ---------- Règles de l'analyseur ----------

// Chaque ligne se termine par FIN_LIGNE : l'analyseur ajoute une fin de ligne au texte s'il n'en
// a pas. Ainsi le parser sait toujours, en lisant un seul symbole, ce qu'il doit faire, et une
// erreur est signalée à l'endroit exact où l'instruction cesse d'être correcte.
programme
    : ligne* EOF
    ;

ligne
    : instruction? FIN_LIGNE
    ;

instruction
    : typeEntite NOM 'en' '(' nombre ',' nombre ')' blocProprietes?   # creation
    | 'supprimer' NOM                                                  # suppression
    | 'configurer' NOM blocProprietes                                  # configuration
    | 'deplacement' NOM appel                                          # choixDeplacement
    | 'remontee' NOM appel                                             # choixRemontee
    | 'equiper' NOM appel (',' appel)*                                 # equipement
    | 'desequiper' NOM NOM                                             # desequipement
    | ordre NOM                                                        # action
    | 'apparence' NOM blocProprietes                                   # apparence
    | 'attendre' ENTIER                                                # attente
    ;

typeEntite      : 'satellite' | 'balise' ;
ordre           : 'arreter' | 'demarrer' | 'remonter' ;
blocProprietes  : '{' (propriete (';' propriete)*)? '}' ;
propriete       : NOM '=' valeur ;
appel           : NOM '(' (propriete (',' propriete)*)? ')' ;
valeur          : nombre | TEXTE ;
nombre          : '-'? (ENTIER | DECIMAL) ;

// ---------- Règles du lexer ----------

NOM         : [a-zA-Z_] [a-zA-Z_0-9]* ;
DECIMAL     : [0-9]+ '.' [0-9]+ ;
ENTIER      : [0-9]+ ;
TEXTE       : '"' ~["\r\n]* '"' ;
COMMENTAIRE : '--' ~[\r\n]* -> skip ;
FIN_LIGNE   : '\r'? '\n' ;
ESPACES     : [ \t]+ -> skip ;
