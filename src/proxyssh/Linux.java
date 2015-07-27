/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Milky_Way
 */
public class Linux {

    public static Session session;
    private static Channel myChannel;

    /*------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------*/
    
    boolean isConnected(Session session) {
        if (session.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }

    // set SSH Connect
    boolean connect(String user, String remoteHost, int remotePort) {
        try {

            JSch jsch = new JSch();
            if (isConnected(session) == true) {
                return false;
            } else {
                session = jsch.getSession(user, remoteHost, remotePort);
                session.setHost(user);
                session.setHost(remoteHost);
                session.setPort(remotePort);
                // se tao 1 ham nhap password
                session.setPassword("123");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    void Inputstuream(Channel myChannel) {
        if (myChannel.isConnected() == false) {
            return;
        }

    }

    boolean letstart(Session session) {
        try {
            if (isConnected(session) == false) {
                return false;
            }
            if (myChannel.isConnected() == false) {
                myChannel = session.openChannel("shell");
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void connectLinux(String user, String remoteHost, int remotePort) throws IOException {
        try {

            JSch jsch = new JSch();
            session = jsch.getSession(user, remoteHost, remotePort);
            String cmd = "ls\nifconfig\n";
            session.setHost(user);
            session.setHost(remoteHost);
            session.setPort(remotePort);
            session.setPassword("123");

            session.setConfig("StrictHostKeyChecking", "no");

            System.out.println("Connected");
            session.connect();
            myChannel = session.openChannel("shell");

            //Inputstream
            PipedInputStream pipeIn = new PipedInputStream();
            PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
            myChannel.setInputStream(pipeIn);

            //Outputsream
            PipedOutputStream pos = new PipedOutputStream();
            PipedInputStream pis = new PipedInputStream(pos);
            myChannel.setOutputStream(pos);

            myChannel.connect(1000);

            pipeOut.write(cmd.getBytes());

            BufferedReader br = new BufferedReader(new InputStreamReader(pis));

            while (true) {

                String line = br.readLine();

                if (line == null) {
                    break;
                }

                System.out.println(line);

            }

        } catch (JSchException ex) {
            System.out.println(ex);

        }

    }

    public boolean disconnectLinux() {
        try {
            if (session.isConnected() == true) {
                myChannel.disconnect();
                session.disconnect();
                return true;
            }
            return true;
        } catch (Exception e) {
            System.out.println("loi: " + e);
            return false;
        }
    }
}
