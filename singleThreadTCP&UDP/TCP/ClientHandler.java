import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.HashMap;

public class ClientHandler implements Runnable {

     Socket client;
     BufferedReader br;
     BufferedWriter bw;
     HashMap<String, String> store = new HashMap<>();
     java.util.Date time;

     /** 
      * Constructor
      Use the socket passed from server;
      Initiate BufferedReader/BufferedWriter;
     */
     public ClientHandler(Socket s){
          try{
               this.client = s;
               this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
               this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
          }catch(Exception e){
               Helper.print("Error in starting thread");
               closeEverything();
          }
          
     }


     /**
      * close socket & BufferedReader/BufferedWriter
      */
     private void closeEverything(){
          try{
               if (!this.client.isClosed()){this.client.close();}
               if (this.br!=null){this.br.close();}
               if (this.bw!=null){this.bw.close();}
          }catch(Exception e){
               Helper.print("Error in closing thread");
          }
          
     }

     /**
      * helper function
      send msg to server through BufferedWriter
      * @param msg : a string which will be sent to server
      */
     private void msgClient(String msg){
          try{
               time = new java.util.Date();
               this.bw.write(msg+ " [ "+ time+" ]");
               this.bw.newLine();
               this.bw.flush();
          }catch(IOException e){
               Helper.print("Can't message client");
          }
     }

     /**
      * helper function
      Provide client options:[PUT, GET, DELETE, DISPLAY, EXIT]
      */
     private void askOperation(){
          try{
               msgClient("");
               msgClient("Operations: 1(Put new pair), 2(Get a value of key), 3(delete a pair), 4(exit), 5(display all) Please enter 1, 2, 3, 4 or 5 only");
          }catch(IOException e){
               Helper.print("Error in messaging client");
          }
     }

     /**
      * helper function
      Print the head of chat bot to greet client
      */
     private void greeting(){
          try{
               msgClient("======= Key-value store =======");
               msgClient("Welcome to the store! ");
          }catch(IOException e){
               Helper.print("Error in client handler");
               closeEverything();
          }
     }

     /**
      * helper function
      call different operation based on client's choice
      * @param num : the operation that client wants to do
      */
     private void handleRequest(int num){
          switch(num){
               case(1):
                    Put();
                    break;
               case(2):
                    Get();
                    break;
               case(3):
                    Delete();
                    break;
               case(5):
                    display();
                    break;
          }
     }
     
     /**
      * DISPLAY Operation
      display all the key-value pair in store,
      otherwise, tell client it is empty.
      */
     private void display(){
          if (this.store.keySet().isEmpty()){
               msgClient("Your store is empty!");
          }else{
               for (String key: this.store.keySet()){
                    msgClient("Key: "+key+"; Value:"+this.store.get(key));
               }
          }
     }

     /**
      * PUT Operation
      ask client for key and value
      add key-value into store hashmap
      */
     private void Put(){
          try{
               String key;
               String value;
               msgClient("What is the key: ");
               key = this.br.readLine();
               msgClient("What is the value: ");
               value = this.br.readLine();
               store.put(key, value);
               msgClient("Done! Added "+key+": "+value+" into the store.");
          }catch(IOException e){
               Helper.print("Error in PUT operation");
               msgClient("Error in Server's PUT operation ");
          }
     }

     /**
      * GET Operation
      ask client for key 
      if the store contains key: return value
      otherwise, let client know it doesn't exist
      */
     private void Get(){
          try{
               String key;
               String value;
               msgClient("What is the key: ");
               key = this.br.readLine();
               if (store.containsKey(key)){
                    value = store.get(key);
                    msgClient("The value of "+key+" is : "+value);
               }else{
                    msgClient("Sorry, There is no "+key+" in the store.");
               }
          }catch(IOException e){
               Helper.print("Error in GET operation");
               msgClient("Error in Server's GET operation ");
          }
     }
     /**
      * DELETE Operation
      ask client for key
      if the store contains key: delete it
      otherwise, let client know it dones't exist
      */
     private void Delete(){
          try{
               String key;
               String value;
               msgClient("What is the key: ");
               key = this.br.readLine();
               if (store.containsKey(key)){
                    store.remove(key);
                    msgClient("Just deleted "+key+" pair in store");
               }else{
                    msgClient("Sorry, There is no "+key+" in the store.");
               }
          }catch(IOException e){
               Helper.print("Error in DELETE operation");
               msgClient("Error in Server's DELETE operation ");
          }
     }

     /**
      * Thread will keep running until client choose to exit
      */
     @Override
     public void run(){
          String request;
          // greet the client 
          greeting();
          while (this.client.isConnected()){
               try{
                    // let client choose the operation 
                    askOperation();

                    // get client's input
                    request = this.br.readLine();

                    // deal with client's choice
                    if (request == null){
                         Helper.print("no request from client, shutting down the server for this client");
                         break;
                    }else if (request.equals("4")){
                         Helper.print("Client exited the server, shut down server now.");
                         break;
                    }else if (request.equals("1")||request.equals("2")||request.equals("3") || request.equals("5")){
                         Helper.print("Message from client is: "+request);
                         handleRequest(Integer.valueOf(request));
                    }else{
                         msgClient("Invalid input, please try again, only enter 1 or 2 or 3 or 4 please.");
                    }
               }catch(IOException e){
                    Helper.print("Error in client handler");
                    closeEverything();
               }
          }
          closeEverything();
     }
}
