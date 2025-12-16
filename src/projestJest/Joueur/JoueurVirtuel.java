package projestJest.Joueur;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Strategie.*;
import java.util.List;

/**
 * Représente un joueur virtuel (IA).
 * Ses décisions sont déléguées à une stratégie (Pattern Strategy).
 */
public class JoueurVirtuel extends Joueur {

    /**
     * Constructeur pour un joueur virtuel.
     * @param nom Nom du bot.
     * @param strategie La stratégie utilisée par ce bot.
     */
    public JoueurVirtuel(String nom, Strategie strategie) {
        super(nom);
        this.strategie = strategie;
    }

    
    /**
     * Fait une offre en piochant deux cartes.
     * @param c1 Première carte.
     * @param c2 Deuxième carte.
     * @param vue Interface utilisateur.
     */
    public void faireOffre(Carte c1, Carte c2, InterfaceUtilisateur vue) {
        this.offre = new Offre(c1, c2);
        vue.afficherMessage(nom + " (IA) a fait son offre.");
    }

    /**
     * Choisit une carte dans l'offre d'un joueur.
     * @param offre L'offre cible.
     * @param vue Interface utilisateur.
     * @return La carte choisie.
     */
    public Carte choisirCarte(Offre offre, InterfaceUtilisateur vue) {
        boolean prendreVisible = strategie.choisirVisibleOuCachee(offre);
        Carte retiree = offre.prendre(prendreVisible);

        vue.afficherMessage(nom + " (IA) a pris : " + retiree);
        vue.afficherMessage(nom + " (IA) possède maintenant dans son Jest : " + this.jest);
        return retiree;
    }

    /**
     * Choisit un joueur cible pour voler une carte.
     * @param joueursValides Liste des joueurs cibles possibles.
     * @param vue Interface utilisateur.
     * @return Le joueur choisi.
     */
    public Joueur choisirJoueurCible(List<Joueur> joueursValides, InterfaceUtilisateur vue) {
        return strategie.choisirOffre(joueursValides);
    }
}
