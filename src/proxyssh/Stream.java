/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyssh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Milky_Way
 */
public class Stream {

    Socket server;

    public Stream(Socket s) {
        server = s;
    }

    public String Instream() throws IOException {
        String inf = null;
        DataInputStream in = new DataInputStream(server.getInputStream());
        inf = in.readUTF();
        return inf;
    }

    public void Outstream(String s) throws IOException {
        DataOutputStream send = new DataOutputStream(server.getOutputStream());
        send.writeUTF(s);
    }
}
