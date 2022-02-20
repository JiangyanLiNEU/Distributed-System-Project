package Client;

import Server.ServerIF;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


/**
 * run Client 1
 */
public class runClient {
    public static void main(String[] args) {
        try{
            // find the server
            Registry r = LocateRegistry.getRegistry("localhost", 1111);
            ServerIF serverif = (ServerIF) r.lookup("StoreServer");

            // get client's name
            System.out.println("what is client's name? -- welcome to runClient1");
            Scanner s = new Scanner(System.in);
            String name = s.nextLine();

            // start a thread running
            new Thread(new Client(name, serverif)).start();
        }catch(Exception e){
            System.out.println("Error in runClient start method");
        }

    }
}

