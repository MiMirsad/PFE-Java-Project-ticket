/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Trains;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import jdk.internal.org.jline.utils.Display;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Administrator
 */
public class LookForPers extends javax.swing.JFrame {
//      private AdminPanel adminPanel;
    private String originalPersID;
      private JasperViewer jasperViewer;
    private String originalGareID;
    private String originalGareName;
    private String originalTNR;
    private String originalAtlas1;
    private int nb;
    private int currentRecord;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
Statement st=null;
    /**
     * Creates new form LookForPers
     */
//    public LookForPers(AdminPanel adminPanel) {
//    initComponents();
//    this.adminPanel = adminPanel;
//}

    public LookForPers(){
        initComponents();
            PersID.setEditable(false);
        PersID.setFocusable(false);
        PersID.setBackground(Color.LIGHT_GRAY);
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
        // Additional initialization if needed
        
 Connection connection = ConnectioDB.getConnection();
       con = ConnectioDB.getConnection();
        try {
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM Personnellee")) {
showRecord();
                if (rs.next()) {
                    // Update Swing components on the EDT
                    PersID.setText(rs.getString("PersID"));
                    PersNom.setText(rs.getString("PersNom"));
                    PersPrenom.setText(rs.getString("PersPrenom"));
                    Username.setText(rs.getString("Username"));
                    Password.setText(rs.getString("Password"));
                    PersVille.setText(rs.getString("PersVille"));
                    PersAdress.setText(rs.getString("PersAdress"));
                    PersTel.setText(rs.getString("PersTel"));
                    PersCin.setText(rs.getString("PersCin"));

                    String typeFromDatabase = rs.getString("Type");
                    for (int i = 0; i < Option.getItemCount(); i++) {
                        if (Option.getItemAt(i).equals(typeFromDatabase)) {
                            Option.setSelectedIndex(i);
                            break; // Stop the loop once a match is found
                        }
                    }
                     String typeFromDatabase1 = rs.getString("SecurtiQuestion");
                    for (int i = 0; i < SecurtiQuestion.getItemCount(); i++) {
                        if (SecurtiQuestion.getItemAt(i).equals(typeFromDatabase1)) {
                            SecurtiQuestion.setSelectedIndex(i);
                            break; // Stop the loop once a match is found
                        }
                    }
                   SecurtiAnwser.setText(rs.getString("SecurtiAnwser")); 
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LookForPers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
private void showRecord() {
        updateJTable();
        updateTextFields();
    }
private void updateJTable() {
    
        try {
        try (PreparedStatement pst = con.prepareStatement("SELECT * FROM Personnellee", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = pst.executeQuery()) {

            DefaultTableModel df = (DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);

            while (resultSet.next()) {
                Vector<Object> rowVector = new Vector<>();
                rowVector.add(resultSet.getInt("PersID"));
                rowVector.add(resultSet.getString("PersNom"));
                rowVector.add(resultSet.getString("PersPrenom"));
                rowVector.add(resultSet.getString("Username"));
                rowVector.add(resultSet.getString("Password"));
                rowVector.add(resultSet.getString("PersVille"));
                rowVector.add(resultSet.getString("PersAdress"));
                rowVector.add(resultSet.getString("PersTel"));
                rowVector.add(resultSet.getString("PersCin"));
                rowVector.add(resultSet.getString("Type"));
                rowVector.add(resultSet.getString("SecurtiQuestion"));
                rowVector.add(resultSet.getString("SecurtiAnwser"));
                
                df.addRow(rowVector);
            }

            // Notify JTable to refresh its view
            ((DefaultTableModel) jTable1.getModel()).fireTableDataChanged();
        }
    } catch (SQLException ex) {
        Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

 private void updateTextFields() {
        try {
            try (PreparedStatement pst = con.prepareStatement("SELECT * FROM Personnellee", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ResultSet resultSet = pst.executeQuery()) {

                if (resultSet.absolute(currentRecord)) {
                    PersID.setText(rs.getString("PersID"));
                PersNom.setText(rs.getString("PersNom"));
                PersPrenom.setText(rs.getString("PersPrenom"));
                Username.setText(rs.getString("Username"));
                Password.setText(rs.getString("Password"));
                PersVille.setText(rs.getString("PersVille"));
                PersAdress.setText(rs.getString("PersAdress"));
                PersTel.setText(rs.getString("PersTel"));
                PersCin.setText(rs.getString("PersCin"));
           String typeFromDatabase = rs.getString("Type");
           String typeFromDatabase1 = rs.getString("SecurtiQuestion");
                SecurtiAnwser.setText(rs.getString("SecurtiAnwser")); 
           
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Delete = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        next2 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        BtnLook2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        PersNom = new javax.swing.JTextField();
        PersPrenom = new javax.swing.JTextField();
        Username = new javax.swing.JTextField();
        PersVille = new javax.swing.JTextField();
        PersAdress = new javax.swing.JTextField();
        PersTel = new javax.swing.JTextField();
        PersCin = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Option = new javax.swing.JComboBox<>();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        PersID = new javax.swing.JTextField();
        SecurtiQuestion = new javax.swing.JComboBox<>();
        SecurtiAnwser = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lookForPers = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Delete.setText("Supprimer");
        Delete.setBackground(new java.awt.Color(0, 102, 102));
        Delete.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Delete.setForeground(new java.awt.Color(255, 255, 255));
        Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteActionPerformed(evt);
            }
        });
        jPanel1.add(Delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 160, -1));

        Edit.setBackground(new java.awt.Color(0, 102, 102));
        Edit.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Edit.setForeground(new java.awt.Color(255, 255, 255));
        Edit.setText("Modifier");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });
        jPanel1.add(Edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 160, -1));

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Rotour");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 590, 131, -1));

        next2.setBackground(new java.awt.Color(0, 102, 102));
        next2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        next2.setForeground(new java.awt.Color(255, 255, 255));
        next2.setText("<");
        next2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                next2ActionPerformed(evt);
            }
        });
        jPanel1.add(next2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 150, -1));

        jButton13.setBackground(new java.awt.Color(0, 102, 102));
        jButton13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("<<");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 150, -1));

        jButton11.setText(">>");
        jButton11.setBackground(new java.awt.Color(0, 102, 102));
        jButton11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 150, 24));

        jButton12.setBackground(new java.awt.Color(0, 102, 102));
        jButton12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText(">");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 150, -1));

        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Imprimer");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 690, 131, -1));

        BtnLook2.setBackground(new java.awt.Color(0, 102, 102));
        BtnLook2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtnLook2.setForeground(new java.awt.Color(255, 255, 255));
        BtnLook2.setText("Rechercher");
        BtnLook2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLook2ActionPerformed(evt);
            }
        });
        jPanel1.add(BtnLook2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 160, -1));

        jButton5.setBackground(new java.awt.Color(0, 102, 102));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Imprimer ficher");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(41, 640, 130, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 0, 420, 880));

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PersNom.setBackground(new java.awt.Color(255, 255, 255));
        PersNom.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        PersNom.setForeground(new java.awt.Color(0, 102, 102));
        PersNom.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(PersNom, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 130, 324, -1));

        PersPrenom.setBackground(new java.awt.Color(255, 255, 255));
        PersPrenom.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        PersPrenom.setForeground(new java.awt.Color(0, 102, 102));
        PersPrenom.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(PersPrenom, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 179, 324, -1));

        Username.setBackground(new java.awt.Color(255, 255, 255));
        Username.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        Username.setForeground(new java.awt.Color(0, 102, 102));
        Username.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(Username, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 228, 324, -1));

        PersVille.setBackground(new java.awt.Color(255, 255, 255));
        PersVille.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        PersVille.setForeground(new java.awt.Color(0, 102, 102));
        PersVille.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(PersVille, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 326, 324, -1));

        PersAdress.setBackground(new java.awt.Color(255, 255, 255));
        PersAdress.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        PersAdress.setForeground(new java.awt.Color(0, 102, 102));
        PersAdress.setCaretColor(new java.awt.Color(0, 102, 102));
        PersAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PersAdressActionPerformed(evt);
            }
        });
        jPanel5.add(PersAdress, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 375, 324, -1));

        PersTel.setBackground(new java.awt.Color(255, 255, 255));
        PersTel.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        PersTel.setForeground(new java.awt.Color(0, 102, 102));
        PersTel.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(PersTel, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 424, 324, -1));

        PersCin.setBackground(new java.awt.Color(255, 255, 255));
        PersCin.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        PersCin.setForeground(new java.awt.Color(0, 102, 102));
        PersCin.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(PersCin, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 473, 324, -1));

        Password.setBackground(new java.awt.Color(255, 255, 255));
        Password.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        Password.setForeground(new java.awt.Color(0, 102, 102));
        Password.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(Password, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 277, 324, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nom");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Prenom");
        jPanel5.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("UserName");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Password");
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Ville");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, -1, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Adresse");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Telephone");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, -1, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("participants du système");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 520, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("C.I.N");
        jPanel5.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 480, -1, -1));

        Option.setBackground(new java.awt.Color(255, 255, 255));
        Option.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        Option.setForeground(new java.awt.Color(0, 102, 102));
        Option.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Agent", " " }));
        jPanel5.add(Option, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 522, 330, -1));

        jCheckBox1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Afficher le Mot de Passe");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel5.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(619, 287, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("ID");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        PersID.setBackground(new java.awt.Color(255, 255, 255));
        PersID.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        PersID.setForeground(new java.awt.Color(0, 102, 102));
        PersID.setCaretColor(new java.awt.Color(0, 102, 102));
        PersID.setDisabledTextColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(PersID, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 324, -1));

        SecurtiQuestion.setBackground(new java.awt.Color(255, 255, 255));
        SecurtiQuestion.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        SecurtiQuestion.setForeground(new java.awt.Color(0, 102, 102));
        SecurtiQuestion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quelle est votre date de naissance?", "Quel était le nom de votre professeur d’école préféré ?", "Quel est votre film préféré?", "Quel était votre première voiture?", "Quel est ton signe astrologique?", " " }));
        jPanel5.add(SecurtiQuestion, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 562, 330, -1));

        SecurtiAnwser.setBackground(new java.awt.Color(255, 255, 255));
        SecurtiAnwser.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        SecurtiAnwser.setForeground(new java.awt.Color(0, 102, 102));
        jPanel5.add(SecurtiAnwser, new org.netbeans.lib.awtextra.AbsoluteConstraints(283, 602, 324, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("questions de sécurité");
        jPanel5.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 560, -1, -1));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("questions Reponse");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 610, -1, -1));

        lookForPers.setBackground(new java.awt.Color(255, 255, 255));
        lookForPers.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lookForPers.setForeground(new java.awt.Color(0, 102, 102));
        lookForPers.setCaretColor(new java.awt.Color(0, 102, 102));
        jPanel5.add(lookForPers, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 140, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Cherche ce que tu veux     ");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 40, -1, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Rechercher Personnelle");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nom", "Prenom", "Username", "Password", "Ville", "Adresse", "Telephone", "CIN", "Post", "Securité question", "Reposnse"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 650, 110));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 890));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
try {
    this.st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
rs = st.executeQuery("SELECT * FROM Personnellee");


    if (rs != null) {
        if (rs.last()) {
            PersID.setText(rs.getString("PersID"));
            PersNom.setText(rs.getString("PersNom"));
            PersPrenom.setText(rs.getString("PersPrenom"));
            Username.setText(rs.getString("Username"));
            Password.setText(rs.getString("Password"));
            PersVille.setText(rs.getString("PersVille"));
            PersAdress.setText(rs.getString("PersAdress"));
            PersTel.setText(rs.getString("PersTel"));
            PersCin.setText(rs.getString("PersCin"));
             String typeFromDatabase = rs.getString("Type");
        Option.setSelectedItem(typeFromDatabase);
        String typeFromDatabase1 = rs.getString("SecurtiQuestion");
        SecurtiQuestion.setSelectedItem(typeFromDatabase1);
             SecurtiAnwser.setText(rs.getString("SecurtiAnwser")); 
            
            JOptionPane.showMessageDialog(this, "Vous avez atteint le dernier résultat.");
        } else {
            JOptionPane.showMessageDialog(this, "Aucun résultat trouvé.");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Le ResultSet est null.");
    }
} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
} catch (NullPointerException ex) {
    JOptionPane.showMessageDialog(this, "NullPointerException: " + ex.getMessage());
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
}

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
   try {
    if (rs != null && !rs.isClosed()) {
        if (rs.next()) {
            PersID.setText(rs.getString(1));
            PersNom.setText(rs.getString(2));
            PersPrenom.setText(rs.getString(3));
            Username.setText(rs.getString(4));
            Password.setText(rs.getString(5));
            PersVille.setText(rs.getString(6));
            PersAdress.setText(rs.getString(7));
            PersTel.setText(rs.getString(8));
            PersCin.setText(rs.getString(9));
            Option.setSelectedItem(String.valueOf(rs.getString(10)));
            SecurtiQuestion.setSelectedItem(String.valueOf(rs.getString(11)));
            SecurtiAnwser.setText(rs.getString(12));
            
        } else {
            JOptionPane.showMessageDialog(this, "Vous avez atteint le dernier enregistrement personnel.");
        }
    }
} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
} finally {
    
}
    }//GEN-LAST:event_jButton12ActionPerformed

    private void next2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_next2ActionPerformed
try {
    if (rs != null && !rs.isClosed()) {
        if (rs.previous()) {  
            PersID.setText(rs.getString(1));
            PersNom.setText(rs.getString(2));
            PersPrenom.setText(rs.getString(3));
            Username.setText(rs.getString(4));
            Password.setText(rs.getString(5));
            PersVille.setText(rs.getString(6));
            PersAdress.setText(rs.getString(7));
            PersTel.setText(rs.getString(8));
            PersCin.setText(rs.getString(9));
            Option.setSelectedItem(String.valueOf(rs.getString(10)));
            SecurtiQuestion.setSelectedItem(String.valueOf(rs.getString(11)));
            SecurtiAnwser.setText(rs.getString(12));
        } else {
        JOptionPane.showMessageDialog(this, "Vous avez atteint le Premier enregistrement personnel.");
        
    }
    }
} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
} finally {
   
}
    }//GEN-LAST:event_next2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
try {
    this.st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
rs = st.executeQuery("SELECT * FROM Personnellee");


    if (rs.first()) {
        PersID.setText(rs.getString("PersID"));
        PersNom.setText(rs.getString("PersNom"));
        PersPrenom.setText(rs.getString("PersPrenom"));
        Username.setText(rs.getString("Username"));
        Password.setText(rs.getString("Password"));
        PersVille.setText(rs.getString("PersVille"));
        PersAdress.setText(rs.getString("PersAdress"));
        PersTel.setText(rs.getString("PersTel"));
        PersCin.setText(rs.getString("PersCin"));
         
        String typeFromDatabase = rs.getString("Type");
        Option.setSelectedItem(typeFromDatabase);
         String typeFromDatabase1 = rs.getString("SecurtiQuestion");
        SecurtiQuestion.setSelectedItem(typeFromDatabase1);
             SecurtiAnwser.setText(rs.getString("SecurtiAnwser")); 

        JOptionPane.showMessageDialog(this, "Vous avez atteint le premier résultat.");
    } else {
        
        JOptionPane.showMessageDialog(this, "Aucun résultat trouvé.");
    }
} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
}      
    }//GEN-LAST:event_jButton13ActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
try {
    int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet enregistrement?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

    if (confirmDialogResult == JOptionPane.YES_OPTION) {
        pst = con.prepareStatement("DELETE FROM Personnellee WHERE PersID=?");
        pst.setString(1, PersID.getText());
        pst.executeUpdate();
        
        
        showRecord();
        JOptionPane.showMessageDialog(this, "Enregistrement supprimé avec succès.");
    } else {
        
        JOptionPane.showMessageDialog(this, "Suppression annulée.");
    }
} catch (SQLException ex) {
    Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
}      
      
    }//GEN-LAST:event_DeleteActionPerformed
private void loadRecordFromDatabase(String persID) {
    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM Personnellee WHERE PersID=?")) {
        pst.setString(1, persID);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                PersNom.setText(rs.getString("PersNom"));
                PersPrenom.setText(rs.getString("PersPrenom"));
                Username.setText(rs.getString("Username"));
                Password.setText(rs.getString("Password"));
                PersVille.setText(rs.getString("PersVille"));
                PersAdress.setText(rs.getString("PersAdress"));
                PersTel.setText(rs.getString("PersTel"));
                PersCin.setText(rs.getString("PersCin"));
                Option.setSelectedItem(rs.getString("Type"));
                SecurtiQuestion.setSelectedItem(rs.getString("SecurtiQuestion"));
                SecurtiAnwser.setText(rs.getString("SecurtiAnwser"));
                originalPersID = rs.getString("PersID");
                PersID.setText(originalPersID);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(LookForPers.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
try {
        Connection connection = ConnectioDB.getConnection();
        con = ConnectioDB.getConnection();

        // Check if any fields are empty
        if (PersNom.getText().isEmpty() || PersPrenom.getText().isEmpty() || Username.getText().isEmpty() || 
            Password.getText().isEmpty() || PersVille.getText().isEmpty() || PersAdress.getText().isEmpty() || 
            PersTel.getText().isEmpty() || PersCin.getText().isEmpty() || SecurtiAnwser.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs avant de procéder à la mise à jour.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the PersID has been changed
   

        int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment mettre à jour cet enregistrement?", "Confirmation de mise à jour", JOptionPane.YES_NO_OPTION);

        if (confirmDialogResult == JOptionPane.YES_OPTION) {
            // Prepare the update query
            String updateQuery = "UPDATE Personnellee SET PersNom=?, PersPrenom=?, Username=?, Password=?, PersVille=?, PersAdress=?, PersTel=?, PersCin=?, Type=?, SecurtiQuestion=?, SecurtiAnwser=? WHERE PersID=?";

            try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
                pst.setString(1, PersNom.getText());
                pst.setString(2, PersPrenom.getText());
                pst.setString(3, Username.getText());
                pst.setString(4, Password.getText());
                pst.setString(5, PersVille.getText());
                pst.setString(6, PersAdress.getText());
                pst.setString(7, PersTel.getText());
                pst.setString(8, PersCin.getText());
                String selectedOption = Option.getSelectedItem().toString();
                pst.setString(9, selectedOption);
                String selectedQuestion = SecurtiQuestion.getSelectedItem().toString();
                pst.setString(10, selectedQuestion);
                pst.setString(11, SecurtiAnwser.getText());
                pst.setInt(12, Integer.parseInt(PersID.getText()));

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Édition réussie!"); // Edit successful
                    showRecord(); // Refresh the record
                } else {
                    JOptionPane.showMessageDialog(null, "Édition échouée!"); // Edit failed
                }
            } catch (SQLException ex) {
                Logger.getLogger(LookForPers.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Mise à jour annulée.");
        }
    } catch (NumberFormatException ex) {
        Logger.getLogger(LookForGares.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_EditActionPerformed

        
    
    private void BtnLook2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLook2ActionPerformed
 DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);

    String searchText = lookForPers.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

    sorter.setRowFilter(new RowFilter<DefaultTableModel, Object>() {
        @Override
        public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
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
    }//GEN-LAST:event_BtnLook2ActionPerformed

    private void PersAdressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PersAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PersAdressActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
if (jCheckBox1.isSelected())
           Password.setEchoChar((char)0);
       else
           Password.setEchoChar('*');
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
int selectedrow=jTable1.getSelectedRow();

PersID.setText(String.valueOf(jTable1.getValueAt(selectedrow, 0)));
PersNom.setText(String.valueOf(jTable1.getValueAt(selectedrow, 1)));
PersPrenom.setText(String.valueOf(jTable1.getValueAt(selectedrow, 2)));
Username.setText(String.valueOf(jTable1.getValueAt(selectedrow, 3)));
Password.setText(String.valueOf(jTable1.getValueAt(selectedrow, 4)));
PersVille.setText(String.valueOf(jTable1.getValueAt(selectedrow, 5)));
PersAdress.setText(String.valueOf(jTable1.getValueAt(selectedrow, 6)));
PersTel.setText(String.valueOf(jTable1.getValueAt(selectedrow, 7)));
PersCin.setText(String.valueOf(jTable1.getValueAt(selectedrow, 8)));
Option.setSelectedItem(String.valueOf(jTable1.getValueAt(selectedrow, 9)));
SecurtiQuestion.setSelectedItem(String.valueOf(jTable1.getValueAt(selectedrow, 10)));
SecurtiAnwser.setText(String.valueOf(jTable1.getValueAt(selectedrow, 11)));
          


        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
   
        this.setVisible(false);  
      AdminPanel AdminPanel = new AdminPanel();
AdminPanel.setVisible(false);
     
// TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
   try {
            // Load the JasperReport template
            JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\report1.jrxml");

            // Get the selected rows from the jTable
            int[] selectedRows = jTable1.getSelectedRows();

            // Check if the user selected any row
            if (selectedRows.length == 0) {
                // If nothing is selected, construct SQL query to print all records
                StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Personnellee");
                String sql = sqlBuilder.toString();
                JRDesignQuery newQuery = new JRDesignQuery();
                newQuery.setText(sql);
                jasdi.setQuery(newQuery);
            } else {
                // Construct the SQL query dynamically based on the user's selection
                StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Personnellee WHERE PersID IN (");
                for (int i = 0; i < selectedRows.length; i++) {
                    String id = jTable1.getValueAt(selectedRows[i], 0).toString(); // Assuming the first column contains the PersID
                    sqlBuilder.append("'").append(id).append("'");
                    if (i < selectedRows.length - 1) {
                        sqlBuilder.append(",");
                    }
                }
                sqlBuilder.append(")");

                // Set the constructed SQL query
                String sql = sqlBuilder.toString();
                JRDesignQuery newQuery = new JRDesignQuery();
                newQuery.setText(sql);
                jasdi.setQuery(newQuery);
            }

            // Compile the JasperReport template
            JasperReport js = JasperCompileManager.compileReport(jasdi);

            // Provide a database connection (replace 'con' with your actual Connection object)
            Connection con = ConnectioDB.getConnection();

            // Fill the JasperReport with data
            JasperPrint jd = JasperFillManager.fillReport(js, null, con);

            // Show the report in a JasperViewer
            jasperViewer = new JasperViewer(jd,false);

            // Add window listener to the JasperViewer frame
            jasperViewer.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // This method is called when the JasperViewer frame is closing
                    // Handle the closing event here (for example, hide the JasperViewer instead of closing the entire application)
                    jasperViewer.setVisible(false);
                    jasperViewer = null; // Set the JasperViewer instance to null after hiding
                }
            });

            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(LookForPers.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }//GEN-LAST:event_jButton4ActionPerformed
private void handleJasperViewerClosing() {
    if (jasperViewer != null) {
        jasperViewer.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // This method is called when the JasperViewer frame is closing
                // Handle the closing event here (for example, hide the JasperViewer instead of closing the entire application)
                jasperViewer.setVisible(false);
                jasperViewer = null; // Set the JasperViewer instance to null after hiding
            }
        });
    }
}
    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked

    jTable1.clearSelection();

        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1MouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
         jTable1.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
try {
    // Load the JasperReport template
    JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\FicherPersn.jrxml");

    // Get the selected row from the jTable
    int selectedRow = jTable1.getSelectedRow();

    // Check if the user selected any row
    if (selectedRow == -1) {
        // If nothing is selected, handle appropriately (e.g., show an error message or default behavior)
      JOptionPane.showMessageDialog(this, "Aucun élément sélectionné.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
   
    } else {
        // Construct the SQL query dynamically based on the user's selection
        String id = jTable1.getValueAt(selectedRow, 0).toString(); // Assuming the first column contains the PersID
        String sql = "SELECT * FROM Personnellee WHERE PersID = '" + id + "'";

        // Set the constructed SQL query
        JRDesignQuery newQuery = new JRDesignQuery();
        newQuery.setText(sql);
        jasdi.setQuery(newQuery);
    }

    // Compile the JasperReport template
    JasperReport js = JasperCompileManager.compileReport(jasdi);

    // Provide a database connection (replace 'con' with your actual Connection object)
    Connection con = ConnectioDB.getConnection();

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
    Logger.getLogger(LookForPers.class.getName()).log(Level.SEVERE, null, ex);
}

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed
private int getValueAsInt(Object value) {
    if (value instanceof Integer) {
        return (int) value; // Convert Integer to int
    } else {
        // Handle other types as needed, for example, throw an exception or return a default value
        return -1;
    }
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
            java.util.logging.Logger.getLogger(LookForPers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LookForPers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LookForPers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LookForPers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LookForPers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnLook2;
    private javax.swing.JButton Delete;
    private javax.swing.JButton Edit;
    private javax.swing.JComboBox<String> Option;
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField PersAdress;
    private javax.swing.JTextField PersCin;
    private javax.swing.JTextField PersID;
    private javax.swing.JTextField PersNom;
    private javax.swing.JTextField PersPrenom;
    private javax.swing.JTextField PersTel;
    private javax.swing.JTextField PersVille;
    private javax.swing.JTextField SecurtiAnwser;
    private javax.swing.JComboBox<String> SecurtiQuestion;
    private javax.swing.JTextField Username;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField lookForPers;
    private javax.swing.JButton next2;
    // End of variables declaration//GEN-END:variables
}

