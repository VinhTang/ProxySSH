package proxyssh;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionUser{

    
    public Socket client;
    private Stream stream;
    public String OK = "!@#";
    public String False = ")(*"; //098
    public boolean flag_checkuser = false;
    
    public String Host;    
    private String receive,send;
    
    
    public SessionUser(Socket S) {
        this.client = S;
        stream= new Stream(client);
        System.out.println("session: "+ client.getInetAddress());
    }

    void getInfo() {
        try {            
            
            receive = stream.Instream();
            System.out.println(receive);
            String user = receive.substring(0, receive.indexOf("@"));
            String password = receive.substring(receive.indexOf("@") + 2);
            
            if (flag_checkuser = CheckUser(user, password) == true) {
                stream.Outstream(OK);
            } else {
                stream.Outstream(False);
            }

        } catch (IOException ex) {
            Logger.getLogger(SessionUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void getHost() {
        try {   
                Host = stream.Instream();
                System.out.println(Host);                                
                stream.Outstream(OK);
            
        } catch (IOException ex) {
            Logger.getLogger(SessionUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void getUserinfo() {
        do {
            do{
                getInfo();
            }while(flag_checkuser==false);
            getHost();
            
            System.out.println("get host: (tu ham session"+Host);
            Linux linux =new Linux(client,"192.168.10.102",22);
            linux.start();
            
        } while (flag_checkuser == false);
    }
    
    // kiem tra LDAP
    private boolean CheckUser(String user, String password) {
        //System.out.println(" Lay dc useraname va password: " + user + " & " + password);
        return true;
    }
}
