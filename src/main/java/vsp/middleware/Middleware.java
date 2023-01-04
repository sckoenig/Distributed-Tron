package vsp.middleware;

import vsp.middleware.clientstub.Marshaller;
import vsp.middleware.namingservice.INamingService;
import vsp.middleware.namingservice.NameResolver;
import vsp.middleware.namingservice.NameServer;
import vsp.middleware.serverstub.Unmarshaller;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Facade for the Middleware.
 */
public class Middleware implements IRegister, IRemoteInvocation {

    private static final Middleware INSTANCE = new Middleware();
    private static final int MIDDLEWARE_THREAD_SIZE = 6;

    public static Middleware getInstance() {
        return INSTANCE;
    }

    private ExecutorService middlewareExecutor;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;
    private NameServer nameServer;
    boolean isNameServerHost;

    private Middleware() {
    }

    public void start(String nameServerAddress, boolean asNameServerHost) {

        this.isNameServerHost = asNameServerHost;
        this.middlewareExecutor = Executors.newFixedThreadPool(MIDDLEWARE_THREAD_SIZE);
        String[] split = nameServerAddress.split(":");
        InetSocketAddress nameServerSocketAddress = new InetSocketAddress(split[0], Integer.parseInt(split[1]));

        try {
            if (asNameServerHost) {
                this.nameServer = new NameServer(nameServerSocketAddress);
                this.nameServer.start();
            }
            INamingService namingService = new NameResolver(middlewareExecutor, nameServerSocketAddress);
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
        if (isNameServerHost) nameServer.stop();
        unmarshaller.stop();
    }
}
