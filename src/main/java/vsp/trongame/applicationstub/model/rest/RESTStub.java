package vsp.trongame.applicationstub.model.rest;

import com.google.gson.Gson;
import edu.cads.bai5.vsp.tron.view.Coordinate;
import vsp.trongame.Modus;
import vsp.trongame.application.model.IUpdateListener;
import vsp.trongame.application.model.datatypes.Direction;
import vsp.trongame.application.model.datatypes.DirectionChange;
import vsp.trongame.application.model.datatypes.GameState;
import vsp.trongame.application.model.datatypes.Steer;
import vsp.trongame.application.model.game.IArena;
import vsp.trongame.application.model.game.IArenaFactory;
import vsp.trongame.application.model.game.IGame;
import vsp.trongame.application.model.game.IGameFactory;
import vsp.trongame.application.model.gamemanagement.IGameManager;
import vsp.trongame.applicationstub.model.rest.registration.RESTRegistration;
import vsp.trongame.applicationstub.model.rest.registration.RPCRegistration;
import vsp.trongame.applicationstub.model.rest.ressources.*;

import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static vsp.trongame.applicationstub.model.rest.RESTStubState.*;
import static vsp.trongame.applicationstub.model.rest.RESTProtocol.*;

/**
 * Stub for realizing the {@link RESTProtocol}. For that, it rests between
 * - the IGameCallee, who in RPC would know the local Game, and the local Game;
 * - the local Game and the local Arena.
 */
public class RESTStub implements IGameManager, IGame, IArena {

    private static final Map<RESTStubState, List<RESTStubState>> TRANSITIONS = Map.of(
            INIT, List.of(RPC_REGISTRATION),
            RPC_REGISTRATION, List.of(REST_REGISTRATION, RUNNING),
            REST_REGISTRATION, List.of(BUILDING_GAME, RUNNING, INIT),
            RUNNING, List.of(INIT),
            BUILDING_GAME, List.of(RUNNING)
    );

    private static final RESTStub INSTANCE = new RESTStub();
    private static final int RESTSTUB_THREAD_POOL = 4;

    public static RESTStub getInstance() {
        return INSTANCE;
    }

    private String localAddress;
    private final RESTServer restServer;
    private final RESTClient restClient;
    private final Map<String, RESTRegistration> restRegistrations; //address to registration
    private final List<RPCRegistration> rpcRegistrations;
    private final ReentrantLock registrationLock;
    private final Map<Coordinate, Direction> coordinateDirectionMapping; //acts as arena
    private final List<Coordinate> coordinates;
    private IGame localGame;
    private IArena localArena;
    private int timer;
    private int playerCount;
    private int restPlayerCount;
    private RESTStubState currentState;
    private final Gson gson;
    private final ExecutorService executorService;

    private RESTStub() {
        this.registrationLock = new ReentrantLock(true);
        this.executorService = Executors.newFixedThreadPool(RESTSTUB_THREAD_POOL);
        this.coordinates = new ArrayList<>();
        this.coordinateDirectionMapping = new HashMap<>();
        this.restRegistrations = new HashMap<>();
        this.rpcRegistrations = new ArrayList<>();
        this.currentState = INIT;
        this.gson = new Gson();
        this.restServer = new RESTServer(this, executorService);
        this.restServer.start();
        this.restClient = new RESTClient();
    }

    public void setIpAddress(String ipAddress){
        localAddress = String.format("http://%s:%s", ipAddress, restServer.getPort());
        System.out.println("REST: "+localAddress) ;
    }


    /**
     * Stops the underlying {@link RESTServer} and any used threads.
     */
    public void stop() {
        restServer.stop();
        executorService.shutdownNow();
    }

    /**
     * Transitions to a new state and executes the state.
     *
     * @param newState the new state
     */
    private void transitionState(RESTStubState newState) {
        registrationLock.lock();
        System.out.println("state transition has lock");
        if (TRANSITIONS.get(currentState).contains(newState)) {
            this.currentState = newState;
            registrationLock.unlock();
            System.out.println("state transition has unlocked");
            executeState();
        } else registrationLock.unlock();
    }

    /**
     * Executes the current state.
     */
    private void executeState() {
        System.out.println("EXECUTING ... " + currentState);
        switch (currentState) {
            case INIT -> reset();
            case RPC_REGISTRATION -> startTimer(timer / 2);
            case REST_REGISTRATION -> registerAtCoordinator();
            case BUILDING_GAME -> buildGame();
            case RUNNING -> startLocalGame();
        }
    }

    /**
     * Starts a timer.
     * @param waitingTime waiting time in milliseconds
     */
    private void startTimer(long waitingTime) {
        executorService.execute(() -> {
            try {
                sleep(waitingTime);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
            if (!currentThread().isInterrupted()) handleTimeOut();
        });
    }

    /**
     * Handels the timeout according to states.
     */
    private void handleTimeOut() {
        if (currentState == REST_REGISTRATION && restPlayerCount >= 2) {
            transitionState(BUILDING_GAME);
        }
        if (currentState == REST_REGISTRATION && restPlayerCount < 2) {
            this.rpcRegistrations.get(0).gameManager().handleGameState(GameState.INIT);
            transitionState(INIT);
        }
        if (currentState == RPC_REGISTRATION && rpcRegistrations.size() != playerCount) {
            transitionState(REST_REGISTRATION);
        }
    }

    /**
     * Resets all Registration and Game Ressource data.
     */
    private void reset() {
        this.restRegistrations.clear();
        this.rpcRegistrations.clear();
        this.coordinates.clear();
        this.coordinateDirectionMapping.clear();
        this.playerCount = this.restPlayerCount = 0;
    }

    /**
     * Attempts to register at a Coordinator as specified in protocol. If there is no Coordinator,
     * a new one is registered. If Registration is denied or Registration of a new Coordinator is not
     * possible, the game is started on RPC Registrations only.
     */
    private void registerAtCoordinator() {
        Registration registration = new Registration(rpcRegistrations.size(), this.localAddress);
        String registrationJson = gson.toJson(registration, Registration.class);
        boolean registrationSuccess = false;
        NameServerEntry coordinator;

        try {
            while (!registrationSuccess) {

                String jsonCoordinator = lookUpCoordinator();
                if (jsonCoordinator.equals("null") || jsonCoordinator.isEmpty()) {
                    System.out.println("no coordiantor or timeout nameserver");
                    registerAsCoordinator();
                    continue;
                }
                coordinator = gson.fromJson(jsonCoordinator, NameServerEntry.class);
                System.out.println(coordinator);;
                int response = restClient.putRESTRessource(coordinator.address(), ROUTE_PUT_REGISTRATION, registrationJson);
                System.out.println("Coordinator response: " + response);

                if (response == STATUS_DENIED || response == STATUS_OK) registrationSuccess = true;
                else registerAsCoordinator();

                if (response == STATUS_DENIED) transitionState(RUNNING); // give up registration and start local game
            }

        } catch (IOException e) {
            e.printStackTrace();
            //on name server problems we start game in rpc middleware only if possible.
            transitionState(RUNNING);
        }
    }

    /**
     * Searches for a Coordinator as specified in protocol.
     *
     * @return json string of {@link NameServerEntry}
     * @throws HttpTimeoutException on name server error
     */
    private String lookUpCoordinator() throws IOException {
        return restClient.getRESTRessource(REST_NAMESERVER_ADDRESS, String.format(GET_NAMESERVER_ROUTE, COORDINATOR_NAME));
    }

    /**
     * Registers as Coordinator as specified in protocol
     *
     * @throws IOException on name server error
     */
    private void registerAsCoordinator() throws IOException {
        System.out.println("Registering as Coordinator...");
        NameServerEntry coordinator = new NameServerEntry(COORDINATOR_NAME, localAddress);
        String newCoordinator = gson.toJson(coordinator, NameServerEntry.class);
        restClient.putRESTRessource(REST_NAMESERVER_ADDRESS, PUT_NAMESERVER_ROUTE, newCoordinator);
    }

    /**
     * Handles the {@link Game} Ressource and changes state.
     *
     * @param game the game from Coordinator
     */
    public void handleRessource(Game game) {
        if (currentState == REST_REGISTRATION || currentState == BUILDING_GAME) {
            restPlayerCount = 0;
            List<LightCycle> allLightCycles = new ArrayList<>();
            for (SuperNode superNode : game.superNodes()) {
                restPlayerCount += superNode.lightCycles().size();
                RESTRegistration registration = new RESTRegistration(superNode.address(), superNode.lightCycles().size(), superNode.lightCycles().get(0).id());
                restRegistrations.put(superNode.address(), registration);
                allLightCycles.addAll(superNode.lightCycles());
            }
            for (LightCycle lightCycle : allLightCycles) {
                this.coordinates.add(lightCycle.position());
                this.coordinateDirectionMapping.put(lightCycle.position(), lightCycle.direction());
            }
            transitionState(RUNNING);
        }
    }

    /**
     * Handels the {@link Steering} Ressource.
     *
     * @param steering the steering
     */
    public void handleRessource(Steering steering) {
        DirectionChange directionChange = steering.turn().equals("RIGHT") ? DirectionChange.RIGHT_STEER : DirectionChange.LEFT_STEER;
        localGame.handleSteer(new Steer(steering.id(), directionChange));
    }

    /**
     * Handels the {@link Registration} Ressource.
     * @param registration the registration
     * @param address the sender's address
     * @return true, if the registration was accepted, false otherwise
     */
    public boolean handleRessource(Registration registration, String address) {
        if (registration == null) return false;
        System.out.println("registration from : " + address);

        registrationLock.lock();
        System.out.println("rest registration has lock");
        boolean registrationAllowed = false;

        if (currentState == REST_REGISTRATION && isRegistrationAllowed(registration.playerCount())) {
            if (restRegistrations.isEmpty()) {
                //startTimer(timer / 2);
            }
            String restAddress = registration.uri();
            restRegistrations.put(restAddress, new RESTRegistration(restAddress, registration.playerCount(), 0));
            restPlayerCount += registration.playerCount();
            registrationAllowed = true;
            System.out.println("registration from " + registration.uri() + " accepted");
        } else {
            System.err.printf("registration from %s denied. With playercount: %s, already registered %s, max playerCount %s. STATE: %s %n", address, registration.playerCount(), restPlayerCount, playerCount, currentState);

        }
        registrationLock.unlock();
        System.out.println("rest registration has unlocked");
        return registrationAllowed;
    }

    /**
     * Builds the {@link Game} ressource as defined in {@link RESTProtocol}.
     */
    private void buildGame() {
        System.out.println("Building game...");
        List<Coordinate> gameCoordiantes = localArena.calculateFairStartingCoordinates(restPlayerCount);
        Collection<RESTRegistration> allRestRegistrations = restRegistrations.values();
        SuperNode[] superNodes = new SuperNode[allRestRegistrations.size()];

        int playerId = 0;
        int index = 0;
        for (RESTRegistration registration : allRestRegistrations) {
            List<LightCycle> lightCycles = new ArrayList<>();
            for (int i = 0; i < registration.playerCount(); i++) {
                LightCycle lightCycle = new LightCycle(playerId, gameCoordiantes.get(playerId), calculateStartingDirection(gameCoordiantes.get(playerId)));
                lightCycles.add(lightCycle);
                playerId++;
            }
            SuperNode superNode = new SuperNode(registration.address(), lightCycles);
            superNodes[index] = superNode;
            index++;
        }
        Game game = new Game(superNodes);
        String gameJson = gson.toJson(game.superNodes(), SuperNode[].class);
        sendRessource(gameJson, ROUTE_PUT_GAME);
        handleRessource(game);
    }

    /**
     * Sends a string ressource to the given route to all known addresses.
     * @param ressource ressource to send, must conform to protocol
     * @param route route to send to, must conform to protocol
     */
    private void sendRessource(String ressource, String route) {
        executorService.execute(() -> {
            System.out.println("SEND... " + restRegistrations);
           for (String address : restRegistrations.keySet())
                if (!address.equals(localAddress))
                    try {
                        System.out.println("send to address: " + address);
                        restClient.putRESTRessource(address, route, ressource);
                    } catch (IOException e) {
                        // continue with next supernode
                    }
        });
    }

    /**
     * Starts the local game by preparing the game and registering all RPC and REST registrations.
     */
    private void startLocalGame() {

        if (restRegistrations.isEmpty() && rpcRegistrations.size() > 1) {
            this.localGame.prepareForRegistration(rpcRegistrations.size());
            registerRPCRegistrationsAtLocalGame();
        } else if (!restRegistrations.isEmpty() && restPlayerCount > 1) {
            this.localGame.prepareForRegistration(restPlayerCount);
            List<Map.Entry<String, RESTRegistration>> temp = new ArrayList<>(restRegistrations.entrySet());
            temp.sort(Map.Entry.comparingByValue()); //sorting in order to correctly assign playerIds

            for (Map.Entry<String, RESTRegistration> entry : temp) {
                if (entry.getKey().equals(this.localAddress)) {
                    registerRPCRegistrationsAtLocalGame();
                } else {
                    RESTRegistration restReg = entry.getValue();
                    localGame.register(getInstance(), null, 0, restReg.playerCount());
                }
            }
        } else transitionState(INIT);
    }

    private void registerRPCRegistrationsAtLocalGame(){
        localGame.register(this, null, 0, 0); //making sure this stub is also informed

        for (RPCRegistration rpcRegistration : rpcRegistrations) {
            this.localGame.register(rpcRegistration.gameManager(), rpcRegistration.updateListener(),
                    rpcRegistration.listenerId(), rpcRegistration.playerCount());
        }
    }

    public RESTStubState getCurrentState() {
        return this.currentState;
    }

    public void createArena(int rows, int columns) {
        this.localArena = IArenaFactory.getArena(Modus.LOCAL, rows, columns);
    }

    public void startGameIfFull() {
        if (restPlayerCount == playerCount) {
            transitionState(BUILDING_GAME);
        }
    }

    private boolean isRegistrationAllowed(int playerCountToRegister) {
        return this.playerCount - this.restPlayerCount >= playerCountToRegister;
    }

    /* ARENA */
    @Override
    public void addPlayerPosition(int playerId, Coordinate coordinate) {
        localArena.addPlayerPosition(playerId, coordinate);
    }

    @Override
    public int getRows() {
        return localArena.getRows();
    }

    @Override
    public int getColumns() {
        return localArena.getColumns();
    }

    @Override
    public void deletePlayerPositions(List<Integer> crashedPlayerIds) {
        localArena.deletePlayerPositions(crashedPlayerIds);
    }

    @Override
    public boolean detectCollision(Coordinate coordinate) {
        return localArena.detectCollision(coordinate);
    }

    @Override
    public List<Coordinate> calculateFairStartingCoordinates(int playerCount) {
        if (!coordinates.isEmpty()) return coordinates;
        return localArena.calculateFairStartingCoordinates(playerCount);
    }

    @Override
    public Direction calculateStartingDirection(Coordinate coordinate) {
        if (!coordinateDirectionMapping.isEmpty()) return coordinateDirectionMapping.get(coordinate);
        return localArena.calculateStartingDirection(coordinate);
    }


    /* GAME */

    @Override
    public void initialize(Modus modus, int speed, int rows, int columns, int waitingTimer,
                           int endingTimer, ExecutorService executorService) {
        this.timer = waitingTimer;
        this.localGame = IGameFactory.getGame(Modus.LOCAL); // knows the "real" Game
        this.localGame.initialize(modus, speed, rows, columns, waitingTimer, endingTimer, executorService);
    }

    @Override
    public void prepareForRegistration(int playerCount) {
        if (currentState == INIT) {
            this.playerCount = playerCount;
            transitionState(RPC_REGISTRATION);
        }
    }

    @Override
    public void register(IGameManager gameManager, IUpdateListener listener, int listenerId, int managedPlayerCount) {
        registrationLock.lock();
        System.out.println("rpc registration has lock");

        if (currentState == RPC_REGISTRATION && isRegistrationAllowed(managedPlayerCount)) {
            rpcRegistrations.add(new RPCRegistration(gameManager, listener, listenerId, managedPlayerCount));
            if (this.playerCount == this.rpcRegistrations.size()) transitionState(RUNNING);

        } else {
            gameManager.handleGameState(GameState.INIT);
        }
        registrationLock.unlock();
        System.out.println("rpc registration has unlocked");
    }

    @Override
    public void handleSteer(Steer steer) {
        String turn = steer.directionChange() == DirectionChange.LEFT_STEER? STEERING_LEFT : STEERING_RIGHT;
        Steering steering = new Steering(steer.playerId(), turn);
        String steeringJson = gson.toJson(steering, Steering.class);

        handleRessource(steering);
        sendRessource(steeringJson, ROUTE_PUT_STEERING);
    }


    /* GAME MANAGER */
    @Override
    public void handleGameState(GameState gameState) {
        if (gameState == GameState.INIT) {
            transitionState(INIT);
        }
    }

    @Override
    public void handleManagedPlayers(int id, List<Integer> managedPlayers) {
        // we don't care, we dont check if we are allowed to steer for the players
    }

}
