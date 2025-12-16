package projestJest;

import projestJest.Carte.*;
import projestJest.Joueur.*;
import projestJest.Variante.*;
import java.util.*;
import java.io.*;

/**
 * Classe centrale du jeu JEST. 
 * Gère le déroulement de la partie, les joueurs, la pioche, les tours et le calcul des scores.
 * Implémente Serializable pour la sauvegarde.
 */
public class Partie implements Serializable {

    private List<Joueur> joueurs;
    private Pioche pioche;
    private List<Carte> trophees;
    private Variante variante;
    private int tour = 1;
    private transient InterfaceUtilisateur vue; 

    /**
     * Constructeur de la Partie.
     * Initialise les listes et la vue par défaut.
     */
    public Partie() {
        joueurs = new ArrayList<>();
        pioche = new Pioche();
        trophees = new ArrayList<>();
        variante = new VarianteClassique(); 
        vue = new VueConsole(); 
    }
    
    /**
     * Initialise ou restaure la vue après le chargement d'une partie.
     */
    public void initVue() {
        if (vue == null) {
            vue = new VueConsole();
        }
        if (vue instanceof VueConsole) {
            ((VueConsole) vue).initScanner();
        }
    }

    /**
     * Ajoute un joueur à la partie.
     * @param j Le joueur à ajouter.
     */
    public void ajouterJoueur(Joueur j) {
        joueurs.add(j);
    }

    /**
     * Définit la variante utilisée pour la partie.
     * @param v La variante à utiliser.
     */
    public void setVariante(Variante v) {
        this.variante = v;
    }
    
    /**
     * Active les cartes d'extension dans la pioche.
     */
    public void activerExtensions() {
        pioche.ajouterExtensions();
    }

    /**
     * Lance la boucle principale du jeu.
     * Gère l'initialisation, les tours de jeu et la fin de partie.
     */
    public void demarrer() {

        vue.afficherMessage("=== Nouvelle partie de JEST ===");
        vue.afficherMessage("Variante utilisée : " + variante.getNom());

        int nbJoueurs = joueurs.size();
        int nbTrophees = (nbJoueurs == 3) ? 2 : 1; 
        
        trophees = pioche.piocherTrophees(nbTrophees);
        vue.afficherTrophees(trophees);

        while (pioche.taille() >= joueurs.size()) {
            vue.afficherTour(tour);
            jouerUnTour();
            tour++;
            
            if (!pioche.estVide()) {
                proposerSauvegarde();
            }
        }

        donnerDernieresCartes();
        attribuerTrophees();
        afficherScores();
        afficherVainqueur();
    }
    
    /**
     * Demande à l'utilisateur s'il souhaite sauvegarder la partie courante.
     */
    private void proposerSauvegarde() {
        int choix = vue.demanderChoixInt("\nVoulez-vous sauvegarder et fermer ou continuer ? 1: Sauvegarder et fermer, 0: Continuer sans sauvegarder", 0, 1);
        if (choix == 1) {
            sauvegarderPartie();
            vue.afficherMessage("Partie sauvegardée. Au revoir !");
            System.exit(0);
        }
    }
    
    /**
     * Sauvegarde l'état actuel de la partie dans un fichier.
     */
    public void sauvegarderPartie() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sauvegarde.ser"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            vue.afficherMessage("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    /**
     * Charge une partie depuis un fichier de sauvegarde.
     * @return L'objet Partie chargé, ou null en cas d'erreur.
     */
    public static Partie chargerPartie() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("sauvegarde.ser"))) {
            Partie p = (Partie) ois.readObject();
            p.initVue();
            return p;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur chargement : " + e.getMessage());
            return null;
        }
    }

    /**
     * Distribue les cartes aux joueurs selon les règles du tour actuel.
     * @return Une Map associant chaque joueur à ses deux cartes distribuées.
     */
    private Map<Joueur, Carte[]> distribuerCartes() {

        Map<Joueur, Carte[]> cartesDistribuees = new HashMap<>();

        if (tour == 1) {
            for (Joueur j : joueurs) {
                cartesDistribuees.put(j,
                        new Carte[]{pioche.piocher(), pioche.piocher()});
            }
        } else {
            List<Carte> tas = new ArrayList<>();

            for (Joueur j : joueurs) {
                Offre o = j.getOffre();
                if (o.getFaceVisible() != null) tas.add(o.getFaceVisible());
                if (o.getFaceCachee() != null) tas.add(o.getFaceCachee());
            }

            for (int i = 0; i < joueurs.size(); i++) {
                if (!pioche.estVide()) tas.add(pioche.piocher());
            }

            Collections.shuffle(tas);

            Iterator<Carte> it = tas.iterator();
            for (Joueur j : joueurs) {
                Carte c1 = it.hasNext() ? it.next() : null;
                Carte c2 = it.hasNext() ? it.next() : null;
                	if (c1 == null || c2 == null) {
                	    break; 
                	}
                cartesDistribuees.put(j, new Carte[]{c1, c2});
            }
        }

        return cartesDistribuees;
    }

    /**
     * Exécute un tour complet de jeu (Offres, Prises).
     */
    private void jouerUnTour() {

        Map<Joueur, Carte[]> cartesDistribuees = distribuerCartes();

        for (Joueur j : joueurs) {
            Carte[] cs = cartesDistribuees.get(j);
            if (cs[0] == null || cs[1] == null)
                throw new RuntimeException("Erreur distribution : carte null.");
            j.faireOffre(cs[0], cs[1], vue);
        }

        vue.afficherOffres(joueurs);

        List<Joueur> ordre = determinerOrdrePrise();
        Set<Joueur> dejaJoue = new HashSet<>();

        while (dejaJoue.size() < joueurs.size()) {

            Joueur actif = trouverProchainJoueur(ordre, dejaJoue);

            List<Joueur> valides = new ArrayList<>();
            for (Joueur j : joueurs) {
                if (j != actif && j.getOffre().estComplete()) {
                    valides.add(j);
                }
            }

            Offre cibleOffre;

            if (valides.isEmpty()) {
                vue.afficherMessage(actif.getNom() + " doit prendre dans sa propre offre.");
                cibleOffre = actif.getOffre();
            } else {
                Joueur cible = actif.choisirJoueurCible(valides, vue);
                cibleOffre = cible.getOffre();
            }

            Carte prise = actif.choisirCarte(cibleOffre, vue);
            actif.ajouterAuJest(prise);

            dejaJoue.add(actif);
        }

        vue.afficherFinTour(tour);
    }

    /**
     * Détermine l'ordre dans lequel les joueurs choisissent une carte.
     * Basé sur la valeur de la carte visible de leur offre.
     * @return Liste ordonnée des joueurs.
     */
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
        
        StringBuilder sb = new StringBuilder("Ordre de prise : ");
        for(Joueur j : ordre) sb.append(j.getNom()).append(" ");
        vue.afficherMessage("\n" + sb.toString());
        
        return ordre;
    }

    /**
     * Trouve le prochain joueur dans l'ordre qui n'a pas encore joué.
     */
    private Joueur trouverProchainJoueur(List<Joueur> ordre, Set<Joueur> dejaJoue) {
        for (Joueur j : ordre)
            if (!dejaJoue.contains(j))
                return j;
        return null;
    }

    /**
     * Distribue les cartes restantes dans les offres à la fin de la partie.
     */
    private void donnerDernieresCartes() {
        vue.afficherMessage("\nDistribution des dernières cartes...");

        for (Joueur j : joueurs) {
            Offre o = j.getOffre();
            if (o.getFaceVisible() != null) j.ajouterAuJest(o.prendreVisible());
            if (o.getFaceCachee() != null) j.ajouterAuJest(o.prendreCachee());
        }
    }

    /**
     * Attribue les trophées aux joueurs selon les règles.
     */
    private void attribuerTrophees() {
        if (trophees.isEmpty()) return;
        
        vue.afficherMessage("\nAttribution des trophées...");
        Iterator<Carte> it = trophees.iterator();
        
        while (it.hasNext()) {
            Carte troph = it.next();
            
            if (troph.getSuite() != null) {
                SuiteCarte couleurTroph = troph.getSuite();
                Joueur gagnant = null;
                int maxVal = -1;
                
                for (Joueur j : joueurs) {
                    for (Carte c : j.getJest().getCartes()) { 
                         if (c.getSuite() == couleurTroph) {
                             if (c.getValeur().getFaceValue() > maxVal) {
                                 maxVal = c.getValeur().getFaceValue();
                                 gagnant = j;
                             }
                         }
                    }
                }
                
                if (gagnant != null) {
                    
                    Joueur joueurCoeurBrisant = null;
                    for (Joueur j : joueurs) {
                        for (Carte c : j.getJest().getCartes()) {
                            if (c.getValeur() == ValeurCarte.SIX) {
                                joueurCoeurBrisant = j;
                                break;
                            }
                        }
                    }
                    
                    if (joueurCoeurBrisant != null && joueurCoeurBrisant != gagnant) {
                        vue.afficherMessage("MAIS " + joueurCoeurBrisant.getNom() + " possède le Coeur Brisant et vole le trophée !");
                        gagnant = joueurCoeurBrisant;
                    }
                    
                    vue.afficherMessage("Le trophée " + troph + " revient à " + gagnant.getNom());
                    gagnant.ajouterAuJest(troph);
                    it.remove(); 
                } else {
                     vue.afficherMessage("Personne ne remporte le trophée " + troph);
                }
            } else {
                 vue.afficherMessage("Trophée spécial non attribué : " + troph);
            }
        }
    }

    /**
     * Calcule et affiche les scores finaux.
     */
    private void afficherScores() {
        for (Joueur j : joueurs) {
            j.getJest().calculerScore(variante); 
        }
    }

    /**
     * Détermine et affiche le vainqueur de la partie.
     */
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
        
        vue.afficherResultats(joueurs, gagnant, max);
    }
}
