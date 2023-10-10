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
        this.numero = numero;
        this.date = date;
        this.client = client;
        this.references = references;
        this.livraison = livraison;
        this.raisonDelai = raisonDelai;
    }

    public boolean checkEtatCommande(){
        return this.livraison;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public HashMap<String, Integer>  getReferences() {
        return references;
    }

    public void setReferences(HashMap<String, Integer> references) {
        this.references = references;
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
        if (this.references.containsKey(produitRef)) {
            this.references.put(produitRef, quantite);
        } else {
            System.out.println("Le produit avec la référence " + produitRef + " n'existe pas dans cette commande.");
        }
    }




}