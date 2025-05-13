
package Trains;
import static Trains.GareDB.conx;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author Administrator
 */
public class TrainMappi extends javax.swing.JFrame {
private GareDB gareDB = null;
private DefaultListModel<String> gareListModel;
  private List<TrainGareMappin> routeGareMappings;
 private Map<Integer, Integer> reversedGareIds = new HashMap<>();
     private AdminPanel adminPanel;
     private AdminPanel originalAdminPanel;
     private boolean isNewBatch = false;
private int currentMappingID = 0;

private void loadInitialMappingID() {
    try {
        GareDB d = GareDB.getCon();
        currentMappingID = d.getHighestMappingID();
        NDS.setText("1"); // Set initial NDS value to "1"
    } catch (SQLException ex) {
        showErrorMessage("Erreur lors de la récupération de l'ID de mappage le plus élevé : " + ex.getMessage());
    }
}
    private void populateGareNamesAndStations(int routeId) {
    try (Connection connection = ConnectioDB.getConnection()) {
        String trainMarkQuery = "SELECT TrainMarque FROM Trains WHERE TrainNumber = ?";
        try (PreparedStatement trainMarkPs = connection.prepareStatement(trainMarkQuery)) {
            trainMarkPs.setInt(1, routeId);
            try (ResultSet trainMarkRs = trainMarkPs.executeQuery()) {
                String trainMark = "Al boraq"; // Default value if TrainMark is not found
                if (trainMarkRs.next()) {
                    trainMark = trainMarkRs.getString("TrainMarque");
                }
                String gareQuery;
                if ("Al boraq".equals(trainMark)|| ("al boraq".equals(trainMark))||("TGV".equals(trainMark))||("alboraq".equals(trainMark))||("AL BORAQ".equals(trainMark))) {
                    gareQuery = "SELECT GareName FROM Gare WHERE AlboraqSupp = 1";
                } else if ("TNR".equals(trainMark) || ("tnr".equals(trainMark))|| ("Tnr".equals(trainMark))) {
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
        Logger.getLogger(TrainMappi.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    public static void adjustTimes(List<TrainGareMappin> trainGareMappings) {
        LocalDateTime previousArrivalTime = null;
        for (TrainGareMappin mapping : trainGareMappings) {
            if (previousArrivalTime == null) {
                Timestamp departureTimestamp = mapping.getDateDepartMapping();
                previousArrivalTime = departureTimestamp.toLocalDateTime();
            }
            if (previousArrivalTime != null) {
                LocalDateTime newDepartureTime = previousArrivalTime.plusMinutes(1);
                mapping.setDateDepartMapping(Timestamp.valueOf(newDepartureTime));
            }
            long distanceKm = mapping.getDstncKM();
            long travelTimeMinutes = calculateTravelTime(distanceKm);
            LocalDateTime departureTime = mapping.getDateDepartMapping().toLocalDateTime();
            LocalDateTime arrivalTime = departureTime.plusMinutes(travelTimeMinutes);
            mapping.setDateArriveMapping(Timestamp.valueOf(arrivalTime));
            previousArrivalTime = arrivalTime;
        }
    }
     private static long calculateTravelTime(long distanceKm) {
        return distanceKm * 10; // Assuming 10 minutes per km
    }
  private int getTrainSpeed(int trainNumber) {
    int speed = 0;
    try {
        Connection connection = ConnectioDB.getConnection();
        String query = "SELECT TrainSpeed FROM Trains WHERE TrainNumber = ?";
        System.out.println("Querying speed for train number: " + trainNumber); // Debugging log
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, trainNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    speed = rs.getInt("TrainSpeed");
                    System.out.println("Retrieved speed: " + speed); // Debugging log
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(TrainMappi.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (speed <= 0) {
        System.err.println("Invalid or no speed found for train number: " + trainNumber); // More explicit error logging
    }
    return speed;
}
private void updateDepartureDate(LocalDateTime newDepartureDate) {
    LocalDateTime currentDepartureDate = DateDepartMapping.getDateTimeStrict();
    if (!Objects.equals(currentDepartureDate, newDepartureDate)) {
        DateDepartMapping.setDateTimePermissive(newDepartureDate);
    }
}
private void updateArrivalDate() {
    try {
        if (TrainN.getSelectedItem() == null || DstncKM.getText().isEmpty() || DateDepartMapping.getDateTimeStrict() == null) {
            return;
        }
        int trainNumber = Integer.parseInt(TrainN.getSelectedItem().toString());
        double distanceKm = Double.parseDouble(DstncKM.getText());
        LocalDateTime departureDate = DateDepartMapping.getDateTimeStrict();
        if (distanceKm <= 0) {
            return; 
        }
        LocalDateTime arrivalDate = calculateArrivalDate(departureDate, trainNumber, distanceKm);
        if (arrivalDate != null) {
            dateArriveMapping.setDateTimeStrict(arrivalDate);
        }
    } catch (NumberFormatException e) {
    } catch (IllegalArgumentException e) {
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error calculating arrival date: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
private LocalDateTime calculateArrivalDate(LocalDateTime departureDate, int trainNumber, double distanceKm) {
    int speedKmPerHour = getTrainSpeed(trainNumber);
    System.out.println("Departure Date: " + departureDate);
    System.out.println("Train Number: " + trainNumber);
    System.out.println("Distance (Km): " + distanceKm);
    if (speedKmPerHour <= 0) {
        throw new IllegalArgumentException("Invalid train speed.");
    }
    double travelTimeHours = distanceKm / speedKmPerHour; // Calculate travel time in hours
    long travelTimeMinutes = (long) (travelTimeHours * 60);
    LocalDateTime arrivalDate = departureDate.plusMinutes(travelTimeMinutes);
    return arrivalDate;
}

private void tryUpdateArrivalDate() {
    if (!SwingUtilities.isEventDispatchThread()) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tryUpdateArrivalDate();
            }
        });
        return;
    }
    try {
        if (TrainN.getSelectedItem() == null || DstncKM.getText().isEmpty() || DateDepartMapping.getDateTimeStrict() == null) {
            return;
        }
        int trainNumber = Integer.parseInt(TrainN.getSelectedItem().toString());
        double distanceKm = Double.parseDouble(DstncKM.getText());
        LocalDateTime departureDate = DateDepartMapping.getDateTimeStrict();
        if (distanceKm <= 0) {
            return;
        }

        // Update the departure date before calculating the arrival date
        updateDepartureDate(departureDate);

        LocalDateTime arrivalDate = calculateArrivalDate(departureDate, trainNumber, distanceKm);
        if (arrivalDate != null) {
            dateArriveMapping.setDateTimePermissive(arrivalDate); // Make sure this method exists and correctly updates the UI
        }
    } catch (NumberFormatException e) {
    } catch (IllegalArgumentException e) {
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error calculating arrival date: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    public TrainMappi() throws SQLException {
         routeGareMappings = new ArrayList<>();
        initComponents();
        updateJTable(jTable1);
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

         //calculateArrivalDate(LocalDateTime.MAX, TEXT_CURSOR, WIDTH);
//         updateArrivalDate();
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
           loadInitialMappingID();
    currentMappingID++;
          TrainN.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                isNewBatch = true; // Start a new batch when a new train number is selected
                int selectedRouteId = Integer.parseInt(TrainN.getSelectedItem().toString());
                populateGareNamesAndStations(selectedRouteId);
            }
        }
    });
       // Assuming RoutId is your JComboBox for train number
    TrainN.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedRouteId = Integer.parseInt(TrainN.getSelectedItem().toString());
//                GareDepartTextField.setText("");
//                GareDariveTextField.setText("");
                populateGareNamesAndStations(selectedRouteId);
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
        deleteMappings();
    TrainN.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int selectedRouteId = Integer.parseInt(TrainN.getSelectedItem().toString());
//                GareDepartTextField.setText("");
//                GareDariveTextField.setText("");
                populateGareNamesAndStations(selectedRouteId);
            }
        }
    });
}

    private void deleteMappings() {
    try {
        // Create a SQL query to delete rows from TrainGareMapping where the current time is greater than DateArriveMapping for each MappingID
        String deleteQuery = "DELETE FROM TrainGareMapping WHERE MappingID IN (SELECT MappingID FROM TrainGareMapping WHERE GETDATE() > DateArriveMapping)";
        
        // Prepare and execute the delete statement
         try ( Connection connection = ConnectioDB.getConnection(); 
                PreparedStatement pst = connection.prepareStatement(deleteQuery)) {
            pst.executeUpdate();
            System.out.println("Deleted rows where current time > LastDateArriveMapping");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void populateTrainMarque() {
    Set<String> uniqueTrainMarks = new HashSet<>(); // Set to store unique train marks
    try (Connection connection = ConnectioDB.getConnection()) {
        String query = "SELECT TrainMarque FROM Trains";
        try (PreparedStatement st = connection.prepareStatement(query);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                String trainMark = rs.getString("TrainMarque").toUpperCase(); // Convert to uppercase
                uniqueTrainMarks.add(trainMark); // Add to set to ensure uniqueness
            }
        }
        
        // Add unique train marks to the combo box
        for (String trainMark : uniqueTrainMarks) {
            TrainMark.addItem(trainMark);
        }
    } catch (SQLException ex) {
        Logger.getLogger(TrainMappi.class.getName()).log(Level.SEVERE, null, ex);
    }
}
private void populateRouteIds() {
    String trainMark = (String) TrainMark.getSelectedItem();
    trainMark = trainMark != null ? trainMark.toUpperCase() : "";

    if (trainMark.isEmpty()) {
        TrainN.removeAllItems(); // Clear RoutId if nothing is selected in TrainMark
        return;
    }

    String query = "";
    switch (trainMark) {
        case "AL BORAQ":
            query = "SELECT TrainNumber FROM Trains WHERE TrainMarque = 'Al boraq'";
            break;
        case "TNR":
            query = "SELECT TrainNumber FROM Trains WHERE TrainMarque = 'TNR'";
            break;
        case "ATLAS":
            query = "SELECT TrainNumber FROM Trains WHERE TrainMarque = 'Atlas'";
            break;
        default:
            // handle unknown train marks here if necessary
            return;
    }

    try (Connection connection = ConnectioDB.getConnection(); 
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        ResultSet resultSet = preparedStatement.executeQuery();
        TrainN.removeAllItems(); // Clear existing items
        while (resultSet.next()) {
            String trainNumber = resultSet.getString("TrainNumber");
            TrainN.addItem(trainNumber);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Échec de la saisie des numéros de train : " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}


public void closePanel(JFrame frame, JPanel panel) {
    // Remove the panel from its parent container
    frame.getContentPane().remove(panel);
    // Dispose the panel
    frame.dispose();
}
public void reopenPanel(JFrame frame, JPanel panel) {
    // Recreate the panel
    panel = new JPanel();
    // Add the panel to the frame's content pane
    frame.add(panel);
    // Pack and display the frame
    frame.pack();
    frame.setVisible(true);
}
public void reloadPanel(JFrame frame, JPanel panel) {
    // Close the current panel
    closePanel(frame, panel);
    
    // Reopen a new instance of the panel
    reopenPanel(frame, panel);
}
int option;
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        TrainMark = new javax.swing.JComboBox<>();
        TrainN = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        GareNom = new javax.swing.JList<>();
        NDS = new javax.swing.JTextField();
        DateDepartMapping = new com.github.lgooddatepicker.components.DateTimePicker();
        dateArriveMapping = new com.github.lgooddatepicker.components.DateTimePicker();
        DstncKM = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gare Liste");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, -1, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Train Marque");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, -1, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Train Nomber");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Sequance Nomber");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 440, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Date Depart");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Date Darrive");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 560, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Distance en Km");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 500, -1, -1));

        jButton1.setText("Ajouter");
        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 620, -1, -1));

        jButton2.setText("Retour");
        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 102));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 620, 110, 30));

        jLabel8.setText("Train Trajet");
        jLabel8.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TrainMark.setBackground(new java.awt.Color(0, 102, 102));
        TrainMark.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainMark.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(TrainMark, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 240, -1));

        TrainN.setBackground(new java.awt.Color(0, 102, 102));
        TrainN.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainN.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(TrainN, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, 240, -1));

        GareNom.setBackground(new java.awt.Color(0, 102, 102));
        GareNom.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        GareNom.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(GareNom);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 170, 240, -1));

        NDS.setBackground(new java.awt.Color(0, 102, 102));
        NDS.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        NDS.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(NDS, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 250, 30));

        DateDepartMapping.setBackground(new java.awt.Color(0, 102, 102));
        DateDepartMapping.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(DateDepartMapping, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, 30));

        dateArriveMapping.setBackground(new java.awt.Color(0, 102, 102));
        dateArriveMapping.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(dateArriveMapping, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 550, -1, 30));

        DstncKM.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        DstncKM.setBackground(new java.awt.Color(0, 102, 102));
        DstncKM.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(DstncKM, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 490, 250, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 580, 90));

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Suprrimer");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 200, -1, -1));

        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("imprimer");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 200, -1, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 892, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
 try {
        if (TrainN.getSelectedItem() == null || TrainN.getSelectedItem().toString().trim().isEmpty()) {
            showErrorMessage("Veuillez sélectionner un identifiant de route.");
            return;
        }

        if (GareNom.getSelectedValue() == null || GareNom.getSelectedValue().toString().trim().isEmpty()) {
            showErrorMessage("Veuillez sélectionner un nom de gare.");
            return;
        }

        if (NDS.getText().trim().isEmpty()) {
            showWarningMessage("Veuillez entrer un numéro de séquence valide.");
            return;
        }

        if (DateDepartMapping.getDateTimeStrict() == null) {
            showErrorMessage("Veuillez sélectionner une date de départ.");
            return;
        }

        int routeId = Integer.parseInt(TrainN.getSelectedItem().toString());
        int sequenceNumber = Integer.parseInt(NDS.getText().trim());
        int gareId = gareDB.getStationIdByName(GareNom.getSelectedValue().toString());

        int distanceKm = Integer.parseInt(DstncKM.getText());
        LocalDateTime departureDate = DateDepartMapping.getDateTimeStrict();
        
        if (departureDate == null) {
            showErrorMessage("Veuillez sélectionner une date de départ.");
            return;
        }
        
        LocalDateTime arrivalDate = calculateArrivalDate(departureDate, routeId, distanceKm);
        
        if (arrivalDate == null) {
            showErrorMessage("Veuillez sélectionner une date d'arrivée.");
            return;
        }
        
        if (isGareNomUsed(currentMappingID, gareId)) {
            showWarningMessage("La gare sélectionnée est déjà utilisée dans ce mappage.");
            return;
        }
        if (isSequenceNumberUsed(currentMappingID, sequenceNumber)) {
            showErrorMessage("Le numéro de séquence est déjà utilisé dans ce mappage.");
            return;
        }

        TrainGareMappin cb = new TrainGareMappin();
        cb.setRoutId(routeId);
        cb.setGareNom(gareId);
        cb.setDateDepartMapping(Timestamp.valueOf(departureDate));
        cb.setDateArriveMapping(Timestamp.valueOf(arrivalDate));
        cb.setNDS(sequenceNumber);
        cb.setDstncKM(distanceKm);

        if ("1".equals(NDS.getText().trim())) {
            currentMappingID++;
        }
        cb.setMappingId(currentMappingID);

        GareDB d = GareDB.getCon();
        d.addRouteMappings(cb);

        showDepartureDateForNextGare(cb);
        // Show success message
        String GareName = getStationNameById(gareId);
        JOptionPane.showMessageDialog(this, "Gare '" + GareName + "' ajoutée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
    } catch (NumberFormatException ex) {
        showErrorMessage("Veuillez entrer des chiffres valides pour le numéro de séquence et la distance.");
        updateJTable(jTable1);
    } catch (Exception ex) {
        showErrorMessage("Une erreur s'est produite : " + ex.getMessage());
    }

    }//GEN-LAST:event_jButton1ActionPerformed
private boolean isSequenceNumberUsed(int mappingId, int sequenceNumber) throws SQLException {
    if (conx == null) {
        System.err.println("Error: Database connection is null.");
        return false;
    }

    // Prepare SQL query to check if the sequence number is used within the same MappingID
    String checkQuery = "SELECT COUNT(*) FROM TrainGareMapping WHERE MappingID = ? AND SequenceNumber = ?";

    try (PreparedStatement checkPs = conx.prepareStatement(checkQuery)) {
        // Set parameters
        checkPs.setInt(1, mappingId);
        checkPs.setInt(2, sequenceNumber);

        try (ResultSet resultSet = checkPs.executeQuery()) {
            if (resultSet.next()) {
                // Retrieve the count of occurrences
                int count = resultSet.getInt(1);
                // Determine if the sequence number is used within the same MappingID
                return count > 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error while checking if sequence number is used: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

    public String getStationNameById(Integer gareId) throws SQLException {
    if (conx == null) {
        System.err.println("Error: Database connection is null.");
        return null;
    }

    String query = "SELECT GareName FROM Gare WHERE GareID = ?";
    try (PreparedStatement ps = conx.prepareStatement(query)) {
        ps.setInt(1, gareId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("GareName");
            } else {
                System.out.println("GareID not found: " + gareId);
                return null; // GareID not found
            }
        }
    } catch (SQLException e) {
        System.err.println("Error while fetching GareName for GareID: " + gareId);
        e.printStackTrace();
        return null; // Or you can rethrow the exception
    }
}
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
 this.setVisible(false);
   AdminPanel AdminPanel = new AdminPanel();
AdminPanel.setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       GareDB d = new GareDB();
 DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int[] selectedRows = jTable1.getSelectedRows();

    if (selectedRows.length == 0) {
        JOptionPane.showMessageDialog(null, "Veuillez sélectionner au moins une ligne à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir supprimer les lignes sélectionnées ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
        return;
    }

    for (int i = selectedRows.length - 1; i >= 0; i--) {
        int mappingID = (int) model.getValueAt(selectedRows[i], 0); // Assuming MappingID is in the first column
        model.removeRow(selectedRows[i]);
        d.deleteMappingByID(mappingID);
    }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
     try {
        // Load the JasperReport template
        JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\Trajit.jrxml");

        // Check if the user selected any row
        if (jTable1.getSelectedRowCount() == 0) {
            // If nothing is selected, construct the SQL query to retrieve all rows
            String sql = "SELECT "
                    + "tgm.MappingID, "
                    + "tgm.TrainN AS TrainNumber, "
                    + "t.TrainMarque, "
                    + "g.GareName, "
                    + "tgm.DateDpart AS DepartureDate, "
                    + "tgm.SequenceNumber, "
                    + "tgm.distanceKmNextGare AS DistanceKmNextGare, "
                    + "tgm.DateArriveMapping AS ArrivalDate "
                    + "FROM "
                    + "Oncf.dbo.TrainGareMapping tgm "
                    + "JOIN "
                    + "Oncf.dbo.Gare g ON tgm.GareN = g.GareID "
                    + "JOIN "
                    + "Oncf.dbo.Trains t ON tgm.TrainN = t.TrainNumber";

            // Set the constructed SQL query
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jasdi.setQuery(newQuery);
        } else {
            // Construct the SQL query dynamically based on the user's selection
            StringBuilder idList = new StringBuilder();
            for (int selectedRow : jTable1.getSelectedRows()) {
                String id = jTable1.getValueAt(selectedRow, 0).toString(); // Assuming the first column contains the MappingID
                idList.append(id).append(",");
            }
            idList.deleteCharAt(idList.length() - 1); // Remove the last comma
            
            // Construct the SQL query to retrieve selected rows
            String sql = "SELECT "
                    + "tgm.MappingID, "
                    + "tgm.TrainN AS TrainNumber, "
                    + "t.TrainMarque, "
                    + "g.GareName, "
                    + "tgm.DateDpart AS DepartureDate, "
                    + "tgm.SequenceNumber, "
                    + "tgm.distanceKmNextGare AS DistanceKmNextGare, "
                    + "tgm.DateArriveMapping AS ArrivalDate "
                    + "FROM "
                    + "Oncf.dbo.TrainGareMapping tgm "
                    + "JOIN "
                    + "Oncf.dbo.Gare g ON tgm.GareN = g.GareID "
                    + "JOIN "
                    + "Oncf.dbo.Trains t ON tgm.TrainN = t.TrainNumber "
                    + "WHERE tgm.MappingID IN (" + idList.toString() + ")";

            // Set the constructed SQL query
            JRDesignQuery newQuery = new JRDesignQuery();
            newQuery.setText(sql);
            jasdi.setQuery(newQuery);
        }

        // Compile the JasperReport template
        JasperReport js = JasperCompileManager.compileReport(jasdi);

        // Provide a database connection (replace 'con' with your actual Connection object)
        Connection con = ConnectioDB.getConnection(); // Replace ConnectioDB.getConnection() with your database connection method

        // Fill the JasperReport with data
        JasperPrint jd = JasperFillManager.fillReport(js, null, con);

        // Use an array to hold the JasperViewer instance
        final JasperViewer[] jasperViewerHolder = new JasperViewer[1];
        jasperViewerHolder[0] = new JasperViewer(jd, false);

        // Add window listener to the JasperViewer frame
        jasperViewerHolder[0].addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // This method is called when the JasperViewer frame is closing
                // Handle the closing event here (for example, hide the JasperViewer instead of closing the entire application)
                jasperViewerHolder[0].setVisible(false);
                jasperViewerHolder[0] = null; // Set the JasperViewer instance to null after hiding
            }
        });

        jasperViewerHolder[0].setVisible(true);
    } catch (JRException ex) {
        ex.printStackTrace();
        // Handle JasperReports exception
    }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
   jTable1.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
  jTable1.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked
  
    private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
}
    private void showDepartureDateForNextGare(TrainGareMappin routeMapping) {
    try {
        int stoppageMinutes = 1;
        Timestamp arrivalTimestamp = routeMapping.getDateArriveMapping();
        Timestamp departureTimestamp = routeMapping.getDateDepartMapping();

        if (arrivalTimestamp != null) {
            // The arrival timestamp is present, proceed with the existing logic
            LocalDateTime calculatedDepartureDateTime = arrivalTimestamp.toLocalDateTime().plusMinutes(stoppageMinutes);
            // Update the DateDepartMapping with the calculated departure timestamp
            DateDepartMapping.setDateTimePermissive(calculatedDepartureDateTime);
            // Print the calculated departure date and time
            System.out.println("Calculated Departure Date and Time: " + calculatedDepartureDateTime);
        } else {
            System.err.println("Warning: Arrival Timestamp is null for route mapping. Unable to calculate departure timestamp.");
            return;
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors du calcul de la date de départ pour la prochaine gare : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
    private void showWarningMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Avertissement", JOptionPane.WARNING_MESSAGE);
}
private void updateJTable(JTable table) {
      GareDB d = GareDB.getCon();
    String query = "SELECT tgm.[MappingID], tgm.[TrainN], t.[TrainMarque], g.[GareName], tgm.[DateDpart], tgm.[SequenceNumber], tgm.[distanceKmNextGare], tgm.[DateArriveMapping] " +
                   "FROM [Oncf].[dbo].[TrainGareMapping] tgm " +
                   "JOIN [Oncf].[dbo].[Gare] g ON tgm.[GareN] = g.[GareID] " +
                   "JOIN [Oncf].[dbo].[Trains] t ON tgm.[TrainN] = t.[TrainNumber]";
    
    try (Statement stmt = d.conx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        // Create a table model and set it on the JTable
        DefaultTableModel model = buildTableModel(rs);
        table.setModel(model);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors du chargement des données : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
    ResultSetMetaData metaData = rs.getMetaData();

    // Names of columns
    Vector<String> columnNames = new Vector<>();
    int columnCount = metaData.getColumnCount();
    for (int column = 1; column <= columnCount; column++) {
        columnNames.add(metaData.getColumnName(column));
    }

    // Data of the table
    Vector<Vector<Object>> data = new Vector<>();
    while (rs.next()) {
        Vector<Object> vector = new Vector<>();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            vector.add(rs.getObject(columnIndex));
        }
        data.add(vector);
    }

    return new DefaultTableModel(data, columnNames);
}

private boolean isGareNomUsed(int mappingId, int gareId) throws SQLException {
    // Prepare SQL query to check if the gareId is used within the same MappingID
    String checkQuery = "SELECT COUNT(*) FROM TrainGareMapping WHERE MappingID = ? AND GareN = ?";

    try (Connection connection = ConnectioDB.getConnection();
         PreparedStatement checkPs = connection.prepareStatement(checkQuery)) {
        // Set parameters
        checkPs.setInt(1, mappingId);
        checkPs.setInt(2, gareId);

        try (ResultSet resultSet = checkPs.executeQuery()) {
            if (resultSet.next()) {
                // Retrieve the count of occurrences
                int count = resultSet.getInt(1);
                // Determine if gareId is used within the same MappingID
                return count > 0;
            }
        }
    }
    return false;
}
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new TrainMappi().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(TrainMappi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.github.lgooddatepicker.components.DateTimePicker DateDepartMapping;
    private javax.swing.JTextField DstncKM;
    private javax.swing.JList<String> GareNom;
    private javax.swing.JTextField NDS;
    private javax.swing.JComboBox<String> TrainMark;
    private javax.swing.JComboBox<String> TrainN;
    private com.github.lgooddatepicker.components.DateTimePicker dateArriveMapping;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
