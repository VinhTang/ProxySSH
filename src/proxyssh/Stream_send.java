/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyssh;

import java.net.Socket;

import com.jcraft.jsch.Channel;
import java.io.DataOutputStream;
import java.io.InputStream;
/*
 Stream receive: Client -> Proxy -> Server
 */

public class Stream_send extends Thread {

    Socket Sock;
    InputStream in;

    public Stream_send(Socket S, InputStream in) {
        this.in = in;
        this.Sock = S;
    }

    public void run() {
        try {
            //Stream_client client = new Stream_client(Sock);
            DataOutputStream send = new DataOutputStream(Sock.getOutputStream());

            String result = null;
            byte[] tmp = new byte[1024];
            int time = 0;
            while (true) {
//                time++;
//                if (time == 10000000) {
//                    break;
//                    
//                }
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    result = (new String(tmp, 0, i));
                    System.out.print(result);
                    send.writeUTF(result);
                }
            }

        } catch (Exception e) {
        }

    }
}
