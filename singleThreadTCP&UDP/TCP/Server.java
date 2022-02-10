
import java.net.*;
import java.util.HashMap;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

import java.io.*;


public class Server{
     ServerSocket ss = null;
     Socket client = null;

     /**
      * Constructor
      * Create a new ServerSocket at the port 9090
      */
     public Server(){
          try{
               this.ss = new ServerSocket(9090);
          }catch(Exception e){
               Helper.print("Error in server's constructor...");
          }
     };

     /**
      * Connect with a client and create a thread to handle the client
      */
     public void startServer(){
          try{
               this.client = this.ss.accept();
               Helper.print("New client is connected!");
               ClientHandler ch = new ClientHandler(this.client);
               Thread thread = new Thread(ch);
               thread.start();
          }catch(IOException e){
               Helper.print("Error in starting server..");
               closeEverything();
          }
     }

     /**
      * helper function 
      * Close serversocket 
      */
     private void closeEverything(){
          try{
               if (this.ss!=null){
                    this.ss.close();
               }
          }catch(Exception e){
               Helper.print("Error in closing server...");
          }
     }

     

     /**
      * Entry point of server
      * @param args
      * @throws IOException
      */
     public static void main(String[] args){ 
          try{
               Server server = new Server();
               Helper.print("Server is running on port 9090");
               server.startServer();
          }catch(Exception e){
               Helper.print("Error in server main section");
          }     
          
          
     }

}
