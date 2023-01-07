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
import java.net.InetAddress;
import java.net.UnknownHostException;
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
            REST_REGISTRATION, List.of(BUILDING_GAME, RUNNING),
            RUNNING, List.of(INIT),
            BUILDING_GAME, List.of(RUNNING)
    );

    private static final RESTStub INSTANCE = new RESTStub();

    public static RESTStub getInstance() {
        return INSTANCE;
    }

    private String localAddress;
    private final RESTServer restServer;
    private final RESTClient restClient;
    private final Map<String, RESTRegistration> restRegistrations;
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
        this.executorService = Executors.newFixedThreadPool(4);
        this.coordinates = new ArrayList<>();
        this.coordinateDirectionMapping = new HashMap<>();
        this.restRegistrations = new HashMap<>();
        this.rpcRegistrations = new ArrayList<>();
        this.currentState = INIT;
        this.gson = new Gson();
        this.restServer = new RESTServer(this, executorService);
        this.restServer.start();
        this.restClient = new RESTClient();


        try {
            this.localAddress = String.format("http://%s:%s", InetAddress.getLocalHost().getHostAddress(), restServer.getPort());
        } catch (UnknownHostException e) {
            localAddress = "";
        }
        //localAddress = String.format("http://%s:%s", "192.168.193.57", restServer.getPort());
        System.out.println(localAddress);
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
    private synchronized void transitionState(RESTStubState newState) {
        if (TRANSITIONS.get(currentState).contains(newState)) {
            this.currentState = newState;
            executeState();
        }
    }

    /**
     * Executes the current state.
     */
    private void executeState() {
        switch (currentState) {
            case INIT -> reset();
            case RPC_REGISTRATION -> startTimer(timer / 2);
            case REST_REGISTRATION -> registerAtCoordinator();
            case BUILDING_GAME -> buildGame();
            case RUNNING -> startLocalGame();
        }
    }

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
        Registration registration = new Registration(restServer.getPort(), rpcRegistrations.size());
        String registrationJson = gson.toJson(registration, Registration.class);
        boolean registrationSuccess = false;
        NameServerEntry coordinator;

        try {
            while (!registrationSuccess) {

                String jsonCoordinator = lookUpCoordinator();
                if (jsonCoordinator.equals("null") || jsonCoordinator.isEmpty()) {
                    registerAsCoordinator();
                    break;
                }

                coordinator = gson.fromJson(jsonCoordinator, NameServerEntry.class);
                int response = restClient.putRESTRessource(coordinator.address(), ROUTE_PUT_REGISTRATION, registrationJson);

                if (response == STATUS_NOT_AVAILABLE || response == -1) registerAsCoordinator();
                if (response == STATUS_DENIED) {
                    transitionState(RUNNING);
                    registrationSuccess = true;
                }
                if (response == STATUS_OK) registrationSuccess = true;

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
     * @throws IOException on name server error
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
        restPlayerCount = 0;
        List<LightCycle> allLightCycles = new ArrayList<>();
        for (SuperNode superNode : game.superNodes()) {
            restPlayerCount += superNode.lightCycles().size();
            RESTRegistration registration = new RESTRegistration(superNode.address(), superNode.lightCycles().size(), superNode.lightCycles().get(0).playerId());
            restRegistrations.put(superNode.address(), registration);
            allLightCycles.addAll(superNode.lightCycles());
        }
        for (LightCycle lightCycle : allLightCycles) {
            this.coordinates.add(lightCycle.position());
            this.coordinateDirectionMapping.put(lightCycle.position(), lightCycle.direction());
        }

        transitionState(RUNNING);
    }

    /**
     * Handels the {@link Steering} Ressource.
     *
     * @param steering the steering
     */
    public void handleRessource(Steering steering) {
        DirectionChange directionChange = steering.turn().equals("RIGHT") ? DirectionChange.RIGHT_STEER : DirectionChange.LEFT_STEER;
        localGame.handleSteer(new Steer(steering.playerId(), directionChange));

    }

    public boolean handleRessource(Registration registration, String address) {

        boolean registrationAllowed = false;

        if (restRegistrations.isEmpty()) {
            startTimer(timer / 2);
        }
        if (isRegistrationAllowed(registration.playerCount())) {
            String restAddress = "http://" + address + ":" + registration.port();
            restRegistrations.put(restAddress, new RESTRegistration(restAddress, registration.playerCount(), 0));
            restPlayerCount += registration.playerCount();
            registrationAllowed = true;

        }
        return registrationAllowed;
    }

    /**
     * Starts the local game by preparing the game and registering all RPC and REST registrations.
     */
    private void startLocalGame() {
        registrationLock.lock();

        if (restRegistrations.isEmpty() && rpcRegistrations.size() > 1) {
            this.localGame.prepareForRegistration(rpcRegistrations.size());
            for (RPCRegistration rpcRegistration : rpcRegistrations) {
                localGame.register(this, null, 0, 0);
                this.localGame.register(rpcRegistration.gameManager(), rpcRegistration.updateListener(),
                        rpcRegistration.listenerId(), rpcRegistration.playerCount());
            }
        }
        if (!restRegistrations.isEmpty() && restPlayerCount > 1) {
            this.localGame.prepareForRegistration(restPlayerCount);
            List<Map.Entry<String, RESTRegistration>> temp = new ArrayList<>(restRegistrations.entrySet());
            temp.sort(Map.Entry.comparingByValue());
            for (Map.Entry<String, RESTRegistration> entry : temp) {
                if (!entry.getKey().equals(this.localAddress)) {
                    RESTRegistration restReg = entry.getValue();
                    localGame.register(getInstance(), null, 0, restReg.playerCount());
                } else {
                    for (RPCRegistration rpcReg : rpcRegistrations) {
                        localGame.register(rpcReg.gameManager(), rpcReg.updateListener(), rpcReg.listenerId(), rpcReg.playerCount());
                    }
                }
            }
        }
        registrationLock.unlock();
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

    /**
     * Builds the {@link Game} ressource as defined in {@link RESTProtocol}.
     */
    private void buildGame() {
        List<Coordinate> coordinates = localArena.calculateFairStartingCoordinates(restPlayerCount);
        List<SuperNode> superNodes = new ArrayList<>();
        int playerId = 0;
        for (Map.Entry<String, RESTRegistration> restR : restRegistrations.entrySet()) {
            List<LightCycle> lightCycles = new ArrayList<>();
            for (int i = 0; i < restR.getValue().playerCount(); i++) {
                LightCycle lightCycle = new LightCycle(playerId, coordinates.get(playerId), calculateStartingDirection(coordinates.get(playerId)));
                lightCycles.add(lightCycle);
                playerId++;
            }
            SuperNode superNode = new SuperNode(restR.getValue().address(), lightCycles);
            superNodes.add(superNode);
        }
        Game game = new Game(superNodes);
        String gameJson = gson.toJson(game, Game.class);
        handleRessource(game);
        sendRessource(gameJson);

    }

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
            System.out.println(playerCount);
            transitionState(RPC_REGISTRATION);
        }
    }

    @Override
    public void register(IGameManager gameManager, IUpdateListener listener, int listenerId, int managedPlayerCount) {
        if (currentState == RPC_REGISTRATION && isRegistrationAllowed(managedPlayerCount)) {
            rpcRegistrations.add(new RPCRegistration(gameManager, listener, listenerId, managedPlayerCount));
            if (this.playerCount == this.rpcRegistrations.size()) transitionState(RUNNING);

        } else {
            gameManager.handleGameState(GameState.INIT);
        }
    }

    @Override
    public void handleSteer(Steer steer) {
        String turn = steer.directionChange() == DirectionChange.LEFT_STEER? STEERING_LEFT : STEERING_RIGHT;
        Steering steering = new Steering(steer.playerId(), turn);
        String steeringJson = gson.toJson(steering, Steering.class);
        handleRessource(steering);
        sendRessource(steeringJson);
    }

    private void sendRessource(String ressource) {
        executorService.execute(() -> {
            for (String address : restRegistrations.keySet())
                if (!address.equals(localAddress))
                    try {
                        restClient.putRESTRessource(address, ROUTE_PUT_GAME, ressource);
                    } catch (IOException e) {
                        //continue with next supernode
                    }
        });

    }


    /* GAME MANAGER */
    @Override
    public void handleGameState(GameState gameState) {
        System.out.println(gameState);
        if (gameState == GameState.INIT) {
            transitionState(INIT);
        }
    }

    @Override
    public void handleManagedPlayers(int id, List<Integer> managedPlayers) {
        // we don't care, we dont check if we are allowed to steer for the players
    }

}
