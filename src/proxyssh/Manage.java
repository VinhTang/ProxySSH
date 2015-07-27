/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProxySSH;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Milky_Way
 */


public class Manage extends Thread{



    private static ServerSocket Sock;
    
    // set session connect to Linux Server
    public void run() {
        try {
            Sock = new ServerSocket(JProxy.localPort);
            JProxy.flag=true;
            while (true) {
                //luong ket noi tu client den server  
                Socket client = Sock.accept();
                client.setSoTimeout(10000);
                
                boolean check=false;
                System.out.println(client.getInetAddress().toString()+":"+client.getPort());
                do {
                    //nhan username password 
                    DataInputStream instream = new DataInputStream(client.getInputStream());
                    String inf = instream.readUTF();
                    String user = inf.substring(0, inf.indexOf("@"));
                    String password = inf.substring(inf.indexOf("@") + 2);
                    System.out.println(user + " == " + password);

                    // kiem tra pass word 
                    check = checkuser(user, password);
                    
                } while (check == false);
                
                proxyssh.InStream in = new proxyssh.InStream(client);
                proxyssh.OutStream out = new proxyssh.OutStream(client);
                in.start();
                out.start();
            }

        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    // kiem tra LDAP
    private boolean checkuser(String user, String password) {
        System.out.println(" Lay dc useraname va password: "+user +" & "+password);
        return true;
    }
    
}


