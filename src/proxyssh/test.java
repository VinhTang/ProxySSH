/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyssh;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

import java.io.PipedOutputStream;
import java.io.PipedInputStream;

/**
 *
 * @author Milky_Way
 */
public class test {

    public static Session session = null;

    public static Channel myChannel;

    public static void getIn(InputStream ins) throws IOException {

        byte[] tmp = new byte[1024];
        String result = null;
        int time = 0;
        int i=0;
        while (true) {
            
            while (ins.available() > 0) {
                i = ins.read(tmp, 0, 1024);
                System.out.println(i);
                if (i < 0) {
                    break;
                }
                result = (new String(tmp, 0, i));
                System.out.println(result);
                System.out.println(i);

            }
        }
    }

    public static void main(String[] args) throws IOException, JSchException {
        JSch jsch = new JSch();

        session = jsch.getSession("vinh", "192.168.10.102", 22);

        session.setPassword("123");
        session.setConfig("StrictHostKeyChecking", "no");
        System.out.println("Connected");
        session.connect();
        myChannel = session.openChannel("shell");

        System.out.println("toi day");

        //Inputstream
        PipedInputStream pipeIn = new PipedInputStream();
        PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
        myChannel.setInputStream(pipeIn);

        InputStream in = myChannel.getInputStream();
        String cmd = "ls\nifconfig\n";

        myChannel.connect();

//            PipedOutputStream pos = new PipedOutputStream();
//            PipedInputStream pis = new PipedInputStream(pos);
//            myChannel.setOutputStream(pos);
        int i = 0;
        while (i < 2) {
            i++;
            System.out.println(i + " -------------------------");
            pipeOut.write(cmd.getBytes());
            getIn(in);

        }
        System.out.println("OUT ROI NE");
        System.out.println("qua");
        if (session == null) {
            System.out.println("ok");
        } else {
            System.out.println("false");
        }

        if (session.isConnected() == true) {
            System.out.println("Dang connect");
        } else {
            System.out.println("khong biet bi gi nua");
        }
        System.out.println("------------------------disconnect--------------------------");
        session.disconnect();
        if (session == null) {
            System.out.println("ok");
        } else {
            System.out.println("false");
        }
        if (session.isConnected() == true) {
            System.out.println("Dang connect");
        } else {
            System.out.println("khong biet bi gi nua");
        }
    }
}
