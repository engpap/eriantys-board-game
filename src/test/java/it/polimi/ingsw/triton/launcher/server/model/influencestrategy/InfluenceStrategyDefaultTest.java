package it.polimi.ingsw.triton.launcher.server.model.influencestrategy;

import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.enums.TowerColor;
import it.polimi.ingsw.triton.launcher.server.model.islands.Island;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;
import it.polimi.ingsw.triton.launcher.server.model.playeractions.MoveStudentOntoIsland;
import it.polimi.ingsw.triton.launcher.utils.exceptions.EndGameException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InfluenceStrategyDefaultTest {
    private Player p1;
    private Player p2;
    private Player[] professors;
    private Island island;

    @BeforeEach
    void setUp() {
        p1 = new Player("playerTest1");
        p2 = new Player("playerTest2");
        p1.setSchoolBoard(TowerColor.BLACK, 2);
        p2.setSchoolBoard(TowerColor.WHITE, 2);
        professors = new Player[5];
        professors[Color.GREEN.ordinal()] = p1;
        professors[Color.RED.ordinal()] = p2;
        professors[Color.YELLOW.ordinal()] = null;
        professors[Color.PINK.ordinal()] = p2;
        professors[Color.BLUE.ordinal()] = p1;
        island = new Island(3);
        p1.getSchoolBoard().addStudentIntoEntrance(Color.YELLOW);
        p1.getSchoolBoard().addStudentIntoEntrance(Color.BLUE);
        p2.getSchoolBoard().addStudentIntoEntrance(Color.PINK);
        p1.getSchoolBoard().addStudentIntoEntrance(Color.GREEN);
        try {
            new MoveStudentOntoIsland(p1.getSchoolBoard(), Color.YELLOW, island).execute();
            new MoveStudentOntoIsland(p1.getSchoolBoard(), Color.BLUE, island).execute();
            new MoveStudentOntoIsland(p2.getSchoolBoard(), Color.PINK, island).execute();
            new MoveStudentOntoIsland(p1.getSchoolBoard(), Color.GREEN, island).execute();
        } catch (IllegalClientInputException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        p1 = null;
        p2 = null;
        professors = null;
        island = null;
    }

    /**
     * Checks if the influence is calculated correctly when the dominator of the island is null.
     */
    @Test
    void testCalculateInfluenceDefaultWhenDominatorIsNull() {
        assertEquals(2, new InfluenceStrategyDefault().execute(p1, professors, null, island));
    }

    /**
     * Checks if the influence is calculated correctly when the dominator of the island is not null, and
     * he's the player interested in calculating the influence.
     */
    @Test
    void testCalculateInfluenceDefaultWhenDominatorIsNotNull() {
        try {
            p1.getSchoolBoard().moveTowerOntoIsland(island.getId());
        } catch (EndGameException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3, new InfluenceStrategyDefault().execute(p1, professors, p1, island));
    }

    /**
     * Checks if the influence is calculated correctly when the dominator of the island is not null, and
     * he's not the player interested in calculating the influence.
     */
    @Test
    void testCalculateInfluenceDefaultWhenDominatorIsNotTheCurrentPlayer() {
        try {
            p2.getSchoolBoard().moveTowerOntoIsland(island.getId());
        } catch (EndGameException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, new InfluenceStrategyDefault().execute(p1, professors, p2, island));
    }
}