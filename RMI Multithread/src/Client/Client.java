package Client;
import Server.ServerIF;

import java.rmi.RemoteException;
import java.util.Locale;
import java.util.Scanner;

public class Client implements ClientIF, Runnable{

    /**
     * Client class properties: name and the server interface
     */
    public String name;
    public ServerIF serverif;

    /**
     * Constructor
     * @param name
     * @param serverif
     */
    public Client(String name, ServerIF serverif) {
        super();
        this.name = name;
        this.serverif = serverif;
        System.out.println("Client "+this.name+" is connected to server!");
    }

    /**
     * make Client runnable so that we can use Thread
     * client's entry point
     */
    @Override
    public void run() {
        System.out.println("========== STORE ==========");

        // ask question and then get client's input
        Scanner s = new Scanner(System.in);
        String input = null;
        String ask = "choose: PUT or GET or DELETE or EXIT: ";
        System.out.println(ask);
        input = s.nextLine();
        input = input.toUpperCase(Locale.ROOT);

        // keep listening until client inputs exit
        while (!input.equalsIgnoreCase("EXIT")) {

            // check if it is valid input
            if (!(input.equals("PUT") || input.equals("GET") || input.equals("DELETE"))) {
                System.out.println("Invalid input! Try Again!");
            } else {

                // if input is valid, then handle the request
                switch (input) {
                    case "PUT": {
                        System.out.println("Enter the key: ");
                        String key = s.nextLine();
                        System.out.println("Enter the value:");
                        String value = s.nextLine();
                        try {
                            put(key, value);
                        } catch (RemoteException e) {
                            System.out.println("Error in Client switch put");
                        }
                        break;
                    }
                    case "GET": {
                        System.out.println("Enter the key: ");
                        String key = s.nextLine();
                        try {
                            get(key);
                        } catch (RemoteException e) {
                            System.out.println("Error in Client switch get");
                        }
                        break;
                    }
                    case "DELETE": {
                        System.out.println("Enter the key: ");
                        String key = s.nextLine();
                        try {
                            delete(key);
                        } catch (RemoteException e) {
                            System.out.println("Error in Client switch delete");
                        }
                        break;
                    }
                }
            }
            System.out.println(ask);
            input = s.nextLine();
            input = input.toUpperCase(Locale.ROOT);
        }
        System.out.println("Thanks for trying the store! BYE!");
    }

    @Override
    public void put(String key, String value) throws RemoteException {
        try{
            // get result from server, either true or false
            boolean result = this.serverif.put(key, value);

            if (result){
                System.out.println("Added it to store!");
            }else{
                System.out.println("Failed...");
            }
        }catch(Exception e){
            System.out.println("Error in client put method...");
        }
    }

    @Override
    public void get(String key) throws RemoteException {
        try{
            // get result from server, either null or a string
            String result = this.serverif.get(key);
            if (result!=null){
                System.out.println("The value of "+key+" is: "+ result);
            }else{
                System.out.println(key+" is not founded in store!");
            }
        }catch(Exception e){
            System.out.println("Error in client get method...");
        }
    }

    @Override
    public void delete(String key) throws RemoteException {
        try{
            // get result from server, either true or false
            boolean result = this.serverif.delete(key);
            if (result){
                System.out.println("Deleted it !");
            }else{
                System.out.println( key + " is not founded! ");
            }
        }catch(Exception e){
            System.out.println("Error in client delete method...");
        }
    }
}
