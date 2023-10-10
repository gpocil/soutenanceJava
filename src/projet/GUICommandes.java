package projet;

import ihm.Formulaire;
import ihm.FormulaireInt;

import java.io.IOException;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUICommandes implements FormulaireInt {

    private GUISite formPP;
    private Site site;
    private JList<Commande> listCommandes;

    /**
     * Constructeur pour l'interface de gestion des commandes.
     *
     * @param formPP Référence à l'interface principale de l'application.
     * @param site   Référence au site initialisé qui contient tous les produits et commandes.
     */
    public GUICommandes(GUISite formPP, Site site) {
        if(formPP == null || site == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être null");
        }
        this.formPP = formPP;
        this.site = site;

        JFrame frame = new JFrame("Gestion des commandes");//fenêtre
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        listCommandes = new JList<>(getCommandesArray());
        JScrollPane scrollCommandes = new JScrollPane(listCommandes); //barre défilement

        JPanel southPanel = new JPanel(new FlowLayout());//conteneur
        JButton btnDeclarerLivree = new JButton("Déclarer comme livrée");
        JButton btnDelete = new JButton("Supprimer commande");
        JButton btnModifier = new JButton("Modifier commande");

        btnDeclarerLivree.addActionListener(e -> {
            try {
                declarerCommandeLivree();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnDelete.addActionListener(e -> {
            try {
                deleteCommande();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnModifier.addActionListener(e -> ouvrirEditeurCommande(site));


        southPanel.add(btnDeclarerLivree);
        southPanel.add(btnDelete);
        southPanel.add(btnModifier);

        frame.add(scrollCommandes, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Transforme l'ArrayList de commandes en array pour affichage par Swing
     * @return
     */
    private Commande[] getCommandesArray() {
        ArrayList<Commande> commandes = site.getCommandesNonLivreesGestion();
        return commandes.toArray(new Commande[0]);//0 = taille de l'élément
    }

    /**
     * Declare une commande livrée, enlève les produits livrés du stock et enregistre les modifs.
     * Appel à checkProduits qui retourne un booléen si la commande est livrable en fonction du stock
     *
     * @throws IOException
     */
    private void declarerCommandeLivree() throws IOException {
        Commande selectedCommande = listCommandes.getSelectedValue();
        if (selectedCommande != null) {
            boolean b = site.checkProduits(selectedCommande).getKey();
            List<Produit> produitsNonDispos = site.checkProduits(selectedCommande).getValue();
            if (b) {
                site.declarerLivree(selectedCommande);
                site.enleverStock(selectedCommande);
                Site.ecriture(site.getCommandes(), site.getStock());
            JOptionPane.showMessageDialog(null, "Commande livree.");
            rafraichir();}
            else {
                StringBuilder message = new StringBuilder("Pas assez de stock pour les produits suivants:\n");
                for (Produit p : produitsNonDispos) {
                    message.append(p.getNom()).append(", quantité restante : ").append(p.getQuantite()).append("\n");
                }
                JOptionPane.showMessageDialog(null, message.toString());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une commande.");
        }
    }

    /**
     * Ouvre le constructeur de modification de commandes
     * Prend le site initialisé en arg pour permettre l'écriture du fichier après modif
     * @param site
     */
    private void ouvrirEditeurCommande(Site site) {
        if (site == null) {
            throw new IllegalArgumentException("Le site ne peut pas être null");
        }
    Commande selectedCommande = listCommandes.getSelectedValue();
    if (selectedCommande != null) {
        new GUIEditeurCommande(selectedCommande, site);
    } else {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une commande.");
    }
}


    /**
     * Supprime une commande et enregistre les modifs dans le fichier
     * @throws IOException
     */
    private void deleteCommande() throws IOException {
        Commande selectedCommande = listCommandes.getSelectedValue();
        if (selectedCommande != null) {
            listCommandes.setListData(getCommandesArray());
            site.supprimerCommande(selectedCommande);
            Site.ecriture(site.getCommandes(), site.getStock());
            JOptionPane.showMessageDialog(null, "Commande supprimée.");
            rafraichir();

        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une commande.");
        }
    }

    /**
     * Rafraichit l'affichage en redéfinissant la liste après modification
     */
    private void rafraichir() {
        listCommandes.setListData(getCommandesArray());
    }

    @Override
    public void submit(Formulaire form, String nom) {
    }
}
