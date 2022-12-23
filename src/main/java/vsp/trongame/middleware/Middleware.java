package vsp.trongame.middleware;

/**
 * Facade for the Middleware.
 */
public class Middleware implements IRegister, IRemoteInvocation {

    private static final Middleware INSTANCE = new Middleware();

    public static Middleware getInstance(){
        return INSTANCE;
    }

    @Override
    public void registerRemoteObject(int serviceID, IRemoteObject remoteObject) {
        // forward to ServerStub
    }

    @Override
    public void invoke(String remoteID, int serviceID, InvocationType type, int[] intParameters, String... stringParameters) {
        // forward to ClientStub
    }
}
