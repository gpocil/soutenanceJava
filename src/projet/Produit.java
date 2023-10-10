package projet;

import java.util.*;


public class Produit
{

    private String  reference;
    private String  nom;
    private double  prix;
    private int     quantite;


    public Produit(String reference,
                   String nom,
                   double prix,
                   int quantite)
    {
        this.reference = reference;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
    }

    public String getReference() {
        return reference;
    }
    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String toString() {
        return reference + ", " + nom + ", " + prix + ", " + quantite;
    }

}