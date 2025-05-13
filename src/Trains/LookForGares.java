package Trains;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.SwingUtilities;

import Trains.AdminPanel;
import static Trains.GareDB.conx;
import com.sun.tools.javac.util.List;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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
public class LookForGares extends javax.swing.JFrame {
   
public AdminPanel createAdminPanelInstance() {
        AdminPanel adminPanel =  new AdminPanel();
        return adminPanel;
}   

 private String originalGareID;
    private String originalGareName;
    private String originalTNR;
    private String originalAtlas1;
private int nb;
private int currentRecord;
     

  

  Connection con=null;
    Statement st=null;
    PreparedStatement pst=null;
    ResultSet rs=null;

     public LookForGares(AdminPanel originalAdminPanel) {
    initComponents();
    
    } 
     
    public LookForGares() {
         initComponents();
             GareID.setEditable(false);
        GareID.setFocusable(false);
        GareID.setBackground(Color.LIGHT_GRAY);
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
          try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Oncf;integratedSecurity=true");
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = st.executeQuery("SELECT * FROM Gare");
   showRecord();
            if (rs.next()) {
               GareID.setText(rs.getString("GareID"));
                GareName.setText(rs.getString("GareName"));
                
                 try {
int tnrSuppValue = rs.getInt("TNRSupp");
int AtlassuppValue = rs.getInt("Atlassupp");
int BoraqSuppValuer = rs.getInt("AlboraqSupp");
//int TermValueValue = rs.getInt("TermSupp");
if (tnrSuppValue == 1) {
    TNR.setText("Oui");
} else {
    TNR.setText("Non");
}
if (AtlassuppValue == 1) {
    Atlas.setText("Oui");
} else {
    Atlas.setText("Non");
}
if (BoraqSuppValuer == 1) {
    Boraq.setText("Oui");
} else {
    Boraq.setText("Non");
}
//if (TermValueValue == 1) {
//    Term.setText("Oui");
//} else {
//    Term.setText("Non");
//}

} catch (SQLException ex) {
    ex.printStackTrace();
}
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
    }

private void showRecord() {
        updateJTable();
        updateTextFields();
    }

    private void updateJTable() {
        try {
            try (PreparedStatement pst = con.prepareStatement("SELECT * FROM Gare", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ResultSet resultSet = pst.executeQuery()) {

                DefaultTableModel df = (DefaultTableModel) jTable3.getModel();
                df.setRowCount(0);

                while (resultSet.next()) {
                   
                    Vector<Object> rowVector = new Vector<>();
                    rowVector.add(resultSet.getString("GareID"));
                    rowVector.add(resultSet.getString("GareName"));
                    rowVector.add(resultSet.getInt("TNRSupp") == 1 ? "Oui" : "Non");
                    rowVector.add(resultSet.getInt("Atlassupp") == 1 ? "Oui" : "Non");
              //      rowVector.add(resultSet.getInt("TermSupp") == 1 ? "Oui" : "Non");
                    rowVector.add(resultSet.getInt("AlboraqSupp") == 1 ? "Oui":"Non");
                    

                    df.addRow(rowVector);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
private void loadTrainsFromDatabase() {
    String gareID = GareID.getText(); // Get GareID from the appropriate source
    
    try (PreparedStatement pst = con.prepareStatement("SELECT tr.TrainNumber, tr.TrainMarque " +
                                                       "FROM TrainGareMapping t " +
                                                       "JOIN Trains tr ON t.TrainN = tr.TrainNumber " +
                                                       "WHERE t.GareN=?")) {
        pst.setString(1, gareID);

        try (ResultSet rs = pst.executeQuery()) {
            DefaultTableModel trainModel = (DefaultTableModel) jTable1.getModel();
            trainModel.setRowCount(0); // Clear previous trains

            while (rs.next()) {
                String trainNumber = rs.getString("TrainNumber");
                String trainMarque = rs.getString("TrainMarque");
                trainModel.addRow(new Object[]{trainNumber, trainMarque});
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}



    private void updateTextFields() {
        try {
            try (PreparedStatement pst = con.prepareStatement("SELECT * FROM Gare", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ResultSet resultSet = pst.executeQuery()) {

                if (resultSet.absolute(currentRecord)) {
                    GareID.setText(resultSet.getString("GareID"));
                    GareName.setText(resultSet.getString("GareName"));
                    TNR.setText(resultSet.getInt("TNRSupp") == 1 ? "Oui" : "Non");
                    Atlas.setText(resultSet.getInt("Atlassupp") == 1 ? "Oui" : "Non");
                        Boraq.setText(resultSet.getInt("AlboraqSupp") == 1? "oui":"Non");
                 //   Term.setText(resultSet.getInt("TermSupp") == 1 ? "Oui" : "Non");
                
                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lookForGare = new javax.swing.JTextField();
        GareID = new javax.swing.JTextField();
        GareName = new javax.swing.JTextField();
        TNR = new javax.swing.JTextField();
        Atlas = new javax.swing.JTextField();
        Boraq = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        next = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        EditGare = new javax.swing.JButton();
        BtnLook = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Est-ce qu'il supporte le TNR ?");

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Est-ce qu'il supporte le Al Atlas ?");

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Nom De La Gare");

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Numéro de Gare");

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Est-ce qu'il supporte le AlBoraq?");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Recherech Par");

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Rechercher un Gare");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.setText("Retour");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 102, 102));
        jButton5.setText("Imprimer List");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 102, 102));
        jButton8.setText("Imprimer Ficher");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel22))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel18))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel19))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel23))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel5)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8)))))
                .addGap(597, 597, 597))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(105, 105, 105)
                .addComponent(jLabel5)
                .addGap(61, 61, 61)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton8))
                .addGap(64, 64, 64)
                .addComponent(jButton1))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 0, 420, 810));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lookForGare.setBackground(new java.awt.Color(0, 102, 102));
        lookForGare.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lookForGare.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(lookForGare, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 200, -1));

        GareID.setBackground(new java.awt.Color(0, 102, 102));
        GareID.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        GareID.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(GareID, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 192, -1));

        GareName.setBackground(new java.awt.Color(0, 102, 102));
        GareName.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        GareName.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(GareName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 192, -1));

        TNR.setBackground(new java.awt.Color(0, 102, 102));
        TNR.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TNR.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(TNR, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 192, -1));

        Atlas.setBackground(new java.awt.Color(0, 102, 102));
        Atlas.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Atlas.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(Atlas, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 192, -1));

        Boraq.setBackground(new java.awt.Color(0, 102, 102));
        Boraq.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Boraq.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(Boraq, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 192, -1));

        jButton7.setBackground(new java.awt.Color(0, 102, 102));
        jButton7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText(">>");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 460, 150, -1));

        jButton6.setBackground(new java.awt.Color(0, 102, 102));
        jButton6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText(">");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, 150, -1));

        next.setBackground(new java.awt.Color(0, 102, 102));
        next.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        next.setForeground(new java.awt.Color(255, 255, 255));
        next.setText("<");
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });
        jPanel1.add(next, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 360, 150, -1));

        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("<<");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 310, 150, -1));

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Supprimer");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 260, 150, -1));

        EditGare.setBackground(new java.awt.Color(0, 102, 102));
        EditGare.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        EditGare.setForeground(new java.awt.Color(255, 255, 255));
        EditGare.setText("Modifier");
        EditGare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditGareActionPerformed(evt);
            }
        });
        jPanel1.add(EditGare, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 210, 150, -1));

        BtnLook.setBackground(new java.awt.Color(0, 102, 102));
        BtnLook.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtnLook.setForeground(new java.awt.Color(255, 255, 255));
        BtnLook.setText("Rechercher");
        BtnLook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLookActionPerformed(evt);
            }
        });
        jPanel1.add(BtnLook, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, -1, -1));

        jTable3.setBackground(new java.awt.Color(0, 102, 102));
        jTable3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jTable3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jTable3.setForeground(new java.awt.Color(0, 0, 0));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Numéro", "Nom", "TNR Support", "tlas Support", "Al boraq Support"
            }
        ));
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, 440, 150));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Train Numero", "Train Marque"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 570, 360, 140));

        jButton9.setText("imprimer");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, 860, 720));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
try {
    rs = st.executeQuery("select * from Gare");

    if (rs.last()) {
        GareID.setText(rs.getString(1));
        GareName.setText(rs.getString(2));
        int tnrValue = rs.getInt(3);
        TNR.setText(tnrValue == 1 ? "Oui" : "Non");
        int AtlasValue = rs.getInt(4);
        Atlas.setText(AtlasValue == 1 ? "Oui" : "Non");
        int BoraqValue = rs.getInt(5);
        Boraq.setText(BoraqValue == 1 ? "Oui":"Non");
        int TermValue = rs.getInt(5);
 //       Term.setText(TermValue == 1 ? "Oui" : "Non");
 

        JOptionPane.showMessageDialog(this, "Vous avez atteint le dernier résultat.");
    } else {  
        JOptionPane.showMessageDialog(this, "Aucun résultat trouvé.");
    }
} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
}
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
try {
            if(!rs.isLast()){
                rs.next();
              GareID.setText(rs.getString(1));
        GareName.setText(rs.getString(2));
        int tnrValue = rs.getInt(3);
        TNR.setText(tnrValue == 1 ? "Oui" : "Non");
        int AtlasValue = rs.getInt(4);
        Atlas.setText(AtlasValue == 1 ? "Oui" : "Non");
        int BoraqValue = rs.getInt(5);
        Boraq.setText(BoraqValue == 1 ? "Oui":"Non");
        int TermValue = rs.getInt(5);
  //      Term.setText(TermValue == 1 ? "Oui" : "Non");
   

            }
        } catch (SQLException ex) {
            Logger.getLogger(RechercherunTrain.class.getName()).log(Level.SEVERE, null, ex);
        }                         
    }//GEN-LAST:event_jButton6ActionPerformed

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
try {
    if(!rs.isFirst()){
                rs.previous();
          GareID.setText(rs.getString(1));
        GareName.setText(rs.getString(2));
        int tnrValue = rs.getInt(3);
        TNR.setText(tnrValue == 1 ? "Oui" : "Non");
        int AtlasValue = rs.getInt(4);
        Atlas.setText(AtlasValue == 1 ? "Oui" : "Non");
        int BoraqValue = rs.getInt(5);
        Boraq.setText(BoraqValue == 1 ? "Oui":"Non");
        int TermValue = rs.getInt(5);
   //     Term.setText(TermValue == 1 ? "Oui" : "Non");
    }

} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
}

    }//GEN-LAST:event_nextActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
try {
    rs = st.executeQuery("SELECT * FROM Gare");

    if (rs.first()) {
        GareID.setText(rs.getString("GareID"));
        GareName.setText(rs.getString("GareName"));

        try {
            int tnrSuppValue = rs.getInt("TNRSupp");
            int AtlasSuppValue = rs.getInt("AtlasSupp");
            int BoraqSuppValue = rs.getInt("AlboraqSupp");
            int TermValue = rs.getInt("TermSupp");
//            int LigneValue = rs.getInt("Ligne");
            // Check the value and set the text accordingly
            TNR.setText(tnrSuppValue == 1 ? "Oui" : "Non");
            Atlas.setText(AtlasSuppValue == 1 ? "Oui" : "Non");
            Boraq.setText(BoraqSuppValue == 1 ? "Oui":"Non" );
   //     Term.setText(TermValue == 1 ? "Oui" : "Non");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

      
        JOptionPane.showMessageDialog(this, "Vous avez atteint le premier résultat.");
    } else {
        
        JOptionPane.showMessageDialog(this, "Aucun résultat trouvé.");
    }
} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
}

   
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
   try {
        int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet enregistrement?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

        if (confirmDialogResult == JOptionPane.YES_OPTION) {
            // Check if the Gare is currently in use in related tables
            String checkQuery = "SELECT COUNT(*) FROM TrainGareMapping WHERE GareN=?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, GareID.getText());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Gare is in use, show a message and do not proceed with deletion
                        JOptionPane.showMessageDialog(this, "Cette gare est actuellement en service et ne peut pas être supprimée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Proceed with deletion if the Gare is not in use
            try (PreparedStatement pst = con.prepareStatement("DELETE FROM Gare WHERE GareID=?")) {
                pst.setString(1, GareID.getText());
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    showRecord(); // Update the displayed records
                    JOptionPane.showMessageDialog(this, "Enregistrement supprimé avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucune gare trouvée avec cet ID.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Suppression annulée.");
        }
    } catch (SQLException ex) {
        Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de la gare.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed


private void loadRecordFromDatabase(String gareID) {
    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM Gare WHERE GareID=?")) {
        pst.setString(1, gareID);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                GareName.setText(rs.getString("GareName"));
                TNR.setText(rs.getString("TNRSupp"));
                Atlas.setText(rs.getString("AtlasSupp"));
             //   Term.setText(rs.getString("TermSupp"));
                Boraq.setText(rs.getString("AlboraqSupp"));
                originalGareID = rs.getString("GareID");
                GareID.setText(originalGareID);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    private void EditGareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditGareActionPerformed
   try {
        if (GareName.getText().isEmpty() || TNR.getText().isEmpty() || Atlas.getText().isEmpty() || Boraq.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs avant de procéder à la mise à jour.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
            return;
        }

//        // Check if GareID has been changed
//        if (!GareID.getText().equals(originalGareID)) {
//            JOptionPane.showMessageDialog(this, "Le GareID ne peut pas être modifié.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
//            GareID.setText(originalGareID); // Reset to original value
//            return;
//        }

        // Validate fields
        int validatedTNR = validateOuiNon(TNR.getText(), "TNR");
        if (validatedTNR == -1) return;

        int validatedAtlas = validateOuiNon(Atlas.getText(), "Atlas");
        if (validatedAtlas == -1) return;

        int validatedBoraq = validateOuiNon(Boraq.getText(), "Boraq");
        if (validatedBoraq == -1) return;

//        int validatedTerm = validateOuiNon(Term.getText(), "Gare de terminus");
//        if (validatedTerm == -1) return;

        int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment mettre à jour cet enregistrement?", "Confirmation de mise à jour", JOptionPane.YES_NO_OPTION);

        if (confirmDialogResult == JOptionPane.YES_OPTION) {
            try (PreparedStatement pst = con.prepareStatement("UPDATE Gare SET GareName=?, TNRSupp=?, AtlasSupp=?, AlboraqSupp=? WHERE GareID=?")) {
                pst.setString(1, GareName.getText());
                pst.setInt(2, validatedTNR);
                pst.setInt(3, validatedAtlas);
             //   pst.setInt(4, validatedTerm);
                pst.setInt(4, validatedBoraq);
                pst.setString(5, GareID.getText()); // Use the original GareID for the WHERE clause only

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Mise à jour réussie!");
                    showRecord(); // Update the displayed records
                    updateTextFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Mise à jour a échoué!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mise à jour annulée.");
        }
    } catch (SQLException | NumberFormatException ex) {
        Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
    }


        // TODO add your handling code here:
    }//GEN-LAST:event_EditGareActionPerformed
private int validateOuiNon(String text, String fieldName) {
    if (text.equalsIgnoreCase("Oui") || text.equalsIgnoreCase("Oi") || text.equalsIgnoreCase("O") || text.equalsIgnoreCase("Ou")) {
        return 1;
    } else if (text.equalsIgnoreCase("Non") || text.equalsIgnoreCase("No") || text.equalsIgnoreCase("N")) {
        return 0;
    } else {
        JOptionPane.showMessageDialog(this, "Veuillez entrer une valeur valide pour " + fieldName + " (Oui/Non).", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
        return -1;
    }
}
    private void BtnLookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLookActionPerformed
 DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable3.setRowSorter(sorter);

    String searchText = lookForGare.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

    sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
        @Override
        public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
            for (int i = 0; i < entry.getValueCount(); i++) {
                // Check if any column value contains the search text
                if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                    return true;
                }
            }
            return false;
        }
    });
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnLookActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        loadTrainsFromDatabase();
        int selectedrow=jTable3.getSelectedRow();

GareID.setText((String) jTable3.getValueAt(selectedrow, 0));
GareName.setText((String) jTable3.getValueAt(selectedrow, 1));
TNR.setText((String) jTable3.getValueAt(selectedrow, 2));
Atlas.setText((String) jTable3.getValueAt(selectedrow, 3));
Boraq.setText((String)jTable3.getValueAt(selectedrow, 4));
//Term.setText((String) jTable3.getValueAt(selectedrow, 5));
       
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      
this.setVisible(false);
AdminPanel AdminPanel = new AdminPanel();
AdminPanel.setVisible(false);
// or however you create an instance of AdminPanel
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
  int[] selectedRows = jTable3.getSelectedRows();

    try {
        // Load the JasperReport template
        JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\GareList.jrxml");

        // Construct the SQL query dynamically based on the selected rows
        StringBuilder sqlBuilder = new StringBuilder("SELECT *, " +
                "CASE WHEN [TNRSupp] = 1 THEN 'oui' ELSE 'Non' END AS TNRSuppText, " +
                "CASE WHEN [AtlasSupp] = 1 THEN 'oui' ELSE 'Non' END AS AtlasSuppText, " +
               
                "CASE WHEN [AlboraqSupp] = 1 THEN 'oui' ELSE 'Non' END AS AlboraqSuppText " +
                "FROM Gare");

        if (selectedRows.length > 0) {
            sqlBuilder.append(" WHERE ");  // Append WHERE clause if rows are selected
            for (int i = 0; i < selectedRows.length; i++) {
                String id = jTable3.getValueAt(selectedRows[i], 0).toString(); // Assuming the first column contains the GareID
                sqlBuilder.append("GareID = '").append(id).append("'");
                if (i < selectedRows.length - 1) {
                    sqlBuilder.append(" OR ");
                }
            }
        }

        // Set the constructed SQL query
        String sql = sqlBuilder.toString();
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jasdi.setQuery(newQuery);

        // Compile the JasperReport template
        JasperReport js = JasperCompileManager.compileReport(jasdi);

        // Provide a database connection (replace 'con' with your actual Connection object)
        Connection con = ConnectioDB.getConnection();

        // Fill the JasperReport with data
        JasperPrint jd = JasperFillManager.fillReport(js, null, con);

        // Show the report in a JasperViewer
        JasperViewer.viewReport(jd,false);
    } catch (JRException ex) {
        Logger.getLogger(LookForPers.class.getName()).log(Level.SEVERE, null, ex);
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
   jTable3.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked
   jTable3.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
      int[] selectedRows = jTable3.getSelectedRows();

    try {
        // Check if any row is selected
        if (selectedRows.length == 0) {
            // If nothing is selected, show a message
            JOptionPane.showMessageDialog(this, "Aucun élément sélectionné.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Load the JasperReport template
        JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\FicherGare.jrxml");

        // Construct the SQL query dynamically based on the selected rows
        StringBuilder sqlBuilder = new StringBuilder("SELECT *, " +
                "CASE WHEN [TNRSupp] = 1 THEN 'oui' ELSE 'Non' END AS TNRSuppText, " +
                "CASE WHEN [AtlasSupp] = 1 THEN 'oui' ELSE 'Non' END AS AtlasSuppText, " +
                "CASE WHEN [AlboraqSupp] = 1 THEN 'oui' ELSE 'Non' END AS AlboraqSuppText " +
                "FROM Gare " +
                "WHERE ");

        for (int i = 0; i < selectedRows.length; i++) {
            String id = jTable3.getValueAt(selectedRows[i], 0).toString(); // Assuming the first column contains the GareID
            sqlBuilder.append("GareID = '").append(id).append("'");
            if (i < selectedRows.length - 1) {
                sqlBuilder.append(" OR ");
            }
        }

        // Set the constructed SQL query
        String sql = sqlBuilder.toString();
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jasdi.setQuery(newQuery);

        // Compile the JasperReport template
        JasperReport js = JasperCompileManager.compileReport(jasdi);

        // Provide a database connection (replace 'con' with your actual Connection object)
        Connection con = ConnectioDB.getConnection();

        // Fill the JasperReport with data
        JasperPrint jd = JasperFillManager.fillReport(js, null, con);

        // Show the report in a JasperViewer
        JasperViewer.viewReport(jd, false);
    } catch (JRException ex) {
        Logger.getLogger(LookForPers.class.getName()).log(Level.SEVERE, null, ex);
    }   // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
 HashMap<String, Object> param = new HashMap<>();

DefaultTableModel tbl = (DefaultTableModel) jTable3.getModel();
int row = jTable3.getSelectedRow();

if (row >= 0) {
    
    // Assuming 'GareName' is in the second column (index 1) of the table
    String gareName = String.valueOf(tbl.getValueAt(row, 1));
    param.put("s", gareName);

    try {
        JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\Garettrain.jrxml");

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

} else {
    JOptionPane.showMessageDialog(null, "selectionner une ligne dans le tableau");
}

        
        

    }//GEN-LAST:event_jButton9ActionPerformed

    
    

    /**
     * @param args the command line arguments
     */
  public static void main(String args[]) {
    SwingUtilities.invokeLater(() -> {
        LookForGares lookForGares = new LookForGares();
        lookForGares.setVisible(true);
    });
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Atlas;
    private javax.swing.JTextField Boraq;
    private javax.swing.JButton BtnLook;
    private javax.swing.JButton EditGare;
    private javax.swing.JTextField GareID;
    private javax.swing.JTextField GareName;
    private javax.swing.JTextField TNR;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField lookForGare;
    private javax.swing.JButton next;
    // End of variables declaration//GEN-END:variables
}
