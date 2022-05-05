package it.polimi.ingsw.triton.launcher.utils.message.servermessage.infoMessage;


import it.polimi.ingsw.triton.launcher.client.ServerMessageVisitor;
import it.polimi.ingsw.triton.launcher.utils.message.servermessage.InfoMessage;

/**
 * This message communicates to the players that one player had to move his towers back to his schoolboard.
 */
public class MoveTowerToSchoolBoardMessage extends InfoMessage {
    private String usernameDominated;
    private int numTowersToSchoolBoard;
    public MoveTowerToSchoolBoardMessage(String usernameDominated, int numTowersToSchoolBoard) {
        this.usernameDominated = usernameDominated;
        this.numTowersToSchoolBoard = numTowersToSchoolBoard;
    }

    public String getUsernameDominated() {
        return usernameDominated;
    }

    public int getNumTowersToSchoolBoard() {
        return numTowersToSchoolBoard;
    }

    @Override
    public void accept(ServerMessageVisitor messageVisitor) {

    }
}