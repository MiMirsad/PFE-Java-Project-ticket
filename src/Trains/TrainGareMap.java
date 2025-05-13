/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Trains;


import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 *
 * @author Administrator
 */
public class TrainGareMap extends javax.swing.JFrame {
private final GareDB gareDB;
private DefaultListModel<String> gareListModel;
private JComboBox<String> RoutId1;

    /**
     * Creates new form RouteGareMap
     */
   private void populateTrainMarque() {
        try (Connection connection = ConnectioDB.getConnection()) {
            String query = "SELECT TrainMarque FROM Trains";
            try (PreparedStatement st = connection.prepareStatement(query);
                 ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    String trainMark = rs.getString("TrainMarque");
                    TrainMark.addItem(String.valueOf(trainMark));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TrainGareMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  private void populateRouteIds() {
    String trainMark = (String) TrainMark.getSelectedItem();
    if (trainMark == null) {
        RoutId.removeAllItems(); // Clear RoutId if nothing is selected in TrainMark
        return;
    }

    String query = "";
    switch (trainMark) {
        case "Al boraq":
            query = "SELECT TrainNumber FROM Trains WHERE TrainMarque = 'Al boraq'";
            break;
        case "TNR":
            query = "SELECT TrainNumber FROM Trains WHERE TrainMarque = 'TNR'";
            break;
        case "Atlas":
            query = "SELECT TrainNumber FROM Trains WHERE TrainMarque = 'Atlas'";
            break;
        default:
            JOptionPane.showMessageDialog(this, "Marque de train non prise en charge : " + trainMark, "Error", JOptionPane.ERROR_MESSAGE);
            return;
    }

    try (Connection connection = ConnectioDB.getConnection(); // Replace YourDBConnectionClass with your actual class for DB connection
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        ResultSet resultSet = preparedStatement.executeQuery();

        RoutId.removeAllItems(); // Clear existing items

        while (resultSet.next()) {
            String trainNumber = resultSet.getString("TrainNumber");
            RoutId.addItem(trainNumber);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Échec de la saisie des numéros de train : " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}


private void populateGareNamesAndStations(int routeId) {
    try (Connection connection = ConnectioDB.getConnection()) {
        // Query to fetch TrainMark based on routeId from Trains table
        String trainMarkQuery = "SELECT TrainMarque FROM Trains WHERE TrainNumber = ?";

        try (PreparedStatement trainMarkPs = connection.prepareStatement(trainMarkQuery)) {
            trainMarkPs.setInt(1, routeId);

            try (ResultSet trainMarkRs = trainMarkPs.executeQuery()) {
                String trainMark = "Al boraq"; // Default value if TrainMark is not found

                if (trainMarkRs.next()) {
                    trainMark = trainMarkRs.getString("TrainMarque");
                }

                // Query to fetch Gare names based on TrainMark
                String gareQuery;

                if ("Al boraq".equals(trainMark)) {
                    gareQuery = "SELECT GareName FROM Gare WHERE AlboraqSupp = 1";
                } else if ("TNR".equals(trainMark)) {
                    gareQuery = "SELECT GareName FROM Gare WHERE TNRSupp = 1";
                } else {
                    gareQuery = "SELECT GareName FROM Gare";
                }

                try (PreparedStatement garePs = connection.prepareStatement(gareQuery);
                     ResultSet gareRs = garePs.executeQuery()) {

                    gareListModel.clear();

                    while (gareRs.next()) {
                        String gareName = gareRs.getString("GareName");
                        gareListModel.addElement(gareName);
                    }
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(TrainGareMap.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    public TrainGareMap() {
          DstncKM = new JTextField();
    String distanceStr = DstncKM.getText();

    if (distanceStr == null || distanceStr.trim().isEmpty()) {
        // Handle empty input, e.g., show an error message or use a default value
        distanceStr = "0";
    }

    int distance = Integer.parseInt(distanceStr);

    initComponents();
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
        gareDB = GareDB.getCon();
    gareListModel = new DefaultListModel<>();
    GareNom.setModel(gareListModel);
populateTrainMarque();
TrainMark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                populateRouteIds(); // Populate RoutId based on the selected item in TrainMarque
            }
        });
    DstncKM.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        public void changedUpdate(javax.swing.event.DocumentEvent evt) {
            updateArrivalDate();
        }
        public void removeUpdate(javax.swing.event.DocumentEvent evt) {
            updateArrivalDate();
        }
        public void insertUpdate(javax.swing.event.DocumentEvent evt) {
            updateArrivalDate();
        }
    });
    if (DstncKM == null) {
        System.out.println("DstncKM is null.");
    } else {
        System.out.println("DstncKM is not null.");
    }

    RoutId.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedRouteId = Integer.parseInt(RoutId.getSelectedItem().toString());
//                GareDepartTextField.setText("");
//                GareDariveTextField.setText("");
                populateGareNamesAndStations(selectedRouteId);
            }
        }
    });

}
    private int getTrainSpeed(int routeId) {
    int speed = 0;
    try {
        Connection connection = ConnectioDB.getConnection();
        // Updated query to select the speed of a specific train
        String query = "SELECT TrainSpeed FROM Trains WHERE TrainNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    speed = rs.getInt("TrainSpeed");
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(TrainGareMap.class.getName()).log(Level.SEVERE, null, ex);
    }
    return speed;
}
private void updateArrivalDate() {
    String distanceStr = DstncKM.getText().trim();

    if (distanceStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "La distance ne peut pas être vide", "Erreur d'entrée", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        int distance = Integer.parseInt(distanceStr);
        int routeId = Integer.parseInt(RoutId.getSelectedItem().toString());
        int speed = getTrainSpeed(routeId);

        if (speed <= 0) {
            JOptionPane.showMessageDialog(this, "La vitesse du train est invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDateTime dateOfDepart = getDateOfDepartFromDatabase(routeId);

        if (dateOfDepart == null) {
            JOptionPane.showMessageDialog(this, "Date de départ non trouvée pour l'itinéraire spécifié", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double travelTimeMinutes = (double) distance / speed * 60;
        LocalDateTime arrivalDate = dateOfDepart.plusMinutes((long) travelTimeMinutes);

        dateArriveMapping.setDateTimeStrict(arrivalDate);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour la distance", "Erreur d'entrée", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Une erreur s'est produite : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

private LocalDateTime getDateOfDepartFromDatabase(int routeId) {
    try {
 Connection connection = ConnectioDB.getConnection();
 String query = "SELECT DateOfDepart FROM Route WHERE RouteId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("DateOfDepart").toLocalDateTime();
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(TrainGareMap.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

private LocalDateTime getArrivalDateFromDatabase(int routeId) {
    try {
 Connection connection = ConnectioDB.getConnection();
        String query = "SELECT DateOfArrive FROM Route WHERE RouteId = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, routeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("DateOfArrive").toLocalDateTime();
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(TrainGareMap.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        RoutId = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        GareNom = new javax.swing.JList<>();
        NDS = new javax.swing.JTextField();
        DstncKM = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        dateArriveMapping = new com.github.lgooddatepicker.components.DateTimePicker();
        jButton3 = new javax.swing.JButton();
        TrainMark = new javax.swing.JComboBox<>();
        DateDepartMapping = new com.github.lgooddatepicker.components.DateTimePicker();

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Train Gare ");
        jLabel1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 265, -1));

        jLabel2.setText("Train Marque");
        jLabel2.setBackground(new java.awt.Color(0, 102, 102));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 150, 20));

        jLabel3.setText("Gare Nom");
        jLabel3.setBackground(new java.awt.Color(0, 102, 102));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 111, -1));

        jLabel4.setText("Date Darrive");
        jLabel4.setBackground(new java.awt.Color(0, 102, 102));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 190, -1));

        jButton1.setText("Ajouter");
        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, 146, -1));

        jLabel5.setText("Numero De Sequence");
        jLabel5.setBackground(new java.awt.Color(0, 102, 102));
        jLabel5.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, -1, -1));

        jLabel7.setText("Date Dparte");
        jLabel7.setBackground(new java.awt.Color(0, 102, 102));
        jLabel7.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, -1));

        jLabel9.setBackground(new java.awt.Color(0, 102, 102));
        jLabel9.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 102));
        jLabel9.setText("Distance En Km");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 190, -1));

        jLabel10.setBackground(new java.awt.Color(0, 102, 102));
        jLabel10.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 102));
        jLabel10.setText("Train Number");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 150, 20));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 720));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setForeground(new java.awt.Color(0, 102, 102));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        RoutId.setBackground(new java.awt.Color(255, 255, 255));
        RoutId.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        RoutId.setForeground(new java.awt.Color(0, 102, 102));
        jPanel2.add(RoutId, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 265, -1));

        GareNom.setBackground(new java.awt.Color(255, 255, 255));
        GareNom.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        GareNom.setForeground(new java.awt.Color(0, 102, 102));
        jScrollPane1.setViewportView(GareNom);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 265, 110));

        NDS.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        NDS.setBackground(new java.awt.Color(255, 255, 255));
        NDS.setForeground(new java.awt.Color(0, 102, 102));
        jPanel2.add(NDS, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 265, -1));

        DstncKM.setBackground(new java.awt.Color(255, 255, 255));
        DstncKM.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        DstncKM.setForeground(new java.awt.Color(0, 102, 102));
        DstncKM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DstncKMActionPerformed(evt);
            }
        });
        jPanel2.add(DstncKM, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 265, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Mapping");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, -1, -1));
        jPanel2.add(dateArriveMapping, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, 260, -1));

        jButton3.setText("Refrech");
        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 102));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 560, 150, 30));

        TrainMark.setBackground(new java.awt.Color(255, 255, 255));
        TrainMark.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainMark.setForeground(new java.awt.Color(0, 102, 102));
        jPanel2.add(TrainMark, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 260, -1));
        jPanel2.add(DateDepartMapping, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 260, 20));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 360, 720));

        pack();
    }// </editor-fold>                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
if (RoutId.getSelectedItem() == null || RoutId.getSelectedItem().toString().trim().isEmpty()) {
    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un identifiant de route.", "Erreur d'entrée", JOptionPane.ERROR_MESSAGE);
    return; // Exit the method to prevent further execution
}

// Check if a Gare name is selected from the list
if (GareNom.getSelectedValue() == null || GareNom.getSelectedValue().trim().isEmpty()) {
    JOptionPane.showMessageDialog(this, "Veuillez sélectionner un nom de gare.", "Erreur d'entrée", JOptionPane.ERROR_MESSAGE);
    return; // Exit the method to prevent further execution
}
        try {
            
    // Check if the route ID selection is empty or null
    if (RoutId.getSelectedItem() == null || RoutId.getSelectedItem().toString().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un identifiant de route.", "Champ vide", JOptionPane.WARNING_MESSAGE);
        return; // Exit the method to prevent further execution
    }
    int routeId = Integer.parseInt(RoutId.getSelectedItem().toString());
    
    // Check if the gare name selection is empty or null
    if (GareNom.getSelectedValue() == null || GareNom.getSelectedValue().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Veuillez sélectionner un nom de gare.", "Champ vide", JOptionPane.WARNING_MESSAGE);
        return; // Exit the method to prevent further execution
    }
    String gareName = GareNom.getSelectedValue();
    Integer gareId = gareDB.getStationIdByName(gareName);
    
    if (gareId == null) {
        JOptionPane.showMessageDialog(this, "GareID non trouvé pour le nom de la gare fourni : " + gareName, "Erreur", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if GareID is not found
    }

    // Check if the sequence number and distance fields are empty
    if (NDS.getText().trim().isEmpty() || DstncKM.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Veuillez entrer des chiffres valides pour le numéro de séquence et la distance.", "Champ vide", JOptionPane.WARNING_MESSAGE);
        return; // Exit the method to prevent further execution
    }
    int sequenceNumber = Integer.parseInt(NDS.getText());
    int distanceKm = Integer.parseInt(DstncKM.getText());

    LocalDateTime arrivalDate = LocalDateTime.now(); // Use your actual logic to determine this value
    Timestamp timestampArrivalDate = Timestamp.valueOf(arrivalDate);
    
    // Create and add RouteGareMapping object to the database as before...
    
    JOptionPane.showMessageDialog(this, "Le Gare " + gareName + " a été ajoutée avec le numéro de séquence " + sequenceNumber, "Succès", JOptionPane.INFORMATION_MESSAGE);

} catch (NumberFormatException ex) {
    JOptionPane.showMessageDialog(this, "Veuillez entrer des chiffres valides pour le numéro de séquence et la distance.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
} catch (Exception ex) {
    JOptionPane.showMessageDialog(this, "Une erreur s'est produite : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
}




        // TODO add your handling code here:
    }                                        

    private void DstncKMActionPerformed(java.awt.event.ActionEvent evt) {                                        
    updateArrivalDate();
    
        // TODO add your handling code here:
    }                                       

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrainGareMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrainGareMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrainGareMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrainGareMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
//public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(() -> {
//            new TrainGareMap().setVisible(true);
//        });
//    }
//}
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrainGareMap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private com.github.lgooddatepicker.components.DateTimePicker DateDepartMapping;
    private javax.swing.JTextField DstncKM;
    private javax.swing.JList<String> GareNom;
    private javax.swing.JTextField NDS;
    private javax.swing.JComboBox<String> RoutId;
    private javax.swing.JComboBox<String> TrainMark;
    private com.github.lgooddatepicker.components.DateTimePicker dateArriveMapping;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration                   

   
}
