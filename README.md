Présentation

Ce projet a été réalisé dans le cadre de l’UE LO02 – Conception orientée objet à l’Université de Technologie de Troyes (UTT).
Il consiste à implémenter une version informatique du jeu de cartes JEST, incluant :

un moteur de jeu complet utilisable en ligne de commande,

une architecture orientée objet modulaire et extensible,

des joueurs humains et des joueurs virtuels,

l'utilisation des patrons de conception Strategy, Visitor et (en phase 3) MVC,

la possibilité future d’intégrer des variantes et des extensions.

Ce dépôt contient l’ensemble des classes Java nécessaires au fonctionnement du jeu ainsi que la documentation associée.

Fonctionnalités principales
Moteur de jeu conforme aux règles officielles

Distribution initiale et gestion des trophées.

Constitution des offres (cartes visible et cachée).

Gestion complète de la phase de prise de cartes.

Application de la règle spéciale du dernier joueur.

Redistribution correcte aux tours suivants.

Calcul complet du score du Jest (Aces, Joker, Coeurs, Paires noires, etc.).

Détermination automatique du vainqueur.

Gestion des joueurs

Joueurs humains avec interactions en console.

Joueurs virtuels utilisant des stratégies :

StrategieAleatoire

StrategieOffensive

StrategieDefensive

Architecture objet robuste

Hiérarchie de cartes : CarteSuite, CarteJoker, CarteTrophee.

Gestion de la pioche.

Classe Offre (visible/cachée).

Classe Jest (main du joueur).

Orchestration complète via la classe Partie.

Calcul des scores via Visitor

Interface VisiteurScore.

Implémentation CompteurScore regroupant toutes les règles de valeur du Jest.

Extensibilité

Ajout simple de nouvelles stratégies IA.

Possibilité d'ajouter des cartes d’extension.

Intégration future de variantes.

Préparation pour une future interface graphique MVC (phase 3 du projet).

Architecture du projet

Organisation simplifiée des packages :

projestJest/
 ├── cartes/            (CarteSuite, CarteJoker, CarteTrophee, ValeurCarte, SuiteCarte)
 ├── joueurs/           (Joueur, JoueurHumain, JoueurVirtuel)
 ├── strategie/         (Strategie, StrategieAleatoire, StrategieOffensive, StrategieDefensive)
 ├── jeu/               (Partie, Pioche, Offre, Jest)
 ├── score/             (VisiteurScore, CompteurScore)
 └── Main/              (Main.java)


Patrons de conception utilisés :

Strategy pour la prise de décision des IA.

Visitor pour le calcul du score du Jest.

MVC prévu pour l’IHM lors de la phase 3.

Exécution (Mode Console)
Compilation

Dans un IDE (Eclipse, IntelliJ, VSCode) : importer le projet puis compiler.
Ou en ligne de commande :

javac -d bin src/**/*.java

Lancement du jeu
java -cp bin projestJest.Main

Paramétrage interactif

À l’exécution, il est demandé :

le nombre de joueurs humains,

le nombre de joueurs virtuels,

les noms des joueurs,

les choix de constitution des offres,

les décisions de prise (visible/cachée),

le choix de l’adversaire chez qui prendre une carte.

Le moteur permet de jouer une partie complète du jeu Jest en console.

Exemple d’exécution
=== JEST - MODE CONSOLE ===
Combien de joueurs humains ? 2
Combien de joueurs virtuels ? 1
Nom du joueur 1 : Jules
Nom du joueur 2 : Paul

Trophées : [Joker, AS de PIQUE]

----------------------------
      TOUR 1
----------------------------
Jules, voici vos cartes :
1 : TROIS de COEUR
2 : QUATRE de CARREAU
Choisissez celle à mettre VISIBLE :
...

Travail restant et extensions possibles

Implémentation des variantes du jeu (choix au début de partie).

Interface graphique conforme au modèle MVC (phase 3 du projet).

Amélioration des stratégies IA.

Gestion de la sauvegarde/chargement d’une partie.

Documentation Javadoc complète et UML final.

Auteurs

Projet réalisé dans le cadre de l’UE LO02 – Automne 2025 (A25).
Binôme :

Jules (Étudiant UTT)

Victor (Étudiant UTT)

Licence

Projet développé à des fins pédagogiques. Toute réutilisation doit mentionner la source.
