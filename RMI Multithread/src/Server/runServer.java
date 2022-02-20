package Server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * start server
 */
public class runServer {

    public static void main(String[] args) {
        try{
            // initialize a new server
            Server server = new Server();
            // register the server with the port number and name
            Registry r = LocateRegistry.createRegistry(1111);
            r.bind("StoreServer", server);
            // log to the terminal
            System.out.println("Server is running on port 1111...");
        }catch(Exception e){
            System.out.println("Error in runServer start method..");
        }

    }
}