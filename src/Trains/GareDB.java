package Trains;
import groovy.sql.Sql;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Administrator
 */
public class GareDB {
     public static Connection conx;
    public static GareDB base;
    public static  Statement st;
    public static PreparedStatement stt;
    public static  ResultSet rs;

 public void GareDB() {
    }

    public static void main() {
        GareDB D = new GareDB();
    }
 public static GareDB getCon() {
        if (base == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conx = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Oncf;integratedSecurity=true");
                st = conx.createStatement();
                base = new GareDB();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return base;
    }

 public static LocalDateTime fetchLastArrivalDateFromDatabase(int routeId, int gareId) throws SQLException {
        try (Connection connection = ConnectioDB.getConnection()) {
        String query = "SELECT TOP 1 DateArriveMapping FROM TrainGareMapping WHERE TrainN = ? AND GareN = ? ORDER BY DateArriveMapping DESC";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, routeId);
            ps.setInt(2, gareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getTimestamp("DateArriveMapping").toLocalDateTime();
                }
            }
        }
    }
    return LocalDateTime.MIN;
}
private void handleDatabaseException(SQLException ex) {
    // Handle the exception or log the error
    ex.printStackTrace();
    // You might want to log the exception to a logger or show a user-friendly error message
}
public int getHighestMappingID() throws SQLException {
    String query = "SELECT MAX(MappingID) AS maxID FROM TrainGareMapping";
    try (Statement stmt = conx.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        if (rs.next()) {
            return rs.getInt("maxID");
        } else {
            return 0; // If no records are found, return 0
        }
    }
}

public void AddGare(GaresBeans cb) {
    try {
        // Check if any of the fields are empty
     StringBuilder emptyFields = new StringBuilder("Veuillez remplir les champs suivants :\n");

     if (cb.getGareName().isEmpty()) {
        emptyFields.append("- Nom de la gare\n");
    }

    if (!cb.isTNR1() && !cb.isTNR0()) {
        emptyFields.append("- Checkbox TNR\n");
    }

    if (!cb.isAtlas1() && !cb.isAtlas0()) {
        emptyFields.append("- Checkbox Atlas\n");
    }
      if (emptyFields.length() > "Veuillez remplir les champs suivants :\n".length()) {
        JOptionPane.showMessageDialog(null, emptyFields.toString());
        return;
    }

        // Display a confirmation dialog
        int dialogResult = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment ajouter cette gare ?", "Confirmation", JOptionPane.YES_NO_OPTION);

    if (dialogResult == JOptionPane.YES_OPTION) {
            String sql = "INSERT INTO Gare (GareName, TNRSupp, AtlasSupp,  AlboraqSupp) VALUES (?, ?, ?,  ?)";
            PreparedStatement ps = conx.prepareStatement(sql);

            ps.setString(1, cb.getGareName());
            ps.setBoolean(2, cb.isTNR1());
            ps.setBoolean(3, cb.isAtlas1());
         //   ps.setBoolean(4, cb.isTerm1());
               ps.setBoolean(4, cb.isBoraq1());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Gare bien ajoutée");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la gare");
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public void insertPersonnelle(Personnelle personnelle) {
    try {
        // Check if any of the textboxes is empty with specific messages
        if (isEmpty(personnelle.getPersNom(), "Veuillez saisir le nom.") ||
            isEmpty(personnelle.getPersPrenom(), "Veuillez saisir le prénom.") ||
            isEmpty(personnelle.getUsername(), "Veuillez saisir le nom d'utilisateur.") ||
            isEmpty(personnelle.getPassword(), "Veuillez saisir le mot de passe.") ||
            isEmpty(personnelle.getPersVille(), "Veuillez saisir la ville.") ||
            isEmpty(personnelle.getPersAdress(), "Veuillez saisir l'adresse.") ||
            isEmpty(personnelle.getPersTel(), "Veuillez saisir le numéro de téléphone.") ||
            isEmpty(personnelle.getPersCin(), "Veuillez saisir le numéro CIN.") ||
            isEmpty(personnelle.getOption(), "Veuillez sélectionner le type.") ||
            isEmpty(personnelle.getSecurtiQuestion(), "Veuillez choisir la question de sécurité.") ||
            isEmpty(personnelle.getSecurtiAnwser(), "Veuillez saisir la réponse à la question de sécurité.")) {
            return;
        }

        // Check if the username already exists in the database
        String checkUsernameQuery = "SELECT COUNT(*) FROM Personnellee WHERE Username = ?";
        try (PreparedStatement checkUsernamePS = conx.prepareStatement(checkUsernameQuery)) {
            checkUsernamePS.setString(1, personnelle.getUsername());
            ResultSet resultSet = checkUsernamePS.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);

            if (count > 0) {
                JOptionPane.showMessageDialog(null, "Le nom d'utilisateur existe déjà. Veuillez choisir un nom d'utilisateur différent.");
                return; // Stop further execution if the username already exists
            }
        }

        // If the username doesn't exist and all textboxes are filled, proceed with the insertion
        int dialogResult = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment ajouter cette personne ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            String insertQuery = "INSERT INTO Personnellee (PersNom, PersPrenom, Username, Password, PersVille, PersAdress, PersTel, PersCin, Type, SecurtiQuestion, SecurtiAnwser) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conx.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, personnelle.getPersNom());
                ps.setString(2, personnelle.getPersPrenom());
                ps.setString(3, personnelle.getUsername());
                ps.setString(4, personnelle.getPassword());
                ps.setString(5, personnelle.getPersVille());
                ps.setString(6, personnelle.getPersAdress());
                ps.setString(7, personnelle.getPersTel());
                ps.setString(8, personnelle.getPersCin());
                ps.setString(9, personnelle.getOption());
                ps.setString(10, personnelle.getSecurtiQuestion());
                ps.setString(11, personnelle.getSecurtiAnwser());

                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    // Retrieve the generated PersID if needed
                    JOptionPane.showMessageDialog(null, "Personne ajoutée avec succès");
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la personne.");
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de la personne. Veuillez réessayer plus tard.\n" + ex.getMessage());
    }
}

private boolean isEmpty(String value, String message) {
    if (value == null || value.trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, message);
        return true;
    }
    return false;
}

public void AddTrain(TrainBeans cb) {
    StringBuilder emptyFields = new StringBuilder("Veuillez remplir les champs suivants :\n");
    boolean hasEmptyFields = false;

    // Check for empty fields
    if (String.valueOf(cb.getTrainNumber()).isEmpty()) {
        emptyFields.append("- Numéro de train\n");
        hasEmptyFields = true;
    }
    if (cb.getTrainMarque().isEmpty()) {
        emptyFields.append("- Marque de train\n");
        hasEmptyFields = true;
    }
    if (cb.getTrainType().isEmpty()) {
        emptyFields.append("- Type de train\n");
        hasEmptyFields = true;
    }
    if (String.valueOf(cb.getTrainCapacity()).isEmpty()) {
        emptyFields.append("- Places en 2ème classe\n");
        hasEmptyFields = true;
    }
    if (String.valueOf(cb.getTrainplacesNum1cls()).isEmpty()) {
        emptyFields.append("- Places en 1ère classe\n");
        hasEmptyFields = true;
    }
    if (cb.getTrainSpeed().isEmpty()) {
        emptyFields.append("- Vitesse du train\n");
        hasEmptyFields = true;
    }

    if (hasEmptyFields) {
        JOptionPane.showMessageDialog(null, emptyFields.toString(), "Champs manquants", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Check if TrainNumber is a valid positive integer
    if (!isPositiveInteger(String.valueOf(cb.getTrainNumber()))) {
        JOptionPane.showMessageDialog(null, "Le numéro de train n'est pas valide. Veuillez saisir un nombre entier positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if TrainSpeed is a valid positive number
    if (!isPositiveNumber(cb.getTrainSpeed())) {
        JOptionPane.showMessageDialog(null, "La vitesse du train n'est pas valide. Veuillez saisir un nombre positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if TrainNumber already exists
    String checkQuery = "SELECT COUNT(*) FROM Trains WHERE TrainNumber = ?";
    try (PreparedStatement checkPs = conx.prepareStatement(checkQuery)) {
        checkPs.setInt(1, cb.getTrainNumber());
        try (ResultSet rs = checkPs.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Le numéro de train existe déjà. Veuillez saisir un autre numéro de train.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Erreur lors de la vérification du numéro de train.", "Erreur", JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
        return;
    }

    // Insert the new train
    String insertQuery = "INSERT INTO Trains (TrainNumber, TrainMarque, TrainType, TrainplacesNum2cls, TrainplacesNum1cls, TrainSpeed) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = conx.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, cb.getTrainNumber());
        ps.setString(2, cb.getTrainMarque());
        ps.setString(3, cb.getTrainType());
        ps.setInt(4, cb.getTrainCapacity());
        ps.setInt(5, cb.getTrainplacesNum1cls());
        ps.setString(6, cb.getTrainSpeed());
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Train ajouté avec succès");
        } else {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du train.");
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du train. Veuillez vérifier les données saisies.", "Erreur", JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
    }
}
private boolean isPositiveInteger(String str) {
    
     return str.matches("\\d+") && Integer.parseInt(str) > 0;
}
   private boolean isPositiveNumber(String str) {
        try {
            double num = Double.parseDouble(str);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
// Method to check if an integer is a valid positive number

Map<String, Integer> gareIdMap = new HashMap<>();
// Add this method to your GareDB class or wherever appropriate
 public void populateGareIdMap() {
        gareIdMap.clear(); // Clear existing entries if needed

        String query = "SELECT GareName, GareID FROM Gare";

        try (Statement stmt = conx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String stationName = rs.getString("GareName");
                int gareId = rs.getInt("GareID");
                gareIdMap.put(stationName, gareId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Log or handle the exception appropriately
        }
        
    for (Map.Entry<String, Integer> entry : gareIdMap.entrySet()) {
        
    }
    }

int extractLineNumber(String line) {
    int lineNumber = 0;
    String numericPart = line.replaceAll("[^0-9]", "");

    try {
        lineNumber = Integer.parseInt(numericPart);
    } catch (NumberFormatException e) {
        e.printStackTrace(); // Log or print the exception
    }

    return lineNumber;
}
private boolean isStationIdExists(int gareId) {
    String query = "SELECT COUNT(*) FROM Gare WHERE GareID = ?";
        try (PreparedStatement ps = conx.prepareStatement(query)) {
            ps.setInt(1, gareId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or print the exception
        }
        return false;
    }
public void AddRDC(RedctionCradBeans cb) throws SQLException {
    int dialogResult = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment ajouter cette Adherent ?", "Confirmation", JOptionPane.YES_NO_OPTION);

    if (dialogResult == JOptionPane.YES_OPTION) {
        String sql = "INSERT INTO adhérent (NomAdh, PrenomAdh, RCT, RP, Img) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cb.getNomAdh());
            ps.setString(2, cb.getPrenomAdh());
            ps.setString(3, cb.getRCT());
            ps.setString(4, cb.getRP());
            ps.setString(5, cb.getImageUrl());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated PersID
                JOptionPane.showMessageDialog(null, "Adherent ajoutée avec succès");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de Adherent.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de Adherent: " + ex.getMessage());
        }
    }
}


public void RDC(RedctionCradBeans cb) throws SQLException {
    StringBuilder emptyFields = new StringBuilder("Veuillez remplir les champs suivants :\n");

    boolean hasEmptyFields = false;

    if (cb.getType().isEmpty()) {
        emptyFields.append("- Carte Type\n");
        hasEmptyFields = true;
    }

    if (cb.getPrc().isEmpty()) {
        emptyFields.append("- Prc\n");
        hasEmptyFields = true;
    }

    if (hasEmptyFields) {
        JOptionPane.showMessageDialog(null, emptyFields.toString(), "Champs manquants", JOptionPane.WARNING_MESSAGE);
    } else {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment ajouter cette Carte ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            String sql = "INSERT INTO CarteDeRemise (Typecart, Prc) VALUES(?, ?)";
            try (PreparedStatement ps = conx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cb.getType());
                ps.setString(2, cb.getPrc());
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    // Retrieve the generated PersID
                    JOptionPane.showMessageDialog(null, "Carte ajoutée avec succès");
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout de Carte.");
                }
            }
        }
    }
}

public void addRouteMappings(TrainGareMappin cb) throws SQLException {
    // Query to get the latest DateArriveMapping for the given TrainN
    String selectQuery = "SELECT TOP 1 DateArriveMapping FROM TrainGareMapping WHERE TrainN = ? ORDER BY DateArriveMapping DESC";
    Timestamp latestDateArriveMapping = null;

    try (PreparedStatement selectPs = conx.prepareStatement(selectQuery)) {
        selectPs.setInt(1, cb.getRoutId());
        ResultSet rs = selectPs.executeQuery();

        if (rs.next()) {
            latestDateArriveMapping = rs.getTimestamp("DateArriveMapping");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exceptions
    }

    // If there is a previous entry, check the time difference
    if (latestDateArriveMapping != null) {
        long timeDifference = cb.getDateArriveMapping().getTime() - latestDateArriveMapping.getTime();
        if (timeDifference < 30 * 60 * 1000) { // 30 minutes in milliseconds
            throw new SQLException("La différence de temps entre la nouvelle entrée et la dernière entrée est inférieure à 30 minutes..");
        }
    }

    // Insert the new mapping if the time difference condition is satisfied
    String insertQuery = "INSERT INTO TrainGareMapping (MappingID, TrainN, GareN, DateDpart, SequenceNumber, distanceKmNextGare, DateArriveMapping) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement ps = conx.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
        ps.setInt(1, cb.getMappingId());
        // Set TrainN
        ps.setInt(2, cb.getRoutId());
        // Set GareN
        ps.setInt(3, cb.getGareNom());
        // Set DateDpart
        ps.setTimestamp(4, cb.getDateDepartMapping());
        // Set SequenceNumber
        ps.setInt(5, cb.getNDS());
        // Set distanceKmNextGare
        ps.setInt(6, cb.getDstncKM());
        // Set DateArriveMapping
        ps.setTimestamp(7, cb.getDateArriveMapping());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exceptions
    }

    // After insertion, show a confirmation message (if needed)
}
public void deleteMappingByID(int mappingID) {
    String deleteQuery = "DELETE FROM [Oncf].[dbo].[TrainGareMapping] WHERE MappingID = ?";
    
    try (PreparedStatement ps = conx.prepareStatement(deleteQuery)) {
        ps.setInt(1, mappingID);
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected <= 0) {
            JOptionPane.showMessageDialog(null, "Aucune entrée avec MappingID = " + mappingID + " n'a été trouvée.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de l'entrée avec MappingID = " + mappingID + " : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

//public int getMaxSequenceNumber(int trainNumber) {
//    int maxSequenceNumber = 0;
//    try {
//        // Execute a query to get the maximum sequence number for the given trainNumber
//        String sql = "SELECT MAX(SequenceNumber) FROM TrainGareMapping WHERE TrainN = ?";
//         PreparedStatement ps = conx.prepareStatement(sql);{
//        ps.setInt(1, trainNumber);
//       ResultSet resultSet = ps.executeQuery();
//        
//        // Retrieve the maximum sequence number from the result set
//        if (resultSet.next()) {
//            maxSequenceNumber = resultSet.getInt(1);
//        }
//        
//        // Close resources
//       resultSet.close();
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        // Handle any exceptions
//    }
//    return maxSequenceNumber;
//}



public List<GaresBeans> getGarebyName(String gareName) {
    List<GaresBeans> myList = new ArrayList<>();

    try {
        rs = st.executeQuery("SELECT GareID, SequenceNumber, TrainN, TrainMarque, MappingID FROM Gare "
                + "JOIN TrainGareMapping ON GareID = GareN "
                + "JOIN Trains ON TrainNumber = TrainN "
                + "WHERE GareName LIKE '" + gareName + "';");

       while (rs.next()) {
                myList.add(new GaresBeans(rs.getInt("GareID"),rs.getInt("SequenceNumber"),rs.getInt("TrainN"),rs.getString("TrainMarque"),rs.getInt("MappingID")));
            }
    } catch (SQLException ex) {
        Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
    
}
    return myList;
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
//public void sadek(List<GaresBeans> list1, List<GaresBeans> list2, TicketUser tiket) {
//      try {
//   // Voyage d = new Voyage();
//    DefaultTableModel tbl = (DefaultTableModel) tiket.jTable1.getModel();
//    
//for (int i = 0; i < list1.size(); i++) {
//    rs = st.executeQuery("SELECT DateDpart, SequenceNumber, GareName, TrainN, TrainMarque, MappingID, " +
//            "(SELECT DateArriveMapping FROM TrainGareMapping WHERE TrainN = " + list2.get(i).getIdTtain() +
//            " AND GareN = " + list2.get(i).getIdGare() + " AND SequenceNumber = " + list2.get(i).getSequenceN() +
//            " AND MappingId=" + list2.get(i).getMappingID() + ") AS ArrivalGare2 " +
//            "FROM TrainGareMapping tgm " +
//            "LEFT JOIN Gare ON GareID LIKE GareN " +
//            "LEFT JOIN Trains ON TrainNumber = TrainN " +
//            "WHERE tgm.GareN = " + list1.get(i).getIdGare() + " AND SequenceNumber= " + list1.get(i).getSequenceN() +
//            " AND TrainN = " + list1.get(i).getIdTtain() + " AND MappingID=" + list1.get(i).getMappingID());
//
//    while (rs.next()) {
//        tbl.addRow(new Object[]{
//            rs.getTimestamp("DateDpart"),
//            rs.getTimestamp("ArrivalGare2"),
//            rs.getInt("SequenceNumber"),
//            rs.getString("GareName"),
//                   rs.getInt("TrainN"),
//                   rs.getInt("MappingID"),
//                   rs.getString("TrainMarque")
//                    
//        });
//    }
//}
//
//} catch (SQLException ex) {
//    Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
//}
//
//}
public void sadek(List<GaresBeans> list1, List<GaresBeans> list2, Ticket tiket) {

    try {
        // Voyage d = new Voyage();
        DefaultTableModel tbl = (DefaultTableModel) tiket.jTable1.getModel();
        List<Object[]> rows = new ArrayList<>(); // Temporary list to store rows

        for (int i = 0; i < list1.size(); i++) {
            rs = st.executeQuery("SELECT DateDpart, SequenceNumber, GareName, TrainN, TrainMarque, MappingID, " +
                    "(SELECT DateArriveMapping FROM TrainGareMapping WHERE TrainN = " + list2.get(i).getIdTtain() +
                    " AND GareN = " + list2.get(i).getIdGare() + " AND SequenceNumber = " + list2.get(i).getSequenceN() +
                    " AND MappingId=" + list2.get(i).getMappingID() + ") AS ArrivalGare2 " +
                    "FROM TrainGareMapping tgm " +
                    "LEFT JOIN Gare ON GareID LIKE GareN " +
                    "LEFT JOIN Trains ON TrainNumber = TrainN " +
                    "WHERE tgm.GareN = " + list1.get(i).getIdGare() + " AND SequenceNumber= " + list1.get(i).getSequenceN() +
                    " AND TrainN = " + list1.get(i).getIdTtain() + " AND MappingID=" + list1.get(i).getMappingID());

            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getTimestamp("DateDpart"),
                    rs.getTimestamp("ArrivalGare2"),
                    rs.getInt("SequenceNumber"),
                    rs.getString("GareName"),
                    rs.getInt("TrainN"),
                    rs.getInt("MappingID"),
                    rs.getString("TrainMarque")
                };
                rows.add(row);
            }
        }
        // Add sorted rows to the table model
        for (Object[] row : rows) {
            tbl.addRow(row);
        }

    } catch (SQLException ex) {
        Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
    }
}
public void sadek1(List<GaresBeans> list1, List<GaresBeans> list2, Ticket tiket) {
      try {
    //Voyage d = new Voyage();
    DefaultTableModel tbl = (DefaultTableModel) tiket.jTable1.getModel();
    
for (int i = 0; i < list1.size(); i++) {
    rs = st.executeQuery("SELECT DateDpart, SequenceNumber, GareName, TrainN, TrainMarque, MappingID, " +
            "(SELECT DateArriveMapping FROM TrainGareMapping WHERE TrainN = " + list1.get(i).getIdTtain() +
            " AND GareN = " + list1.get(i).getIdGare() + " AND SequenceNumber = " + list1.get(i).getSequenceN() +
            " AND MappingId=" + list1.get(i).getMappingID() + ") AS ArrivalGare2 " +
            "FROM TrainGareMapping tgm " +
            "LEFT JOIN Gare ON GareID LIKE GareN " +
            "LEFT JOIN Trains ON TrainNumber = TrainN " +
            "WHERE tgm.GareN = " + list2.get(i).getIdGare() + " AND SequenceNumber= " + list2.get(i).getSequenceN() +
            " AND TrainN = " + list2.get(i).getIdTtain() + " AND MappingID=" + list2.get(i).getMappingID());

    while (rs.next()) {
        tbl.addRow(new Object[]{
            rs.getTimestamp("DateDpart"),
            rs.getTimestamp("ArrivalGare2"),
            rs.getInt("SequenceNumber"),
            rs.getString("GareName"),
                   rs.getInt("TrainN"),
                   rs.getInt("MappingID"),
                   rs.getString("TrainMarque")
        });
    }
}

} catch (SQLException ex) {
    Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
}

}



public List<TrainInfo> getTrainInfoByCriteria(String trainNumber, String trainMarque) {
    List<TrainInfo> trainInfoList = new ArrayList<>();

    try {
        rs = st.executeQuery("SELECT TrainplacesNum2cls, TrainplacesNum1cls, TrainNumVoit2Cls, TrainNumVoit1Cls, TrainSpeed " +
                             "FROM trains " +
                             "WHERE TrainNumber = '" + trainNumber + "' AND TrainMarque = '" + trainMarque + "'");
        while (rs.next()) {
            TrainInfo trainInfo = new TrainInfo(
                rs.getInt("TrainplacesNum2cls"),
                rs.getInt("TrainplacesNum1cls"),
                rs.getInt("TrainNumVoit2Cls"),
                rs.getInt("TrainNumVoit1Cls")
            );
            trainInfoList.add(trainInfo);
        }
        rs.close();
    } catch (SQLException ex) {
        Logger.getLogger(GareDB.class.getName()).log(Level.SEVERE, null, ex);
    }
    return trainInfoList;
}




 
}
