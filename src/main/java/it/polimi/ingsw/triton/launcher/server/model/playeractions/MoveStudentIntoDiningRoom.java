package it.polimi.ingsw.triton.launcher.server.model.playeractions;

import it.polimi.ingsw.triton.launcher.server.model.GeneralCoinSupply;
import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import it.polimi.ingsw.triton.launcher.utils.message.Message;
import it.polimi.ingsw.triton.launcher.utils.message.servermessage.EmptyGeneralCoinSupplyMessage;
import it.polimi.ingsw.triton.launcher.utils.message.servermessage.UpdateWalletMessage;
import it.polimi.ingsw.triton.launcher.utils.obs.Observable;

import java.util.NoSuchElementException;

public class MoveStudentIntoDiningRoom extends Observable<Message> implements Action {
    private final Color student;
    private Player currentPlayer;
    private GeneralCoinSupply generalCoinSupply;

    /**
     * @param student     the color of student to move into the dining room.
     * @param currentPlayer the current player who executes the action.
     * @param generalCoinSupply the coin supply of the game.
     */
    public MoveStudentIntoDiningRoom(Color student, Player currentPlayer, GeneralCoinSupply generalCoinSupply) {
        this.student = student;
        this.currentPlayer = currentPlayer;
        this.generalCoinSupply = generalCoinSupply;
    }

    /**
     * @param studentColor is the color of student
     * @return true if the number of students of a certain color is a multiple of 3, false otherwise.
     */
    private boolean isMultiple3(Color studentColor) {
        return (currentPlayer.getSchoolBoard().getStudentsNumber(studentColor) % 3) == 0;
    }

    private boolean isEmptyEntrance(){
        int numStudentsEntrance = 0;
        for(int i = 0; i < currentPlayer.getSchoolBoard().getEntrance().length; i++){
            numStudentsEntrance += currentPlayer.getSchoolBoard().getEntrance()[i];
        }
        return numStudentsEntrance == 0;
    }

    private boolean noStudentsColorInTheEntrance(){
        return currentPlayer.getSchoolBoard().getEntrance()[student.ordinal()] == 0;
    }

    /**
     * Calls increaseValue() to add a coin in the wallet and to remove a coin from the supply.
     */
    private void updateWallet() {
        try{
            generalCoinSupply.decrement();
            currentPlayer.getWallet().increaseValue();
            notify(new UpdateWalletMessage(currentPlayer.getUsername()));
        }catch (NoSuchElementException e){
            notify(new EmptyGeneralCoinSupplyMessage(currentPlayer.getUsername()));
        }
    }

    /**
     * Adds a student in the schoolBoard dining room and check if there is
     * a number of students which is multiple of 3.
     */
    @Override
    public void execute() throws IllegalClientInputException {
        if(isEmptyEntrance())
            throw new IllegalClientInputException();
        else if(noStudentsColorInTheEntrance())
            throw new IllegalClientInputException();
        else{
            currentPlayer.getSchoolBoard().addStudentIntoDiningRoom(student);
            if (isMultiple3(student))
                updateWallet();
        }
    }
}
