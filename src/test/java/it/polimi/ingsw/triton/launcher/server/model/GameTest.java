package it.polimi.ingsw.triton.launcher.server.model;

import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CharacterCard;
import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.enums.TowerColor;
import it.polimi.ingsw.triton.launcher.server.model.enums.Wizard;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;
import it.polimi.ingsw.triton.launcher.utils.exceptions.ChangeTurnException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    public void setUp(){
        game = new Game(3);
        game.addPlayer("TestPlayer1");
        game.addPlayer("TestPlayer2");
        game.addPlayer("TestPlayer3");
        try {
            game.chooseTowerColor("TestPlayer1",TowerColor.WHITE);
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        } catch (ChangeTurnException e) {
            e.printStackTrace();
        }
        try {
            game.chooseTowerColor("TestPlayer2",TowerColor.BLACK);
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        } catch (ChangeTurnException e) {
            e.printStackTrace();
        }
        try {
            game.chooseTowerColor("TestPlayer3",TowerColor.GREY);
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        } catch (ChangeTurnException e) {
            e.printStackTrace();
        }
        try {
            game.chooseWizard("TestPlayer1", Wizard.BLUE);
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        } catch (ChangeTurnException e) {
            e.printStackTrace();
        }
        try {
            game.chooseWizard("TestPlayer2", Wizard.GREEN);
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        } catch (ChangeTurnException e) {
            e.printStackTrace();
        }
        try {
            game.chooseWizard("TestPlayer3", Wizard.PURPLE);
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        } catch (ChangeTurnException e) {
            e.printStackTrace();
        }
        game.setup();
    }

    @AfterEach
    public void tearDown(){
        game = null;
    }



    /**
     * Tests if the number of islands created is twelve.
     */
    @Test
    public void checkNumberOfIslands(){
        assertEquals(12, game.getIslands().size());
    }

    /**
     * Tests if mother nature is onto an island at the begin of the game.
     */
    @Test
    public void checkMotherNaturePosition(){
        assertTrue(game.getMotherNature().getPosition() != null);
    }

    /**
     * Tests if the number of students in the bag at the begin of the game is greater than zero.
     */
    @Test
    public void checkInitialNumberStudentsInBag(){
        for(Color color: Color.values())
            assertTrue(game.getBag().getStudents()[color.ordinal()] > 0);
    }

    /**
     * Tests if the number of students is 0 when the island contains mother nature.
     */
    @Test
    public void checkNumberOfStudentsOnIslandWithMotherNature(){
        Island islandWithMotherNature = game.getMotherNature().getPosition();
        int numOfStudents = 0;
        for(Color color: Color.values())
            numOfStudents += islandWithMotherNature.getStudents()[color.ordinal()];
        assertEquals(0, numOfStudents);
    }

    /**
     * Tests if the number of students is 0 when the island is opposite to mother nature..
     */
    @Test
    public void checkNumberOfStudentsOnIslandOppositeToMotherNature(){
        int idIslandOppositeToMotherNature = (game.getMotherNature().getPosition().getId() + 6) % 12;
        int numOfStudents = 0;
        for(Color color: Color.values())
            numOfStudents += game.getIslands().get(idIslandOppositeToMotherNature).getStudents()[color.ordinal()];
        assertEquals(0, numOfStudents);
    }

    /**
     * Tests if the number of students is 1 onto other islands.
     */
    @Test
    public void checkNumberOfStudentsOnOtherIslands(){
        int idIslandWithMotherNature = game.getMotherNature().getPosition().getId();
        int idIslandOppositeToMotherNature = (game.getMotherNature().getPosition().getId() + 6) % 12;
        boolean numOfStudentsNotEqualsToOne = false;
        int numOfStudents = 0;
        for(Island island: game.getIslands()){
            if(island.getId() != idIslandWithMotherNature && island.getId() != idIslandOppositeToMotherNature){
                for(Color color: Color.values()){
                    numOfStudents += island.getStudents()[color.ordinal()];
                }
                if(numOfStudents != 1)
                    numOfStudentsNotEqualsToOne = true;
            }
            numOfStudents = 0;
        }
        assertFalse(numOfStudentsNotEqualsToOne);
    }

    /**
     * This test checks if game creates three cloudtiels when there are three players.
     */
    @Test
    public void checkCreateCloudTilesWithThreePlayers(){
        assertEquals(3,game.getCloudTiles().size());
    }

    /**
     * This method checks if game add nine students into the entrance of every players when the game has three players.
     */
    @Test
    public void checkIfEachPlayerHasNineStudentsIntoEntrance(){
        int numStudentsIntoEntrance;
        for (Player player: game.getPlayers()) {
            numStudentsIntoEntrance = 0;
            for (int i = 0; i < Color.values().length; i++){
                numStudentsIntoEntrance += player.getSchoolBoard().getEntrance()[i];
            }
            assertEquals(9,numStudentsIntoEntrance);
        }
    }

    /**
     * Tests if the number of character cards created is three.
     */
    @Test
    public void checkCharacterCardsSizeIsThree(){
        assertEquals(3, game.getCharacterCards().size());
    }

   /* @Test
    public void checksIfEachCloudTilesHasFourStudents(){
        int numStudentsOnCloudTile;
        for (CloudTile cloudTile : game.getCloudTiles()) {
            numStudentsOnCloudTile = 0;
            for (int i = 0; i < Color.values().length; i++){
                numStudentsOnCloudTile += cloudTile.getStudents()[i];
            }
            assertEquals(4,numStudentsOnCloudTile);
        }
    }*/

    /**
     * Tests if the method launches an exception if the nickname is already chosen.
     */
    @Test
    public void addPlayerWhenUsernameIsChosen(){
        Player playerTest = new Player("TestPlayer1");
        assertThrows(IllegalArgumentException.class, ()->game.addPlayer(playerTest.getUsername()));
    }

    /**
     * Tests if the method launches an exception if the nickname is eriantys.
     */
    @Test
    public void addPlayerWhenUsernameIsEriantys(){
        Player playerTest = new Player("eriantys");
        assertThrows(IllegalArgumentException.class, ()->game.addPlayer(playerTest.getUsername()));
    }

    /**
     * Tests if the method launches an exception if the nickname is written in capital letters
     * but is already chosen.
     */
    @Test
    public void addPlayerWhenUsernameIsChosenInCapitalLetters(){
        Player playerTest = new Player("TESTPLAYER2");
        assertThrows(IllegalArgumentException.class, ()->game.addPlayer(playerTest.getUsername()));
    }

    /**
     * Tests if the array of chosen tower colors is updated correctly when a player
     * selects a tower color.
     */
    @Test
    public void checkTowerColorSelected(){
        assertTrue(game.getTowerColorChosen()[TowerColor.WHITE.ordinal()]);
    }

   /* @Test
    public void checkSortingPlayers(){
        player1.setLastPlayedAssistantCard(new AssistantCard(AssistantCardType.SNAKE));
        player2.setLastPlayedAssistantCard(new AssistantCard(AssistantCardType.TIGER));
        player3.setLastPlayedAssistantCard(new AssistantCard(AssistantCardType.CAT));
        game.sortPlayerPerTurn();
        assertEquals("TestPlayer2", game.getPlayers().get(0).getUsername());
        assertEquals("TestPlayer3", game.getPlayers().get(1).getUsername());
        assertEquals("TestPlayer1", game.getPlayers().get(2).getUsername());

    }*/


    /**
     * Tests if the id of the three character cards are different.
     */
    @Test
    public void checkCharacterCardsIdAreDifferent(){
        boolean foundSameId = false;
        for(int i = 0; i < game.getCharacterCards().size() - 1; i++){
            for(int j = i + 1; j < game.getCharacterCards().size(); j++){
                if(game.getCharacterCards().get(i).getId() == game.getCharacterCards().get(j).getId())
                    foundSameId = true;
            }
        }
        assertFalse(foundSameId);
    }




    //test calculate winner
   /*@Test
    public void checkOneWinnerWithTower(){
        game.getPlayers().get(0).getSchoolBoard().moveTowerOntoIsland(2);
        game.getPlayers().get(1).getSchoolBoard().moveTowerOntoIsland(5);
        game.getPlayers().get(2).getSchoolBoard().moveTowerOntoIsland(3);
        assertEquals(game.getPlayers().get(1).getUsername(), game.calculateWinner());
    }


    @Test
    public void checkOneWinnerWithProfessors(){
        game.getPlayers().get(0).getSchoolBoard().moveTowerOntoIsland(2);
        game.getPlayers().get(1).getSchoolBoard().moveTowerOntoIsland(5);
        game.getPlayers().get(2).getSchoolBoard().moveTowerOntoIsland(5);
        game.getProfessors()[Color.BLUE.ordinal()] = game.getPlayers().get(1);
        game.getProfessors()[Color.GREEN.ordinal()] = game.getPlayers().get(2);
        game.getProfessors()[Color.YELLOW.ordinal()] = game.getPlayers().get(2);
        assertEquals(game.getPlayers().get(2).getUsername(), game.calculateWinner());

    }

    @Test
    public void checkTieWinners(){
        game.getPlayers().get(0).getSchoolBoard().moveTowerOntoIsland(2);
        game.getPlayers().get(1).getSchoolBoard().moveTowerOntoIsland(5);
        game.getPlayers().get(2).getSchoolBoard().moveTowerOntoIsland(5);
        game.getProfessors()[Color.BLUE.ordinal()] = game.getPlayers().get(1);
        game.getProfessors()[Color.GREEN.ordinal()] = game.getPlayers().get(2);
        assertEquals("Tie", game.calculateWinner());
    }*/

    /**
     * Tests if the method returns the correct island when exists.
     */
    @Test
    void testGetIslandByIdWhenExists(){
        Island island;
        int indexIsland = 4;
        try {
            island = game.getIslandByID(indexIsland);
        } catch (IllegalClientInputException e) {
            throw new RuntimeException(e);
        }
        assertEquals(indexIsland, island.getId());
    }

    /**
     * Tests if the method launches an exception when the island doesn't exists.
     */
    @Test
    void testGetIslandByIdWhenNotExists(){
        assertThrows(IllegalClientInputException.class, ()->game.getIslandByID(20));
    }

    /**
     * Tests if the method returns the correct cloud tile when exists.
     */
    @Test
    void testGetCloudTileByIdWhenExists(){
        CloudTile cloudTile;
        int indexCloudTile = 1;
        try {
            cloudTile = game.getCloudTileById(indexCloudTile);
        } catch (IllegalClientInputException e) {
            throw new RuntimeException(e);
        }
        assertEquals(indexCloudTile, cloudTile.getId());
    }

    /**
     * Tests if the method launches an exception when the cloud tile doesn't exists.
     */
    @Test
    void testGetCloudTileByIdWhenNotExists(){
        assertThrows(IllegalClientInputException.class, ()->game.getCloudTileById(20));
    }

    /**
     * Tests if the method returns the correct character card when exists.
     */
    @Test
    void testGetCharacterCardByIdWhenExists(){
        CharacterCard characterCard;
        int indexCharacterCard = game.getCharacterCards().get(1).getId();
        try {
            characterCard = game.getCharacterCardByID(indexCharacterCard);
        } catch (IllegalClientInputException e) {
            throw new RuntimeException(e);
        }
        assertEquals(indexCharacterCard, characterCard.getId());
    }

    /**
     * Tests if the method launches an exception when the character card doesn't exists.
     */
    @Test
    void testGetCharacterCardByIdWhenNotExists(){
        assertThrows(IllegalClientInputException.class, ()->game.getCharacterCardByID(20));
    }
}

