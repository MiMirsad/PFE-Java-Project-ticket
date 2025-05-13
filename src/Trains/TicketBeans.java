/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Trains;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class TicketBeans extends GareDB {
   private int TrainNumber;
            private String Gare2;
           private String Gare1;
                    private String Prix;
                    private String numberOfTickets;
                            private String IDcarte;
                            private String Carte;
                                    private String Second;
                                   private String  First;
                                            private String  DateArrive;
                                              private String Datedepart;
                                              
                                              
                                              
                                              static TicketBeans obj;
                                              public static TicketBeans setObj(){
                                                  if(obj == null){
                                                      obj = new TicketBeans();
                                                  }
                                                  return obj;
                                              }
                                              

    public TicketBeans() {
    }

    public int getTrainNumber() {
        return TrainNumber;
    }

    public void setTrainNumber(int TrainNumber) {
        this.TrainNumber = TrainNumber;
    }

    public String getGare2() {
        return Gare2;
    }

    public void setGare2(String Gare2) {
        this.Gare2 = Gare2;
    }

    public String getGare1() {
        return Gare1;
    }

    public void setGare1(String Gare1) {
        this.Gare1 = Gare1;
    }

    public String getPrix() {
        return Prix;
    }

    public void setPrix(String Prix) {
        this.Prix = Prix;
    }

    public String getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(String numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public String getIDcarte() {
        return IDcarte;
    }

    public void setIDcarte(String IDcarte) {
        this.IDcarte = IDcarte;
    }

    public String getCarte() {
        return Carte;
    }

    public void setCarte(String Carte) {
        this.Carte = Carte;
    }

    public String getSecond() {
        return Second;
    }

    public void setSecond(String Second) {
        this.Second = Second;
    }

    public String getFirst() {
        return First;
    }

    public void setFirst(String First) {
        this.First = First;
    }

    public String getDateArrive() {
        return DateArrive;
    }

    public void setDateArrive(String DateArrive) {
        this.DateArrive = DateArrive;
    }

    public String getDatedepart() {
        return Datedepart;
    }

    public void setDatedepart(String Datedepart) {
        this.Datedepart = Datedepart;
    }

    public TicketBeans(int TrainNumber, String Gare2, String Gare1, String Prix, String numberOfTickets, String IDcarte, String Carte, String Second, String First, String DateArrive, String Datedepart) {
        this.TrainNumber = TrainNumber;
        this.Gare2 = Gare2;
        this.Gare1 = Gare1;
        this.Prix = Prix;
        this.numberOfTickets = numberOfTickets;
        this.IDcarte = IDcarte;
        this.Carte = Carte;
        this.Second = Second;
        this.First = First;
        this.DateArrive = DateArrive;
        this.Datedepart = Datedepart;
    }

    
//    public void getInfo() {
//    int a = 0;
//    Ticket d = new Ticket();
//    int numberOfInserts = (Integer) Ticket.setObj().Nomber.getValue();  // Get the number of inserts from the JSpinner
//    PreparedStatement ps = null;
//    
//    try {
//        
//        if(Ticket.setObj().classComboBox.getSelectedItem().equals("1er Class")){
//         ps = conx.prepareStatement("insert into voyage (Vplace, Mappigid, TrainN, TrainMarque, GareDepart, DateDepart, GareArrive, dateArrive, Vclass, idcart, nomprenom, prix, fktrain)"
//                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
//         
//        }else{
//            
//         ps = conx.prepareStatement("insert into voyage (Vclass2, Mappigid, TrainN, TrainMarque, GareDepart, DateDepart, GareArrive, dateArrive, Vclass, idcart, nomprenom, prix, fktrain)"
//                + " values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
//        }
//        
//        
//        
//        for (int i = 0; i < numberOfInserts; i++) {
//            ps.setString(1, Ticket.setObj().Place.getText());
//            ps.setString(2, Ticket.setObj().mapping.getText());
//            ps.setString(3, Ticket.setObj().Train.getText());
//            ps.setString(4, Ticket.setObj().Marque.getText());
//            ps.setString(5, Ticket.setObj().garedpart.getText());
//            ps.setString(6, Ticket.setObj().Depart.getText());
//            ps.setString(7, Ticket.setObj().garearrive.getText());
//            ps.setString(8, Ticket.setObj().Arrival.getText());
//            ps.setString(9, Ticket.setObj().Class.getText());
//            ps.setString(10, Ticket.setObj().IDcarte.getText());
//            ps.setString(11, Ticket.setObj().NomPrenom.getText());
//            ps.setString(12, Ticket.setObj().Prix.getText());
//            ps.setInt(13, Integer.parseInt(Ticket.setObj().Train.getText()));       
//
//            a += ps.executeUpdate();
//        }
//        
//    } catch (SQLException ex) {
//        Logger.getLogger(TicketBeans.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    
//    if (a == 1) {
//        JOptionPane.showMessageDialog(null, "bien ajouter");
//    }
//}

    
public void getInfo() {
    int a = 0;
    Ticket d = new Ticket();
    int numberOfInserts = (Integer) Ticket.setObj().Nomber.getValue();  // Get the number of inserts from the JSpinner
    PreparedStatement ps = null;
    String classType = Ticket.setObj().classComboBox.getSelectedItem().toString();
    int maxVplaceOrVclass2 = 0;
    String mappigid = Ticket.setObj().mapping.getText();
   
    try {
        // Fetch the maximum Vplace or Vclass2 value where Mappigid matches
        String queryMax = "";
        if (classType.equals("1er Class")) {
            queryMax = "SELECT Count(Vplace) AS maxVal FROM voyage WHERE Mappigid = ?";
        } else {
            queryMax = "SELECT Count(Vclass2) AS maxVal FROM voyage WHERE Mappigid = ?";
        }
        
        try (PreparedStatement psMax = conx.prepareStatement(queryMax)) {
            psMax.setString(1, mappigid);
            try (ResultSet rs = psMax.executeQuery()) {
                if (rs.next()) {
                    maxVplaceOrVclass2 = rs.getInt("maxVal");
                }
            }
        }
        
        // Prepare the insert statement based on the class type
        if (classType.equals("1er Class")) {
            ps = conx.prepareStatement("insert into voyage (Vplace, Mappigid, TrainN, TrainMarque, GareDepart, DateDepart, GareArrive, dateArrive, Vclass, prix, fktrain)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)");
        } else {
            ps = conx.prepareStatement("insert into voyage (Vclass2, Mappigid, TrainN, TrainMarque, GareDepart, DateDepart, GareArrive, dateArrive, Vclass, prix, fktrain)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?)");
        }

        for (int i = 0; i < numberOfInserts; i++) {
            if (classType.equals("1er Class")) {
                ps.setInt(1, maxVplaceOrVclass2 + 1);  // Increment Vplace
            } else {
                ps.setInt(1, maxVplaceOrVclass2 + 1);  // Increment Vclass2
            }
            ps.setString(2, mappigid);
            ps.setString(3, Ticket.setObj().Train.getText());
            ps.setString(4, Ticket.setObj().Marque.getText());
            ps.setString(5, Ticket.setObj().garedpart.getText());
            ps.setString(6, Ticket.setObj().Depart.getText());
            ps.setString(7, Ticket.setObj().garearrive.getText());
            ps.setString(8, Ticket.setObj().Arrival.getText());
            ps.setString(9, Ticket.setObj().Class.getText());
            ps.setString(10, Ticket.setObj().Prix.getText());
            ps.setInt(11, Integer.parseInt(Ticket.setObj().Train.getText()));       

            a += ps.executeUpdate();
            maxVplaceOrVclass2++;  // Increment the counter for the next insert
        }
        
    } catch (SQLException ex) {
        Logger.getLogger(TicketBeans.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    if (a == numberOfInserts) {
        JOptionPane.showMessageDialog(null, "bien ajouter");
    }
}




    
    
   
                                                    
}
