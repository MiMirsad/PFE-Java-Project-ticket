


package Trains;
import Trains.ConnectioDB;
import Trains.AdminPanel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.Connection;
import javax.swing.JFrame;

public class Loginn extends javax.swing.JFrame {
ConnectioDB con;
PreparedStatement pst;
ResultSet rs; 
    /**
     * Creates new form Loginn
     */
    public Loginn() {
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        password = new javax.swing.JPasswordField();
        username = new javax.swing.JTextField();
        Select = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jScrollPane1.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.setText("Se Connecter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 400, 244, -1));

        password.setBackground(new java.awt.Color(255, 255, 255));
        password.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        password.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 240, 275, 42));

        username.setBackground(new java.awt.Color(255, 255, 255));
        username.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        username.setForeground(new java.awt.Color(0, 0, 0));
        username.setMinimumSize(new java.awt.Dimension(6, 42));
        jPanel1.add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 180, 275, 46));

        Select.setBackground(new java.awt.Color(0, 102, 102));
        Select.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        Select.setForeground(new java.awt.Color(255, 255, 255));
        Select.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admin", "Agent" }));
        Select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectActionPerformed(evt);
            }
        });
        jPanel1.add(Select, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 300, 275, 42));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 102));
        jButton2.setText("Mot de passe oublié");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 400, 287, 37));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Se Connecter");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 100, -1, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nom d'utilisateur");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(491, 186, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Mot de Pass");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(504, 242, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Se connecter Comme");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(473, 302, -1, -1));

        jCheckBox1.setBackground(new java.awt.Color(0, 102, 102));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Afficher le Mot de Passe");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 350, -1, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Downloads\\Logo-oncf.png")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 460, 640));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String sername = username.getText();
        String Password = password.getText();
        String option = Select.getSelectedItem().toString();
        if (sername.equals ("")|| (Password.equals(""))|| option.equals("") ) {
            JOptionPane.showMessageDialog(rootPane, "certains champs sont vides");
        }else{

            try {
                Connection con = ConnectioDB.getConnection();
                String sql = "SELECT * FROM [Personnellee] WHERE Username=? AND Password=?";
                pst = con.prepareStatement(sql);
                pst.setString(1,sername);
                pst.setString(2,Password);
                rs=pst.executeQuery();
                if (rs.next()) {
                    String s1 = rs.getString("Type");
                    String un = rs.getString("Username");
                    if (option.equalsIgnoreCase("Admin") && s1.equalsIgnoreCase("admin")) {
                        AdminPanel ap = new AdminPanel(un);
                        ap.setVisible(true);
                        setVisible(false);
                    } else if (option.equalsIgnoreCase("Agent") && s1.equalsIgnoreCase("agent")) {
                        UserPanel up = new UserPanel(un);
                        up.setVisible(true);
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Option et type d'utilisateur ne correspondent pas.");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "UIdentifiants incorrects.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Loginn.class.getName()).log(Level.SEVERE, null, ex);

            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
new ForgtenPass().setVisible(true);
this.setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
       if (jCheckBox1.isSelected())
           password.setEchoChar((char)0);
       else
           password.setEchoChar('*');
           
           
       
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void SelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelectActionPerformed

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
if (evt.getKeyCode()==KeyEvent.VK_ENTER){
    String sername = username.getText();
        String Password = password.getText();
        String option = Select.getSelectedItem().toString();
        if (sername.equals ("")|| (Password.equals(""))|| option.equals("") ) {
            JOptionPane.showMessageDialog(rootPane, "certains champs sont vides");
        }else{

            try {
                Connection con = ConnectioDB.getConnection();
                String sql = "SELECT * FROM [Personnellee] WHERE Username=? AND Password=?";
                pst = con.prepareStatement(sql);
                pst.setString(1,sername);
                pst.setString(2,Password);
                rs=pst.executeQuery();
                if (rs.next()) {
                    String s1 = rs.getString("Type");
                    String un = rs.getString("Username");
                    if (option.equalsIgnoreCase("Admin") && s1.equalsIgnoreCase("admin")) {
                        AdminPanel ap = new AdminPanel(un);
                        ap.setVisible(true);
                        setVisible(false);
                    } else if (option.equalsIgnoreCase("Agent") && s1.equalsIgnoreCase("agent")) {
                        UserPanel up = new UserPanel(un);
                        up.setVisible(true);
                        setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Option et type d'utilisateur ne correspondent pas.");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "UIdentifiants incorrects.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Loginn.class.getName()).log(Level.SEVERE, null, ex);

            }

        } 
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1KeyPressed
 
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
            java.util.logging.Logger.getLogger(Loginn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Loginn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Loginn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Loginn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Loginn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox Select;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables

    private Connection establishConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void handleLoginFailure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
