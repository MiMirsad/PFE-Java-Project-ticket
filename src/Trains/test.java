/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Trains;

import static Trains.GareDB.conx;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author Administrator
 */
public class test extends javax.swing.JFrame {
    private int trainNumber;
       private String gare1;
    private String gare2;
  private Connection conx;
    public test(int trainNumber, String gare1, String gare2) throws SQLException {
        initComponents();
        this.trainNumber = trainNumber;
        this.conx = ConnectioDB.getConnection();
        this.gare1 = gare1;
        this.gare2 = gare2;
        // second.setSelected(true);
         IDcarte.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed for this implementation
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Not needed for this implementation
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Fetch data from the database when the user releases a key
                loadAdherentDetails(IDcarte.getText());
            }
        });
   Carte.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
                IDcarte.setEnabled(isSelected);
//                perc.setEnabled(isSelected);
//                NomPrenom.setEnabled(isSelected);
            }
        });
     perc.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            updatePrice();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updatePrice();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            // Plain text components don't fire these events
        }
    });
    IDcarte.setEnabled(false);
        perc.setEnabled(false);
        NomPrenom.setEnabled(false);
        // Adding ItemListener to checkboxes to ensure only one can be checked at a time
        first.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    second.setSelected(false);
                }
            }
        });

        second.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    first.setSelected(false);
                }
            }
        });
    
        populateGareComboBoxes();
        populateTrainNumberComboBox();
        populateDates();
         first.addActionListener(e -> updatePrice());
    second.addActionListener(e -> updatePrice());
    numberOfTickets.addChangeListener(e -> updatePrice());
    TrainNumber.addActionListener(e -> updatePrice());
    Gare1.addActionListener(e -> updatePrice());
    Gare2.addActionListener(e -> updatePrice());
    Datedepart.addDateTimeChangeListener(e -> updatePrice());
    DateArrive.addDateTimeChangeListener(e -> updatePrice());
    
    }

    private void populateTrainNumberComboBox() {
        // Assuming TrainNumber is the name of the JComboBox
        TrainNumber.addItem(String.valueOf(trainNumber)); // Add the train number to the combobox
    }
     private void populateGareComboBoxes() {
        // Assuming Gare1 and Gare2 are the names of the JComboBoxes
        Gare1.addItem(gare1);
        Gare2.addItem(gare2);
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
                    String nomPrenomValue = nomAdhValue + " " + prenomAdhValue; // Concatenate NomAdh and PrenomAdh
                    NomPrenom.setText(nomPrenomValue);
                    String prc = resultSet.getString("RP");
                    perc.setText(prc);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Voyage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   public Integer getStationIdByName(String gareName) throws SQLException {
    if (conx == null) {
        System.err.println("Error: Database connection is null.");
        return null;
    }

    String query = "SELECT GareID FROM Gare WHERE GareName = ?";
    try (PreparedStatement ps = conx.prepareStatement(query)) {
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
   public test() {
        initComponents();
        Carte.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        updateCarteFields();
    }
});
         this.trainNumber = trainNumber;
        populateTrainNumberComboBox();
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
    }
private void populateDates() throws SQLException {
    Integer gare1Id = getStationIdByName(gare1);
    Integer gare2Id = getStationIdByName(gare2);

    if (gare1Id != null && gare2Id != null) {
        LocalDateTime departureDateTime = fetchDateTimeByGare(gare1Id);
        LocalDateTime arrivalDateTime = fetchDateTimeByGare(gare2Id);

        if (departureDateTime != null && arrivalDateTime != null) {
            Datedepart.setDateTimePermissive(departureDateTime);
            DateArrive.setDateTimePermissive(arrivalDateTime);
        } else {
            // Handle if dates couldn't be fetched
            System.out.println("Unable to fetch dates for selected stations.");
        }
    } else {
        // Handle if station IDs couldn't be fetched
        System.out.println("Unable to fetch station IDs for selected stations.");
    }
}
void populateArrivalDateTimePicker(int gareId) {
    LocalDateTime arrivalDateTime = fetchDateTimeByGare1(gareId);
    if (arrivalDateTime != null) {
        DateArrive.setDateTimePermissive(arrivalDateTime);
    } else {
        // Handle if arrival date couldn't be fetched
        System.out.println("Unable to fetch arrival date for selected station.");
    }
}

void populateDepartureDateTimePicker(int gareId) {
    LocalDateTime departureDateTime = fetchDateTimeByGare(gareId);
    if (departureDateTime != null) {
        Datedepart.setDateTimePermissive(departureDateTime);
    } else {
        // Handle if departure date couldn't be fetched
        System.out.println("Unable to fetch departure date for selected station.");
    }
}
private LocalDateTime fetchDateTimeByGare(int gareId) {
    try (Connection connection = ConnectioDB.getConnection()) {
        if (connection == null) {
            System.err.println("Error: Database connection is null.");
            return null;
        }
        
        String query = "SELECT DateDpart FROM TrainGareMapping WHERE GareN = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, gareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("DateDpart").toLocalDateTime();
                } else {
                    System.out.println("No data found for GareN: " + gareId);
                    return null;
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return null;
    }
}
private LocalDateTime fetchDateTimeByGare1(int gareId) {
    try (Connection connection = ConnectioDB.getConnection()) {
        if (connection == null) {
            System.err.println("Error: Database connection is null.");
            return null;
        }
        
        String query = "SELECT DateDpart FROM TrainGareMapping WHERE GareN = ?";
        try (PreparedStatement st = connection.prepareStatement(query)) {
            st.setInt(1, gareId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("DateDpart").toLocalDateTime();
                } else {
                    System.out.println("No data found for GareN: " + gareId);
                    return null;
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return null;
    }
}
 public boolean bookTicket(int trainId, int numberOfTickets, boolean isFirstSelected, boolean isSecondSelected) {
        try {
            Connection connection = ConnectioDB.getConnection();

            // Determine the ticket class based on the state of the checkboxes
            String ticketClass = determineTicketClass(isFirstSelected, isSecondSelected);

            // Retrieve current capacity of the train for the specified class
            int currentCapacity = getCurrentCapacity(connection, trainId, isFirstSelected, isSecondSelected);

            // Check if there are enough available seats
            if (currentCapacity >= numberOfTickets) {
                // Proceed with booking
                updateCapacity(connection, trainId, numberOfTickets, currentCapacity, ticketClass);
                return true; // Booking successful
            } else {
                // Not enough available seats
                return false; // Booking failed
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle database error
            return false; // Booking failed
        }
    }
  private String determineTicketClass(boolean isFirstSelected, boolean isSecondSelected) {
        if (isFirstSelected) {
            return "first";
        } else if (isSecondSelected) {
            return "second";
        } else {
            // Neither checkbox is selected, handle as needed
            return null; // Indicate unknown ticket class
        }
    }
private int getCurrentCapacity(Connection connection, int trainId, boolean isFirstSelected, boolean isSecondSelected) throws SQLException {
        if (!isFirstSelected && !isSecondSelected) {
            // Neither checkbox is selected, return 0 for unknown ticket class
            return 0;
        }

        String capacityColumn;
        if (isFirstSelected) {
            capacityColumn = "TrainplacesNum1cls";
        } else { // Second class selected
            capacityColumn = "TrainplacesNum2cls";
        }

        String query = "SELECT " + capacityColumn + " FROM Trains WHERE TrainNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, trainId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(capacityColumn);
                }
            }
        }
        return 0; // Return 0 if capacity not found
    }


private void updateCapacity(Connection connection, int trainId, int numberOfTickets, int currentCapacity, String ticketClass) throws SQLException {
        String capacityColumn = (ticketClass.equals("first")) ? "TrainplacesNum1cls" : "TrainplacesNum2cls";
        int newCapacity = currentCapacity - numberOfTickets;
        String query = "UPDATE Trains SET " + capacityColumn + " = ? WHERE TrainNumber = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, newCapacity);
            ps.setInt(2, trainId);
            ps.executeUpdate();
        }
    }
          private double calculateSpeedCharge(int trainNumber) {
        // Get train speed from database
        int trainSpeed = getTrainSpeed(trainNumber);

        // Example logic to calculate charge based on train speed
        if (trainSpeed > 100) {
            return 20; // Example charge for trains with speed greater than 100 km/h
        } else {
            return 0; // No additional charge
        }
    }
       private double calculateDistanceCharge(int trainNumber) {
        // Get distance from TrainGareMapping table
        double distance = getDistanceFromTrainGareMapping(trainNumber);

        // Example logic to calculate charge based on distance
        return distance * 0.1; 
    }
private double calculatePrice(boolean isFirstClass, int trainNumber) {
    // Calculate additional charges based on distance and train speed
    double distance = getDistanceFromTrainGareMapping(trainNumber);
    double trainSpeed = getTrainSpeed(trainNumber);

    // Calculate base price using the provided formula
    double basePrice = ((trainSpeed * distance) / 20) / 20;

    // Apply additional charge for first class
    if (isFirstClass) {
        basePrice *= 1.5; // Adjust the multiplier to set the desired percentage increase for first class
    } else {
        basePrice *= 1.2; // Adjust the multiplier to set the desired percentage increase for second class
    }

    // Apply reduction based on perc.text if it's not empty
    String percText = perc.getText().trim();
    if (!percText.isEmpty()) {
        try {
            double reduction = Double.parseDouble(percText);
            basePrice -= (basePrice * (reduction / 100.0)); // Subtract the reduction from the base price
        } catch (NumberFormatException ex) {
            // Handle the case where perc.text is not a valid number
            System.err.println("Invalid value for reduction percentage: " + percText);
        }
    }

    return basePrice;
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
private void updatePrice() {
    // Get values from input fields
    boolean isFirstClass = first.isSelected();
    int trainNumber = Integer.parseInt(TrainNumber.getSelectedItem().toString()); // Convert selected item to string and parse as integer
    
    // Fetch distance from the database
    double distance = getDistanceFromTrainGareMapping(trainNumber);
    
    // Calculate the price
    double price = calculatePrice(isFirstClass, trainNumber);
    
    // Update the Prix JTextField with the calculated price
    Prix.setText(String.valueOf(price));
}
     private double getDistanceFromTrainGareMapping(int trainNumber) {
        double distance = 0;
        try {
            Connection connection = ConnectioDB.getConnection();
            String query = "SELECT distanceKmNextGare FROM TrainGareMapping WHERE TrainN = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, trainNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        distance = rs.getDouble("distanceKmNextGare");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle database error
        }
        return distance;
    }
     private void updateCarteFields() {
    IDcarte.setEnabled(Carte.isSelected());
    perc.setEnabled(Carte.isSelected());
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TrainNumber = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Gare2 = new javax.swing.JComboBox<>();
        Datedepart = new com.github.lgooddatepicker.components.DateTimePicker();
        DateArrive = new com.github.lgooddatepicker.components.DateTimePicker();
        Prix = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Gare1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        numberOfTickets = new javax.swing.JSpinner();
        first = new javax.swing.JCheckBox();
        second = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        Carte = new javax.swing.JCheckBox();
        IDcarte = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        perc = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        NomPrenom = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setText("Train Number");
        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));

        TrainNumber.setBackground(new java.awt.Color(255, 255, 255));
        TrainNumber.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainNumber.setForeground(new java.awt.Color(0, 102, 102));

        jLabel2.setText("gare de départ");
        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("gare d'arrivée");
        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));

        Gare2.setBackground(new java.awt.Color(255, 255, 255));
        Gare2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Gare2.setForeground(new java.awt.Color(0, 102, 102));

        Datedepart.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        DateArrive.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        Prix.setBackground(new java.awt.Color(255, 255, 255));
        Prix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Prix.setForeground(new java.awt.Color(0, 102, 102));
        Prix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrixActionPerformed(evt);
            }
        });

        jLabel4.setText("Date de départ");
        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel5.setText("Date d'arrivée");
        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Prix");
        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));

        Gare1.setBackground(new java.awt.Color(255, 255, 255));
        Gare1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Gare1.setForeground(new java.awt.Color(0, 102, 102));

        jLabel7.setText("Nombre De Tickets");
        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));

        numberOfTickets.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        first.setText("1");
        first.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        first.setForeground(new java.awt.Color(255, 255, 255));

        second.setText("2");
        second.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        second.setForeground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Class");
        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Print");
        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel9.setText("Réduction Carte");
        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));

        Carte.setText("oui");
        Carte.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Carte.setForeground(new java.awt.Color(255, 255, 255));
        Carte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CarteActionPerformed(evt);
            }
        });

        IDcarte.setBackground(new java.awt.Color(255, 255, 255));
        IDcarte.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        IDcarte.setForeground(new java.awt.Color(0, 102, 102));

        jLabel10.setText("ID Réduction Carte");
        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));

        jLabel11.setText("Reduction %");
        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));

        perc.setBackground(new java.awt.Color(255, 255, 255));
        perc.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        perc.setForeground(new java.awt.Color(0, 102, 102));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nom et Prenom");

        NomPrenom.setBackground(new java.awt.Color(255, 255, 255));
        NomPrenom.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        NomPrenom.setForeground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(IDcarte)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(first, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(second, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(DateArrive, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Datedepart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TrainNumber, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numberOfTickets)
                    .addComponent(Carte, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Prix)
                    .addComponent(perc)
                    .addComponent(NomPrenom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(Gare1, 0, 119, Short.MAX_VALUE)
                        .addComponent(Gare2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(TrainNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(Gare1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Gare2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Datedepart, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DateArrive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(first)
                            .addComponent(second)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(Carte))))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IDcarte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(perc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(NomPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(numberOfTickets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Prix, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(64, 64, 64)
                .addComponent(jButton1)
                .addContainerGap())
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
String trainNumber = TrainNumber.getSelectedItem().toString();
String departureStation = Gare1.getSelectedItem().toString();
String arrivalStation = Gare2.getSelectedItem().toString();
String departureDate = Datedepart.getDateTimePermissive().toString();
String arrivalDate = DateArrive.getDateTimePermissive().toString();
String ticketClass = (first.isSelected()) ? "First Class" : "Second Class";
int numberOfTicketsInt = (int) numberOfTickets.getValue();
String totalPrice = Prix.getText();

// Input stream to load JasperReport file
//InputStream inputStream = null;

try {
    // Load the JasperReport
    InputStream inputStream = new FileInputStream("C:\\Project\\ONCF\\src\\oncf\\report4.jasper");
    JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);

    // Create a new HashMap to hold parameters for the report
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("TrainNumber", trainNumber);
    parameters.put("DepartureStation", departureStation);
    parameters.put("ArrivalStation", arrivalStation);
    parameters.put("DepartureDate", departureDate);
    parameters.put("ArrivalDate", arrivalDate);
    parameters.put("TicketClass", ticketClass);
    parameters.put("NumberOfTickets", numberOfTicketsInt);
    parameters.put("TotalPrice", totalPrice);

    // Compile the JasperPrint object using the JasperReport and parameters
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

    // Export the report to PDF file
    JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Project\\ONCF\\src\\oncf\\report.pdf");

    // Optionally, you can open or view the generated PDF file here

} catch (FileNotFoundException ex) {
    // Handle file not found exception
    ex.printStackTrace();
    Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
} catch (JRException ex) {
    // Handle JasperReports exception
    ex.printStackTrace();
    Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
} finally {
    // Close the input stream in a finally block to ensure it's always closed
    InputStream inputStream = null;
    if (inputStream != null) {
        try {
            inputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

 
    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void PrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrixActionPerformed

    }//GEN-LAST:event_PrixActionPerformed

    private void CarteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CarteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CarteActionPerformed

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
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new test().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox Carte;
    private com.github.lgooddatepicker.components.DateTimePicker DateArrive;
    private com.github.lgooddatepicker.components.DateTimePicker Datedepart;
    private javax.swing.JComboBox<String> Gare1;
    private javax.swing.JComboBox<String> Gare2;
    private javax.swing.JTextField IDcarte;
    private javax.swing.JTextField NomPrenom;
    private javax.swing.JTextField Prix;
    private javax.swing.JComboBox<String> TrainNumber;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox first;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSpinner numberOfTickets;
    private javax.swing.JTextField perc;
    private javax.swing.JCheckBox second;
    // End of variables declaration//GEN-END:variables
}
