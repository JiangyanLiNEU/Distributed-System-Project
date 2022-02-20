package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client interface
 * three abstract methods: put, get and delete
 */
public interface ClientIF extends Remote {
    void put(String key, String value) throws RemoteException;

    void get(String key) throws RemoteException;

    void delete(String key) throws RemoteException;
}
