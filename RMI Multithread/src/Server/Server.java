package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Server implements ServerIF {

    /**
     * server class properties
     */
    private static final long serialVersionUID = 1L;
    HashMap<String, String> store;

    /**
     * Constructor: create new empty store for clients
     *
     * @throws RemoteException
     */
    public Server() throws RemoteException {
        super();
        UnicastRemoteObject.exportObject(this, 0);
        this.store = new HashMap<>();
    }

    /**
     * put a new key-value pair into store
     *
     * @param key
     * @param value
     * @return true if pair is added otherwise return false
     * @throws RemoteException
     */
    @Override
    public synchronized Boolean put(String key, String value) throws RemoteException {
        try {
            this.store.put(key, value);
            System.out.println("Put new pair in: " + key + "-" + value);
            return true;
        } catch (Exception e) {
            System.out.println("Error in Server PUT");
            return false;
        }
    }

    /**
     * get a value for the certain key
     *
     * @param key
     * @return true the value if the key is founded, otherwise return null
     * @throws RemoteException
     */
    @Override
    public synchronized String get(String key) throws RemoteException {
        try {
            if (store.containsKey(key)) {
                System.out.println("Get the pair: " + key + "-" + store.get(key));
                return store.get(key);
            } else {
                System.out.println("Not found!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error in Server get");
            return null;
        }
    }

    /**
     * delete a key-value pair
     *
     * @param key
     * @return true if delete successfully otherwise return false
     * @throws RemoteException
     */
    @Override
    public synchronized Boolean delete(String key) throws RemoteException {
        try {
            if (store.containsKey(key)) {
                System.out.println("Deleted the pair: " + key + "-" + store.get(key));
                store.remove(key);
                return true;
            } else {
                System.out.println("Not found!");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error in Server delete");
            return false;
        }
    }
}