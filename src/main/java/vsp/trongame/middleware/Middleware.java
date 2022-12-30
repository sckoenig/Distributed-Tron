package vsp.trongame.middleware;

import vsp.trongame.app.model.gamemanagement.Configuration;
import vsp.trongame.applicationstub.util.Service;
import vsp.trongame.middleware.clientstub.Marshaller;
import vsp.trongame.middleware.namingservice.INamingService;
import vsp.trongame.middleware.namingservice.NameResolver;
import vsp.trongame.middleware.namingservice.NameServer;
import vsp.trongame.middleware.serverstub.Unmarshaller;

import java.io.IOException;
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
                try {
                    this.nameServer = new NameServer(address);
                    middlewareExecutor.execute(() -> {
                        try {
                            this.nameServer.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

            String[] adressSplit = address.split(":");
            INamingService namingService = new NameResolver(new InetSocketAddress(adressSplit[0], Integer.parseInt(adressSplit[1])), middlewareExecutor);
            this.marshaller = new Marshaller(middlewareExecutor, namingService);
            this.unmarshaller = new Unmarshaller(middlewareExecutor, namingService);
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
        System.out.println("INVOKE: "+remoteID+" "+ Service.getByOrdinal(serviceID) + " " + type + " "+ Arrays.toString(intParameters) + "" + Arrays.toString(stringParameters));
        marshaller.invoke(remoteID, serviceID, type, intParameters, stringParameters);
    }

    public void stop() {
        if (middlewareExecutor != null) middlewareExecutor.shutdownNow();
        unmarshaller.shutDown();
        nameServer.shutDown();
    }
}
