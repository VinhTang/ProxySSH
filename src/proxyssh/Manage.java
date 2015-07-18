/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProxySSH;

/**
 *
 * @author Milky_Way
 */

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.ServerSocket;
import java.io.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class Manage {

    public static Session session;

    boolean startproxy(int localPort) {
        try {
            ServerSocket Sock = new ServerSocket(localPort);
            return true;

        } catch (IOException e) {
            System.out.println(e);
            return false;

        }

    }

    void connectLinux(String user, String remoteHost, int remotePort) {
        try {
            String cmd="cat Desktop/logcmd.txt\nps\nexit\n";
           
            JSch jsch = new JSch();
            System.out.println(cmd);
            session = jsch.getSession(user, remoteHost, remotePort);           
            UserInfo ui = new MyUserInfo();            
            session.setUserInfo(ui);            
            
            session.connect();
            Channel channel = session.openChannel("shell");
            System.out.println("1  "+cmd);

            
            PipedInputStream pipeIn = new PipedInputStream();
            PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
            
            //FileOutputStream fileOut = new FileOutputStream("./shell.txt");
            ByteOutputStream fileOut =new ByteOutputStream();
            
            channel.setInputStream(pipeIn);
            channel.setOutputStream(fileOut);
            channel.connect();
            
            
            
            String str ="null";
                    str= fileOut.toString();
            System.out.println(str);
            System.out.println("test");
//            String result = out.toString();
//           System.out.println(result);
            
//            InputStream inStream= new ByteArrayInputStream(cmd.getBytes());
//            channel.setInputStream(inStream);
//            
//            ByteArrayOutputStream outStream =new ByteArrayOutputStream();
//            System.out.println(outStream.toString());
//            PrintStream ps =new PrintStream(outStream);
//            channel.setOutputStream(ps);
            
            
//            String result = outStream.toString();
//            System.out.println(result);
//            System.out.println("ket thuc");
//            print.println(cmd);
//            print.println("cat Desktop/logcmd.txt");
//            print.println("exit");
    //      channel.setInputStream(System.in);
    //      channel.setOutputStream(System.out);
            
            
      
        } catch (Exception e) {
        }

    }

    void disconnectLinux() {
        if (session.isConnected()) {
            session.disconnect();
        }
        return;
    }

    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

        public String getPassword() {
            return passwd;
        }

        public boolean promptYesNo(String str) {
            Object[] options = {"yes", "no"};
            int foo = JOptionPane.showOptionDialog(null,
                    str,
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo == 0;
        }

        String passwd;
        JTextField passwordField = (JTextField) new JPasswordField(20);

        public String getPassphrase() {
            return null;
        }

        public boolean promptPassphrase(String message) {
            return true;
        }

        public boolean promptPassword(String message) {
            Object[] ob = {passwordField};
            int result = JOptionPane.showConfirmDialog(null, ob, message,
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                passwd = passwordField.getText();
                return true;
            } else {
                return false;
            }
        }

        public void showMessage(String message) {
            JOptionPane.showMessageDialog(null, message);

        }
        final GridBagConstraints gbc
                = new GridBagConstraints(0, 0, 1, 1, 1, 1,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 0), 0, 0);
        private Container panel;

        public String[] promptKeyboardInteractive(String destination,
                String name,
                String instruction,
                String[] prompt,
                boolean[] echo) {
            panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            gbc.weightx = 1.0;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.gridx = 0;
            panel.add(new JLabel(instruction), gbc);
            gbc.gridy++;

            gbc.gridwidth = GridBagConstraints.RELATIVE;

            JTextField[] texts = new JTextField[prompt.length];
            for (int i = 0; i < prompt.length; i++) {
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = 0;
                gbc.weightx = 1;
                panel.add(new JLabel(prompt[i]), gbc);

                gbc.gridx = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weighty = 1;
                if (echo[i]) {
                    texts[i] = new JTextField(20);
                } else {
                    texts[i] = new JPasswordField(20);
                }
                panel.add(texts[i], gbc);
                gbc.gridy++;
            }

            if (JOptionPane.showConfirmDialog(null, panel,
                    destination + ": " + name,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE)
                    == JOptionPane.OK_OPTION) {
                String[] response = new String[prompt.length];
                for (int i = 0; i < prompt.length; i++) {
                    response[i] = texts[i].getText();
                }
                return response;
            } else {
                return null;  // cancel
            }
        }
    }

}
