package projestJest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pioche {



	private List<Carte> cartes;
	
	

	public List<Carte> getCartes() {
		return cartes;
	}



	public void setCartes(List<Carte> cartes) {
		this.cartes = cartes;
	}
	
	public Pioche() {
		cartes = new ArrayList<>();
        creerPioche();         // Remplir la pioche
	}
	
	

	/**
	 * Methode pour melanger la pioche
	 */
    public void melanger() {
    	Collections.shuffle(cartes);
    }
    
    /**
     *  Vérifier si la pioche est vide
     */
    public boolean estVide() {
        return cartes.isEmpty();
    }
    
 // Tirer deux cartes de la pioche
    public List<Carte> piocherDeuxCartes() {
        List<Carte> tirage = new ArrayList<>();
        for (int i = 0; i < 2 && !cartes.isEmpty(); i++) {
            tirage.add(cartes.remove(0));
        }
        return tirage;
    }

    public void creerPioche() {
    		cartes.add( new Carte(ValeurCarte.AS, SuiteCarte.PIQUE) );
    		cartes.add( new Carte(ValeurCarte.DEUX, SuiteCarte.PIQUE) );
    		cartes.add( new Carte(ValeurCarte.TROIS, SuiteCarte.PIQUE) );
    		cartes.add( new Carte(ValeurCarte.QUATRE, SuiteCarte.PIQUE) );
    		cartes.add( new Carte(ValeurCarte.AS, SuiteCarte.CARREAU) );
    		cartes.add( new Carte(ValeurCarte.DEUX, SuiteCarte.CARREAU) );
    		cartes.add( new Carte(ValeurCarte.TROIS, SuiteCarte.CARREAU) );
    		cartes.add( new Carte(ValeurCarte.QUATRE, SuiteCarte.CARREAU) );
    		cartes.add( new Carte(ValeurCarte.AS, SuiteCarte.COEUR) );
    		cartes.add( new Carte(ValeurCarte.DEUX, SuiteCarte.COEUR) );
    		cartes.add( new Carte(ValeurCarte.TROIS, SuiteCarte.COEUR) );
    		cartes.add( new Carte(ValeurCarte.QUATRE, SuiteCarte.COEUR) );
    		cartes.add( new Carte(ValeurCarte.AS, SuiteCarte.TREFLE) );
    		cartes.add( new Carte(ValeurCarte.DEUX, SuiteCarte.TREFLE) );
    		cartes.add( new Carte(ValeurCarte.TROIS, SuiteCarte.TREFLE) );
    		cartes.add( new Carte(ValeurCarte.QUATRE, SuiteCarte.TREFLE) );
        cartes.add( new Carte(ValeurCarte.JOKER) ); 
    }
    /*
    public CarteTrophee piocherTrophees(final int nbreTrophee) {
        // TODO Auto-generated return
        return null;
    }
    */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pioche pioche = new Pioche();
		System.out.println("La pioche avant d'etre melangée contient:");
		System.out.println(pioche.getCartes());
		
		pioche.melanger();
		System.out.println("La pioche après avoir été melangée est la suivante:");
		System.out.println(pioche.getCartes());
		

	}

}
