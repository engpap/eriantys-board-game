package it.polimi.ingsw.triton.launcher.server.controller;

import it.polimi.ingsw.triton.launcher.server.model.Game;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CardEffect;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CardEffect01;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CardEffect03;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CardEffect05;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;
import it.polimi.ingsw.triton.launcher.server.view.VirtualView;
import it.polimi.ingsw.triton.launcher.utils.IllegalClientInputException;
import it.polimi.ingsw.triton.launcher.utils.message.ErrorTypeID;
import it.polimi.ingsw.triton.launcher.utils.message.Message;
import it.polimi.ingsw.triton.launcher.utils.message.clientmessage.*;
import it.polimi.ingsw.triton.launcher.utils.obs.Observer;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Server has a reference to Controller. This reference is passed to
 * ServeOneClient, so the latter can send new data to the Controller.
 *
 * NoSuchElementException is caught when there is no next player.
 */

public class Controller implements Observer<Message> {
    private final Game game;
    private final ArrayList<VirtualView> virtualViews = new ArrayList<>();
    String senderUsername;

    public Controller(Game game) {
        this.game = game;
    }

    public ArrayList<VirtualView> getVirtualViews() {
        return virtualViews;
    }

    public void addPlayer(String username) throws IllegalArgumentException {
        game.addPlayer(username);
    }

    public void addGameObserver(VirtualView virtualView) {
        game.addObserver(virtualView);
    }

    public VirtualView getVirtualViewByUsername(String username) throws NoSuchElementException {
        for (VirtualView vw : virtualViews) {
            if (vw.getUsername().equals(username))
                return vw;
        }
        throw new NoSuchElementException("The virtualview does not exist");
    }

    /**
     * Calls a method in virtualView to send the request to a player for selecting a tower color.
     *
     * @param username the receiver username of the message
     */
    public void createTowerColorRequestMessage(String username) {
        getVirtualViewByUsername(username).askTowerColor(game.getTowerColorChosen());
    }

    public void createWizardRequestMessage(String username) {
        getVirtualViewByUsername(username).askWizard();
    }

    public void createAssistantCardRequestMessage(String username) {
        getVirtualViewByUsername(username).askAssistantCard();
    }

    private void createMoveStudentRequest(String username){
        getVirtualViewByUsername(username).askMoveStudentFromEntrance();
    }


    /**
     * In the login phase, it selects the methods in game to setting some properties to the players.
     *
     * @param message the message received.
     */
    private void loginPhaseSwitch(Message message) {
        switch (message.getMessageType()) {
            case TOWER_COLOR_REPLY: {
                try {
                    game.chooseTowerColor(senderUsername, ((TowerColorReply) message).getPlayerColor());
                    createTowerColorRequestMessage(game.getNextPlayer(game.getPlayerByUsername(senderUsername)).getUsername());
                    game.setCurrentPlayer(game.getNextPlayer(game.getPlayerByUsername(senderUsername)));
                    break;
                } catch (IllegalClientInputException e) {
                    getVirtualViewByUsername(senderUsername).showErrorMessage(ErrorTypeID.WRONG_COLOR);
                    createTowerColorRequestMessage(senderUsername);
                } catch (NoSuchElementException e) {
                    createWizardRequestMessage(senderUsername);
                    game.setCurrentPlayer(game.getPlayers().get(0));
                    break;
                }
            }
            case WIZARD_REPLY: {
                try {
                    game.chooseWizard(((ClientMessage) message).getSenderUsername(), ((WizardReply) message).getPlayerWizard());
                    createWizardRequestMessage(game.getNextPlayer(game.getPlayerByUsername(senderUsername)).getUsername());
                    game.setCurrentPlayer(game.getNextPlayer(game.getPlayerByUsername(senderUsername)));
                    break;
                } catch (IllegalClientInputException e) {
                    getVirtualViewByUsername(senderUsername).showErrorMessage(ErrorTypeID.WRONG_WIZARD);
                    createWizardRequestMessage(senderUsername);
                    break;
                } catch (NoSuchElementException e) {
                    game.setup();
                    createAssistantCardRequestMessage(game.getCurrentPlayer().getUsername());
                    game.setCurrentPlayer(game.getPlayers().get(0));
                    break;
                }
            }
            default: {
                characterCardsParametersSwitch(message);
                break;
            }
        }
    }

    /**
     * In the planning phase, it selects the methods in game to do some actions.
     *
     * @param message the message received.
     */
    private void planningPhaseSwitch(Message message) {
        switch (message.getMessageType()) {
            case ASSISTANT_CARD_REPLY: {
                try {
                    game.chooseAssistantCard(((ClientMessage) message).getSenderUsername(), ((AssistantCardReply) message).getChosenAssistantCard());
                    createAssistantCardRequestMessage(game.getNextPlayer(game.getPlayerByUsername(senderUsername)).getUsername());
                    game.setCurrentPlayer(game.getNextPlayer(game.getPlayerByUsername(senderUsername)));
                    break;
                } catch (IllegalClientInputException e) {
                    getVirtualViewByUsername(senderUsername).showErrorMessage(ErrorTypeID.ASSISTANTCARD_ALREADY_CHOSEN);
                    createWizardRequestMessage(senderUsername);
                    break;
                } catch (NoSuchElementException e) {
                    createAssistantCardRequestMessage(game.getCurrentPlayer().getUsername());
                    game.sortPlayerPerTurn();
                    game.setCurrentPlayer(game.getPlayers().get(0));
                    game.actionPhase();
                    createMoveStudentRequest(game.getCurrentPlayer().getUsername());
                    break;
                }
            }
        }
    }

    private void actionPhaseSwitch(Message message) {
        switch (message.getMessageType()) {
            case MOVE_STUDENT_ONTO_ISLAND: {
                try {
                    game.chooseAssistantCard(((ClientMessage) message).getSenderUsername(), ((AssistantCardReply) message).getChosenAssistantCard());
                    createAssistantCardRequestMessage(game.getNextPlayer(game.getPlayerByUsername(senderUsername)).getUsername());
                    game.setCurrentPlayer(game.getNextPlayer(game.getPlayerByUsername(senderUsername)));
                    break;
                } catch (IllegalClientInputException e) {
                    getVirtualViewByUsername(senderUsername).showErrorMessage(ErrorTypeID.ASSISTANTCARD_ALREADY_CHOSEN);
                    createWizardRequestMessage(senderUsername);
                    break;
                } catch (NoSuchElementException e) {
                    createAssistantCardRequestMessage(game.getCurrentPlayer().getUsername());
                    game.sortPlayerPerTurn();
                    game.setCurrentPlayer(game.getPlayers().get(0));
                    game.actionPhase();
                    break;
                }
            }
        }
    }

    /**
     * It manages the creation of the effects and calls a method in game to execute them.
     *
     * @param message the message received
     */
    private void characterCardsParametersSwitch(Message message) {
        CardEffect cardEffect;
        switch (message.getMessageType()) {
            case CHARACTER_CARD_01_PARAMETER:
                cardEffect = new CardEffect01(game.getCharacterCardById(1), ((CharacterCard01Reply) message).getStudent(), ((CharacterCard01Reply) message).getIsland(), game.getBag());
                game.useCharacterCardsWithPreparation(game.getCharacterCardById(1), cardEffect);
                break;
            case CHARACTER_CARD_03_PARAMETER:
                cardEffect = new CardEffect03(((CharacterCard03Reply) message).getIsland(), game.getPlayers(), game.getProfessors());
                game.useCharacterCardsWithPreparation(game.getCharacterCardById(3), cardEffect);
                break;
            case CHARACTER_CARD_05_PARAMETER:
                cardEffect = new CardEffect05(((CharacterCard05Reply) message).getIsland(), game.getCharacterCardById(5));
                game.useCharacterCardsWithPreparation(game.getCharacterCardById(5), cardEffect);
                break;
            case CHARACTER_CARD_09_PARAMETER:
                break;
            case CHARACTER_CARD_10_PARAMETER:
                break;
            case CHARACTER_CARD_11_PARAMETER:
                break;
            case CHARACTER_CARD_12_PARAMETER:
                break;
        }
    }

    @Override
    public void update(Message message) {
        senderUsername = ((ClientMessage) message).getSenderUsername();
        switch (game.getGameState()) {
            case LOGIN:
                loginPhaseSwitch(message);
                break;
            case PLANNING_PHASE:
                planningPhaseSwitch(message);
                break;
            case ACTION_PHASE:
                actionPhaseSwitch(message);
                break;
            default:
                break;
        }
    }
}








    /*private Game game;


    public void addPlayer(String username){
        if(game.isUsernameChosen(username)){
            //return an error message
        }
        else
            game.addPlayer(username);
    }
     */


// ---------------------------------------------------------
// TO DELETE: some examples to check that the server works
// example, just to test if the server works
    /*private String username;


    // this is called by ServeOneClient
    public void setUsername(String username) {
        this.username = username;
    }

    public void print() {
        System.out.println("bro:" + username);
    }*/
