package it.polimi.ingsw.triton.launcher.server.model.playeractions;

import it.polimi.ingsw.triton.launcher.server.model.Bag;
import it.polimi.ingsw.triton.launcher.server.model.GeneralCoinSupply;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CharacterCard;
import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.enums.TowerColor;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UseCharacterCardTest {
    private CharacterCard characterCard;
    private Player player;
    private GeneralCoinSupply generalCoinSupply;

    @BeforeEach
    void setup() {
        characterCard = new CharacterCard(3, 2, 0, new Bag());
        player = new Player("Test");
        for (int i = 0; i < 5; i++)
            player.getWallet().increaseValue();
        generalCoinSupply = new GeneralCoinSupply(5);
    }

    @AfterEach
    void tearDown() {
        characterCard = null;
        player = null;
        generalCoinSupply = null;
    }

    /**
     * Tests if the card can be purchased by the player.
     */
    @Test
    void testCardCanNotBePurchased() {
        player.getWallet().decrease(4);
        UseCharacterCard ucc = new UseCharacterCard(characterCard, player, generalCoinSupply);
        assertThrows(IllegalClientInputException.class, ucc::execute);
    }

    /**
     * Tests if the card cost is increased after the player bought it.
     */
    @Test
    void checkNewPriceAfterBuyCard() {
        int oldCost = characterCard.getCost();
        UseCharacterCard ucc = new UseCharacterCard(characterCard, player, generalCoinSupply);
        try {
            ucc.execute();
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        }
        assertEquals(oldCost + 1, characterCard.getCost());
    }

    /**
     * Tests if the player's wallet is decreased correctly.
     */
    @Test
    void checkWalletAfterBuyCard() {
        int oldCost = characterCard.getCost();
        int oldAmount = player.getWallet().getValue();
        UseCharacterCard ucc = new UseCharacterCard(characterCard, player, generalCoinSupply);
        try {
            ucc.execute();
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        }
        assertEquals(oldAmount - oldCost, player.getWallet().getValue());
    }

    /**
     * Tests if the general coin supply is increased correctly.
     */
    @Test
    void checkGeneralCoinSupplyAfterBuyCard() {
        int oldCost = characterCard.getCost();
        int oldAmount = generalCoinSupply.getCoinsAmount();
        UseCharacterCard ucc = new UseCharacterCard(characterCard, player, generalCoinSupply);
        try {
            ucc.execute();
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        }
        assertEquals(oldAmount + oldCost, generalCoinSupply.getCoinsAmount());
    }

    /**
     * Tests if the method launches an exception when the card is the number 7 but the player has not
     * any students in his entrance.
     */
    @Test
    void testWhenCard7AndEntranceIsEmpty() {
        Bag bag = new Bag();
        bag.fillBag();
        player.setSchoolBoard(TowerColor.BLACK, 2);
        characterCard = new CharacterCard(7, 1, 0, bag);
        UseCharacterCard ucc = new UseCharacterCard(characterCard, player, generalCoinSupply);
        assertThrows(IllegalClientInputException.class, ucc::execute);
    }

    /**
     * Tests if the method launches an exception when the card is the number 10 but the player has not
     * any students in his entrance.
     */
    @Test
    void testWhenCard10AndEntranceIsEmpty() {
        player.setSchoolBoard(TowerColor.BLACK, 2);
        try {
            player.getSchoolBoard().addStudentIntoDiningRoom(Color.BLUE);
        } catch (IllegalClientInputException e) {
            throw new RuntimeException(e);
        }
        characterCard = new CharacterCard(10, 1, 0, new Bag());
        UseCharacterCard ucc = new UseCharacterCard(characterCard, player, generalCoinSupply);
        assertThrows(IllegalClientInputException.class, ucc::execute);
    }

    /**
     * Tests if the method launches an exception when the card is the number 10 but the player has not
     * any students in his dining room.
     */
    @Test
    void testWhenCard10AndDiningRoomIsEmpty() {
        player.setSchoolBoard(TowerColor.BLACK, 2);
        player.getSchoolBoard().addStudentIntoEntrance(Color.BLUE);
        characterCard = new CharacterCard(10, 1, 0, new Bag());
        UseCharacterCard ucc = new UseCharacterCard(characterCard, player, generalCoinSupply);
        assertThrows(IllegalClientInputException.class, ucc::execute);
    }
}