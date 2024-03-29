package it.polimi.ingsw.triton.launcher.client.model;

import it.polimi.ingsw.triton.launcher.server.model.AssistantCard;
import it.polimi.ingsw.triton.launcher.server.model.CloudTile;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CharacterCard;
import it.polimi.ingsw.triton.launcher.server.model.enums.Wizard;
import it.polimi.ingsw.triton.launcher.server.model.islands.Island;
import it.polimi.ingsw.triton.launcher.server.model.player.AssistantDeck;
import it.polimi.ingsw.triton.launcher.server.model.player.SchoolBoard;
import it.polimi.ingsw.triton.launcher.utils.Utility;
import it.polimi.ingsw.triton.launcher.utils.obs.Observable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class saves the state of the game and relevant information.
 * It's updated every time the server model sends messages with updated objects.
 * This class contains all the objects useful for the player and these are used for visualization in cli/gui view.
 */
public class ClientModel extends Observable<Object> {
    private String username;
    private AssistantDeck assistantDeck;
    private List<CharacterCard> availableCharacterCards;
    private List<Island> islands;
    private Map<String, SchoolBoard> schoolBoards;
    private List<CloudTile> cloudTiles;
    private Island motherNaturePosition;
    private boolean expertMode;
    private String[] professors;
    private int wallet;
    private Map<String, Wizard> chosenWizardsPerUsername;
    private final Map<String, AssistantCard> lastAssistantCardPlayedPerUsername = new HashMap<>();
    private CharacterCard lastCharacterCardPlayed;

    /**
     * Generates the string with the descriptions of all the objects that are in client model.
     * This string is useful for the player to choose the strategies of his moves.
     *
     * @return the string with the descriptions of all the elements of client model.
     */
    @Override
    public String toString() {
        String result = "";
        if (expertMode)
            result += "\nEXPERT GAME:";
        else
            result += "\nGAME:";
        result += " \n- " + assistantDeck.toString() +
                " \n- Players' Wizards: " + chosenWizardsPerUsername.toString() +
                " \n- School Boards:\n" + printAllSchoolBoards() +
                " \n- Cloud Tiles:" + printCloudTiles() +
                " \n- Islands =" + printIslands() +
                " \n- Mother Nature is on Island " + motherNaturePosition.getId() +
                " \n- Professors on table: " + Utility.printColoredProfessorsOnTable(professors);
        if (expertMode)
            result += "\n- Character Cards: " + printAvailableCharacterCard();
        result += "\n";
        return result;
    }

    public Map<String, AssistantCard> getLastAssistantCardPlayedPerUsername() {
        return lastAssistantCardPlayedPerUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AssistantDeck getAssistantDeck() {
        return assistantDeck;
    }

    public void setAssistantDeck(AssistantDeck assistantDeck) {
        this.assistantDeck = assistantDeck;
    }

    /**
     * Removes the assistant card the player has just played.
     *
     * @param assistantCardToRemove the assistant card to remove from the client model.
     */
    public void removeCard(AssistantCard assistantCardToRemove) {
        assistantDeck.getAssistantDeck().removeIf(assistantCard -> assistantCard.getType().getValue() == assistantCardToRemove.getType().getValue());
    }

    public Map<String, SchoolBoard> getSchoolBoards() {
        return schoolBoards;
    }

    public List<CharacterCard> getAvailableCharacterCards() {
        return availableCharacterCards;
    }

    public void setAvailableCharacterCards(List<CharacterCard> availableCharacterCards) {
        this.availableCharacterCards = availableCharacterCards;
    }

    /**
     * Updates one of the character cards that are in the client model.
     * This method is called by the serverMessageVisitor when a character card is played by one of the players
     * and so, its cost is increased.
     *
     * @param newCharacterCard the updated character card.
     */
    public void setAvailableCharacterCard(CharacterCard newCharacterCard) {
        CharacterCard characterCardToUpdate = getCharacterCardById(newCharacterCard.getId());
        availableCharacterCards.set(availableCharacterCards.indexOf(characterCardToUpdate), newCharacterCard);
    }


    public CharacterCard getCharacterCardById(int id) {
        for (CharacterCard characterCard : availableCharacterCards) {
            if (characterCard.getId() == id)
                return characterCard;
        }
        return null;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public void setIslands(List<Island> islands) {
        this.islands = islands;
    }

    /**
     * Updates one of the islands with updated values such as number of students, number of towers or the presence
     * of mother nature.
     *
     * @param newIsland the updated island.
     */
    public void setIsland(Island newIsland) {
        for (Island island : islands) {
            if (island.getId() == newIsland.getId())
                islands.set(islands.indexOf(island), newIsland);
        }
    }

    /**
     * Removes an island in the list of islands after this one merged with another island.
     * The island with mother nature remains in the list of islands.
     *
     * @param islandToDelete the island to remove from the list of islands in client model.
     */
    public void removeIsland(Island islandToDelete) {
        islands.removeIf(island -> island.getId() == islandToDelete.getId());
    }

    public void setSchoolBoards(Map<String, SchoolBoard> schoolBoards) {
        this.schoolBoards = schoolBoards;
    }


    public CloudTile getCloudTileById(int id) {
        for (CloudTile cloudTile : cloudTiles)
            if (cloudTile.getId() == id)
                return cloudTile;
        return null;
    }


    public SchoolBoard getMySchoolBoard() {
        return schoolBoards.get(this.username);
    }

    public void setSchoolBoard(String username, SchoolBoard schoolBoard) {
        this.schoolBoards.put(username, schoolBoard);
    }

    public AssistantCard getLastAssistantCardPlayed(String username) {
        return lastAssistantCardPlayedPerUsername.get(username);
    }

    public void setLastAssistantCardPlayed(String username, AssistantCard lastAssistantCardPlayed) {
        this.lastAssistantCardPlayedPerUsername.put(username, lastAssistantCardPlayed);
    }

    /**
     * Generates a string with the description of player's wallet.
     * This method is called every time the player uses a character card or deposits a student in a specific
     * position in his dining room.
     *
     * @return the string with the description of player's wallet.
     */
    public String printWallet() {
        String results = "Your Wallet: " + wallet;
        if (wallet == 1)
            results += " coin";
        else
            results += " coins";
        return results;
    }

    /**
     * @param cloudTiles is the new list to substitute.
     */
    public void setCloudTiles(List<CloudTile> cloudTiles) {
        this.cloudTiles = cloudTiles;
    }

    public void setCloudTile(CloudTile newCloudTile) {
        for (CloudTile cloudTile : cloudTiles) {
            if (cloudTile.getId() == newCloudTile.getId())
                cloudTiles.set(cloudTiles.indexOf(cloudTile), newCloudTile);
        }
    }

    /**
     * @param newCloudTile to remove, indeed it has no more students on it.
     */
    public void removeCloudTile(CloudTile newCloudTile) {
        cloudTiles.removeIf(cloudTile -> cloudTile.getId() == newCloudTile.getId());
    }

    /**
     * Generates a string with the description of the cloud tiles.
     * This method is called every time the player has to choose a cloud tile to draw students.
     *
     * @return the string with the description of the cloud tiles.
     */
    public String printCloudTiles() {
        StringBuilder results = new StringBuilder(" ");
        for (CloudTile cloudTile : cloudTiles) {
            results.append(cloudTile.toString());
        }
        return results.toString();
    }

    /**
     * Generates a string with the description of available character cards.
     * This method is called every time the player wants to play a character card and digits the command --playCC.
     *
     * @return the string with the description of available character cards.
     */
    public String printAvailableCharacterCard() {
        StringBuilder results = new StringBuilder(" ");
        for (CharacterCard characterCard : availableCharacterCards) {
            results.append(characterCard.toString());
        }
        return results.toString();
    }

    /**
     * Generates a string with the description of the islands.
     * This method is called every time the player moves a student or when mother nature is moving onto another island.
     *
     * @return the string with the description of the islands.
     */
    public String printIslands() {
        StringBuilder results = new StringBuilder(" ");
        for (Island island : islands) {
            results.append(island.toString());
        }
        return results.toString();
    }

    /**
     * Generates a string with the description of this model's owner school board.
     * This method is called every time the player has to move a student from entrance.
     *
     * @return the string with the description of this model's owner school board.
     */
    public String printYourSchoolBoard() {
        for (Map.Entry<String, SchoolBoard> schoolBoardEntry : schoolBoards.entrySet()) {
            if (schoolBoardEntry.getKey().equals(username))
                return printSchoolBoard(schoolBoardEntry);
        }
        return "";
    }


    /**
     * Generates a string with the description of opponents' school boards.
     * This method is called every time the player has to move a student from entrance to establish the strategy.
     *
     * @return the string with the description of opponents' school boards.
     */
    public String printOtherSchoolBoards() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, SchoolBoard> schoolBoardEntry : schoolBoards.entrySet()) {
            if (!schoolBoardEntry.getKey().equals(username))
                result.append(printSchoolBoard(schoolBoardEntry));
        }
        return result.toString();
    }

    /**
     * Generates a string with the description of all players' school boards.
     * This method is called every time the player has to move a student from entrance.
     *
     * @return the string with the description of all players' school boards.
     */
    public String printAllSchoolBoards() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, SchoolBoard> schoolBoardEntry : schoolBoards.entrySet())
            result.append(printSchoolBoard(schoolBoardEntry));
        return result.toString();
    }

    private String printSchoolBoard(Map.Entry<String, SchoolBoard> schoolBoardEntry) {
        String result = "\t";
        if (schoolBoardEntry.getKey().equals(username))
            result += "Your ";
        else
            result += schoolBoardEntry.getKey() + "'s ";
        result += "SchoolBoard: " + schoolBoardEntry.getValue().toString();
        result += "\t\t\tProfessors: " + printProfessors(schoolBoardEntry.getKey()) + "\n";
        return result;
    }

    /**
     * Generates a string with the description of professors belonged to each player.
     * This method is called every time school boards are shown.
     *
     * @return the string with the description of professors belonged to each player.
     */
    public String printProfessors(String owner) {
        return Utility.printColoredProfessorsOnSchoolBoard(professors, owner);
    }

    public Island getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public void setMotherNaturePosition(Island motherNaturePosition) {
        this.motherNaturePosition = motherNaturePosition;
    }

    public String[] getProfessors() {
        return professors;
    }

    public void setProfessors(String[] professors) {
        this.professors = professors;
    }

    public void setWallet(int value) {
        this.wallet = value;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public void setExpertMode(boolean expertMode) {
        this.expertMode = expertMode;
    }

    public Map<String, Wizard> getChosenWizardsPerUsername() {
        return chosenWizardsPerUsername;
    }

    public void setChosenWizardsPerUsername(Map<String, Wizard> chosenWizardsPerUsername) {
        this.chosenWizardsPerUsername = chosenWizardsPerUsername;
    }

    public CharacterCard getLastCharacterCardPlayed() {
        return lastCharacterCardPlayed;
    }

    public void setLastCharacterCardPlayed(CharacterCard lastCharacterCardPlayed) {
        this.lastCharacterCardPlayed = lastCharacterCardPlayed;
    }

    public void resetLastCharacterCardPlayed() {
        this.lastCharacterCardPlayed = null;
    }

    public int getWallet() {
        return wallet;
    }
}
