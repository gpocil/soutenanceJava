package projet;

import ihm.Formulaire;
import ihm.FormulaireInt;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUICommandes implements FormulaireInt {

    private GUISite formPP;
    private Site site;
    private JList<Commande> listCommandes;

    public GUICommandes(GUISite formPP, Site site) {
        this.formPP = formPP;
        this.site = site;

        JFrame frame = new JFrame("Gestion des commandes");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        listCommandes = new JList<>(getCommandesArray());
        JScrollPane scrollCommandes = new JScrollPane(listCommandes);

        JPanel southPanel = new JPanel(new FlowLayout());
        JButton btnDeclarerLivree = new JButton("Déclarer comme livrée");
        JButton btnDelete = new JButton("Supprimer commande");
        JButton btnModifier = new JButton("Modifier commande");

        btnDeclarerLivree.addActionListener(e -> declarerCommandeLivree());
        btnDelete.addActionListener(e -> deleteCommande());
        btnModifier.addActionListener(e -> ouvrirEditeurCommande());


        southPanel.add(btnDeclarerLivree);
        southPanel.add(btnDelete);
        southPanel.add(btnModifier);

        frame.add(scrollCommandes, BorderLayout.CENTER);
        frame.add(southPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private Commande[] getCommandesArray() {
        ArrayList<Commande> commandes = site.getCommandesNonLivreesGestion();
        return commandes.toArray(new Commande[0]);
    }

    private void declarerCommandeLivree() {
        Commande selectedCommande = listCommandes.getSelectedValue();
        if (selectedCommande != null) {
            boolean b = site.checkProduits(selectedCommande).getKey();
            List<Produit> produitsNonDispos = site.checkProduits(selectedCommande).getValue();
            if (b) {
                site.declarerLivree(selectedCommande);
                site.enleverStock(selectedCommande);
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
private void ouvrirEditeurCommande(){
    Commande selectedCommande = listCommandes.getSelectedValue();
    if (selectedCommande != null) {
        new GUIEditeurCommande(selectedCommande);
    } else {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une commande.");
    }
}



    private void deleteCommande() {
        Commande selectedCommande = listCommandes.getSelectedValue();
        if (selectedCommande != null) {
            listCommandes.setListData(getCommandesArray());
            site.supprimerCommande(selectedCommande);
            JOptionPane.showMessageDialog(null, "Commande supprimée.");
            rafraichir();

        } else {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une commande.");
        }
    }

    private void rafraichir() {
        listCommandes.setListData(getCommandesArray());
    }

    @Override
    public void submit(Formulaire form, String nom) {
    }
}
