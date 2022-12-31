package vsp.trongame.middleware;

import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.clientstub.Marshaller;
import vsp.trongame.middleware.namingservice.INamingService;
import vsp.trongame.middleware.namingservice.NameResolver;
import vsp.trongame.middleware.namingservice.NameServer;
import vsp.trongame.middleware.serverstub.Unmarshaller;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade for the Middleware.
 */
public class Middleware implements IRegister, IRemoteInvocation {

    private static final Middleware INSTANCE = new Middleware();
    private static final int MIDDLEWARE_THREAD_SIZE = 10;

    public static Middleware getInstance() {
        return INSTANCE;
    }

    private ExecutorService middlewareExecutor;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private NameServer nameServer;

    private Middleware() {
    }

    public void start(String address, boolean nameServer) {

        this.middlewareExecutor = Executors.newFixedThreadPool(MIDDLEWARE_THREAD_SIZE);

        try {
            if (nameServer) {
                this.nameServer = new NameServer();
                this.nameServer.startWithAddress(address);
            }
            INamingService namingService = new NameResolver(middlewareExecutor);

            this.marshaller = new Marshaller(middlewareExecutor, namingService);
            this.unmarshaller = new Unmarshaller(middlewareExecutor, namingService);

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerRemoteObject(int serviceID, String remoteId, IRemoteObject remoteObject) {
        // forward to ServerStub
        unmarshaller.registerRemoteObject(serviceID, remoteId, remoteObject);
    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters) {
        // forward to ClientStub
        marshaller.invoke(remoteID, serviceID, type, intParameters, stringParameters);
    }

    public void stop() {
        if (middlewareExecutor != null) middlewareExecutor.shutdownNow();
        unmarshaller.shutDown();
        nameServer.stop();
    }
}
