package projet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import util.*;

import javax.swing.*;

public class Site implements Initialisation {
    private ArrayList<Produit> stock;
    private ArrayList<Commande> commandes;

    /**
     * Le site se construit par initialisation du stock et liste de commandes par appel
     * à Initialisation() qui lit les fichiers txt contenant la data
     */
    public Site() {
        stock = new ArrayList<Produit>();
        initialisation("src/data/Produits.txt");
        commandes = new ArrayList<Commande>();
        initialisation("src/data/Commandes.txt");


    }

    /**
     * Return un string concaténé des différents produits spéparés pas un saut de ligne
     * @return
     */
    public String listerTousProduits() {
        String res = "";
        for (Produit prod : stock)
            res = res + prod.toString() + "\n";

        return res;
    }

    /**
     * Return un string concaténé des différentes commandes spéparés pas un saut de ligne
     *
     * @return
     */
    public String listerToutesCommandes() {
        String res = "";
        for (Commande c : commandes)
            res = res + c.toString() + "\n";
        return res;
    }

    /**
     * Renvoie une un string de toutes les commandes non livrées
     *
     * @return
     */
    public String getCommandesNonLivrees() {
        String res = "";
        for (Commande c : commandes) {
            if (!c.isLivre()) {
                res = res + c.toString() + "\n";
            }
        }
        return res;
    }

    /**
     * Renvoie le toString d'une commande si correspondance avec int en paramètre
     *
     * @param numero Le numéro de la commande
     * @return toString de la commande si trouvée; sinon  "Commande non trouvée".
     */
    public String listerCommande(int numero) {
        String res = "Commande non trouvée";
        for (Commande c : commandes) {
            if (numero == c.getNumero()) {
                res = c.toString();
                break;
            }
        }
        return res;
    }

    /**
     * Initialise les données à partir d'un fichier. Selon le nom du fichier,
     * il initialise soit le stock de produits, soit la liste des commandes.
     *
     * @param nomFichier Le nom du fichier à lire (soit "Produits.txt" pour les produits, soit "Commandes.txt" pour les commandes).
     */
    public void initialisation(String nomFichier) {
        String[] lignes = Terminal.lireFichierTexte(nomFichier);
        for (String ligne : lignes) {
            String[] champs = ligne.split("[;]");
            if (nomFichier.equals("src/data/Produits.txt")) {
                String reference = champs[0];
                String nom = champs[1];
                double prix = Double.parseDouble(champs[2]);
                int quantite = Integer.parseInt(champs[3]);
                Produit p = new Produit(reference, nom, prix, quantite);
                stock.add(p);
            } else if (nomFichier.equals("src/data/Commandes.txt")) {
                String numero = champs[0];
                String date = champs[1];
                String client = champs[2];
                HashMap<String, Integer> references = new HashMap<>();
                String[] produits = champs[3].split("\\|");//regex \\| = caractères d'échappement
                for (String produit : produits) {
                    String[] refQuantite = produit.split("=");//Split au signe =
                    String refProduit = refQuantite[0];
                    int quantite = Integer.parseInt(refQuantite[1]);
                    references.put(refProduit, quantite);
                }
                boolean livraison = Boolean.parseBoolean(champs[4]);
                if (!livraison) {//Distinction commande livrée ou non
                    String raisonDelai = champs[5];
                    Commande c = new Commande(Integer.parseInt(numero), date, client, references, livraison, raisonDelai);
                    commandes.add(c);
                } else {
                    Commande c = new Commande(Integer.parseInt(numero), date, client, references, livraison, "");
                    commandes.add(c);
                }

            }
        }
    }
    /**
     * Sauvegarde les informations des produits et des commandes dans des fichiers séparés

     * @param commandes La liste des commandes à sauvegarder.
     * @param produits  La liste des produits à sauvegarder.
     * @throws IOException Si une erreur se produit lors de l'écriture dans les fichiers.
     */

    public static void ecriture(List<Commande> commandes, List<Produit> produits) throws IOException {
        // Écriture des produits dans Produits.txt
        List<String> produitLines = new ArrayList<>();
        for (Produit produit : produits) {
            String line = produit.getReference() + ";" +
                    produit.getNom() + ";" +
                    produit.getPrix() + ";" +
                    produit.getQuantite();
            produitLines.add(line);
        }
        Files.write(Paths.get("src/data/Produits.txt"), produitLines);

        // Écriture des commandes dans Commandes.txt
        List<String> commandeLines = new ArrayList<>();
        for (Commande commande : commandes) {
            StringBuilder sb = new StringBuilder();
            sb.append(commande.getNumero()).append(";");
            sb.append(commande.getDate()).append(";");
            sb.append(commande.getClient()).append(";");

            HashMap<String, Integer> references = commande.getReferences();
            StringJoiner produitsJoiner = new StringJoiner("|");
            for (Map.Entry<String, Integer> entry : references.entrySet()) {
                produitsJoiner.add(entry.getKey() + "=" + entry.getValue());
            }
            sb.append(produitsJoiner.toString()).append(";");
            sb.append(commande.isLivre()).append(";");

            // Ajout de la raison de retard si la commande n'est pas livrée
            if (!commande.isLivre()) {
                sb.append(commande.getRaisonDelai());
            }

            commandeLines.add(sb.toString());
        }
        Files.write(Paths.get("src/data/Commandes.txt"), commandeLines);
    }



    public ArrayList<Produit> getStock() {
        return stock;
    }

    public void setStock(ArrayList<Produit> stock) {
        this.stock = stock;
    }

    public void setCommandes(ArrayList<Commande> commandes) {
        this.commandes = commandes;
    }

    public ArrayList<Commande> getCommandes() {
        return commandes;
    }

    public ArrayList<Commande> getCommandesNonLivreesGestion() {
        ArrayList<Commande> liste = new ArrayList<>();
        for (Commande c : commandes) {
            if (!c.isLivre()) {
                liste.add(c);
            }
        }
        return liste;
    }

    /**
     * Suppression de la commande c de la liste des commandes
     * @param c
     */
    public void supprimerCommande(Commande c) {
        this.commandes.remove(c);
    }

    /**
     * Déclaration de la commande c comme livrée
     * @param c
     */
    public void declarerLivree(Commande c) {
        c.setLivre(true);
        c.setRaisonDelai("");
    }

    /**
     * return le produit dans le stock en fonction de la référence
     * @param reference
     * @return
     */
    public Produit getProduitStock(String reference) {
        ArrayList<Produit> stock = this.getStock();
        for (Produit p : stock) {
            if (p.getReference().equals(reference)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Vérifie la disponibilité des produits d'une commande dans le stock
     *
     * Parcourt tous les produits de la commande et vérifie leur disponibilité.
     * Si un produit n'est pas disponible ou si sa
     * quantité en stock est inférieure à la quantité requise, ce produit est ajouté
     * à la liste des produits non disponibles == > Return de cette liste pour affichage
     *
     * return Map.Entry contenant un booléen et une liste de produits
     * boolean est true si tous les produits sont dispo, faux sinon.

     * @param c
     * @return boolean livrable ou non + liste de produits non livrables SN
     */

    public Map.Entry<Boolean, List<Produit>> checkProduits(Commande c) { //Map.entry = key/value
        HashMap<String, Integer> produits = c.getReferences();
        List<Produit> produitsNonDispos = new ArrayList<>();

        for (Map.Entry<String, Integer> tuple : produits.entrySet()) { //entrySet = récup key/value dans la map
            String produitRef = tuple.getKey();
            Integer quantiteRequise = tuple.getValue();
            Produit produitEnStock = getProduitStock(produitRef);//Return produit en focntion de sa ref

            if (produitEnStock.getQuantite() == 0 || produitEnStock.getQuantite() < quantiteRequise) {
                produitsNonDispos.add(produitEnStock);
            }
        }

        Boolean dispo = produitsNonDispos.isEmpty();
        return new AbstractMap.SimpleEntry<>(dispo, produitsNonDispos);
    }

    /**
     * Fonction qui gère la décrémentation de la quantité des produits dans le stocj à la livraison d'une commmande
     * @param c commande à livrer
     * @return le stocj mis à jour
     */
    public ArrayList<Produit> enleverStock(Commande c) {
        HashMap<String, Integer> produitsCommandes = c.getReferences();
        ArrayList<Produit> stock = this.getStock();
        ArrayList<Produit> newStock = new ArrayList<>();

        for (Map.Entry<String, Integer> tuple : produitsCommandes.entrySet()) {
            String produitRef = tuple.getKey();
            Integer quantiteRequise = tuple.getValue();

            for (Produit p : stock) {
                if (p.getReference().equals(produitRef)) {
                    int nouvelleQuantite = p.getQuantite() - quantiteRequise;
                    p.setQuantite(nouvelleQuantite);
                    newStock.add(p);
                    break;
                }
            }
        }
        return newStock;
    }

    /**
     *
     * @return uniquement les commandes livrées
     */
    private ArrayList<Commande> getCommandesLivrees() {
        ArrayList<Commande> liste = new ArrayList<>();
        for (Commande c : commandes) {
            if (c.isLivre()) {
                liste.add(c);
            }
        }
        return liste;
    }

    /**
     * Prend le prix de chaque commande livrée en multpipliant le prix du produit par sa quantité et en les additionnant au total des autres produits
     * Appelle getPrixProduits qui compare si la réfé&rence correspond bien à un produit et en ressort le prix
     * @return le prix total
     */
    public double getTotalCommandesLivrees() {
        ArrayList<Commande> liste = this.getCommandesLivrees();
        double total = 0;
        for (Commande c : liste) {
            HashMap<String, Integer> produits = c.getReferences();
            for (Map.Entry<String, Integer> entry : produits.entrySet()) {
                String refProduit = entry.getKey();
                int quantite = entry.getValue();
                double prixProduit = getPrixProduit(refProduit, quantite);
                total += prixProduit * quantite;
            }
        }
        return total;
    }

    /**
     * Compare si la réfé&rence correspond bien à un produit et en ressort le prix
     * @param refProduit
     * @param quantite
     * @return
     */
    public double getPrixProduit(String refProduit, int quantite) {
        for (Produit produit : stock) {
            if (produit.getReference().equals(refProduit)) {
                return produit.getPrix() * quantite;
            }
        }
        throw new IllegalArgumentException("Produit avec la référence " + refProduit + " non trouvé.");
    }
}





