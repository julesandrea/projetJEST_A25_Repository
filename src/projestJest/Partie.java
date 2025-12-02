package projestJest;

import java.util.*;

public class Partie {

    private List<Joueur> joueurs;
    private Pioche pioche;
    private List<Carte> trophees;
    private Variante variante;
    private int tour = 1;

    public Partie() {
        joueurs = new ArrayList<>();
        pioche = new Pioche();
        trophees = new ArrayList<>();
        variante = new VarianteClassique(); // variante par défaut
    }

    public void ajouterJoueur(Joueur j) {
        joueurs.add(j);
    }

    public void setVariante(Variante v) {
        this.variante = v;
    }

    /* ============================================================
                       DÉMARRAGE DE LA PARTIE
       ============================================================ */
    public void demarrer() {

        System.out.println("=== Nouvelle partie de JEST ===");
        System.out.println("Variante utilisée : " + variante.getNom());

        // Pioche des trophées
        trophees = pioche.piocherTrophees();
        System.out.println("\nTrophées : " + trophees);

        // Boucle des tours
        while (!pioche.estVide()) {
            System.out.println("\n----------------------------");
            System.out.println("      TOUR " + tour);
            System.out.println("----------------------------");

            jouerUnTour();
            tour++;
        }

        donnerDernieresCartes();
        afficherScores();
        afficherVainqueur();
    }

    /* ============================================================
                      DISTRIBUTION DES CARTES
       ============================================================ */

    private Map<Joueur, Carte[]> distribuerCartes() {

        Map<Joueur, Carte[]> cartesDistribuees = new HashMap<>();

        if (tour == 1) {
            // TOUR 1 : distribution directe depuis la pioche
            for (Joueur j : joueurs) {
                cartesDistribuees.put(j,
                        new Carte[]{pioche.piocher(), pioche.piocher()});
            }
        } else {
            // TOURS SUIVANTS : récupérer les cartes restantes
            List<Carte> tas = new ArrayList<>();

            for (Joueur j : joueurs) {
                Offre o = j.getOffre();
                if (o.getFaceVisible() != null) tas.add(o.getFaceVisible());
                if (o.getFaceCachee() != null) tas.add(o.getFaceCachee());
            }

            // Ajouter autant de cartes que de joueurs
            for (int i = 0; i < joueurs.size(); i++) {
                if (!pioche.estVide()) tas.add(pioche.piocher());
            }

            // Mélanger
            Collections.shuffle(tas);

            // Redistribuer
            Iterator<Carte> it = tas.iterator();
            for (Joueur j : joueurs) {
                Carte c1 = it.hasNext() ? it.next() : null;
                Carte c2 = it.hasNext() ? it.next() : null;
                cartesDistribuees.put(j, new Carte[]{c1, c2});
            }
        }

        return cartesDistribuees;
    }

    /* ============================================================
                            UN TOUR COMPLET
       ============================================================ */

    private void jouerUnTour() {

        Map<Joueur, Carte[]> cartesDistribuees = distribuerCartes();

        /* --- Phase OFFRE --- */
        for (Joueur j : joueurs) {
            Carte[] cs = cartesDistribuees.get(j);
            if (cs[0] == null || cs[1] == null)
                throw new RuntimeException("Erreur distribution : carte null.");
            j.faireOffre(cs[0], cs[1]);
        }

        afficherOffres();

        /* --- Phase PRISE --- */
        List<Joueur> ordre = determinerOrdrePrise();
        Set<Joueur> dejaJoue = new HashSet<>();

        while (dejaJoue.size() < joueurs.size()) {

            Joueur actif = trouverProchainJoueur(ordre, dejaJoue);

            // Liste des adversaires valides
            List<Joueur> valides = new ArrayList<>();
            for (Joueur j : joueurs) {
                if (j != actif && j.getOffre().estComplete()) {
                    valides.add(j);
                }
            }

            Offre cibleOffre;

            if (valides.isEmpty()) {
                // Cas spécial : seul sa propre offre reste valide
                System.out.println(actif.getNom() + " doit prendre dans sa propre offre.");
                cibleOffre = actif.getOffre();
            } else {
                // Le joueur CHOISIT un adversaire valide
                Joueur cible = actif.choisirJoueurCible(valides);
                cibleOffre = cible.getOffre();
            }

            // Choix visible/cachée
            Carte prise = actif.choisirCarte(cibleOffre);
            actif.ajouterAuJest(prise);

            dejaJoue.add(actif);
        }

        System.out.println("\nFin du tour " + tour + ".");
    }

    /* ============================================================
                         ORDRE DE PRISE
       ============================================================ */

    private List<Joueur> determinerOrdrePrise() {

        List<Joueur> ordre = new ArrayList<>(joueurs);

        ordre.sort((j1, j2) -> {
            Carte c1 = j1.getOffre().getFaceVisible();
            Carte c2 = j2.getOffre().getFaceVisible();

            int v1 = c1.getValeur().getFaceValue();
            int v2 = c2.getValeur().getFaceValue();

            if (v1 != v2) return Integer.compare(v2, v1);

            return Integer.compare(c2.getSuite().getForce(), c1.getSuite().getForce());
        });

        System.out.println("\nOrdre de prise : " + ordre);
        return ordre;
    }

    private Joueur trouverProchainJoueur(List<Joueur> ordre, Set<Joueur> dejaJoue) {
        for (Joueur j : ordre)
            if (!dejaJoue.contains(j))
                return j;
        return null;
    }

    /* ============================================================
                          AFFICHAGE OFFRES
       ============================================================ */

    private void afficherOffres() {
        System.out.println("\nOffres actuelles :");
        for (Joueur j : joueurs) {
            System.out.println("- " + j.getNom() + " : " + j.getOffre());
        }
    }

    /* ============================================================
                        FIN DE PARTIE : CARTES RESTANTES
       ============================================================ */

    private void donnerDernieresCartes() {
        System.out.println("\nDistribution des dernières cartes...");

        for (Joueur j : joueurs) {
            Offre o = j.getOffre();
            if (o.getFaceVisible() != null) j.ajouterAuJest(o.prendreVisible());
            if (o.getFaceCachee() != null) j.ajouterAuJest(o.prendreCachee());
        }
    }

    /* ============================================================
                             SCORES
       ============================================================ */

    private void afficherScores() {
        System.out.println("\n=== Scores finaux ===");

        for (Joueur j : joueurs) {
            int score = j.getJest().calculerScore(variante); // variante activée
            System.out.println(j.getNom() + " -> " + score + " points | Jest : " + j.getJest());
        }
    }

    /* ============================================================
                           VAINQUEUR
       ============================================================ */

    private void afficherVainqueur() {

        Joueur gagnant = null;
        int max = Integer.MIN_VALUE;

        for (Joueur j : joueurs) {
            int score = j.getJest().calculerScore(variante);
            if (score > max) {
                max = score;
                gagnant = j;
            }
        }

        System.out.println("\nLe vainqueur est : " +
                gagnant.getNom() + " avec " + max + " points.");
    }
}
