package projestJest;

import projestJest.Carte.*;
import projestJest.Joueur.*;
import projestJest.Variante.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;

/**
 * La classe Partie représente le cœur du modèle du jeu JEST.
 * Elle gère l'état global du jeu, incluant les joueurs, la pioche, les trophées, le déroulement des tours
 * et l'application des règles via les variantes.
 * 
 * Cette classe implémente Serializable pour permettre la sauvegarde et le chargement de l'état du jeu.
 * Elle utilise également PropertyChangeSupport pour notifier les observateurs (comme le Contrôleur) des changements d'état.
 */
public class Partie implements Serializable {

    /**
     * Nom de la propriété notifiant un message textuel à afficher.
     */
    public static final String PROP_MESSAGE = "message";

    /**
     * Nom de la propriété notifiant la mise à jour de la liste des trophées.
     */
    public static final String PROP_TROPHEES = "trophees";

    /**
     * Nom de la propriété notifiant le changement de tour.
     */
    public static final String PROP_TOUR = "tour";

    /**
     * Nom de la propriété notifiant la mise à jour des offres des joueurs.
     */
    public static final String PROP_OFFRES = "offres";

    /**
     * Nom de la propriété notifiant la fin d'un tour.
     */
    public static final String PROP_FIN_TOUR = "fin_tour";

    /**
     * Nom de la propriété notifiant les résultats finaux de la partie.
     */
    public static final String PROP_RESULTATS = "resultats";

    /**
     * Liste des joueurs participant à la partie.
     */
    private List<Joueur> joueurs;

    /**
     * La pioche contenant les cartes non distribuées.
     */
    private Pioche pioche;

    /**
     * Liste des cartes trophées mises en jeu commun.
     */
    private List<Carte> trophees;

    /**
     * La variante de règles appliquée pour cette partie.
     */
    private Variante variante;

    /**
     * Compteur du nombre de tours joués.
     */
    private int tour = 1;

    /**
     * Référence vers la vue utilisée pour les interactions synchrones (choix utilisateur).
     * Marqué transient car la vue n'est pas sérialisée avec le modèle.
     */
    private transient InterfaceUtilisateur vue; 
    
    /**
     * Support pour la gestion des écouteurs de changements de propriétés.
     */
    private PropertyChangeSupport diffuseur;

    /**
     * Retourne la liste des joueurs.
     * @return La liste modifiable des joueurs.
     */
    public List<Joueur> getJoueurs() { return joueurs; }

    /**
     * Retourne la liste des trophées.
     * @return La liste modifiable des trophées en jeu.
     */
    public List<Carte> getTrophees() { return trophees; }

    /**
     * Retourne le numéro du tour.
     * @return Le numéro du tour actuel.
     */
    private int getTour() { return tour; } 

    /**
     * Constructeur par défaut de la Partie.
     * Initialise les collections, la pioche, la variante classique et une vue console par défaut.
     */
    public Partie() {
        joueurs = new ArrayList<>();
        pioche = new Pioche();
        trophees = new ArrayList<>();
        variante = new VarianteClassique(); 
        vue = new VueConsole();
        diffuseur = new PropertyChangeSupport(this);
    }
    
    /**
     * Ajoute un écouteur de changement de propriété.
     * 
     * @param pcl L'écouteur à ajouter.
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        diffuseur.addPropertyChangeListener(pcl);
    }

    /**
     * Retire un écouteur de changement de propriété.
     * 
     * @param pcl L'écouteur à retirer.
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        diffuseur.removePropertyChangeListener(pcl);
    }

    /**
     * Définit la vue à utiliser pour les interactions utilisateur.
     * 
     * @param vue L'instance de l'interface utilisateur.
     */
    public void setVue(InterfaceUtilisateur vue) {
        this.vue = vue;
    }

    /**
     * Initialise ou restaure les composants non sérialisés après un chargement.
     * Recrée la vue par défaut et le support de propriétés si nécessaire.
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
     * Ajoute un nouveau joueur à la partie.
     * 
     * @param j Le joueur à ajouter.
     */
    public void ajouterJoueur(Joueur j) {
        joueurs.add(j);
    }

    /**
     * Définit la variante de règles à utiliser.
     * 
     * @param v La nouvelle variante.
     */
    public void setVariante(Variante v) {
        this.variante = v;
    }
    
    /**
     * Active l'ajout des cartes d'extension dans la pioche.
     */
    public void activerExtensions() {
        pioche.ajouterExtensions();
    }

    /**
     * Lance la boucle principale du jeu.
     * Cette méthode orchestre l'ensemble de la partie : initialisation, gestion des tours tant que la pioche le permet,
     * distribution finale, attribution des trophées et annonce du vainqueur.
     */
    public void demarrer() {

        diffuseur.firePropertyChange(PROP_MESSAGE, null, "=== Nouvelle partie de JEST ===");
        diffuseur.firePropertyChange(PROP_MESSAGE, null, "Variante utilisée : " + variante.getNom());

        int nbJoueurs = joueurs.size();
        int nbTrophees = (nbJoueurs == 3) ? 2 : 1; 
        
        trophees = pioche.piocherTrophees(nbTrophees);
        
        diffuseur.firePropertyChange(PROP_TROPHEES, null, trophees);

        while (pioche.taille() >= joueurs.size()) {
            diffuseur.firePropertyChange(PROP_TOUR, 0, tour); 
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
     * Propose à l'utilisateur de sauvegarder la partie en cours.
     * Si l'utilisateur accepte, la partie est sauvegardée et l'application se ferme.
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
     * Sauvegarde l'état actuel de l'objet Partie dans un fichier via sérialisation.
     */
    public void sauvegarderPartie() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("sauvegarde.ser"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            diffuseur.firePropertyChange(PROP_MESSAGE, null, "Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }
    
    /**
     * Charge une partie précédemment sauvegardée depuis le fichier de sauvegarde.
     * 
     * @return L'instance de Partie restaurée, ou null si le chargement échoue.
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
     * Distribue deux cartes à chaque joueur depuis la pioche.
     * Au premier tour, les cartes viennent directement de la pioche.
     * Aux tours suivants, les cartes restantes des offres précédentes sont mélangées à la pioche avant reditribution.
     * 
     * @return Une map associant chaque joueur à un tableau de deux cartes distribuées.
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
     * Exécute la logique d'un tour de jeu complet.
     * Inclut la distribution, la phase d'offre par chaque joueur, et la phase de prise de cartes.
     */
    private void jouerUnTour() {

        Map<Joueur, Carte[]> cartesDistribuees = distribuerCartes();

        for (Joueur j : joueurs) {
            Carte[] cs = cartesDistribuees.get(j);
            if (cs[0] == null || cs[1] == null)
                throw new RuntimeException("Erreur distribution : carte null.");
            j.faireOffre(cs[0], cs[1], vue);
        }

        diffuseur.firePropertyChange(PROP_OFFRES, null, joueurs); 

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
    }

    /**
     * Calcule l'ordre de prise des joueurs pour le tour en cours.
     * L'ordre est déterminé par la valeur faciale de la carte visible de l'offre (plus haute valeur commence),
     * puis par la force de la couleur en cas d'égalité.
     * 
     * @return La liste des joueurs triée selon l'ordre de prise.
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
     * Identifie le prochain joueur devant jouer dans la liste ordonnée, en ignorant ceux ayant déjà joué.
     * 
     * @param ordre La liste ordonnée des joueurs pour ce tour.
     * @param dejaJoue L'ensemble des joueurs ayant déjà effectué leur prise.
     * @return Le prochain joueur actif, ou null si tous ont joué.
     */
    private Joueur trouverProchainJoueur(List<Joueur> ordre, Set<Joueur> dejaJoue) {
        for (Joueur j : ordre)
            if (!dejaJoue.contains(j))
                return j;
        return null;
    }

    /**
     * Distribue automatiquement les cartes restées dans les offres à la fin de la partie vers les Jests respectifs des joueurs.
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
     * Analyse les Jests finaux pour attribuer les trophées aux joueurs remplissant les conditions.
     * Gère les conditions spéciales comme le vol de trophée par le "Coeur Brisant" (Joker).
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
     * Déclenche le calcul des scores finaux pour tous les joueurs en fonction de la variante active.
     */
    private void afficherScores() {
        for (Joueur j : joueurs) {
            j.getJest().calculerScore(variante); 
        }
    }

    /**
     * Détermine le vainqueur de la partie en comparant les scores finaux.
     * Notifie les résultats pour affichage.
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
        
        diffuseur.firePropertyChange(PROP_RESULTATS, null, joueurs);
    }
}
