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

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String toString()
    {
        return String.format("%-15s %-50s %3.2f   %3d",reference,nom,prix,quantite);
    }

}