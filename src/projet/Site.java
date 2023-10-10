package projet;

import java.util.*;

import util.*;

import javax.swing.*;

public class Site implements Initialisation
{
    private ArrayList<Produit> stock;
    private ArrayList<Commande> commandes;

    public Site()
    {
        stock = new ArrayList<Produit>();
        initialisation("src/data/Produits.txt");
        commandes = new ArrayList<Commande>();
        initialisation("src/data/Commandes.txt");


    }
    public String listerTousProduits()
    {
        String res="";
        for(Produit prod : stock)
            res=res+prod.toString()+"\n";

        return res;
    }
    public String listerToutesCommandes()
    {
        String res="";
        for(Commande c : commandes)
            res=res+c.toString()+"\n";
        return res;
    }
    /**
     * Renvoie une un string de toutes les commandes non livrées
     *
     * @return Chaîne de caractères contenant les commandes non livrées
     */
    public String getCommandesNonLivrees(){
        String res = "";
        for(Commande c : commandes) {
            if (!c.isLivre()){
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
    public String listerCommande(int numero)
    {
        String res="Commande non trouvée";
        for (Commande c : commandes){
            if (numero == c.getNumero()){
                res=c.toString();
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
        for(String ligne :lignes) {
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
                if(!livraison){//Distinction commande livrée ou non
                    String raisonDelai = champs[5];
                    Commande c = new Commande(Integer.parseInt(numero), date, client, references, livraison, raisonDelai);
                    commandes.add(c);
                }
                else {
                    Commande c = new Commande(Integer.parseInt(numero), date, client, references, livraison, "");
                    commandes.add(c);
                }

            }
        }
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
        for (Commande c : commandes){
            if (!c.isLivre()){
                liste.add(c);
            }
        }
        return liste;
    }

    public void supprimerCommande(Commande c) {
        this.commandes.remove(c);
    }
    public void declarerLivree(Commande c) {
        c.setLivre(true);
        c.setRaisonDelai("");
    }
    public Produit getProduitStock(String reference) {
        ArrayList<Produit> stock = this.getStock();
        for (Produit p : stock) {
            if (p.getReference().equals(reference)) {
                return p;
            }
        }
        return null;
    }

    public Map.Entry<Boolean, List<Produit>> checkProduits(Commande c) {
        HashMap<String, Integer> produits = c.getReferences();
        List<Produit> produitsNonDispos = new ArrayList<>();

        for (Map.Entry<String, Integer> tuple : produits.entrySet()) {
            String produitRef = tuple.getKey();
            Integer quantiteRequise = tuple.getValue();
            Produit produitEnStock = getProduitStock(produitRef);

            if (produitEnStock.getQuantite() == 0 || produitEnStock.getQuantite() < quantiteRequise) {
                produitsNonDispos.add(produitEnStock);
            }
        }

        Boolean dispo = produitsNonDispos.isEmpty();
        return new AbstractMap.SimpleEntry<>(dispo, produitsNonDispos);
    }
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
    private ArrayList<Commande> getCommandesLivrees(){
        ArrayList<Commande> liste = new ArrayList<>();
        for (Commande c : commandes){
            if (c.isLivre()){
                liste.add(c);
            }
        }
        return liste;
    }

   /** public double getTotalCommandesLivrees() {
        ArrayList<Commande> liste = this.getCommandesLivrees();
        double total = 0;

        for (Commande c : liste) {
            HashMap<String, Integer> produits = c.getReferences();
            for (Map.Entry<String, Integer> entry : produits.entrySet()) {
                String refProduit = entry.getKey();
                int quantite = entry.getValue();
                double prixProduit = c.getPrixProduit(c.getReferences(), refProduit);
                total += prixProduit * quantite;
            }
        }
        return total;
    }
**/



}


