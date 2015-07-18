/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProxySSH;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Milky_Way
 */
public class Server {

    public static void main(String[] args) {
        try {
            

            
            System.out.println("Test" + args.toString());
            if (args.length < 2) {
                throw new IllegalArgumentException("Must start at least one server");
            }
            Server s = new Server(System.out, 10);

            int i = 0;
            while (i < args.length) {
                if (args[i].equals("-control")) {
                    i++;
                    String password = args[i++];
                    int port = Integer.parseInt(args[i++]);
                    s.addService(new Control(s, password), port);
                    System.out.println("day");
                } else {
                    String serviceName = args[i++];
                    Class serviceClass = Class.forName(serviceName);
                    Service service = (Service) serviceClass.newInstance();
                    int port = Integer.parseInt(args[i++]);
                    s.addService(service, port);
                    System.out.println(serviceName +"-"+serviceClass+"-"+port);
                    System.out.println("ne");
                }
        
            }

        } catch (Exception ex) {
            System.err.println("Server " + ex);
            System.exit(1);
        }
    }

    ConnectionManager connectionManager;
    Hashtable services;
    ThreadGroup threadGroup;
    PrintWriter logStream;

    public Server(OutputStream logStream, int maxConnections) {
        setLogStream(logStream);
        log("Starting server");
        System.out.println("Log bật");
        threadGroup = new ThreadGroup("Server");
        connectionManager = new ConnectionManager(threadGroup, maxConnections);
        connectionManager.start();
        services = new Hashtable();
    }

    public void setLogStream(OutputStream out) {

        if (out != null) {
            logStream = new PrintWriter(new OutputStreamWriter(out));
        } else {
            logStream = null;
        }
    }

    protected synchronized void log(String s) {
System.out.println("[" + new Date() + "]" + s);
        if (logStream != null) {
            logStream.println("[" + new Date() + "]" + s);
            
            logStream.flush();
        }
        
    }

    protected void log(Object o) {
        log(o.toString());
        
    }

    public void addService(Service service, int port) throws IOException {
        Integer key = new Integer(port);
        if (services.get(key) != null) {
            throw new IllegalArgumentException("Port " + port + " already in use.");
        }

        Listener listener = new Listener(threadGroup, port, service);
        services.put(key, listener);
        log("Starting service " + service.getClass().getName() + "on port " + port);
        listener.start();
        System.out.println("Test start listenner" + key +" -|||||- "+ service.getClass().getName() +" -|||||- "+ listener) ;

    }

    public void removeService(int port) {
        Integer key = new Integer(port);
        final Listener listener = (Listener) services.get(key);
        if (listener == null) {
            return;
        }
        listener.pleaseStop();
        services.remove(key);
        log("Stopping service " + listener.service.getClass().getName() + " on port " + port);
    }

    public class Listener extends Thread {

        ServerSocket listen_socket;
        int port;
        Service service;
        boolean stop = false;

        public Listener(ThreadGroup group, int port, Service service) throws IOException {
            super(group, "Listener: " + port);
            listen_socket = new ServerSocket(port);
            listen_socket.setSoTimeout(600000);
            this.port = port;
            this.service = service;
        }

        public void pleaseStop() {
            this.stop = true;
            this.interrupt();
        }

        public void run() {
            while (!stop) {
                try {
                    Socket client = listen_socket.accept();
                    connectionManager.addConnection(client, service);
                } catch (Exception e) {
                    System.err.println("Loi run: " + e);
                }

            }
        }
    }

    public class ConnectionManager extends Thread {

        int maxConnections;
        Vector connections;

        public ConnectionManager(ThreadGroup group, int maxConnections) {
            super(group, "ConnectionManager");
            this.setDaemon(true);
            this.maxConnections = maxConnections;
            connections = new Vector(maxConnections);
            log("Starting connection manger. Max connections: " + maxConnections);
        }

        synchronized void addConnection(Socket s, Service service) {
            if (connections.size() >= maxConnections) {
                try {
                    PrintWriter out = new PrintWriter(s.getOutputStream());
                    out.printf("Connection refused; server has reached maxium number of clients.");
                    out.flush();
                    log("Connection refused to " + s.getInetAddress().getHostAddress()
                            + ":" + s.getPort() + ": max connecitons reached");
                } catch (IOException e) {
                    log(e);
                }
            } else {
                Connection c = new Connection(s, service);
                connections.addElement(c);
                log("Connected to" + s.getInetAddress().getHostAddress()
                        + ": " + s.getPort() + " on port " + s.getLocalPort()
                        + "for service " + service.getClass().getName());
                c.start();
            }
        }

        public synchronized void endConnection() {
            this.notify();
        }

        public synchronized void setMaxConnections(int max) {
            maxConnections = max;
        }

        public synchronized void printConnections(PrintWriter out) {
            for (int i = 0; i < connections.size(); i++) {
                Connection c = (Connection) connections.elementAt(i);
                out.println("CONNECTED TO "
                        + c.client.getInetAddress().getHostAddress() + ":"
                        + c.client.getPort() + " ON PORT " + c.client.getLocalPort()
                        + "FOR SERVICE " + c.service.getClass().getName());
            }
        }

        public void run() {
            while (true) {
                for (int i = 0; i < connections.size(); i++) {
                    Connection c = (Connection) connections.elementAt(i);
                    if (!c.isAlive()) {
                        connections.removeElementAt(i);
                        log("Connection to " + c.client.getInetAddress().getHostAddress()
                                + ":" + c.client.getPort() + " closed.");
                    }
                }
                try {
                    synchronized (this) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public class Connection extends Thread {

        Socket client;     // The socket to talk to the client through
        Service service;   // The service being provided to that client

        public Connection(Socket client, Service service) {
            super("Server.Connection:" + client.getInetAddress().getHostAddress()
                    + ":" + client.getPort());
            this.client = client;
            this.service = service;
        }

        public void run() {
            try {
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();
                service.serve(in, out);
            } catch (IOException e) {
                log(e);
            } finally {
                connectionManager.endConnection();
            }
        }
    }

    public interface Service {

        public void serve(InputStream in, OutputStream out) throws IOException;
    }

    public static class Time implements Service {

        public void serve(InputStream i, OutputStream o) throws IOException {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(o));
            out.println(new Date());
            out.close();
            i.close();
        }

    }
//example

    public static class Reverse implements Service {

        public void serve(InputStream i, OutputStream o) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(i));
            PrintWriter out
                    = new PrintWriter(new BufferedWriter(new OutputStreamWriter(o)));
            out.println("Welcome to the line reversal server.");
            out.println("Enter lines.  End with a '.' on a line by itself");
            for (;;) {
                out.print("> ");
                out.flush();
                String line = in.readLine();
                if ((line == null) || line.equals(".")) {
                    break;
                }
                for (int j = line.length() - 1; j >= 0; j--) {
                    out.print(line.charAt(j));
                }
                out.println();
            }
            out.close();
            in.close();
        }
    }

    public static class Control implements Service {

        Server server;             // The server we control
        String password;           // The password we require
        boolean connected = false; // Whether a client is already connected to us

        /**
         * Create a new Control service. It will control the specified Server
         * object, and will require the specified password for authorization
         * Note that this Service does not have a no argument constructor, which
         * means that it cannot be dynamically instantiated and added as the
         * other, generic services above can be.
         *
         */
        public Control(Server server, String password) {
            this.server = server;
            this.password = password;
        }

        /**
         * This is the serve method that provides the service. It reads a line
         * the client, and uses java.util.StringTokenizer to parse it into
         * commands and arguments. It does various things depending on the
         * command.
         *
         */
        public void serve(InputStream i, OutputStream o) throws IOException {
            // Setup the streams
            BufferedReader in = new BufferedReader(new InputStreamReader(i));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(o));
            String line;
            boolean authorized = false;  // Has the user has given the password yet?
            int num;

            // If there is already a client connected to this service, display a
            // message to this client and close the connection.  We use a 
            // synchronized block to prevent a race condition.
            synchronized (this) {
                if (connected) {
                    out.println("ONLY ONE CONTROL CONNECTION ALLOWED AT A TIME.");
                    out.close();
                    return;
                } else {
                    connected = true;
                }
            }

            for (;;) {  // infinite loop
                out.print("> ");           // Display a prompt
                out.flush();               // Make it appear right away
                line = in.readLine();      // Get the user's input
                if (line == null) {
                    break;   // Quit if we get EOF.
                }
                try {
                    // Use a StringTokenizer to parse the user's command
                    StringTokenizer t = new StringTokenizer(line);
                    if (!t.hasMoreTokens()) {
                        continue;  // if input was blank line
                    }          // Get the first word of the input and convert to lower case
                    String command = t.nextToken().toLowerCase();
                    // Now compare it to each of the possible commands, doing the
                    // appropriate thing for each command
                    if (command.equals("password")) {       // Password command
                        String p = t.nextToken();             // Get the next word of input
                        if (p.equals(this.password)) {        // Does it equal the password
                            out.println("OK");                  // Say so
                            authorized = true;                  // Grant authorization
                        } else {
                            out.println("INVALID PASSWORD"); // Otherwise fail
                        }
                    } else if (command.equals("add")) {       // Add Service command
                        // Check whether password has been given
                        if (!authorized) {
                            out.println("PASSWORD REQUIRED");
                        } else {
                            // Get the name of the service and try to dynamically load
                            // and instantiate it.  Exceptions will be handled below
                            String serviceName = t.nextToken();
                            Class serviceClass = Class.forName(serviceName);
                            Service service;
                            try {
                                service = (Service) serviceClass.newInstance();
                            } catch (NoSuchMethodError e) {
                                throw new IllegalArgumentException("Service must have a "
                                        + "no-argument constructor");
                            }
                            int port = Integer.parseInt(t.nextToken());
                            // If no exceptions occurred, add the service
                            server.addService(service, port);
                            out.println("SERVICE ADDED");      // acknowledge
                        }
                    } else if (command.equals("remove")) {   // Remove service command
                        if (!authorized) {
                            out.println("PASSWORD REQUIRED");
                        } else {
                            int port = Integer.parseInt(t.nextToken());  // get port
                            server.removeService(port);     // remove the service on it
                            out.println("SERVICE REMOVED"); // acknowledge
                        }
                    } else if (command.equals("max")) {      // Set max connection limit
                        if (!authorized) {
                            out.println("PASSWORD REQUIRED");
                        } else {
                            int max = Integer.parseInt(t.nextToken());        // get limit
                            server.connectionManager.setMaxConnections(max);  // set limit
                            out.println("MAX CONNECTIONS CHANGED");           // acknowledge
                        }
                    } else if (command.equals("status")) {    // Status Display command
                        if (!authorized) {
                            out.println("PASSWORD REQUIRED");
                        } else {
                            // Display a list of all services currently running
                            Enumeration keys = server.services.keys();
                            while (keys.hasMoreElements()) {
                                Integer port = (Integer) keys.nextElement();
                                Listener listener = (Listener) server.services.get(port);
                                out.println("SERVICE " + listener.service.getClass().getName()
                                        + " ON PORT " + port);
                            }
                            // Display a list of all current connections
                            server.connectionManager.printConnections(out);
                            // Display the current connection limit
                            out.println("MAX CONNECTIONS: "
                                    + server.connectionManager.maxConnections);
                        }
                    } else if (command.equals("help")) {            // Help command
                        // Display command syntax.  Password not required
                        out.println("COMMANDS:\n"
                                + "\tpassword <password>\n"
                                + "\tadd <service> <port>\n"
                                + "\tremove <port>\n"
                                + "\tmax <max-connections>\n"
                                + "\tstatus\n"
                                + "\thelp\n"
                                + "\tquit");
                    } else if (command.equals("quit")) {
                        break;    // Quit command.  Exit.
                    } else {
                        out.println("UNRECOGNIZED COMMAND");  // Unknown command error
                    }
                } catch (Exception e) {
                    // If an exception occurred during the command, print an error
                    // message, then output details of the exception.
                    out.println("EXCEPTION WHILE PARSING OR EXECUTING COMMAND:");
                    out.println(e);
                }
            }
            // Finally, when the loop command loop ends, close the streams
            // and set our connected flag to false so that other clients can
            // now connect.
            out.close();
            in.close();
            connected = false;
        }

    }
}
