package net.strawberrystudios.noskwl.gui;

import java.awt.event.KeyEvent;
import static java.lang.System.out;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import net.strawberrystudios.noskwl.TextAreaWriter;
import net.strawberrystudios.noskwl.client.Client;
import net.strawberrystudios.noskwl.packet.Packet;
import net.strawberrystudios.noskwl.packet.PacketFactory;

//10.52.161.110
/**
 *
 * @author Jamie Gregory @ Strawberry Studios (2015)
 */
public class GUI extends javax.swing.JFrame {

    Client c;
    Thread clientThread;
    ListModel<String> lm;
    PacketFactory pf;
    int count = 0;
    ArrayList<String> connections = new ArrayList<>();
    String username;

    public GUI() {
        initComponents();
        setLocationRelativeTo(null);
        c = new Client(new TextAreaWriter(msgDisp));
        lm = (ListModel) new DefaultListModel();
        this.pf = new PacketFactory();
//        username = (JOptionPane.showInputDialog(null,
//                "Enter a username:", "User name",
//                JOptionPane.CANCEL_OPTION));
        menUsernameActionPerformed(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        conectionList = new javax.swing.JList();
        btnSend = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        msgDisp = new javax.swing.JTextArea();
        msgField = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menConnect = new javax.swing.JMenuItem();
        menUsername = new javax.swing.JMenuItem();
        menExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menClrCht = new javax.swing.JMenuItem();
        menHelp = new javax.swing.JMenu();
        menAbt = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));
        setForeground(new java.awt.Color(0, 0, 0));

        conectionList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(conectionList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setText("Friends");

        msgDisp.setEditable(false);
        msgDisp.setColumns(20);
        msgDisp.setRows(5);
        jScrollPane2.setViewportView(msgDisp);

        msgField.setText("Type your message here");
        msgField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                msgFieldMouseClicked(evt);
            }
        });
        msgField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                msgFieldKeyPressed(evt);
            }
        });

        jMenu1.setText("File");

        menConnect.setText("Connect");
        menConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menConnectActionPerformed(evt);
            }
        });
        jMenu1.add(menConnect);

        menUsername.setText("Username...");
        menUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menUsernameActionPerformed(evt);
            }
        });
        jMenu1.add(menUsername);

        menExit.setText("Exit");
        menExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menExitActionPerformed(evt);
            }
        });
        jMenu1.add(menExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        menClrCht.setText("Clear Chat");
        menClrCht.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menClrChtActionPerformed(evt);
            }
        });
        jMenu2.add(menClrCht);

        jMenuBar1.add(jMenu2);

        menHelp.setText("Help");

        menAbt.setText("About");
        menAbt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menAbtActionPerformed(evt);
            }
        });
        menHelp.add(menAbt);

        jMenuBar1.add(menHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(btnSend)
                        .addGap(162, 456, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                            .addComponent(msgField))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(msgField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btnSend)
                        .addGap(0, 22, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menExitActionPerformed
        // TODO add your handling code here:
        //wait
        //wait
        //wait
        System.exit(0);
    }//GEN-LAST:event_menExitActionPerformed

    private void menConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menConnectActionPerformed
        c.setServer(JOptionPane.showInputDialog(null, "Please enter the IP "
                + "adress you are connecting to:", "Enter IP",
                JOptionPane.CANCEL_OPTION), 7862);
        clientThread = new Thread(c);
        clientThread.start();
        c.setUsername(username);
//        connections.add(c.getUsername());
//       
//        for (String s : connections) {
//            ((DefaultListModel) lm).addElement(s);
//            conectionList.setModel(lm);
//            
//        }

    }//GEN-LAST:event_menConnectActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        String message = msgField.getText();
        c.sendMessageToAll(message);
        //msgDisp.append(message);
        msgField.setText("");
    }//GEN-LAST:event_btnSendActionPerformed

    private void msgFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_msgFieldMouseClicked
        count++;
        if (count == 1) {
            msgField.setText("");
        }
    }//GEN-LAST:event_msgFieldMouseClicked

    private void menUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menUsernameActionPerformed
        username = (JOptionPane.showInputDialog(null, "Enter a user name: ", "User name",
                JOptionPane.CANCEL_OPTION));
        msgDisp.append("Welcome, " + username + "\n");
        setTitle(username + "'s chat");
    }//GEN-LAST:event_menUsernameActionPerformed

    private void msgFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_msgFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnSendActionPerformed(null);
        }
    }//GEN-LAST:event_msgFieldKeyPressed

    private void menClrChtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menClrChtActionPerformed
        msgDisp.setText("");
    }//GEN-LAST:event_menClrChtActionPerformed

    private void menAbtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menAbtActionPerformed
        JOptionPane.showConfirmDialog(null, "nothing here yet!");
    }//GEN-LAST:event_menAbtActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });

    }

    private void displayConnetion(String username) {

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JList conectionList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem menAbt;
    private javax.swing.JMenuItem menClrCht;
    private javax.swing.JMenuItem menConnect;
    private javax.swing.JMenuItem menExit;
    private javax.swing.JMenu menHelp;
    private javax.swing.JMenuItem menUsername;
    private javax.swing.JTextArea msgDisp;
    private javax.swing.JTextField msgField;
    // End of variables declaration//GEN-END:variables

}
