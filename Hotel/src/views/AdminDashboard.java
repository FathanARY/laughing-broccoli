/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package views;

import controllers.RoomController;
import models.Room;
import models.RoomType;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author LENOVO
 */
public class AdminDashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminDashboard.class.getName());
    private RoomController roomController;

    /**
     * Creates new form AdminDashboard
     */
    public AdminDashboard() {
        initComponents();
        initCustomComponents();
    }

    private void initCustomComponents() {
        roomController = new RoomController();
        setupTable();
        setupButtons();
        loadRooms();
    }

    private void setupTable() {
        // Set table model
        String[] columnNames = {"ID", "Room Number", "Type", "Price", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing directly in table
            }
        };
        jTable1.setModel(model);
        
        // Add selection listener
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean rowSelected = jTable1.getSelectedRow() != -1;
                    updateButton.setEnabled(rowSelected);
                    deleteButton.setEnabled(rowSelected);
                }
            }
        });
        
        // Initial button state
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void setupButtons() {
        // Create Button
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRoomDialog(null);
            }
        });

        // Update Button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jTable1.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) jTable1.getValueAt(selectedRow, 0);
                    Room room = roomController.getRoomById(id);
                    if (room != null) {
                        showRoomDialog(room);
                    }
                }
            }
        });

        // Delete Button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRoom();
            }
        });
    }

    private void loadRooms() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data
        
        List<Room> rooms = roomController.getAllRooms();
        for (Room room : rooms) {
            model.addRow(new Object[]{
                room.getId(),
                room.getRoomNumber(),
                room.getRoomTypeName(),
                room.getPrice(),
                room.getStatus()
            });
        }
    }

    private void showRoomDialog(Room room) {
        // Create a dialog to add/edit room
        javax.swing.JDialog dialog = new javax.swing.JDialog(this, room == null ? "Create Room" : "Update Room", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new java.awt.GridLayout(6, 2, 10, 10));
        dialog.setLocationRelativeTo(this);

        javax.swing.JLabel lblNumber = new javax.swing.JLabel("Room Number:");
        javax.swing.JTextField txtNumber = new javax.swing.JTextField();
        
        javax.swing.JLabel lblType = new javax.swing.JLabel("Room Type:");
        javax.swing.JComboBox<String> cmbType = new javax.swing.JComboBox<>();
        
        // Load room types
        List<RoomType> types = roomController.getAllRoomTypes();
        for (RoomType type : types) {
            cmbType.addItem(type.getId() + " - " + type.getName());
        }

        javax.swing.JLabel lblPrice = new javax.swing.JLabel("Price:");
        javax.swing.JTextField txtPrice = new javax.swing.JTextField();
        
        javax.swing.JLabel lblStatus = new javax.swing.JLabel("Status:");
        javax.swing.JComboBox<String> cmbStatus = new javax.swing.JComboBox<>(new String[]{"available", "occupied", "maintenance"});

        javax.swing.JButton btnSave = new javax.swing.JButton("Save");
        javax.swing.JButton btnCancel = new javax.swing.JButton("Cancel");

        // Pre-fill if editing
        if (room != null) {
            txtNumber.setText(room.getRoomNumber());
            txtPrice.setText(String.valueOf(room.getPrice()));
            cmbStatus.setSelectedItem(room.getStatus());
            // Select correct type
            for (int i = 0; i < cmbType.getItemCount(); i++) {
                if (cmbType.getItemAt(i).startsWith(room.getRoomTypeId() + " -")) {
                    cmbType.setSelectedIndex(i);
                    break;
                }
            }
        }

        btnSave.addActionListener(e -> {
            try {
                String number = txtNumber.getText();
                String typeStr = (String) cmbType.getSelectedItem();
                int typeId = Integer.parseInt(typeStr.split(" - ")[0]);
                double price = Double.parseDouble(txtPrice.getText());
                String status = (String) cmbStatus.getSelectedItem();

                if (room == null) {
                    if (roomController.addRoom(number, typeId, price, status)) {
                        JOptionPane.showMessageDialog(dialog, "Room created successfully!");
                        loadRooms();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to create room.");
                    }
                } else {
                    if (roomController.updateRoom(room.getId(), number, typeId, price, status)) {
                        JOptionPane.showMessageDialog(dialog, "Room updated successfully!");
                        loadRooms();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to update room.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please check price and other fields.");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(lblNumber);
        dialog.add(txtNumber);
        dialog.add(lblType);
        dialog.add(cmbType);
        dialog.add(lblPrice);
        dialog.add(txtPrice);
        dialog.add(lblStatus);
        dialog.add(cmbStatus);
        dialog.add(btnSave);
        dialog.add(btnCancel);

        dialog.setVisible(true);
    }

    private void deleteRoom() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) return;

        int id = (int) jTable1.getValueAt(selectedRow, 0);
        String number = (String) jTable1.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete room " + number + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (roomController.deleteRoom(id)) {
                JOptionPane.showMessageDialog(this, "Room deleted successfully!");
                loadRooms();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete room.");
            }
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        updateButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton1.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        jButton1.setText("Create");

        updateButton.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        updateButton.setText("Update");

        deleteButton.setFont(new java.awt.Font("Dubai Medium", 0, 14)); // NOI18N
        deleteButton.setText("Delete");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Dubai Medium", 0, 48)); // NOI18N
        jLabel1.setText("Room Controls");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1034, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(468, 468, 468)
                        .addComponent(jLabel1)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Rooms", jPanel1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 712, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Report", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AdminDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
