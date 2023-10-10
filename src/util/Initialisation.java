package util;

import projet.Commande;
import projet.Produit;

import java.io.IOException;
import java.util.List;

public interface Initialisation {

    void initialisation(String fileName);

    static void ecriture(List<Commande> c, List<Produit> p) throws IOException {

    }

}
