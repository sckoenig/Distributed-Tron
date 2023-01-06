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
import vsp.trongame.applicationstub.model.rest.ressources.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;


import static vsp.trongame.applicationstub.model.rest.AdapterState.*;

public class RESTAdapter implements IGameManager, IGame, IArena {

    public static final String REST_NAMESERVER_ADDRESS = "https://name-service.onrender.com/";
    public static final String PUT_NAMESERVER_ROUTE = "/entry";
    public static final String GET_NAMESERVER_ROUTE = "/entry/%s"; //entry/{name}
    public static final String COORDINATOR_NAME = "tron.coordinator";
    private static final RESTAdapter INSTANCE = new RESTAdapter();

    public static RESTAdapter getInstance() {
        return INSTANCE;
    }
    private String localAddress; //our address
    private IGame localGame;
    private IArena localArena;
    private Map<String, RESTRegistration> restRegistrations;
    private List<RPCRegistration> rpcRegistrations;
    private int playerCount;
    private int registeredPlayerCount;
    private Map<Coordinate, Direction> coordinateDirectionMapping;
    private List<Coordinate> coordinates;
    private AdapterState currentState;
    private final Gson gson;

    private final RESTServer restServer;
    //nameServer ??

    private RESTAdapter() {
        currentState = INIT;
        gson = new Gson();
        restServer = new RESTServer(this);
        restServer.start();
    }

    public void setLocalAddress(String address) {
        this.localAddress = address;
    }

    public AdapterState getCurrentState() {
        return this.currentState;
    }

    public void stop(){
        restServer.stop();
    }

    public String getRESTRessource(String route, String address) throws IOException {
        URL url = new URL(address+route);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(1000);

        StringBuilder response = new StringBuilder("");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        while( (inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();

        return new String(response);
    }

    public int putRESTRessource(String route, String ressource, String address) throws IOException {

        URL url = new URL(address+route);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setConnectTimeout(1000);
        connection.setDoOutput(true);
        connection.getOutputStream().write(ressource.getBytes(StandardCharsets.UTF_8));
        connection.getOutputStream().close();

        return connection.getResponseCode();
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
        return coordinates;
    }

    @Override
    public Direction calculateStartingDirection(Coordinate coordinate) {
        return coordinateDirectionMapping.get(coordinate);
    }

    /* GAME */

    @Override
    public void initialize(Modus modus, int speed, int rows, int columns, int waitingTimer, int endingTimer, ExecutorService executorService) {
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
        if (isRegistrationAllowed()) {
            rpcRegistrations.add(new RPCRegistration(gameManager, listener, listenerId, managedPlayerCount));
            if (this.playerCount == this.registeredPlayerCount) transitionState(RUNNING);

        } else {
            gameManager.handleGameState(GameState.INIT);
        }
    }

    private void handleTimeOut() {

    }

    /**
     * @return
     */
    private synchronized boolean isRegistrationAllowed() {
        //TODO
        //synchronized : can be used by server thread
        return false;
    }

    @Override
    public void handleSteer(Steer steer) {
        localGame.handleSteer(steer);
    }

    /* GAME MANAGER */
    @Override
    public void handleGameState(GameState gameState) {
        if (gameState == GameState.INIT) {
            transitionState(AdapterState.INIT);
        }
    }

    @Override
    public void handleManagedPlayers(int id, List<Integer> managedPlayers) {
        // we don't care, we dont check if we are allowed to steer for the players
    }

    /* ADAPTER */

    private void transitionState(AdapterState newState) {
        this.currentState = newState;
        executeState();
    }

    private void executeState() {

        switch (currentState) {
            case INIT -> reset();
            case RPC_REGISTRATION -> {
            } //startTimer
            case REST_REGISTRATION -> registerAtCoordinator();
            case RUNNING -> startLocalGame();
        }
    }

    private void reset() {
        this.restRegistrations.clear();
        this.rpcRegistrations.clear();
        this.coordinates.clear();
        this.coordinateDirectionMapping.clear();
    }

    public void setArenaSize(int rows, int columns) {
        this.localArena = IArenaFactory.getArena(Modus.LOCAL, rows, columns);
    }

    public void handleRessource(Game game) {
        List<LightCycle> allLightCycles = new ArrayList<>();
        for (SuperNode superNode : game.superNodes()) {
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

    public void handleRessource(Steering steering) {
        DirectionChange directionChange = steering.turn().equals("RIGHT") ? DirectionChange.RIGHT_STEER : DirectionChange.LEFT_STEER;
        localGame.handleSteer(new Steer(steering.playerId(), directionChange));
    }

    private void startLocalGame() {
        if (restRegistrations.isEmpty()) {
            this.localGame.prepareForRegistration(rpcRegistrations.size());
            for (RPCRegistration rpcRegistration : rpcRegistrations) {
                this.localGame.register(rpcRegistration.gameManager(), rpcRegistration.updateListener(),
                        rpcRegistration.listenerId(), rpcRegistration.playerCount());
            }
        } else {
            this.localGame.prepareForRegistration(registeredPlayerCount);
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

    }

    private void registerAtCoordinator() throws IOException {
        //TODO
        Registration registration = new Registration(this.localAddress, rpcRegistrations.size());
        String coordinator = lookUpCoordinator();
        putRESTRessource(RESTServer.ROUTE_PUT_REGISTRATION, registration, coordinator.)
    }

    private String lookUpCoordinator() throws IOException {
        String response = getRESTRessource(String.format(GET_NAMESERVER_ROUTE, COORDINATOR_NAME), REST_NAMESERVER_ADDRESS);

        if (response.isEmpty()){ // no coordinator found
            String nameServerRessource = String.format("{\"name\":\"%s\", \"address\":\"%s\"}", COORDINATOR_NAME, localAddress);
            putRESTRessource(PUT_NAMESERVER_ROUTE, nameServerRessource,REST_NAMESERVER_ADDRESS);
        }

        return getRESTRessource(String.format(GET_NAMESERVER_ROUTE, COORDINATOR_NAME), REST_NAMESERVER_ADDRESS);
    }

    public boolean handleRessource(Registration registration) {
        /*if(restRegistrations.isEmpty()){
            //startTimer
        }
        if(currentState == REST_REGISTRATION
                && (registration.playerCount() + registeredPlayerCount < playerCount)){ //FLAG fehlt noch
           restRegistrations.put(new RESTRegistration(registration.address(), registration.playerCount(), 0));
            registeredPlayerCount += registration.playerCount();
        }
        if(registeredPlayerCount == playerCount){
            // buildGame Ressource
            Game game  = buildGame();
            //restRegistrations.entrySet().stream().forEach(e -> sendRessource(e.getValue(), game));
            */
        return false;

    }

    private void sendRessource(String address, Game game) {

    }

    private void buildGame() {

    }


}
