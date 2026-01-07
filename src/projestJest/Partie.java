package projestJest;

import projestJest.Carte.*;
import projestJest.Joueur.*;
import projestJest.Variante.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;

/**
 * Classe centrale du jeu JEST. 
 * Gère le déroulement de la partie, les joueurs, la pioche, les tours et le calcul des scores.
 * Implémente Serializable pour la sauvegarde.
 */
public class Partie implements Serializable {

    // Propriétés pour PropertyChangeEvent
    public static final String PROP_MESSAGE = "message";
    public static final String PROP_TROPHEES = "trophees";
    public static final String PROP_TOUR = "tour";
    public static final String PROP_OFFRES = "offres";
    public static final String PROP_FIN_TOUR = "fin_tour";
    public static final String PROP_RESULTATS = "resultats";

    private List<Joueur> joueurs;
    private Pioche pioche;
    private List<Carte> trophees;
    private Variante variante;
    private int tour = 1;
    private transient InterfaceUtilisateur vue; // Pour les inputs synchrones uniquement
    
    private PropertyChangeSupport diffuseur;

    public List<Joueur> getJoueurs() { return joueurs; }
    public List<Carte> getTrophees() { return trophees; }
    private int getTour() { return tour; } 

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
        diffuseur = new PropertyChangeSupport(this);
    }
    
    // --- Gestion des PropertyChangeListener ---
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        diffuseur.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        diffuseur.removePropertyChangeListener(pcl);
    }

    /**
     * Initialise ou restaure la vue après le chargement d'une partie.
     */
    public void setVue(InterfaceUtilisateur vue) {
        this.vue = vue;
    }

    /**
     * Initialise ou restaure la vue après le chargement d'une partie.
     */
    public void initVue() {
        if (vue == null) {
            vue = new VueConsole();
        }
        if (diffuseur == null) {
            diffuseur = new PropertyChangeSupport(this);
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

        diffuseur.firePropertyChange(PROP_MESSAGE, null, "=== Nouvelle partie de JEST ===");
        diffuseur.firePropertyChange(PROP_MESSAGE, null, "Variante utilisée : " + variante.getNom());

        int nbJoueurs = joueurs.size();
        int nbTrophees = (nbJoueurs == 3) ? 2 : 1; 
        
        trophees = pioche.piocherTrophees(nbTrophees);
        // Notification Trophées
        diffuseur.firePropertyChange(PROP_TROPHEES, null, trophees);

        while (pioche.taille() >= joueurs.size()) {
            diffuseur.firePropertyChange(PROP_TOUR, 0, tour); // old value not relevant really
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
            diffuseur.firePropertyChange(PROP_MESSAGE, null, "Partie sauvegardée. Au revoir !");
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
            diffuseur.firePropertyChange(PROP_MESSAGE, null, "Erreur lors de la sauvegarde : " + e.getMessage());
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

        diffuseur.firePropertyChange(PROP_OFFRES, null, joueurs); // Notify Views

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
                diffuseur.firePropertyChange(PROP_MESSAGE, null, actif.getNom() + " doit prendre dans sa propre offre.");
                cibleOffre = actif.getOffre();
            } else {
                Joueur cible = actif.choisirJoueurCible(valides, vue);
                cibleOffre = cible.getOffre();
            }

            Carte prise = actif.choisirCarte(cibleOffre, vue);
            actif.ajouterAuJest(prise);

            dejaJoue.add(actif);
        }

        diffuseur.firePropertyChange(PROP_FIN_TOUR, 0, tour);
        // setChanged(); notifyObservers(); // REMOVED
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

            int v1 = (c1.getValeur() != null) ? c1.getValeur().getFaceValue() : 0;
            int v2 = (c2.getValeur() != null) ? c2.getValeur().getFaceValue() : 0;

            if (v1 != v2) return Integer.compare(v2, v1);

            int s1 = (c1.getSuite() != null) ? c1.getSuite().getForce() : 0;
            int s2 = (c2.getSuite() != null) ? c2.getSuite().getForce() : 0;

            return Integer.compare(s2, s1);
        });
        
        StringBuilder sb = new StringBuilder("Ordre de prise : ");
        for(Joueur j : ordre) sb.append(j.getNom()).append(" ");
        diffuseur.firePropertyChange(PROP_MESSAGE, null, "\n" + sb.toString());
        
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
        diffuseur.firePropertyChange(PROP_MESSAGE, null, "\nDistribution des dernières cartes...");

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
        
        diffuseur.firePropertyChange(PROP_MESSAGE, null, "\nAttribution des trophées...");
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
                        diffuseur.firePropertyChange(PROP_MESSAGE, null, "MAIS " + joueurCoeurBrisant.getNom() + " possède le Coeur Brisant et vole le trophée !");
                        gagnant = joueurCoeurBrisant;
                    }
                    
                    diffuseur.firePropertyChange(PROP_MESSAGE, null, "Le trophée " + troph + " revient à " + gagnant.getNom());
                    gagnant.ajouterAuJest(troph);
                    it.remove(); 
                } else {
                     diffuseur.firePropertyChange(PROP_MESSAGE, null, "Personne ne remporte le trophée " + troph);
                }
            } else {
                 diffuseur.firePropertyChange(PROP_MESSAGE, null, "Trophée spécial non attribué : " + troph);
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
            j.setScore(score); 
            if (score > max) {
                max = score;
                gagnant = j;
            }
        }
        
        // Notification Resultats Finaux
        // On passe une map ou une liste pour simplifier. La vue affichera le podium.
        diffuseur.firePropertyChange(PROP_RESULTATS, null, joueurs);
    }
}
