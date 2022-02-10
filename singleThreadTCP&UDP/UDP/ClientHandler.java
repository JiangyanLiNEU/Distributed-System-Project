
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Date;
import java.util.HashMap;


public class ClientHandler implements Runnable{
     DatagramSocket ds;
     byte[] receive_buffer;
     byte[] send_buffer;
     DatagramPacket receive_dp;
     DatagramPacket send_dp;
     InetAddress ip;
     int port;
     HashMap<String, String> store;

     /**
      * Constructor
      * @param ds the DatagramSocket that server passed to
      * Initiate buffer and DatagramPocket to communicate with server
      */
     public ClientHandler(DatagramSocket ds){
          this.ds = ds;
          this.receive_buffer = new byte[65535];
          this.send_buffer = new byte[65535];
          this.receive_dp = new DatagramPacket(this.receive_buffer, this.receive_buffer.length);
          this.send_dp = new DatagramPacket(this.send_buffer, this.send_buffer.length);
     }

     /**
      * listen to client and collect the message sent from client
      * @return message or null(If error happens)
      */
     private String listen(){
          try{
               ds.receive(receive_dp);
               ip = receive_dp.getAddress();
               port = receive_dp.getPort();
               // Helper.p("Client ip: "+ip+" client port: "+port);
               String request = new String(receive_dp.getData(), 0, receive_dp.getLength());
               Helper.p("[Client]: "+request);
               return request;
          }catch(Exception e){
               Helper.p("error in listen");
               return null;
          }
     }

     /**
      * Helper function
      * @return the current time
      */
     private java.util.Date getTime(){
          return new java.util.Date();
     }

     /**
      * Helper function
      * @param msg the message which will be sent to server
      * use send_buffer and send_packet
      */
     private void msgClient(String msg){
          try{
               msg = msg+" "+getTime();
               this.send_buffer = msg.getBytes();
               this.send_dp = new DatagramPacket(this.send_buffer, this.send_buffer.length, this.ip, this.port);
               this.ds.send(this.send_dp);
          }catch (Exception e) {
               Helper.p("Error when messaging client...");
          }
          
     }

     /**
      * Helper function
      * Greet client when it first connects to server
      */
     private void greet() throws Exception{
          msgClient("Welcome to store"); 
     }

     /**
      * Helper function
      * Initialize the store for key-value pair
      */
     private void createStore(){
          this.store = new HashMap<>();
          msgClient("A new store is created for you.");
     }

     /**
      * Helper function
      * Provide client with operation options
      */
     private void askOperation(){
          msgClient("=====================");
          msgClient("1.put");
          msgClient("2.get");
          msgClient("3.delete");
          msgClient("4.display all");
          msgClient("5.exit");
          msgClient("Please enter 1 or 2 or 3 or 4 or 5");
          msgClient("=====================");
     }

     /**
      * Helper function
      * @param num operation that client chosed
      */
     private void handleRequest(int num){
          switch(num){
               case 1:
                   put();
                   break;
               case 2:
                   get();
                   break;
               case 3:
                   delete();
                   break;
               case 4:
                   display();
                   break;
          }
     }

     /**
      * PUT operation
      * ask for key and value
      * add it to store
      */
     private void put(){
          msgClient("What is the key: ");
          String key = listen();
          msgClient("What is the value: ");
          String value = listen();
          this.store.put(key, value);
          msgClient("Added!");
     }
     
     /**
      * GET operation
      * ask for key
      * return value if key exists in store
      * otherwise inform client it doesn't exist
      */
     private void get(){
          msgClient("What is the key: ");
          String key = listen();
          if (this.store.containsKey(key)){
               msgClient("Value: "+this.store.get(key));
          }else{
               msgClient("Key not founded!");
          }
     }

     /**
      * DELETE operation
      * ask for key
      * delete it if key exists in store
      * otherwise inform client it doesn't exist
      */
     private void delete(){
          msgClient("What is the key: ");
          String key = listen();
          if (this.store.containsKey(key)){
               this.store.remove(key);
               msgClient("Deleted "+key);
          }else{
               msgClient("Key not founded!");
          }
     }

     /**
      * DISPLAY operation
      * print all the key-value in store
      * otherwise inform client it is empty
      */
     private void display(){
          msgClient("=====================");
          if (this.store.keySet().isEmpty()){
               msgClient("Your store is empty now!");
          }else{
               for (String key : this.store.keySet()){
                    msgClient("Key: "+key+" Value: "+this.store.get(key));
               }
          }
          msgClient("=====================");
     }
     
     /**
      * Thread run
      * listen first to get client's port and ip address
      * greet client and initiate store
      * keep asking operations and handle the request
      */
     @Override
     public void run(){
          // get ip and port
          listen();

          // greet client 
          greet();

          // Initialize the store
          createStore();

          // keep asking operations
          while (true){
               askOperation();
               String choice = listen();
               choice = choice.trim();
               if (choice==null){
                    msgClient("Empty input, please try again...");
               }else if (choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4")){
                    handleRequest(Integer.valueOf(choice));
               }else if(choice.equals("5")){
                    msgClient("Thanks for trying store! BYE!");
                    break;
               }else{
                    msgClient("Invalid input, please try again...");
               }
          }
          
     }
}

























// public class ClientHandler extends Thread {
//      DatagramSocket ds;
//      DatagramPacket dp;
//      byte[] buffer;
//      InetAddress clientIP;
//      int clientPort=-1;
//      HashMap<String, String> store;
  
//      public ClientHandler(Server server){
//           this.ds = server.ds;
//           this.dp = server.dp;
//           this.buffer = server.buffer; 
//      }

//      private void closeEverything(){
//           if (ds.isConnected()){ds.close();}
//      }

//      private void msgClient(String msg){
//           if (this.clientIP==null || this.clientPort==-1){
//                Helper.p("We can't message client for lack of its IP address and port number...");
//           }else{
//               try{
//                    Helper.p("msg send to client is : "+msg);
//                //     byte[] buffer = new byte[65535];
//                    buffer = msg.getBytes();
//                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length, this.clientIP, this.clientPort);
//                    this.ds.send(dp);
//                // this.buffer = msg.getBytes();
//                // this.dp = new DatagramPacket(buffer, buffer.length, this.clientIP, this.clientPort);
//                // this.ds.send(this.dp);
//                //     Helper.p("[Sent to client]");
//               }catch(Exception e){
//                    Helper.p("Error in client handler msgClient method...");
//               } 
//           }
//      }

//      private void getInfo(){
//           try{
//                     ds.receive(dp);
//                     //  extract infomation about client
//                     this.clientIP = dp.getAddress();
//                     this.clientPort = dp.getPort();
//                     String msg = new String(dp.getData(), 0, dp.getLength());
//                     // Helper.p("[Client: ] "+msg);
//                     // Helper.p("Got Client's infomation: ");
//                     Helper.p("[Client ip: "+this.clientIP+" and Client port: "+this.clientPort+"]");

//                }catch(Exception e){
//                     Helper.p("Error in client handler listening method...");
//                }
          
//      }
//      private void askOperation(){
//           msgClient("Please enter 1 or 2 or 3:");
//           msgClient("1:Put a key-value");
//           msgClient("2:Get value of a key");
//           msgClient("3:Delete a key-value");

//      }
//      private void startStore(){
//           store = new HashMap<>();
//           msgClient("Welcome to key-value store");
//      }

//      private String getRequest(){
//           Helper.p("Inside getRequest method");
//           try{
//                ds.receive(dp);
//                String request = new String(dp.getData(), 0, dp.getLength());
//                return request;
//           }catch(Exception e){
//                Helper.p("Error in getRequest in server");
//                return null;
//           }
//      }

//      private void handleRequest(int num){
//           Helper.p("Inside handleRequest method");
//           switch (num){
//                case 1:
//                    put();
//                    break;
//                case 2:
//                    get();
//                    break;
//                case 3:
//                    delete();
//                    break;
//                default:
//                    break;
//           }
//      }

//      private void put(){
//           Helper.p("Inside put method");
//           msgClient("What is the key?");
//           String key = getRequest();
//           msgClient("What is the value?");
//           String value = getRequest();
//           store.put(key, value);
//           msgClient("Done! "+key+"-"+value+" is added into the store.");
//      }
//      private void get(){
//           Helper.p("Inisde get method");
//           msgClient("What is the key?");
//           String key = getRequest();
//           if (key==null){
//                msgClient("The key you input is null, I can't find it.");
//           }else if (store.containsKey(key)){
//                String value = store.get(key);
//                msgClient("The value of "+key+" is: "+value);
//           }else{
//                msgClient("There is no "+key +"i n store.");
//           }
//      }
//      private void delete(){
//           Helper.p("Inisde delete method");
//           msgClient("What is the key?");
//           String key = getRequest();
//           if (key==null){
//                msgClient("The key you input is null, I can't find it.");
//           }else if (store.containsKey(key)){
//                store.remove(key);
//                msgClient("Done! "+key+" is removed!");
//           }else{
//                msgClient("There is no "+key +"i n store.");
//           }
//      }

//      @Override
//      public void run(){
//           try{
//                getInfo();
//                startStore();
//                while (true){
//                     askOperation();
//                     String choice = getRequest();
//                     if (choice == null){
//                          msgClient("I didn't catch what you entered.");
//                     }else if (choice.equals("1")||choice.equals("2")||choice.equals("3")){
//                          Helper.p(("[Client choosed: ] "+choice));
//                          handleRequest(Integer.valueOf(choice));
//                     }else{
//                          msgClient("Invalid input!");
//                     }

//                }
//           }catch(Exception e){
//                Helper.p("Error in client handler");
//           }
          
//      }
// }
