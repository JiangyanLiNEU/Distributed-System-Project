
import java.net.Socket;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class TCPclient {
     
     Socket s;
     BufferedWriter bw;
     BufferedReader br;
     
     /**
      * Constructor 
      * @param socket passed by user based on user's IP and port input
      * Initiate BufferedWriter/BufferedReader
      */
     public TCPclient(Socket socket) throws IOException{
          try{
               this.s = socket;
               this.bw = new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream()));
               this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
          }catch(Exception e){
               Helper.print("Error in TCPclient constructor...");
          }
          
     }

     /**
      * helper function 
      * send message which user input in terminal to server
      * it will keep listening to input from client
      */
     private void sendMsg(){
          try{
               Scanner scan = new Scanner(System.in);
               while (this.s.isConnected()){
                    String msg = scan.nextLine();
                    this.bw.write(msg);
                    this.bw.newLine();
                    this.bw.flush();
               }
          }catch(IOException e){
               Helper.print("Error when sending msg to server");
               closeEverything();
          }
     }

     /**
      * Client keeps listenning to server
      */
     private void listenServer(){
          new Thread(new Runnable() {
               @Override
               public void run(){
                    String msg;
                    while (s.isConnected()){
                         try{
                              // Get message from server
                              msg = br.readLine();
                              if (msg==null){
                                   Helper.print("Server is shutted down. If you want to use server again, please restart:)");
                                   break;
                              }else{
                                   Helper.print(msg);
                              }
                         }catch(IOException e){
                              Helper.print("Error while listenning to server...");
                              closeEverything();
                         }
                    }
                    // if server is not connected, then close client too
                    Helper.print("Server is shutted down, Client will be closed now too.");
                    closeEverything();
               }
          }).start();
     }

     /**
      * helper function
      * close socket, BufferedWriter and BufferedReader
      */
     private void closeEverything(){
          try{
               if (!this.s.isClosed()){this.s.close();}
               if(this.br!=null){this.br.close();}
               if(this.bw!=null){this.bw.close();}
          }catch(Exception e){
               Helper.print("error in closing client");
          }
     }

     /**
      * Entry point of client
      * Ask client for IP and port => create socket
      * create a new client => keep listening to server and messaging server
      */
     public static void main(String[] args) throws IOException {
          Scanner scanner = new Scanner(System.in);
          Helper.print("Enter the IP address of the server: ");
          String ip = scanner.nextLine();
          Helper.print("Enter the port number of the server: ");
          String port = scanner.nextLine();
          int portNumber = Integer.valueOf(port);
          try{
               Socket ss = new Socket(ip, portNumber);
               TCPclient client = new TCPclient(ss);
               client.listenServer();
               client.sendMsg();
               scanner.close();
          }catch(IOException e){
               Helper.print("Sorry I can't find this server, please check your ip address and port number.");
          }
     }
}
