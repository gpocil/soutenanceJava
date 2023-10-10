package projet;

import ihm.*;

public class GUISite implements FormulaireInt
{
    private Site site;

    /**
     * Constructeur de la fenêtre principale, prend Site initialisé par les fichiers en arg
     * @param site
     */
    public GUISite(Site site)
    {
        this.site = site;

        Formulaire form = new Formulaire("Site de vente",this,1100,730);

        form.addLabel("Afficher tous les produits du stock");
        form.addButton("AFF_STOCK","Tous le stock");
        form.addLabel("");
        form.addLabel("Afficher tous les bons de commande");
        form.addButton("AFF_COMMANDES","Toutes les commandes");
        form.addLabel("");
        form.addLabel("Gestion de commandes");
        form.addButton("AFF_GESTION_C","Gestion de commandes");
        form.addLabel("");
        form.addLabel("Afficher la liste des commandes en attente");
        form.addButton("AFF_COMMANDES_NON_LIVREES","Commandes en attente");
        form.addLabel("");
        form.addLabel("Calculer prix commandes livrées");
        form.addButton("PRIX_COMM_LIVREES","Prix commandes livrées");
        form.addLabel("");
        form.addText("NUM_COMMANDE","Numero de commande",true,"1");
        form.addButton("AFF_COMMANDE","Afficher");
        form.addLabel("");

        form.setPosition(400,0);
        form.addZoneText("RESULTATS","Resultats",
                            true,
                            "",
                            600,700);

        form.afficher();
    }

    /**
     * Envoi formulaire en fonction du bouton et appel des focntions correspondantes
     *
     * @param form      Le formulaire contenant les informations
     * @param nomSubmit Le nom du bouton
     */

    public void submit(Formulaire form,String nomSubmit)
    {
        if (nomSubmit.equals("AFF_STOCK"))
            {
                String res = site.listerTousProduits();
                form.setValeurChamp("RESULTATS",res);
            }

        if (nomSubmit.equals("AFF_COMMANDES"))
            {
                String res = site.listerToutesCommandes();
                form.setValeurChamp("RESULTATS",res);
            }

        if (nomSubmit.equals("AFF_COMMANDE"))
            {
                String numStr = form.getValeurChamp("NUM_COMMANDE");
                int num = Integer.parseInt(numStr);
                String res = site.listerCommande(num);
                form.setValeurChamp("RESULTATS",res);
            }
        if (nomSubmit.equals("AFF_COMMANDES_NON_LIVREES"))
        {
            String res = site.getCommandesNonLivrees();
            form.setValeurChamp("RESULTATS",res);
        }
        if (nomSubmit.equals("AFF_GESTION_C"))
        {
            GUICommandes g = new GUICommandes(this, site);
        }
        if (nomSubmit.equals("PRIX_COMM_LIVREES"))
        {
            double prix = site.getTotalCommandesLivrees();
            form.setValeurChamp("RESULTATS", String.valueOf(prix) + " euros.");
        }
    }

}