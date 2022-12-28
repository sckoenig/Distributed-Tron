package vsp.trongame.middleware;

import vsp.trongame.middleware.clientstub.Marshaller;
import vsp.trongame.middleware.serverstub.Unmarshaller;

import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade for the Middleware.
 */
public class Middleware implements IRegister, IRemoteInvocation {

    private static final Middleware INSTANCE = new Middleware();
    private static final int MIDDLEWARE_THREAD_SIZE = 5;

    public static Middleware getInstance() {
        return INSTANCE;
    }

    private ExecutorService middlewareExecutor;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    private Middleware() {
    }

    public void start() {

        this.middlewareExecutor = Executors.newFixedThreadPool(MIDDLEWARE_THREAD_SIZE);

        try {
            this.marshaller = new Marshaller(middlewareExecutor);
            this.unmarshaller = new Unmarshaller(middlewareExecutor);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerRemoteObject(int serviceID, IRemoteObject remoteObject) {
        // forward to ServerStub
        unmarshaller.registerRemoteObject(serviceID, remoteObject);
    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters) {
        // forward to ClientStub
        marshaller.invoke(remoteID, serviceID, type, intParameters, stringParameters);
    }

    public void stop() {
        middlewareExecutor.shutdownNow();
    }
}
