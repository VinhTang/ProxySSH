/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProxySSH;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Milky_Way
 */
public class Manage extends Thread {

    private static ServerSocket Sock;
    public static Socket client;
    
    public void run() {
        try {
            
            Sock = new ServerSocket(JProxy.localPort);
            JProxy.flag_start = true;
            String receive;

            //luong ket noi tu client den server
            while (true) {
                client = Sock.accept();
                proxyssh.SessionUser User =new proxyssh.SessionUser(client);
                User.getUserinfo();
                //client.setSoTimeout();                
                System.out.println(client.getInetAddress().toString() + ":" + client.getPort() + " : " + client.getLocalAddress());
                
            }

        } catch (IOException ex) {
            System.out.println("Loi start proxy: "+ex);
        }
    }


}
