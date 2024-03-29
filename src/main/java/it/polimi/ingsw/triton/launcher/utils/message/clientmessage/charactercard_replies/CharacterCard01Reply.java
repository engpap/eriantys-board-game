package it.polimi.ingsw.triton.launcher.utils.message.clientmessage.charactercard_replies;

import it.polimi.ingsw.triton.launcher.server.controller.visitors.ClientMessageModifierVisitor;
import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.utils.exceptions.EndGameException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;

/**
 * This message is sent by the client to communicate to server the color and the id of the island chosen by the current player
 * in order to build and apply the effect of the character card 01.
 */
public class CharacterCard01Reply extends CharacterCardReply {
    private final Color student;
    private final int islandID;

    public CharacterCard01Reply(Color student, int islandID) {
        this.islandID = islandID;
        this.student = student;
    }

    public Color getStudent() {
        return student;
    }

    public int getIslandID() {
        return islandID;
    }

    @Override
    public void modifyModel(ClientMessageModifierVisitor visitor) throws IllegalClientInputException, EndGameException {
        visitor.visitForModify(this);
    }
}
