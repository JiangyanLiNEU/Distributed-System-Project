
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.crypto.Data;

public class S {
     DatagramSocket ds;

     /**
      * Constructor
      * @param port the surver will be running on
      */
     public S(int port){
          try{
               this.ds = new DatagramSocket(port);
          }catch(IOException e){
               Helper.p("Error in server constructor");
          }
     }
  
     /**
      * Create a thread and start it to handle client
      */
     public void startThread(){
          ClientHandler ch = new ClientHandler(ds);
          Thread thread = new Thread(ch);
          thread.start();
     }
     /**
      * Entry point of UDP Server
      * Create a new server and start the thread to handle client
      */
     public static void main(String[] args) throws Exception {
          try{
               S server = new S(111);
               Helper.p("Server is runnong on port "+111);
               server.startThread();
          }catch(Exception e){
               Helper.p("Error in main section ");
          }
          
     }
}
