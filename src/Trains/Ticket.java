/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Trains;

import static Trains.ConnectioDB.getConnection;
import static Trains.GareDB.rs;
import static Trains.GareDB.st;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JRException;
import static org.codehaus.groovy.ast.tools.GeneralUtils.param;
/**
 *
 * @author Administrator
 */
public class Ticket extends javax.swing.JFrame {
    
    
    static Ticket obj;
    public static Ticket setObj(){
        if(obj == null){
            obj = new Ticket();
        }
        return obj;
    }
    
    
    
    static int numPlace;
    static int numPlace2;
    
    
    
private String gare1;
Voyage voyage = new Voyage();

String Departuregare = voyage.getSelectedGare1();

String Arrivegare = voyage.getSelectedGare2();
 private Connection conx;
    private String gare2;
      private int closestTrain;
      
    private String selectedDepartureTime;
private String selectedArrivalTime;

private double getTotalDistanceFromTrainGareMapping(int trainNumber, int departureGare, int arriveGare) {
    double totalDistance = 0;
    try {
        Connection connection = ConnectioDB.getConnection();
        String query = "SELECT SUM(distanceKmNextGare) as totalDistance " +
                       "FROM TrainGareMapping " +
                       "WHERE TrainN = ? " +
                       "AND SequenceNumber >= (SELECT MAX(SequenceNumber) FROM TrainGareMapping WHERE TrainN = ? AND GareN = ?) " +
                       "AND SequenceNumber <= (SELECT MAX(SequenceNumber) FROM TrainGareMapping WHERE TrainN = ? AND GareN = ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, trainNumber);
            ps.setInt(2, trainNumber);
            ps.setInt(3, departureGare);
            ps.setInt(4, trainNumber);
            ps.setInt(5, arriveGare);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalDistance = rs.getDouble("totalDistance");
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace(); // Handle database error
    }
    return totalDistance;
}

    

public Ticket(int closestTrain, String selectedGare1, String selectedGare2) throws SQLException {
    this.closestTrain = closestTrain;
    this.Departuregare = selectedGare1;
    this.Arrivegare = selectedGare2;
    initComponents();
Nomber.setModel(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
    l1.setText(selectedGare1);
    conx = ConnectioDB.getConnection();
    l2.setText(selectedGare2);
   // updatePrice();
   
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
      private int getTrainSpeed(int trainNumber) {
        int speed = 0;
        try {
            Connection connection = ConnectioDB.getConnection();
            String query = "SELECT TrainSpeed FROM Trains WHERE TrainNumber = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, trainNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        speed = rs.getInt("TrainSpeed");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle database error
        }
        return speed;
    }

    public Ticket() {
        initComponents();
 
         this.conx = ConnectioDB.getConnection();

    //l1.setText(selectedGare1);
    conx = ConnectioDB.getConnection();

}



public Integer getStationIdByName(String gareName) throws SQLException {
    try (Connection connection = ConnectioDB.getConnection()) {
        if (connection == null) {
            System.err.println("Error: Database connection is null.");
            return null;
        }

        String query = "SELECT GareID FROM Gare WHERE GareName = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, gareName.trim()); // Trim the gareName to ensure whitespace is not an issue
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("GareID");
                } else {
                    System.out.println("GareName not found: " + gareName);
                    return null; // GareName not found
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while fetching GareID for GareName: " + gareName);
            e.printStackTrace();
            return null; // Or you can rethrow the exception
        }
    }
}

public String getTrainByID(String train) throws SQLException {
    try (Connection connection = ConnectioDB.getConnection()) {
        if (connection == null) {
            System.err.println("Error: Database connection is null.");
            System.err.println("1.");
            return null;
        }

        String query = "SELECT CONCAT(TrainNumber, ' ', TrainMarque) AS TrainInfo FROM Trains WHERE TrainNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, train.trim()); // Trim the train to ensure whitespace is not an issue
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Get train info from the result set
                    String trainInfo = rs.getString("TrainInfo");
                    // Convert train info to uppercase
                    return trainInfo.toUpperCase();
                } else {
                    System.out.println("Train info not found for TrainNumber: " + train);
                    return null; // Train info not found
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Error while fetching Train info for TrainNumber: " + train);
        e.printStackTrace();
        return null; // Or you can rethrow the exception
    }
}

public String formatTime(int minutes) {
    int hours = minutes / 60;
    int remainingMinutes = minutes % 60;
    return String.format("%02dh%02dmin", hours, remainingMinutes);
}
   private void loadAdherentDetails(String IDcarte) {
        try (Connection connection = ConnectioDB.getConnection()) {
            String query = "SELECT RP, NomAdh, PrenomAdh FROM adhérent WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, IDcarte);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String nomAdhValue = resultSet.getString("NomAdh");
                    String prenomAdhValue = resultSet.getString("PrenomAdh");
                  //  String nomPrenomValue = nomAdhValue + " " + prenomAdhValue; // Concatenate NomAdh and PrenomAdh
//                    NomPrenom.setText(nomPrenomValue);
                    String prc = resultSet.getString("RP");
            //        perc.setText(prc);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Voyage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       public void setSelectedGares(String gare1, String gare2) {
        this.gare1 = gare1;
        this.gare2 = gare2;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        tab1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        l1 = new javax.swing.JLabel();
        l2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        classComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Nomber = new javax.swing.JSpinner();
        jLabel17 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        Depart = new javax.swing.JTextField();
        Train = new javax.swing.JTextField();
        Marque = new javax.swing.JTextField();
        Arrival = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        Class = new javax.swing.JTextField();
        garedpart = new javax.swing.JTextField();
        garearrive = new javax.swing.JTextField();
        mapping = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        Place = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        Prix = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane3.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        tab1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText(">");
        jLabel2.setFont(new java.awt.Font("Agency FB", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        tab1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 53, -1, -1));

        l1.setFont(new java.awt.Font("Agency FB", 1, 48)); // NOI18N
        l1.setForeground(new java.awt.Color(0, 102, 102));
        l1.setText("jLabel3");
        tab1.add(l1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, -1, -1));

        l2.setText("jLabel3");
        l2.setFont(new java.awt.Font("Agency FB", 1, 48)); // NOI18N
        l2.setForeground(new java.awt.Color(0, 102, 102));
        tab1.add(l2, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, -1, -1));

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Réserver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        tab1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 433, -1, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Nomber de Ticket");
        tab1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 430, -1, -1));

        classComboBox.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        classComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1er Class", "2eme Class", " " }));
        tab1.add(classComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 388, 156, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date depart", "Date arive", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        tab1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, -1, 190));
        tab1.add(Nomber, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 430, 160, -1));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Class de Voyage");
        tab1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(181, 394, -1, -1));

        jTabbedPane3.addTab("Voyage", tab1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Departure : ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 130, -1));

        jLabel6.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Train Nomber :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 97, -1));

        jLabel7.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Train Marque :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 97, -1));

        jLabel5.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Arrival :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 97, -1));

        jLabel8.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Place N° :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 109, 75, -1));

        Depart.setBackground(new java.awt.Color(255, 255, 255));
        Depart.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Depart.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(Depart, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 231, -1));

        Train.setBackground(new java.awt.Color(255, 255, 255));
        Train.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Train.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(Train, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 220, 230, -1));

        Marque.setBackground(new java.awt.Color(255, 255, 255));
        Marque.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Marque.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(Marque, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 230, -1));

        Arrival.setBackground(new java.awt.Color(255, 255, 255));
        Arrival.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Arrival.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(Arrival, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 230, -1));

        jButton2.setText("Imprimer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 425, -1, -1));

        jLabel16.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Class");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 63, 75, -1));

        Class.setBackground(new java.awt.Color(255, 255, 255));
        Class.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Class.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(Class, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 60, 231, -1));

        garedpart.setBackground(new java.awt.Color(0, 102, 102));
        garedpart.setForeground(new java.awt.Color(0, 102, 102));
        garedpart.setCaretColor(new java.awt.Color(0, 102, 102));
        garedpart.setDisabledTextColor(new java.awt.Color(0, 102, 102));
        garedpart.setSelectedTextColor(new java.awt.Color(0, 102, 102));
        garedpart.setSelectionColor(new java.awt.Color(0, 102, 102));
        jPanel1.add(garedpart, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 425, 71, -1));

        garearrive.setBackground(new java.awt.Color(0, 102, 102));
        garearrive.setForeground(new java.awt.Color(0, 102, 102));
        garearrive.setCaretColor(new java.awt.Color(0, 102, 102));
        garearrive.setDisabledTextColor(new java.awt.Color(0, 102, 102));
        garearrive.setSelectedTextColor(new java.awt.Color(0, 102, 102));
        garearrive.setSelectionColor(new java.awt.Color(0, 102, 102));
        garearrive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                garearriveActionPerformed(evt);
            }
        });
        jPanel1.add(garearrive, new org.netbeans.lib.awtextra.AbsoluteConstraints(408, 425, 71, -1));

        mapping.setBackground(new java.awt.Color(0, 102, 102));
        mapping.setForeground(new java.awt.Color(0, 102, 102));
        mapping.setCaretColor(new java.awt.Color(0, 102, 102));
        mapping.setDisabledTextColor(new java.awt.Color(0, 102, 102));
        mapping.setSelectedTextColor(new java.awt.Color(0, 102, 102));
        mapping.setSelectionColor(new java.awt.Color(0, 102, 102));
        jPanel1.add(mapping, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 425, 71, -1));

        jButton3.setText("Réserver un autre billet");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 460, -1, -1));

        Place.setBackground(new java.awt.Color(255, 255, 255));
        Place.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Place.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.add(Place, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 230, -1));

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 520));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        Prix.setBackground(new java.awt.Color(0, 102, 102));
        Prix.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        Prix.setForeground(new java.awt.Color(255, 255, 255));
        Prix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrixActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 102));
        jLabel14.setText("Prix");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(Prix, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Prix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap(422, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 0, 370, 510));

        jTabbedPane3.addTab("Voyage", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 860, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("tab3", jPanel3);

        getContentPane().add(jTabbedPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 525));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
          DefaultTableModel tbl = (DefaultTableModel) jTable1.getModel();
            int row = jTable1.getSelectedRow();
        if (row == -1) {
                // No row selected, show an error message or handle it accordingly
                JOptionPane.showMessageDialog(this, "Please select a row from the table first.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
 
       Depart.setText(String.valueOf(tbl.getValueAt(row, 0)));
            Arrival.setText(String.valueOf(tbl.getValueAt(row, 1)));
               Train.setText( String.valueOf(tbl.getValueAt(row, 4)));
                mapping.setText(String.valueOf(tbl.getValueAt(row, 5)));
                Marque.setText(String.valueOf(tbl.getValueAt(row, 6)));
                    Depart.setText(String.valueOf(tbl.getValueAt(row, 0)));


         

          
     
        GareDB d = new GareDB();

        int trainPlace1 = 0;
        int trainPlace2 = 0;
        
        
        String test1  =  String.valueOf(classComboBox.getSelectedItem());
        try {
        
        if(test1.equals("1er Class")){
                       GareDB.rs = GareDB.st.executeQuery("select count(Vplace) as 'count', TrainplacesNum1cls \n"
                    + "  from Trains join Voyage\n"
                    + "  on TrainNumber like fktrain where TrainNumber = "+Train.getText()+"   group by TrainplacesNum1cls ;");
        while (rs.next()) {
                numPlace = rs.getInt("count");
                trainPlace1 = rs.getInt("TrainplacesNum1cls");
            }
            rs.close();
        }else{
                       GareDB.rs = GareDB.st.executeQuery("select count(VClass2) as 'count', TrainplacesNum2cls \n"
                    + "  from Trains join Voyage\n"
                    + "  on TrainNumber like fktrain where TrainNumber = "+Train.getText()+"   group by  TrainplacesNum2cls ;");
         while (rs.next()) {
                numPlace2 = rs.getInt("count");
                trainPlace2 = rs.getInt("TrainplacesNum2cls");
            }
            rs.close();
        
        }
  
        } catch (SQLException ex) {
            Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
        }
        

           int numberOfInserts = (Integer) Ticket.setObj().Nomber.getValue();
  if (numberOfInserts == 0) {
        JOptionPane.showMessageDialog(null, "Le nombre d'Tocket est de 0.");
        return;
    }
         int test = 1;
      int availableSeats;
        if (test1.equals("1er Class")) {
            availableSeats = trainPlace1 - numPlace;
        } else {
            availableSeats = trainPlace2 - numPlace2;
        }

        if (numberOfInserts > availableSeats) {
            JOptionPane.showMessageDialog(null, "Pas assez de places disponibles");
            return;
        }
            
                if (test1.equals("1er Class")) {

                        test = test + numPlace;
                        Place.setText(String.valueOf(test));
                } else {

                    try {
                        GareDB.rs = GareDB.st.executeQuery("SELECT count(Vclass2) as 'count'   FROM Voyage WHERE TrainN = " + Train.getText());
                        
                        test = test +numPlace;
                        Place.setText(String.valueOf(test));
                    } catch (SQLException ex) {
                        Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            jTabbedPane3.setSelectedIndex(1);

            String txtfield = String.valueOf(classComboBox.getSelectedItem());
            Class.setText(txtfield);

            updatePrice();
    
            // updatePlaceAndVoitureFields(Train.getText(), Marque.getText(), Class.getText(), mapping.getText());
//          TicketBeans.setObj().getInfo();
//        }else{
//            JOptionPane.showMessageDialog(null, "Aucun place");
//        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void updatePrice() {
        GareDB d = new GareDB();
    System.out.println("updatePrice() method called."); // Debug statement
    try {
        String trainText = Train.getText();
        String classText = Class.getText();
        Integer departureGareText = d.getStationIdByName(garedpart.getText());
        Integer arriveGareText = d.getStationIdByName(garearrive.getText());

     if (trainText.isEmpty() || departureGareText == 0 || arriveGareText == 0) {
    // System.out.println("One or more required fields are empty.");
    return; // Exit the method if any required field is empty
}


        int trainNumber = Integer.parseInt(trainText);
        String travelClass = classText;
        int departureGare = departureGareText;
        int arriveGare =arriveGareText;

        // Calculate price
        double price = calculatePrice(trainNumber, travelClass, departureGare, arriveGare);
//        System.out.println("Calculated Price: " + price);

        DecimalFormat dc = new DecimalFormat("#.##");
        
        // Display price
        Prix.setText(dc.format(price));
    } catch (NumberFormatException ex) {
        // Handle number format exception
        System.out.println("NumberFormatException occurred: " + ex.getMessage());
        ex.printStackTrace();
    } catch (SQLException ex) {
        // Handle SQL exception
        ex.printStackTrace();
    }
}
private double calculatePrice(int trainNumber, String travelClass, int departureGare, int arriveGare) throws SQLException {
    double price = 0.0;

    // Get total distance
    double totalDistance = getTotalDistanceFromTrainGareMapping(trainNumber, departureGare, arriveGare);

    // Get train speed
    int trainSpeed = getTrainSpeed(trainNumber);

    // Calculate basic price based on distance and speed
    price = ((trainSpeed * totalDistance) / 20) / 20;

    // If 1er class, add 20%
    if (travelClass.equalsIgnoreCase("1er class")) {
        price *= 1.20;
    }

    // Return the calculated price without any reduction
    return price;
}



    private void PrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrixActionPerformed

    }//GEN-LAST:event_PrixActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         TicketBeans.setObj().getInfo();
       try {
    HashMap<String, Object> param = new HashMap<>();
    
    // Get the value of 'numberOfInserts' from wherever it's coming
    int numberOfInserts = (Integer) Ticket.setObj().Nomber.getValue();
    
    // Put the parameter value into the map
    param.put("TopN", numberOfInserts);
    
    // Load the JasperReport template from the JRXML file
    JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\Ticket1.jrxml");
    
    // Compile the JasperReport template
    JasperReport js = JasperCompileManager.compileReport(jasdi);
    
    // Provide a database connection (replace 'con' with your actual Connection object)
    Connection con = ConnectioDB.getConnection();
    
    // Fill the JasperReport with data
    JasperPrint jd = JasperFillManager.fillReport(js, param, con);
    
    // Show the report in a JasperViewer
    JasperViewer.viewReport(jd, false);
} catch (JRException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
}

      
        
       // TicketBeans.setObj().getInfo();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void garearriveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_garearriveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_garearriveActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
  // Clearing text fields
        jTabbedPane3.setSelectedIndex(0);
  
        DefaultTableModel tbl = (DefaultTableModel) jTable1.getModel();
        tbl.setRowCount(0);
    Arrival.setText("");
//    Carte.setSelected(false); // Assuming you want to uncheck the checkbox
    Class.setText("");
    Depart.setText("");
 //   IDcarte.setText("");
    Marque.setText("");
 //   NomPrenom.setText("");
    Place.setText("");
    Prix.setText("");
    Train.setText("");
    classComboBox.setSelectedIndex(0); // Assuming you want to reset the combo box selection

    // Navigating back to the "Voyage" panel
voyage.setVisible(true);
dispose();
     

    }//GEN-LAST:event_jButton3ActionPerformed


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
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ticket.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ticket().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField Arrival;
    public javax.swing.JTextField Class;
    public javax.swing.JTextField Depart;
    public javax.swing.JTextField Marque;
    public javax.swing.JSpinner Nomber;
    public javax.swing.JTextField Place;
    public javax.swing.JTextField Prix;
    public javax.swing.JTextField Train;
    public javax.swing.JComboBox<String> classComboBox;
    public javax.swing.JTextField garearrive;
    public javax.swing.JTextField garedpart;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane3;
    public javax.swing.JTable jTable1;
    public javax.swing.JLabel l1;
    public javax.swing.JLabel l2;
    public javax.swing.JTextField mapping;
    private javax.swing.JPanel tab1;
    // End of variables declaration//GEN-END:variables
}
