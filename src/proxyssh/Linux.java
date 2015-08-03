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
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import static proxyssh.test.myChannel;
import static proxyssh.test.session;

/**
 *
 * @author Milky_Way
 */
public class Linux extends Thread {

    public static Session session = null;
    private static Channel myChannel;

    private Socket client;
    private String remoteHost;
    private int remotePort;

    /*----------------------------------------------------------------------------*/
    public Linux(Socket sock, String Host, int Port) {
        this.remoteHost = Host;
        this.remotePort = Port;
        this.client = sock;
    }
    /*----------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------*/

    boolean isConnected(Session session) {

        if (session.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }
    /*----------------------------------------------------------------------------*/
    // set SSH Connect
    private PipedInputStream pipeIn;
    private PipedOutputStream pipeOut;

    private InputStream in;
    /*----------------------------------------------------------------------------*/

    boolean connect(String remoteHost, int remotePort) {
        try {

            if (session != null) {
                System.out.println("Da co phien sseion" + session.getHost().toString());
                return false;
            } else {

                System.out.println("vao toi day roi ne:" + remoteHost + " - " + remotePort);
                JSch jsch = new JSch();
                session = jsch.getSession("vinh", remoteHost, remotePort);
                // se tao 1 ham nhap password

                session.setPassword("123");
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
                myChannel = session.openChannel("shell");

                //create Stream
                pipeIn = new PipedInputStream();
                pipeOut = new PipedOutputStream(pipeIn);
                myChannel.setInputStream(pipeIn);

                in = myChannel.getInputStream();

                //start channel
                myChannel.connect();
                return true;
            }
        } catch (JSchException | IOException e) {
            System.out.println("false ket noi linux: " + e);
            return false;
        }
    }
    /*----------------------------------------------------------------------------*/

    public void run() {

        if (connect(remoteHost, remotePort) == false) {
            System.out.println("Co loi o giai doan ket noi");
            return;
        } else {
            System.out.println("Xong buoc connect");
            Stream str = new Stream(client);
            System.out.println("test: " + client.getInetAddress().getHostName() + "--- " + remoteHost + "@" + remotePort);

            int i = 0;
            try {
                while (true) {
                    i++;
                    System.out.println("-------------------------------");
                    System.out.println("xong lan: " + i);
                    System.out.println("-------------------------------");

                    String cmd = str.Instream();
                    Inputstream(cmd);
                    //Inputstream(str.Instream());

                    str.Outstream(getIn(in));

                }

            } catch (Exception ex) {
                System.out.println("Loi giao tiep : " + ex);
            }
        }
    }
    /*----------------------------------------------------------------------------*/

    String getIn(InputStream in) throws IOException {
        String result = null;
        byte[] tmp = new byte[1024];
        int time = 0;
        while (true) {
            time++;
            if (time  == 2) {
                break;
            }
            while (in.available() > 0) {
                time = 0;
                int i = in.read(tmp, 0, 1024);
               
                if (i < 0) {
                    time = 0;
                    break;
                }
                result = (new String(tmp, 0, i));
            }
        }
        return result;
    }

    void Inputstream(String cmd) throws IOException {
        pipeOut.write(cmd.getBytes());
    }
    /*----------------------------------------------------------------------------*/

    boolean disconnectLinux() {
        if (session == null) {
            return true;
        } else if (session.isConnected() == true) {
            //myChannel.disconnect();
            session.disconnect();
            session = null;
            return true;
        }

        return false;
    }
}
    // public void connectLinux(String user, String remoteHost, int remotePort) throws IOException {
//     try {

    //         JSch jsch = new JSch();
//         session = jsch.getSession(user, remoteHost, remotePort);
//         String cmd = "ls\nifconfig\n";
//         session.setHost(user);
//         session.setHost(remoteHost);
//         session.setPort(remotePort);
//         session.setPassword("123");
//         session.setConfig("StrictHostKeyChecking", "no");
//         System.out.println("Connected");
//         session.connect();
//         myChannel = session.openChannel("shell");
//         //Inputstream
//         PipedInputStream pipeIn = new PipedInputStream();
//         PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
//         myChannel.setInputStream(pipeIn);
//         //Outputsream
//         PipedOutputStream pos = new PipedOutputStream();
//         PipedInputStream pis = new PipedInputStream(pos);
//         myChannel.setOutputStream(pos);
//         myChannel.connect(1000);
//         pipeOut.write(cmd.getBytes());
//         BufferedReader br = new BufferedReader(new InputStreamReader(pis));
//         while (true) {
//             String line = br.readLine();
//             if (line == null) {
//                 break;
//             }
//             System.out.println(line);
//         }
//     } catch (JSchException ex) {
//         System.out.println(ex);
//     }
// }

