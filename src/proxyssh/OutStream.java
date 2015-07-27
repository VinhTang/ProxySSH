/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyssh;


import java.io.DataOutputStream;
import java.net.Socket;

/**
 *
 * @author Milky_Way
 */
public class OutStream extends Thread {

    Socket server;

    public OutStream(Socket s) {
        server = s;
    }

    public void run() {
        try {
            
            DataOutputStream send = new DataOutputStream(server.getOutputStream());
            
           
            while (true) {
//                
//                String s=null;                
//                send.writeUTF(s);
                
            }
        } catch (Exception e) {

        }
    }

}
