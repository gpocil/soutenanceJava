package projet;

import projet.Commande;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel; //affichage tableau

public class GUIEditeurCommande {

    private Commande commande;
    private Site site;

    public GUIEditeurCommande(Commande commande, Site site) {
        if(commande == null || site == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être null");
        }

        this.commande = commande;
        this.site = site;
        setupUI();
    }
    /**
     * Initialise et configure l'UI pour la modification d'une commande
     */
    private void setupUI() {
        JFrame frame = new JFrame("Modification de la Commande");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Produit");
        columnNames.add("Quantité");

        Vector<Vector<Object>> data = new Vector<>();//Vector = ArrayList synchronisé (safe pour multithread), compatible avec DefaultTableModel
        HashMap<String, Integer> produits = commande.getReferences();
        for (String key : produits.keySet()) {
            Vector<Object> row = new Vector<>();
            row.add(key);
            row.add(produits.get(key));
            data.add(row);
        }

        JTable table = new JTable(data, columnNames);

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton saveButton = new JButton("Enregistrer les modifications");
        saveButton.addActionListener(e -> {
            try {
                updateCommande(commande, table);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            rafraichir(table);
            frame.dispose();
        });

        frame.add(saveButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Met à jour les quantités des produits dans une commande par appel à setQuantiteProduit et enregistre les modifs dans les fichiers data
     *
     * @param commande La commande à mettre à jour.
     * @param table    La JTable contenant les données modifiées de la commande.
     * @throws IOException Si une erreur se produit lors de l'écriture dans le fichier.
     */
    private void updateCommande(Commande commande, JTable table) throws IOException {
        for (int row = 0; row < table.getRowCount(); row++) {
            String produitRef = table.getValueAt(row, 0).toString();
            try {
                int updatedQuantity = Integer.parseInt(table.getValueAt(row, 1).toString());
                commande.setQuantiteProduit(produitRef, updatedQuantity);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "La quantité pour le produit " + produitRef + " n'est pas un nombre valide.");
                return;
            }
            Site.ecriture(site.getCommandes(), site.getStock());
        }
    }

    /**
     * Mise à jour de la table après modifications
     * @param table
     */
    private void rafraichir(JTable table) {
        // Supprime les lignes existantes
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Ajoute les nouvelles
        HashMap<String, Integer> produits = commande.getReferences();
        for (String key : produits.keySet()) {
            Vector<Object> row = new Vector<>();
            row.add(key);
            row.add(produits.get(key));
            model.addRow(row);
        }
    }




}
