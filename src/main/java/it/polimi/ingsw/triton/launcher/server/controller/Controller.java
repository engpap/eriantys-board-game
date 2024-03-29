package it.polimi.ingsw.triton.launcher.server.controller;

import it.polimi.ingsw.triton.launcher.server.controller.visitors.ClientMessageErrorVisitor;
import it.polimi.ingsw.triton.launcher.server.controller.visitors.ClientMessageExceptionalVisitor;
import it.polimi.ingsw.triton.launcher.server.controller.visitors.ClientMessageModifierVisitor;
import it.polimi.ingsw.triton.launcher.server.controller.visitors.ClientMessageStandardVisitor;
import it.polimi.ingsw.triton.launcher.server.model.enums.GameState;
import it.polimi.ingsw.triton.launcher.server.model.game.ExpertGame;
import it.polimi.ingsw.triton.launcher.server.model.game.Game;
import it.polimi.ingsw.triton.launcher.server.model.game.GameMode;
import it.polimi.ingsw.triton.launcher.server.model.islands.Island;
import it.polimi.ingsw.triton.launcher.server.network.ServeOneClient;
import it.polimi.ingsw.triton.launcher.server.view.VirtualView;
import it.polimi.ingsw.triton.launcher.utils.exceptions.*;
import it.polimi.ingsw.triton.launcher.utils.message.ErrorTypeID;
import it.polimi.ingsw.triton.launcher.utils.message.clientmessage.ClientMessage;
import it.polimi.ingsw.triton.launcher.utils.message.clientmessage.charactercard_replies.CharacterCardReply;
import it.polimi.ingsw.triton.launcher.utils.obs.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller is used to manage the game flow, using visitor pattern to modify the model and to send the correct following message, basing on ClientMessage type
 * Controller is notified by virtual view, when a new message is received
 * Login, number of players and game mode are not managed here, but in server
 * Controller catches all exceptions that model throws
 */

public class Controller implements Observer<ClientMessage> {
    private static final String VIRTUALVIEW_NOT_EXISTS = "The Virtual View does not exist";
    private final List<VirtualView> virtualViews = new ArrayList<>();
    private GameMode game;
    private VirtualView virtualViewToValidate;

    public Controller() {
        this.game = Game.instance(3);
    }

    /**
     * Calls a method in virtualView to send the request to a player for selecting a tower color.
     *
     * @param username the receiver player's username of the message.
     */
    public void createTowerColorRequestMessage(String username) {
        getVirtualViewByUsername(username).askTowerColor(game.getTowerColorChosen());
        virtualViewToValidate = getVirtualViewByUsername(username);
    }

    /**
     * This method is called by a virtual view notify.
     *
     * @param message the message that was received from the client.
     *                Received message is used to modify the model, using overriding to establish what changes I have to do.
     *                modifyModel throws some exceptions, to manage the game flow correctly also in exceptional situations.
     *                4 situations can be verified:
     *                Standard situation, the game is following the regular flow;
     *                Error situation, the user gave a wrong input, so the message must be re-sent;
     *                Exceptional situation, this game micro-phase is ended, so we need to start the next one. For example turn is changed;
     *                End game situations, the game is over and winner must be calculated.
     */
    @Override
    public void update(ClientMessage message) {
        try {
            message.modifyModel(new ClientMessageModifierVisitor(game));
            virtualViewToValidate = getVirtualViewByUsername(game.getCurrentPlayer().getUsername());
            message.createStandardNextMessage(new ClientMessageStandardVisitor(game, virtualViewToValidate));
        } catch (NullPointerException e) {
            virtualViewToValidate = getVirtualViewByUsername(game.getCurrentPlayer().getUsername());
            virtualViewToValidate.showErrorMessage(ErrorTypeID.NULL_VALUE);
            message.createInputErrorMessage(new ClientMessageErrorVisitor(game, virtualViewToValidate));
        } catch (IllegalClientInputException e) {
            virtualViewToValidate = getVirtualViewByUsername(game.getCurrentPlayer().getUsername());
            virtualViewToValidate.showErrorMessage(e.getTypeError());
            message.createInputErrorMessage(new ClientMessageErrorVisitor(game, virtualViewToValidate));
        } catch (LastMoveException | CharacterCardWithParametersException | ChangeTurnException e) {
            virtualViewToValidate = getVirtualViewByUsername(game.getCurrentPlayer().getUsername());
            message.createExceptionalNextMessage(new ClientMessageExceptionalVisitor(game, virtualViewToValidate));
        } catch (EndGameException e) {
            game.calculateWinner();
            resetGame();
        }
    }

    /**
     * Adds an observer (virtual view) of the game.
     *
     * @param virtualView the virtual view that is an observer of the game.
     */
    public void addGameObserver(VirtualView virtualView) {
        game.addObserver(virtualView);
        for (Island island : game.getIslandManager().getIslands())
            island.addObserver(virtualView);
        game.getIslandManager().addObserver(virtualView);
    }

    /**
     * Removes an observer (virtual view) of the game.
     *
     * @param virtualView the virtual view that was an observer of the game.
     */
    public void removeGameObserver(VirtualView virtualView) {
        game.removeObserver(virtualView);
        for (Island island : game.getIslandManager().getIslands())
            island.removeObserver(virtualView);
        game.getIslandManager().removeObserver(virtualView);
    }

    /**
     * @param username the virtual view player's username.
     * @return the virtual view associated to a specific player.
     * @throws NoSuchElementException if the virtual view doesn't exist.
     */
    public VirtualView getVirtualViewByUsername(String username) throws NoSuchElementException {
        for (VirtualView vw : virtualViews) {
            if (vw.getUsername().equals(username))
                return vw;
        }
        throw new NoSuchElementException(VIRTUALVIEW_NOT_EXISTS);
    }

    /**
     * This method returns the Virtual View associated with the ServeOneClient passed as parameter.
     *
     * @param serveOneClient that has a Virtual View
     * @return Virtual View associated with the ServeOneClient
     */
    public VirtualView getLastVirtualViewByServeOneClient(ServeOneClient serveOneClient) {
        return virtualViews.stream().filter(vv -> vv.getServeOneClient().equals(serveOneClient)).findAny().orElseThrow(() -> new NoSuchElementException(VIRTUALVIEW_NOT_EXISTS));
    }

    public List<VirtualView> getVirtualViews() {
        return virtualViews;
    }

    /**
     * Removes the player from the game and his virtual view from the controller.
     *
     * @param vv the virtual view associated to the player to disconnect.
     */
    public synchronized void disconnectPlayer(VirtualView vv) {
        game.removePlayer(vv.getUsername());
        virtualViews.remove(vv);
    }

    public void resetGame() {
        game.endGame(true);
    }

    /**
     * Disconnects all the players removing their virtual views and calling endGame() to reset the instance of the Game.
     * Calling endGame(), Virtual Views will be removed as Game Observers.
     */
    public void disconnectAllPlayers() {
        game.endGame(false);
        for (VirtualView virtualView : virtualViews)
            virtualView.removeObserver(this);
        virtualViews.clear();
    }


    public void addVirtualView(VirtualView virtualView) {
        this.virtualViews.add(virtualView);
    }

    public GameState getGameState() {
        return game.getGameState();
    }

    public int getMaxNumberOfGamePlayers() {
        return game.getMaxNumberOfPlayers();
    }

    public void setMaxNumberOfGamePlayers(int maxNumPlayers) {
        game.setMaxNumberOfPlayers(maxNumPlayers);
    }

    public void removeGamePlayer(String username) {
        game.removePlayer(username);
    }


    public void addGamePlayer(String username) {
        game.addPlayer(username);
    }

    public void setSETUPAsGameState() {
        game.setGameState(GameState.SETUP);
    }

    public void makeGameModeExpert() {
        this.game = new ExpertGame(game);
    }

    public void setVirtualViewToValidate(VirtualView virtualViewToValidate) {
        this.virtualViewToValidate = virtualViewToValidate;
    }


    public void notifyVirtualView(ServeOneClient serveOneClient, ClientMessage message) {
        VirtualView virtualView = virtualViews.stream().filter(vv -> vv.getServeOneClient().equals(serveOneClient)).findAny().orElseThrow(() -> new NoSuchElementException(VIRTUALVIEW_NOT_EXISTS));
        if (checkCorrectnessOfClientMessage(virtualView, message) && checkCorrectnessOfSender(virtualView))
            virtualView.notify(message);
        else {
            virtualView.showErrorMessage(ErrorTypeID.ILLEGAL_MOVE);
            virtualViews.forEach(vv -> vv.showErrorMessage(ErrorTypeID.COMPROMISED));
            disconnectAllPlayers();
        }

    }

    public boolean checkCorrectnessOfSender(VirtualView virtualView) {
        return virtualView == virtualViewToValidate;
    }

    public boolean checkCorrectnessOfClientMessage(VirtualView virtualView, ClientMessage message) {
        if (message instanceof CharacterCardReply) {
            return virtualView.getLastCharacterCardMessage().getExpectedResponseMessageClasses().contains(message.getClass());
        } else return virtualView.getLastMessage().getExpectedResponseMessageClasses().contains(message.getClass());
    }

}