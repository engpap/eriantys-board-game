package it.polimi.ingsw.triton.launcher.server.model.game;

import it.polimi.ingsw.triton.launcher.server.model.*;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CardEffect;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CharacterCard;
import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.enums.GameState;
import it.polimi.ingsw.triton.launcher.server.model.enums.TowerColor;
import it.polimi.ingsw.triton.launcher.server.model.enums.Wizard;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;
import it.polimi.ingsw.triton.launcher.server.model.player.SchoolBoard;
import it.polimi.ingsw.triton.launcher.server.model.professor.ProfessorsManager;
import it.polimi.ingsw.triton.launcher.utils.exceptions.*;
import it.polimi.ingsw.triton.launcher.utils.message.servermessage.InfoMessage;
import it.polimi.ingsw.triton.launcher.utils.obs.Observable;

import java.util.ArrayList;
import java.util.Map;

public abstract class GameMode extends Observable<InfoMessage>{
    public abstract void setup();
    abstract void setupPlayers();
    abstract void planningPhase();
    public abstract void executeActionMoveStudentToDiningRoom(Color student) throws LastMoveException, IllegalClientInputException;
    public abstract void useCharacterCard(String username, int idCard) throws IllegalClientInputException, CharacterCardWithParametersException;
    public abstract void applyCharacterCardEffect(int characterCardID, CardEffect cardEffect) throws IllegalClientInputException, EndGameException;
    public abstract ArrayList<CharacterCard> getCharacterCards();
    public abstract CharacterCard getCharacterCardByID(int id) throws IllegalClientInputException;
    public abstract void setGameState(GameState setup);
    public abstract boolean[] getTowerColorChosen();
    public abstract Player getCurrentPlayer();
    public abstract void calculateWinner();
    public abstract void addPlayer(String username);
    public abstract ArrayList<Island> getIslands();
    public abstract GameState getGameState();
    public abstract void disconnectPlayers();
    public abstract ArrayList<Player> getPlayers();
    public abstract void chooseTowerColor(String senderUsername, TowerColor playerColor) throws IllegalClientInputException, ChangeTurnException;
    public abstract void chooseWizard(String senderUsername, Wizard playerWizard) throws IllegalClientInputException, ChangeTurnException;
    public abstract void chooseAssistantCard(String senderUsername, AssistantCard chosenAssistantCard) throws IllegalClientInputException, ChangeTurnException;
    public abstract void executeActionMoveStudentToIsland(Color student, int islandID) throws IllegalClientInputException, LastMoveException;
    public abstract void moveMotherNature(int numSteps) throws IllegalClientInputException, EndGameException, ChangeTurnException;
    public abstract CloudTile getCloudTileById(int selectedCloudTileID) throws IllegalClientInputException;
    public abstract void chooseCloudTile(CloudTile cloudTileById) throws IllegalClientInputException, ChangeTurnException, EndGameException;
    public abstract Player getPlayerByUsername(String senderUsername);
    public abstract MotherNature getMotherNature();
    public abstract ProfessorsManager getProfessorsManager();
    public abstract Player[] getProfessors();
    public abstract Island getIslandByID(int islandID) throws IllegalClientInputException;
    public abstract Bag getBag();
    public abstract ArrayList<Wizard> getAvailableWizards();
    abstract void setupMotherNature();
    abstract void setupBag();
    abstract void setupIslands();
    abstract void createCloudTiles();
    abstract void setupEntrance();
    abstract void checkNumberMoves() throws LastMoveException;
    abstract Map<String, SchoolBoard> getAllSchoolBoards();
    public abstract ArrayList<CloudTile> getCloudTiles();
    abstract String[] professorsWithUsernameOwner();
    public abstract int getMaxNumberOfPlayers();
    public abstract void setMaxNumberOfPlayers(int maxNumberOfPlayers);
    abstract void setCurrentPlayer(Player player);
}