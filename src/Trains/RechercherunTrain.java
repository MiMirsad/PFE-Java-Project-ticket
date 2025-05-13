
package Trains;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

public class RechercherunTrain extends javax.swing.JFrame {
     private String originalTrainMarque;
    private String originalTrainType;
    private String originalTrainCapacity;
    private String originalTrainplacesNum1cls;
    private String originalTrainNumVoiture;
    private String originalTrainNumVoit1Cls;
    private String originalTrainSpeed;
      private JComboBox<String> autoCompleteComboBox;
    private List<String> suggestions;

   public RechercherunTrain(AdminPanel originalAdminPanel) {
    initComponents();
    
    showRecord(); 
    } 
private void loadRecordFromDatabase(String trainNumber) {
    try (PreparedStatement pst = con.prepareStatement("SELECT TrainNumber, TrainMarque, TrainType, TrainplacesNum2cls, TrainplacesNum1cls, TrainNumVoit2Cls, TrainNumVoit1Cls, TrainSpeed FROM Trains WHERE TrainNumber=?")) {
        pst.setString(1, trainNumber);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                originalTrainMarque = rs.getString("TRAINMARQUE"); // Capital letters
                originalTrainType = rs.getString("TRAINTYPE"); // Capital letters
                originalTrainCapacity = rs.getString("TRAINPLACESNUM2CLS"); // Capital letters
                originalTrainplacesNum1cls = rs.getString("TRAINPLACESNUM1CLS"); // Capital letters
                originalTrainNumVoiture = rs.getString("TRAINNUMVOIT2CLS"); // Capital letters
                originalTrainNumVoit1Cls = rs.getString("TRAINNUMVOIT1CLS"); // Capital letters
                originalTrainSpeed = rs.getString("TRAINSPEED"); // Capital letters
                   
                // Populate the form fields with the retrieved values
                TrainMarque.setText(originalTrainMarque);
                TrainType.setText(originalTrainType);
                TrainCapacity.setText(originalTrainCapacity);
                TrainplacesNum1cls.setText(originalTrainplacesNum1cls);
             
                TrainSpeed.setText(originalTrainSpeed);
            } else {
                // Handle the case where the record with the specified train number is not found
            }
        }
    } catch (SQLException ex) {
        // Handle SQL exception
        ex.printStackTrace();
    }
}


  private boolean changesDetected() {


    return !TrainMarque.getText().equals(originalTrainMarque) ||
           !TrainType.getText().equals(originalTrainType) ||
           !TrainCapacity.getText().equals(originalTrainCapacity) ||
           !TrainSpeed.getText().equals(originalTrainSpeed);
}
  Connection con=null;
    Statement st=null;
    PreparedStatement pst=null;
    ResultSet rs=null;
    /**
     * Creates new form RechercherunTrain
     */
    public RechercherunTrain() {
    initComponents();
     

        TrainNumber.setEditable(false);
        TrainNumber.setFocusable(false);
        TrainNumber.setBackground(Color.LIGHT_GRAY);
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
        //  initializeMappingTable();
    BtnLook.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent evt) {
        BtnLookActionPerformed(evt);
  showRecord(); 
        }
    });
    try {   
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Oncf;integratedSecurity=true");
        con = con; // Assign conx to con
        showRecord();
        st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        rs = st.executeQuery("select * from Trains");
        if (rs.next()) {
            TrainNumber.setText(rs.getString(1));
            TrainMarque.setText(rs.getString(2));
            TrainType.setText(rs.getString(3));
            TrainCapacity.setText(rs.getString(4));
            TrainplacesNum1cls.setText(rs.getString(5));
            TrainSpeed.setText(rs.getString(6));
        }
    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println(ex);
    }
}



    public void showRecord(){
        try{
            pst=con.prepareStatement("select * from Trains");
            rs=pst.executeQuery();
            ResultSetMetaData rsm=(ResultSetMetaData) rs.getMetaData();
            int n=rsm.getColumnCount();
            DefaultTableModel df=(DefaultTableModel) jTable1.getModel();
            df.setRowCount(0);
           while (rs.next()) {
    Vector<Object> obj = new Vector<>();
    for (int i = 1; i <= n; i++) {
        obj.add(rs.getString(i));
    }
    df.addRow(obj);
}
        }
        catch(SQLException ex){
            System.out.println(ex);
        }
        
    }
private void loadRoutesFromDatabase() {
    String trainNumber = TrainNumber.getText(); // Get train number from the TrainNumber textbox
    
    try (PreparedStatement pst = con.prepareStatement("SELECT t.MappingID, \n" +
"       g.GareName, \n" +
"       CASE WHEN g.TNRSupp = 1 THEN 'oui' ELSE 'no' END AS TNRSupp, \n" +
"       CASE WHEN g.AtlasSupp = 1 THEN 'oui' ELSE 'no' END AS AtlasSupp, \n" +
"       CASE WHEN g.AlboraqSupp = 1 THEN 'oui' ELSE 'no' END AS AlboraqSupp\n" +
"FROM TrainGareMapping t\n" +
"JOIN Gare g ON t.GareN = g.GareID\n" +
"WHERE t.TrainN = ?")) {
        pst.setString(1, trainNumber);

        try (ResultSet rs = pst.executeQuery()) {
            DefaultTableModel routeModel = (DefaultTableModel) TrainGareMappingTable.getModel();
            routeModel.setRowCount(0); // Clear previous routes

            while (rs.next()) {
                String mappingID = rs.getString("MappingID");
                String gareName = rs.getString("GareName");
                String TNRSupp = rs.getString("TNRSupp");
                String AtlasSupp = rs.getString("AtlasSupp");
                String AlboraqSupp = rs.getString("AlboraqSupp");
                routeModel.addRow(new Object[]{mappingID, gareName,TNRSupp,AlboraqSupp,AtlasSupp});
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
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

        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        TrainNumber = new javax.swing.JTextField();
        TrainCapacity = new javax.swing.JTextField();
        TrainType = new javax.swing.JTextField();
        TrainSpeed = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        TrainMarque = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        TrainplacesNum1cls = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lookForTrain = new javax.swing.JTextField();
        BtnLook = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TrainGareMappingTable = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setForeground(new java.awt.Color(0, 102, 102));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Train Marque");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 248, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Train Type");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 294, -1, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Train Capacitié");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 337, -1, -1));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Train Vitasse");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 525, -1, -1));

        TrainNumber.setBackground(new java.awt.Color(255, 255, 255));
        TrainNumber.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainNumber.setForeground(new java.awt.Color(0, 102, 102));
        jPanel3.add(TrainNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 199, 192, -1));

        TrainCapacity.setBackground(new java.awt.Color(255, 255, 255));
        TrainCapacity.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainCapacity.setForeground(new java.awt.Color(0, 102, 102));
        jPanel3.add(TrainCapacity, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 337, 192, -1));

        TrainType.setBackground(new java.awt.Color(255, 255, 255));
        TrainType.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainType.setForeground(new java.awt.Color(0, 102, 102));
        jPanel3.add(TrainType, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 291, 192, -1));

        TrainSpeed.setBackground(new java.awt.Color(255, 255, 255));
        TrainSpeed.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainSpeed.setForeground(new java.awt.Color(0, 102, 102));
        jPanel3.add(TrainSpeed, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 522, 192, -1));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Numéro de Train.");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 202, -1, -1));

        TrainMarque.setBackground(new java.awt.Color(255, 255, 255));
        TrainMarque.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainMarque.setForeground(new java.awt.Color(0, 102, 102));
        TrainMarque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TrainMarqueActionPerformed(evt);
            }
        });
        jPanel3.add(TrainMarque, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 245, 192, -1));

        jTable1.setBackground(new java.awt.Color(255, 255, 255));
        jTable1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 102, 102));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numéro De Train", "Train Marque", "Train Type", "Train Capacitté", "1er Class places Nomber", "Vitasse"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 580, 610, 113));

        jLabel35.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("1er class N° de Places");
        jPanel3.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(27, 386, -1, -1));

        TrainplacesNum1cls.setBackground(new java.awt.Color(255, 255, 255));
        TrainplacesNum1cls.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainplacesNum1cls.setForeground(new java.awt.Color(0, 102, 102));
        jPanel3.add(TrainplacesNum1cls, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 383, 192, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Cherche ce que tu veux     ");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        lookForTrain.setBackground(new java.awt.Color(255, 255, 255));
        lookForTrain.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lookForTrain.setForeground(new java.awt.Color(0, 102, 102));
        jPanel3.add(lookForTrain, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 192, -1));

        BtnLook.setBackground(new java.awt.Color(255, 255, 255));
        BtnLook.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BtnLook.setForeground(new java.awt.Color(0, 102, 102));
        BtnLook.setText("Rechercher");
        BtnLook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnLookActionPerformed(evt);
            }
        });
        jPanel3.add(BtnLook, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 110, -1, -1));

        Edit.setBackground(new java.awt.Color(255, 255, 255));
        Edit.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Edit.setForeground(new java.awt.Color(0, 102, 102));
        Edit.setText("Modifier");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });
        jPanel3.add(Edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 195, 130, -1));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 102));
        jButton3.setText("Supprimer");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 237, 130, -1));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 102, 102));
        jButton4.setText("<<");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 279, 130, -1));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(0, 102, 102));
        jButton5.setText(">");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 321, 130, -1));

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(0, 102, 102));
        jButton6.setText("<");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 360, 130, -1));

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(0, 102, 102));
        jButton7.setText(">>");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(629, 393, 130, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Rechercher un Train");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 23, -1, -1));

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton8.setForeground(new java.awt.Color(0, 102, 102));
        jButton8.setText("Retour");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 640, 130, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.setText("Imprimer List");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 460, 170, -1));

        TrainGareMappingTable.setBackground(new java.awt.Color(255, 255, 255));
        TrainGareMappingTable.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        TrainGareMappingTable.setForeground(new java.awt.Color(0, 102, 102));
        TrainGareMappingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Route ID", "Gare", "Title 3", "Title 4", "Title 5"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(TrainGareMappingTable);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 560, 400, 130));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 102));
        jButton2.setText("Imprimer Ficher");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 500, 170, -1));

        jButton9.setText("Imprimer");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 490, -1, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 0, 1260, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed

        if (TrainMarque.getText().isEmpty() || TrainType.getText().isEmpty() ||
        TrainCapacity.getText().isEmpty() || 
        TrainSpeed.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs avant de procéder à la mise à jour.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (!isPositiveNumber(TrainSpeed.getText())) {
        JOptionPane.showMessageDialog(this, "La vitesse du train n'est pas valide. Veuillez saisir un nombre positif.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!isPositiveNumber(TrainCapacity.getText())) {
        JOptionPane.showMessageDialog(this, "La vitesse du train n'est pas valide. Veuillez saisir un nombre positif.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
        return;
    }
        if (!isPositiveNumber(TrainplacesNum1cls.getText())) {
        JOptionPane.showMessageDialog(this, "La vitesse du train n'est pas valide. Veuillez saisir un nombre positif.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
        return;
    }
                if (!isPositiveNumber(TrainNumber.getText())) {
        JOptionPane.showMessageDialog(this, "La vitesse du train n'est pas valide. Veuillez saisir un nombre positif.", "Erreur de mise à jour", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (!changesDetected()) {
        int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Aucune modification détectée. Voulez-vous quand même mettre à jour?", "Avertissement", JOptionPane.YES_NO_OPTION);

        if (confirmDialogResult != JOptionPane.YES_OPTION) {
            // User clicked "No" or closed the confirmation dialog
            JOptionPane.showMessageDialog(this, "Mise à jour annulée.");
            return; // Exit the method
        }
    }

    int updateConfirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir mettre à jour les informations du train?", "Confirmation de mise à jour", JOptionPane.YES_NO_OPTION);
    if (updateConfirmation != JOptionPane.YES_OPTION) {
        return; // Exit the method if the user chooses not to update
    }

    try (PreparedStatement pst = con.prepareStatement("UPDATE Trains SET TrainMarque=?, TrainType=?, TrainplacesNum2cls=?, TrainplacesNum1cls=?,  TrainSpeed=? WHERE TrainNumber=?")) {
        pst.setString(1, TrainMarque.getText());
        pst.setString(2, TrainType.getText());
        pst.setString(3, TrainCapacity.getText());
        pst.setString(4, TrainplacesNum1cls.getText());
        pst.setString(5, TrainSpeed.getText());
        pst.setString(6, TrainNumber.getText());

        int rowsAffected = pst.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Mise à jour réussie!");
            showRecord(); // Update the displayed records
            con.commit(); // Commit the transaction
        } else {
            JOptionPane.showMessageDialog(this, "Mise à jour a échoué! Le numéro de Train n'a pas changé.");
        }

    } catch (SQLException ex) {
         Logger.getLogger(RechercherunTrain.class.getName()).log(Level.SEVERE, null, ex);
    }
    // TODO add your handling code here:

        // TODO add your handling code here:
    }//GEN-LAST:event_EditActionPerformed
private boolean isPositiveNumber(String str) {
    try {
        double num = Double.parseDouble(str);
        return num > 0;
    } catch (NumberFormatException e) {
        return false;
    }
}
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

     int confirmDialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet enregistrement?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

    if (confirmDialogResult == JOptionPane.YES_OPTION) {
        try {
            // Check if the train is currently in use in TrainGareMapping
            String checkQuery = "SELECT COUNT(*) FROM TrainGareMapping WHERE TrainN=?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, TrainNumber.getText());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Train is in use, show a message and do not proceed with deletion
                        JOptionPane.showMessageDialog(this, "Ce train est actuellement en service et ne peut pas être supprimé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            // Proceed with deletion if the train is not in use
            try (PreparedStatement deleteStmt = con.prepareStatement("DELETE FROM Trains WHERE TrainNumber=?")) {
                deleteStmt.setString(1, TrainNumber.getText());
                int rowsAffected = deleteStmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Train supprimé avec succès");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun train trouvé avec ce numéro.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
                
                showRecord();
            }

        } catch (SQLException ex) {
            Logger.getLogger(RechercherunTrain.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du train.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }




        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void BtnLookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnLookActionPerformed
    
      DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);

    String searchText = lookForTrain.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

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
    }//GEN-LAST:event_BtnLookActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
try {
    rs = st.executeQuery("select * from Trains");
    if (rs.first()) {
        TrainNumber.setText(rs.getString(1));
        TrainMarque.setText(rs.getString(2));
        TrainType.setText(rs.getString(3));
        TrainCapacity.setText(rs.getString(4));
        TrainplacesNum1cls.setText(rs.getString(5));
        TrainSpeed.setText(rs.getString(5));
        
        JOptionPane.showMessageDialog(this, "Vous avez atteint le premier enregistrement.");
    } else {
        JOptionPane.showMessageDialog(this, "Aucun enregistrement trouvé dans la base de données.");
    }
} catch (SQLException ex) {
    Logger.getLogger(RechercherunTrain.class.getName()).log(Level.SEVERE, null, ex);
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
    try {
    if (!rs.isLast()) {
        rs.next();
        TrainNumber.setText(rs.getString(1));
        TrainMarque.setText(rs.getString(2));
        TrainType.setText(rs.getString(3));
        TrainCapacity.setText(rs.getString(4));
        TrainplacesNum1cls.setText(rs.getString(5));
        TrainSpeed.setText(rs.getString(6));
    } else {
        JOptionPane.showMessageDialog(this, "Vous avez atteint le dernier enregistrement.");
    }
} catch (SQLException ex) {
    Logger.getLogger(RechercherunTrain.class.getName()).log(Level.SEVERE, null, ex);
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
try {
    if (!rs.isFirst()) {
        rs.previous();
        TrainNumber.setText(rs.getString(1));
        TrainMarque.setText(rs.getString(2));
        TrainType.setText(rs.getString(3));
        TrainCapacity.setText(rs.getString(4));
        TrainplacesNum1cls.setText(rs.getString(5));
        TrainSpeed.setText(rs.getString(6));
    } else {
        JOptionPane.showMessageDialog(this, "Vous avez atteint le premier enregistrement.");
    }
} catch (SQLException ex) {
    Logger.getLogger(RechercherunTrain.class.getName()).log(Level.SEVERE, null, ex);
}
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
      try {
    rs = st.executeQuery("select * from Trains");
    if (rs.last()) {
        TrainNumber.setText(rs.getString(1));
        TrainMarque.setText(rs.getString(2));
        TrainType.setText(rs.getString(3));
        TrainCapacity.setText(rs.getString(4));
        TrainplacesNum1cls.setText(rs.getString(5));
        TrainSpeed.setText(rs.getString(6));

        JOptionPane.showMessageDialog(this, "Vous avez atteint le dernier enregistrement.");
    } else {
        JOptionPane.showMessageDialog(this, "Aucun enregistrement trouvé dans la base de données.");
    }
} catch (SQLException ex) {
    Logger.getLogger(RechercherunTrain.class.getName()).log(Level.SEVERE, null, ex);
}
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
 loadRoutesFromDatabase();
        int selectedrow=jTable1.getSelectedRow();

TrainNumber.setText((String) jTable1.getValueAt(selectedrow, 0));
TrainMarque.setText((String) jTable1.getValueAt(selectedrow, 1));
TrainType.setText((String) jTable1.getValueAt(selectedrow, 2));
TrainCapacity.setText((String) jTable1.getValueAt(selectedrow, 3));
TrainplacesNum1cls.setText((String)jTable1.getValueAt(selectedrow, 4));
TrainSpeed.setText((String) jTable1.getValueAt(selectedrow, 5));
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
 this.setVisible(false);
   AdminPanel AdminPanel = new AdminPanel();
AdminPanel.setVisible(false);
   
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void TrainMarqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TrainMarqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TrainMarqueActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
          int[] selectedRows = jTable1.getSelectedRows();

    try {
        JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\Train.jrxml");
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Trains");

        if (selectedRows.length > 0) {
            sqlBuilder.append(" WHERE TrainNumber IN (");
            for (int i = 0; i < selectedRows.length; i++) {
                String id = jTable1.getValueAt(selectedRows[i], 0).toString(); // Assuming the first column contains the TrainID
                sqlBuilder.append("'").append(id).append("'");
                if (i < selectedRows.length - 1) {
                    sqlBuilder.append(",");
                }
            }
            sqlBuilder.append(")");
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


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
   jTable1.clearSelection();
   TrainGareMappingTable.clearSelection();
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel3MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    try {
    // Load the JasperReport template
    JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\ficherTrain.jrxml");

    // Get the selected row from the jTable
    int selectedRow = jTable1.getSelectedRow();

    // Check if the user selected any row
    if (selectedRow == -1) {
        // If nothing is selected, handle appropriately (e.g., show an error message or default behavior)
        System.out.println("No row selected.");
        return;
    } else {
        // Construct the SQL query dynamically based on the user's selection
        String id = jTable1.getValueAt(selectedRow, 0).toString(); // Assuming the first column contains the PersID
        String sql = "SELECT * FROM Trains WHERE TrainNumber = '" + id + "'";

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

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
      
        HashMap<String,Object> param = new HashMap<>();
       
        DefaultTableModel tbl = (DefaultTableModel) jTable1.getModel();
        int row = jTable1.getSelectedRow();
        
        if (row >= 0) {
            
            int idGare = Integer.parseInt(String.valueOf(tbl.getValueAt(row, 0)));
            param.put("s", idGare);

            try {
                JasperDesign jasdi = JRXmlLoader.load("C:\\Project\\ONCF\\src\\oncf\\TrainGare1.jrxml");

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

        }else{
            JOptionPane.showMessageDialog(null, "selectionner un ligne dans le talbleau");
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

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
            java.util.logging.Logger.getLogger(RechercherunTrain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RechercherunTrain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RechercherunTrain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RechercherunTrain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RechercherunTrain().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnLook;
    private javax.swing.JButton Edit;
    private javax.swing.JTextField TrainCapacity;
    private javax.swing.JTable TrainGareMappingTable;
    private javax.swing.JTextField TrainMarque;
    private javax.swing.JTextField TrainNumber;
    private javax.swing.JTextField TrainSpeed;
    private javax.swing.JTextField TrainType;
    private javax.swing.JTextField TrainplacesNum1cls;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField lookForTrain;
    // End of variables declaration//GEN-END:variables
}
