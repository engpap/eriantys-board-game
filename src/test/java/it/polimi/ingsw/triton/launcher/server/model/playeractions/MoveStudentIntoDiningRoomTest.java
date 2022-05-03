package it.polimi.ingsw.triton.launcher.server.model.playeractions;

import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.enums.TowerColor;
import it.polimi.ingsw.triton.launcher.server.model.player.SchoolBoard;
import it.polimi.ingsw.triton.launcher.server.model.player.Wallet;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveStudentIntoDiningRoomTest {

    private Wallet wallet;
    private SchoolBoard schoolBoard;

    @BeforeEach
    void setup() {
        wallet = new Wallet();
        schoolBoard = new SchoolBoard(TowerColor.BLACK, 2);
    }

    @AfterEach
    void tearDown() {
        wallet = null;
        schoolBoard = null;
    }
}
    /**
     * Test when the student color is null.
     */
    /*
    @Test
    void testWhenStudentIsNull() {
        MoveStudentIntoDiningRoom mv = new MoveStudentIntoDiningRoom(null, wallet, schoolBoard);
        assertThrows(NullPointerException.class, mv::execute);
    }
    */

    /**
     * Test if the wallet is not increased after the move because it's not a multiple of 3.
     */
    /* LUCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    @Test
    void testWhenIsNotMultiple3(){
        int coins = wallet.getValue();
        MoveStudentIntoDiningRoom mv = new MoveStudentIntoDiningRoom(Color.BLUE, wallet, schoolBoard);
        try {
            mv.execute();
        } catch (IllegalClientInputException e) {
            e.printStackTrace();
        }
        assertEquals(coins, wallet.getValue());
    }

    /**
     * Test if the wallet is increased after the move because it's a multiple of 3.
     */
    /* LUCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    @Test
    void testWhenIsMultiple3(){
        int coins = wallet.getValue();
        MoveStudentIntoDiningRoom mv = new MoveStudentIntoDiningRoom(Color.BLUE, wallet, schoolBoard);
        for(int i = 0; i < 3; i++){
            try {
                mv.execute();
            } catch (IllegalClientInputException e) {
                e.printStackTrace();
            }
        }
        assertEquals(coins+1, wallet.getValue());
    }
}
*/
