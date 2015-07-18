/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProxySSH; 

import java.net.*;
import java.io.*;
import java.util.*;
import java.net.Socket;
/**
 *
 * @author Milky_Way
 */
public class Proxy {
    public static void main(String[] args) {
        try{
            JProxy proxy =new JProxy();
            proxy.setVisible(true);
            if(args.length == 0 || args.length%3!=0)
                throw new IllegalArgumentException("Sai tham so truyen vao");
            
        }catch(Exception e){
        }
    }
    
}
