
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.DrbgParameters.Reseed;
import java.util.Scanner;

public class C {

     
     byte[] buffer;
     DatagramPacket dp;
     DatagramSocket ds;
     DatagramPacket send_dp;
     byte[] send_buffer;
     InetAddress ip;
     int port;

     /**
      * Constructor :
     * Create a new DatagramSocket;
     * Initiate send and receive DatagramPacket;
     * ip and port are needed when we try to send message to server;
      */
     public C(String ip, int port){
          try{
               this.ip = InetAddress.getByName(ip);
               this.port = port;
               this.ds = new DatagramSocket();;
               this.buffer = new byte[65535];
               this.dp = new DatagramPacket(this.buffer, this.buffer.length);
               this.send_buffer = new byte[65535];
               this.send_dp = new DatagramPacket(send_buffer, send_buffer.length);
          }catch(Exception e){
               Helper.p("Error in client constructor..");
          }
          
     }

     /*
     Client will listen to server all the time 
     Therefore, I use a thread initializer here
     */
     private void listen(){
          new Thread(
               new Runnable() {
                    public void run(){
                         while (true){
                              try{
                                   ds.receive(dp);
                                   String response = new String(dp.getData(), 0, dp.getLength());
                                   Helper.p("[Server: ]"+response);
                              }catch(Exception e){
                                   Helper.p("error in listen");
                              }
                         }
                    }
               }
          ).start();
     }

     /**
      * Helper function
      * send message to server through buffer and DatagramPacket
      */
     private void msgServer(String msg){
          try{
               this.send_buffer = msg.getBytes();
               this.send_dp = new DatagramPacket(this.send_buffer, this.send_buffer.length, this.ip, this.port);
               this.ds.send(this.send_dp);
          }catch(Exception e){
               Helper.p("Error in msgServer.");
          }
          
     }
     /**
      * Helper function
      * send a message to server, so that server can get client's ip and port
      * This is crutial - client has to message server first otherwise server will never know the information about client
      */
     private void sendInfo() throws Exception{
          msgServer("New client is connected.");
     }


     /**
      * Entry point of Client
      * Ask client for IP and port => create DatagramSocket based on ip and port
      * Send server a message so that server can collect client's IP and port
      * client will keep listenning to server
      * client will also message server if anything is input into terminal
      */
     public static void main(String[] args) throws Exception {
          try{ 
               // Get ip and port
               Scanner s = new Scanner(System.in);
               Helper.p("Please enter the ip address: (should be same as server's address)");
               String ip = s.nextLine();
               Helper.p("Please enter port number: (should be same as server's port)");
               int port = Integer.valueOf(s.nextLine());
     
               // Create new client
               C client = new C(ip, port);
               Helper.p("new client is created...");
     
               // send client's ip and port to server
               client.sendInfo();
     
               // keep listenning to server
               client.listen();
               
               // message server whenever client input something in terminal
               while (true){
                    client.msgServer(s.nextLine());
               }
               
          }catch(Exception e){
               Helper.p("Error in C's main section");
          }
         
     }    
}
