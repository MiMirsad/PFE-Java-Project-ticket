/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Trains;

import Trains.GareDB;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 *
 * @author Administrator
 */
public class Pers extends javax.swing.JFrame {
private AdminPanel adminPanel;
public Pers(AdminPanel adminPanel) {
    initComponents();
    
    this.adminPanel = adminPanel;
}
    /**
     * Creates new form Pers
     */
    public Pers () {
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
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        PersNom = new javax.swing.JTextField();
        PersPrenom = new javax.swing.JTextField();
        Username = new javax.swing.JTextField();
        PersVille = new javax.swing.JTextField();
        PersAdress = new javax.swing.JTextField();
        PersTel = new javax.swing.JTextField();
        PersCin = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        Option = new javax.swing.JComboBox<>();
        SecurtiQuestion = new javax.swing.JComboBox<>();
        SecurtiAnwser = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PersNom.setBackground(new java.awt.Color(0, 102, 102));
        PersNom.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        PersNom.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(PersNom, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, 360, -1));

        PersPrenom.setBackground(new java.awt.Color(0, 102, 102));
        PersPrenom.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        PersPrenom.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(PersPrenom, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 360, -1));

        Username.setBackground(new java.awt.Color(0, 102, 102));
        Username.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Username.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Username, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 360, -1));

        PersVille.setBackground(new java.awt.Color(0, 102, 102));
        PersVille.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        PersVille.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(PersVille, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 360, -1));

        PersAdress.setBackground(new java.awt.Color(0, 102, 102));
        PersAdress.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        PersAdress.setForeground(new java.awt.Color(255, 255, 255));
        PersAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PersAdressActionPerformed(evt);
            }
        });
        jPanel2.add(PersAdress, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 360, -1));

        PersTel.setBackground(new java.awt.Color(0, 102, 102));
        PersTel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        PersTel.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(PersTel, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 330, 360, -1));

        PersCin.setBackground(new java.awt.Color(0, 102, 102));
        PersCin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        PersCin.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(PersCin, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, 360, -1));

        Password.setBackground(new java.awt.Color(0, 102, 102));
        Password.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Password.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(Password, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, 360, -1));

        Option.setBackground(new java.awt.Color(0, 102, 102));
        Option.setForeground(new java.awt.Color(255, 255, 255));
        Option.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "Agent", " " }));
        jPanel2.add(Option, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 410, 360, -1));

        SecurtiQuestion.setBackground(new java.awt.Color(0, 102, 102));
        SecurtiQuestion.setForeground(new java.awt.Color(255, 255, 255));
        SecurtiQuestion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quelle est votre date de naissance?", "Quel était le nom de votre professeur d’école préféré ?", "Quel est votre film préféré?", "Quel était votre première voiture?", "Quel est ton signe astrologique?", " " }));
        jPanel2.add(SecurtiQuestion, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 450, 360, -1));

        SecurtiAnwser.setBackground(new java.awt.Color(0, 102, 102));
        SecurtiAnwser.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        SecurtiAnwser.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(SecurtiAnwser, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 490, 360, -1));

        jCheckBox1.setForeground(new java.awt.Color(0, 102, 102));
        jCheckBox1.setText("Afficher le Mot de Passe");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 70, -1));

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Effacer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 210, 50));

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Ajouter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 570, 210, 50));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, 560, 750));

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB", 0, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Ajouter Personnelle");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 400, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nom");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Prenom");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, -1, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("UserName");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Password");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Ville");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Adresse");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, -1, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Telephone");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 340, -1, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("C.I.N");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, -1, -1));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("participants du système");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, -1, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("questions de sécurité");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, -1, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("questions Reponse");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, -1, -1));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 102));
        jButton3.setText("Rotour");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 570, 210, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 410, 750));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PersAdressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PersAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PersAdressActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
   
        Personnelle personnelle = new Personnelle();
        
   personnelle.setPersNom(PersNom.getText());
        personnelle.setUsername(Username.getText());
        personnelle.setPersPrenom(PersPrenom.getText());
        personnelle.setPassword(String.valueOf(Password.getPassword()));
        personnelle.setPersVille( PersVille.getText());
        personnelle.setPersAdress(PersAdress.getText());
        personnelle.setPersTel(PersTel.getText());
        personnelle.setPersCin(PersCin.getText());
        personnelle.setOption(String.valueOf(Option.getSelectedItem()));
        personnelle.setSecurtiQuestion(String.valueOf(SecurtiQuestion.getSelectedItem()));
        personnelle.setSecurtiAnwser(SecurtiAnwser.getText());
        
        GareDB d = GareDB.getCon();
        d.insertPersonnelle(personnelle);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
if (jCheckBox1.isSelected())
           Password.setEchoChar((char)0);
       else
           Password.setEchoChar('*');

        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
PersNom.setText("");
PersPrenom.setText("");
Username.setText("");
Password.setText("");
PersVille.setText("");
PersAdress.setText("");
PersTel.setText("");
PersCin.setText("");
SecurtiAnwser.setText("");
PersNom.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
showAdminPanel();     
// TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed
 public void showAdminPanel() {
        if (adminPanel != null) {
            adminPanel.setVisible(true);
            this.setVisible(false);
        } else {
            System.out.println("AdminPanel is null. Make sure to initialize it before using.");
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
            java.util.logging.Logger.getLogger(Pers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Pers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Option;
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField PersAdress;
    private javax.swing.JTextField PersCin;
    private javax.swing.JTextField PersNom;
    private javax.swing.JTextField PersPrenom;
    private javax.swing.JTextField PersTel;
    private javax.swing.JTextField PersVille;
    private javax.swing.JTextField SecurtiAnwser;
    private javax.swing.JComboBox<String> SecurtiQuestion;
    private javax.swing.JTextField Username;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

   
}
