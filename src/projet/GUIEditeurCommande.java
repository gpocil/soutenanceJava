package projet;

import projet.Commande;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class GUIEditeurCommande {

    private Commande commande;

    public GUIEditeurCommande(Commande commande) {
        this.commande = commande;
        setupUI();
    }

    private void setupUI() {
        JFrame frame = new JFrame("Modification de la Commande");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("Produit");
        columnNames.add("Quantité");

        Vector<Vector<Object>> data = new Vector<>();
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
            updateCommande(commande, table);
            rafraichir(table);
            frame.dispose();
        });

        frame.add(saveButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateCommande(Commande commande, JTable table) {
        for (int row = 0; row < table.getRowCount(); row++) {
            String produitRef = table.getValueAt(row, 0).toString(); // 0 est l'index de colonne pour la référence du produit
            int updatedQuantity = Integer.parseInt(table.getValueAt(row, 1).toString()); // 1 est l'index de colonne pour la quantité
            commande.setQuantiteProduit(produitRef, updatedQuantity);
        }
    }
    private void rafraichir(JTable table) {
        // Supprimer toutes les lignes existantes
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // Ajouter à nouveau toutes les commandes depuis votre commande actuelle
        HashMap<String, Integer> produits = commande.getReferences();
        for (String key : produits.keySet()) {
            Vector<Object> row = new Vector<>();
            row.add(key);
            row.add(produits.get(key));
            model.addRow(row);
        }
    }




}
