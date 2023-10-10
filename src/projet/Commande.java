package projet;

import java.util.*;


public class Commande
{
    private int     numero;
    private String  date;
    private String  client;
    private HashMap<String, Integer> references; //Hashmap Référence du produit + Quantité du produit ==> Facilité pour écriture/lecture dans fichiers txt
    private boolean livraison;
    private String raisonDelai;


    public Commande(int numero, String date, String client, HashMap<String, Integer> references, boolean livraison, String raisonDelai) {
        if (date == null || client == null || references == null || raisonDelai == null) {
            throw new IllegalArgumentException("Aucun des paramètres ne peut être null");
        }

        this.numero = numero;
        this.date = date;
        this.client = client;
        this.references = references;
        this.livraison = livraison;
        this.raisonDelai = raisonDelai;
    }



    public int getNumero() {
        return numero;
    }



    public String getDate() {
        return date;
    }

    public String getClient() {
        return client;
    }

    public HashMap<String, Integer>  getReferences() {
        return references;
    }

    public boolean isLivre() {
        return livraison;
    }

    public void setLivre(boolean livre) {
        this.livraison = livre;
    }

    public String getRaisonDelai() {
        return raisonDelai;
    }

    public void setRaisonDelai(String raisonDelai) {
        this.raisonDelai = raisonDelai;
    }

    @Override
    public String toString() {
        String tostring = numero + " | " + date + " | " + client + " | " + references;

        if (livraison) {
            tostring += " | Commande livrée";
        } else {
            tostring += " | Commande non livrée ; " + raisonDelai;
        }

        return tostring;
    }

    /**
     * Met à jour la quantité d'un produit dans la commande en fonction de sa référence.
     * Si le produit avec la référence donnée n'existe pas dans la commande, une notification est affichée.
     *
     * @param produitRef La référence du produit dont la quantité doit être mise à jour.
     * @param quantite La nouvelle quantité du produit.
     */
    public void setQuantiteProduit(String produitRef, int quantite) {
        if (produitRef == null) {
            throw new IllegalArgumentException("La référence du produit ne peut pas être null");
        }
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
        if (this.references.containsKey(produitRef)) {
            this.references.put(produitRef, quantite);
        } else {
            System.out.println("Le produit avec la référence " + produitRef + " n'existe pas dans cette commande.");
        }
    }




}