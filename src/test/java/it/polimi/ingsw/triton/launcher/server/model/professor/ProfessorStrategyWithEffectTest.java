package it.polimi.ingsw.triton.launcher.server.model.professor;

import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.enums.TowerColor;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProfessorStrategyWithEffectTest {

    // first three methods are a copy of the ones present in ProfessorStrategyDefaultTest

    ProfessorsManager professorsManager;
    Player player;

    private Player[] professors;

    @BeforeEach
    void setUp() {
        professorsManager = new ProfessorsManager();
        professorsManager.setProfessorStrategy(new ProfessorStrategyWithEffect());
        professors = new Player[5];
        player = new Player("TestPlayer");
        player.setSchoolBoard(TowerColor.BLACK, 2);
    }

    @AfterEach
    void tearDown() {
        professorsManager = null;
        player = null;
    }

    /**
     * Tests if the method throws an exception if the parameter color is null.
     */
    @Test
    void throwExceptionIfColorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> professorsManager.updateProfessorsForAddInDiningRoom(player, null, professors));
    }


    /**
     * Tests if the influence is updated correctly when the professor
     * is not taken.
     */
    @Test
    void updateProfessorInfluenceWhenPlayerIsNull() {
        player.getSchoolBoard().getDiningRoom()[Color.BLUE.ordinal()] = 1;
        professorsManager.updateProfessorsForAddInDiningRoom(player, Color.BLUE, professors);
        assertEquals(player, professors[Color.BLUE.ordinal()]);
    }

    /**
     * Tests if the influence is updated correctly when the professor is taken
     * when new player has more students as the previous one.
     */
    @Test
    void updateProfessorInfluenceWhenGreaterThan() {
        Player playerWithProfessor = new Player("TestPlayer1");
        playerWithProfessor.setSchoolBoard(TowerColor.WHITE, 2);
        playerWithProfessor.getSchoolBoard().getDiningRoom()[Color.BLUE.ordinal()] = 1;
        professors[Color.BLUE.ordinal()] = playerWithProfessor;
        player.getSchoolBoard().getDiningRoom()[Color.BLUE.ordinal()] = 2;
        professorsManager.updateProfessorsForAddInDiningRoom(player, Color.BLUE, professors);
        assertEquals(player, professors[Color.BLUE.ordinal()]);
    }

    /**
     * Tests if the influence is updated correctly when the professor is taken and
     * when new player has the same number of students as the previous one.
     */
    @Test
    void updateProfessorInfluenceWhenEquals() {
        Player playerWithProfessor = new Player("TestPlayer1");
        playerWithProfessor.setSchoolBoard(TowerColor.WHITE, 2);
        playerWithProfessor.getSchoolBoard().getDiningRoom()[Color.BLUE.ordinal()] = 1;
        professors[Color.BLUE.ordinal()] = playerWithProfessor;
        player.getSchoolBoard().getDiningRoom()[Color.BLUE.ordinal()] = 1;
        professorsManager.updateProfessorsForAddInDiningRoom(player, Color.BLUE, professors);
        assertEquals(player, professors[Color.BLUE.ordinal()]);
    }

}