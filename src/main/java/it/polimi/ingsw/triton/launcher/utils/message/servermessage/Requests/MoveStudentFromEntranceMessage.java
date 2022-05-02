package it.polimi.ingsw.triton.launcher.utils.message.servermessage.Requests;

import it.polimi.ingsw.triton.launcher.client.ClientVisitor;
import it.polimi.ingsw.triton.launcher.utils.message.MessageType;
import it.polimi.ingsw.triton.launcher.utils.message.servermessage.ServerMessage;

/**
 * This message asks to the player which student wants to move.
 */
public class MoveStudentFromEntranceMessage extends ServerMessage {
    public MoveStudentFromEntranceMessage() {
        super(MessageType.MOVE_STUDENT_FROM_ENTRANCE);
    }

    @Override
    public void accept(ClientVisitor messageVisitor) {

    }
}