package projestJest;

import java.util.List;
import java.util.Scanner;

public class JoueurHumain extends Joueur {

    private static Scanner scanner = new Scanner(System.in);

    public JoueurHumain(String nom) {
        super(nom);
    }

    @Override
    public void faireOffre(Carte c1, Carte c2) {
        System.out.println("\n" + nom + ", voici vos cartes : ");
        System.out.println("1 : " + c1);
        System.out.println("2 : " + c2);
        System.out.print("Choisissez celle à mettre VISIBLE : ");

        int choix = scanner.nextInt();

        if (choix == 1)
            offre = new Offre(c1, c2);
        else
            offre = new Offre(c2, c1);

        System.out.println(nom + " a fait son offre : " + offre);
    }

    @Override
    public Carte choisirCarte(Offre offre) {
        System.out.println("\n" + nom + ", offre sélectionnée : " + offre);
        System.out.println("1 : Prendre la carte visible");
        System.out.println("2 : Prendre la carte cachée");

        int choix = scanner.nextInt();
        return (choix == 1 ? offre.prendreVisible() : offre.prendreCachee());
    }

    @Override
    public Joueur choisirJoueurCible(List<Joueur> joueursValides) {
        System.out.println("\n" + nom + ", choisissez un joueur chez qui prendre une carte :");

        for (int i = 0; i < joueursValides.size(); i++) {
            System.out.println((i + 1) + " : " + joueursValides.get(i).getNom());
        }

        int choix = scanner.nextInt() - 1;

        return joueursValides.get(choix);
    }
}