package Trains;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
//import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Voyageuser extends javax.swing.JFrame {

    static Voyageuser obj;

    public static Voyageuser setObj() {
        if (obj == null) {
            obj = new Voyageuser();
        }
        return obj;
    }

    private void populateGare1() {
        try (Connection connection = ConnectioDB.getConnection()) {
            String query = "SELECT GareName FROM Gare";
            try (PreparedStatement st = connection.prepareStatement(query); ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    String trainMark = rs.getString("GareName");
                    Gare1.addItem(trainMark);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Voyageuser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populateGare2() {
        try (Connection connection = ConnectioDB.getConnection()) {
            String query = "SELECT GareName FROM Gare";
            try (PreparedStatement st = connection.prepareStatement(query); ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    String trainMark = rs.getString("GareName");
                    Gare2.addItem(trainMark);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Voyageuser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Voyageuser() {
        initComponents();
        populateGare1();
        populateGare2();
//        AutoCompleteDecorator.decorate(Gare1);
  //      AutoCompleteDecorator.decorate(Gare2);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Etes-vous sûr de vouloir fermer l'application ?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirmed == JOptionPane.YES_OPTION) {
                    // If user confirms, close the application
                    dispose(); // This line is optional depending on your application's needs
                    System.exit(0);
                } else {
                    // If user clicks "No" or closes the dialog, do nothing
                }
            }
        });
        Gare1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedItem = (String) Gare1.getSelectedItem();
                    if (selectedItem != null) {
                        // Disable selected item in Gare2
                        disableSelectedItem(Gare2, selectedItem);
                        // Check if the same gare is selected in Gare1 and Gare2
                        checkSameGareSelected(selectedItem, (String) Gare2.getSelectedItem());
                    }
                }
            }
        });
        datee.setDate(LocalDate.now());
        Gare2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedItem = (String) Gare2.getSelectedItem();
                    if (selectedItem != null) {
                        disableSelectedItem(Gare1, selectedItem);
                        checkSameGareSelected((String) Gare1.getSelectedItem(), selectedItem);
                    }
                }
            }
        });
    }

    private void checkSameGareSelected(String gare1, String gare2) {
        if (gare1 != null && gare2 != null && gare1.equals(gare2)) {
            JOptionPane.showMessageDialog(this, "Please select different Gares for Gare1 and Gare2.");
        }
    }

    private void disableSelectedItem(JComboBox comboBox, String selectedItem) {
        ComboBoxRenderer renderer = new ComboBoxRenderer(selectedItem);
        comboBox.setRenderer(renderer);
    }

    class ComboBoxRenderer extends DefaultListCellRenderer {

        private String disabledItem;

        public ComboBoxRenderer(String disabledItem) {
            this.disabledItem = disabledItem;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value != null && value.equals(disabledItem)) {
                c.setEnabled(false);
            }
            return c;
        }
    }

    public String getTrainNumberByMarque(String trainMarque) throws SQLException {
        // Extract the train number from the combined string
        String trainNumber = trainMarque.split("\\s+")[0]; // Split the string by whitespace and get the first part
        try (Connection connection = ConnectioDB.getConnection()) {
            if (connection == null) {
                System.err.println("Error: Database connection is null.");
                System.err.println("1.");
                return null;
            }
            String query = "SELECT TrainNumber FROM Trains WHERE TrainMarque = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, trainNumber.trim()); // Trim the train number to ensure whitespace is not an issue
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("TrainNumber");
                    } else {
                        System.out.println("Train number not found for marque: " + trainMarque);
                        return null; // Train number not found
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching TrainNumber for marque: " + trainMarque);
            e.printStackTrace();
            return null; // Or you can rethrow the exception
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Gare2 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Gare1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        datee = new com.github.lgooddatepicker.components.DatePicker();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("gare de départ");
        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 146, -1));

        jLabel2.setText("gare d'arrivée");
        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, -1, -1));

        Gare2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Gare2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(Gare2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 290, 160, -1));

        jButton1.setText("Rechercher");
        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 541, 150, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Trains/Logo-oncf.png"))); // NOI18N
        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(302, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(461, 0, -1, 640));

        Gare1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jPanel1.add(Gare1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 220, 156, -1));

        jLabel5.setText("Voyage");
        jLabel5.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 150, -1));

        jLabel6.setText("Date de Depart");
        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, -1, -1));
        jPanel1.add(datee, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 380, -1, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 102));
        jButton2.setText("Retour");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 540, 150, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
String selectedGare1 = (String) Gare1.getSelectedItem();
    String selectedGare2 = (String) Gare2.getSelectedItem();
    TrainBeans trainBeans = new TrainBeans();
    GareDB d = GareDB.getCon();

    List<GaresBeans> listDepart = d.getGarebyName(selectedGare1);
    List<GaresBeans> listArrive = d.getGarebyName(selectedGare2);

    if (!listDepart.isEmpty() && !listArrive.isEmpty()) {
        if (listDepart.get(0).getSequenceN() < listArrive.get(0).getSequenceN()) {
            d.sadek(listDepart, listArrive, Ticket.setObj());
            Ticket ticket = Ticket.setObj();

            // Set the values in the Ticket object
            ticket.l1.setText(selectedGare1);
            ticket.l2.setText(selectedGare2);
            // ticket.Train.setText(String.valueOf(trainNumber));
            // ticket.Marque.setText(String.valueOf(trainMarque));
            ticket.garedpart.setText(selectedGare1);
            ticket.garearrive.setText(selectedGare2);

            ticket.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Aucun voyage valide.");
        }
    } else if (!listArrive.isEmpty() && !listDepart.isEmpty() && listArrive.get(0).getSequenceN() > listDepart.get(0).getSequenceN()) {
        d.sadek1(listArrive, listDepart, Ticket.setObj());
        Ticket ticket = Ticket.setObj();

        int selectedRowIndex = ticket.jTable1.getSelectedRow();
        if (selectedRowIndex != -1) { // Check if a row is selected
            ticket.l1.setText(selectedGare1);
            ticket.l2.setText(selectedGare2);
            ticket.garedpart.setText(selectedGare1);
            ticket.garearrive.setText(selectedGare2);

            ticket.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Aucun voyage valide.");
        }
    } else {
        JOptionPane.showMessageDialog(null, "Aucun voyage valide");
    }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    
   // Hide the current frame
dispose();






    }//GEN-LAST:event_jButton2ActionPerformed
    public Integer getStationIdForGare1() {
        String selectedGare1 = (String) Gare1.getSelectedItem();
        return getStationIdByName(selectedGare1);
    }

    private Integer getStationIdByName(String selectedGare) {
        Integer stationId = null;
        try (Connection connection = ConnectioDB.getConnection()) {
            String query = "SELECT GareID FROM Stations WHERE GareName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, selectedGare);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    stationId = resultSet.getInt("GareID");
                } else {
                    System.err.println("Gare not found: " + selectedGare);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return stationId;
    }


    public Integer getStationIdForGare2() {
        String selectedGare2 = (String) Gare2.getSelectedItem();
        // Use the selectedGare2 to retrieve station ID
        return getStationIdByName(selectedGare2);
    }

    public String getSelectedGare1() {
        return (String) Gare1.getSelectedItem();
    }

    public String getSelectedGare2() {
        return (String) Gare2.getSelectedItem();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Voyageuser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox<String> Gare1;
    public javax.swing.JComboBox<String> Gare2;
    public com.github.lgooddatepicker.components.DatePicker datee;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
