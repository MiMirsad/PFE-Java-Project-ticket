package Trains;

    

import static Trains.GareDB.conx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConnectioDB {

    
     public  Statement st;
    public  ResultSet rs;
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Oncf;integratedSecurity=true");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectioDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return con;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Handle exception, log, or rethrow as needed
                e.printStackTrace();
            }
        }
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                // Handle exception, log, or rethrow as needed
                e.printStackTrace();
            }
        }
    }

    PreparedStatement prepareStatement(String sql) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}