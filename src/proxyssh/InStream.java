/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyssh;

import java.io.DataInputStream;
import java.net.Socket;

/**
 *
 * @author Milky_Way
 */
public class InStream extends Thread {

    Socket server;

    public InStream(Socket s) {
        server = s;
    }

    public void run() {
        try {
            // kiem tra user
            DataInputStream in = new DataInputStream(server.getInputStream());   
            String inf = in.readUTF();
//            
//            String user = inf.substring(0, inf.indexOf("@"));
//            String password =inf.substring(inf.indexOf("@")+2);
            //tao 1 hàm kiem tra user pass word ở đây !!!!
            
            
            
            String receive;
            while (true) {
//                receive = in.readUTF();
//                System.out.println(receive);
            }
        } catch (Exception e) {

        }
    }

}
