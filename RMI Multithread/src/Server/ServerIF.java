package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Server interface
 * three abstract methods: put, get and delete
 */
public interface ServerIF extends Remote {

    Boolean put(String key, String value) throws RemoteException;
    String get(String key) throws RemoteException;
    Boolean delete(String key) throws RemoteException;
}