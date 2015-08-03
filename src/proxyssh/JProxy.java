/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProxySSH;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import proxyssh.Linux;

/**
 *
 * @author Milky_Way
 */
public class JProxy extends javax.swing.JFrame {

    // Tao tay truoc
    public static String remoteHost = "192.168.178.132";
    public static int localPort = 9090, remotePort = 22;
    

    public static boolean flag_start = false; //flag start proxy

    public JProxy() {
        initComponents();
        jBtnConnect.setVisible(false);
        jBtnDisconnect.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblstatus = new java.awt.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTxtResult = new javax.swing.JTextArea();
        jBtnStop = new javax.swing.JButton();
        jLblInfo = new javax.swing.JLabel();
        jBtnStart = new javax.swing.JButton();
        jBtnConnect = new javax.swing.JButton();
        jBtnDisconnect = new javax.swing.JButton();
        jLblServer = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Proxy");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("Proxy"); // NOI18N
        setResizable(false);

        jTxtResult.setColumns(20);
        jTxtResult.setRows(5);
        jScrollPane1.setViewportView(jTxtResult);

        jBtnStop.setText("Stop");
        jBtnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnStopActionPerformed(evt);
            }
        });

        jLblInfo.setText("Information: ");

        jBtnStart.setText("Start");
        jBtnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnStartActionPerformed(evt);
            }
        });

        jBtnConnect.setText("Connect");
        jBtnConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnConnectActionPerformed(evt);
            }
        });

        jBtnDisconnect.setText("Disconnect");
        jBtnDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnDisconnectActionPerformed(evt);
            }
        });

        jLblServer.setText("Linux: ");
        jLblServer.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLblServer, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBtnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBtnConnect, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jBtnStop, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(lblstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jBtnDisconnect)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jBtnStart)
                        .addComponent(jBtnStop)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtnConnect)
                    .addComponent(jBtnDisconnect)
                    .addComponent(jLblServer, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jBtnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnStopActionPerformed
        // TODO add your handling code here:
        Runtime.getRuntime().exit(0);
    }//GEN-LAST:event_jBtnStopActionPerformed

    private void jBtnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnStartActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (flag_start == false) {
            Manage mgt = new Manage();
            mgt.start();
            lblstatus.setText("OK");
            System.out.println("proxy: " + localPort
                    + " <destination host>: " + remoteHost
                    + " <destination port>: " + remotePort);
            jBtnConnect.setVisible(true);
            jBtnDisconnect.setVisible(true);
        } else {
            lblstatus.setText("False");
        }
    }//GEN-LAST:event_jBtnStartActionPerformed

    private void jBtnConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnConnectActionPerformed
        // TODO add your handling code here:
//
//        if (linux.session == null) {
//            linux.connect("vinh", "192.168.178.132", remotePort);
//            lblstatus.setText("Connect");
//            System.out.println("Kiem tra ngoai: " + linux.isConnected(linux.session));
//        } else {
//            if (linux.isConnected(linux.session) == true) {
//                lblstatus.setText("Connect");
//            } else {
//                lblstatus.setText("False");
//            }
//        }
    }//GEN-LAST:event_jBtnConnectActionPerformed

    private void jBtnDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnDisconnectActionPerformed
        // TODO add your handling code here:
//        Linux linux = Linux(proxyssh.SessionUser.client);
//        if (linux.disconnectLinux() == true) {
//            lblstatus.setText("Disconnect");
//        } else {
//            lblstatus.setText("False");
//        }
    }//GEN-LAST:event_jBtnDisconnectActionPerformed

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
            java.util.logging.Logger.getLogger(JProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JProxy().setVisible(true);

            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtnConnect;
    private javax.swing.JButton jBtnDisconnect;
    private javax.swing.JButton jBtnStart;
    private javax.swing.JButton jBtnStop;
    private javax.swing.JLabel jLblInfo;
    private javax.swing.JLabel jLblServer;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea jTxtResult;
    private java.awt.Label lblstatus;
    // End of variables declaration//GEN-END:variables
}
