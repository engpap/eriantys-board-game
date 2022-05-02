package it.polimi.ingsw.triton.launcher.utils.message.clientmessage.characterCardReply;

import it.polimi.ingsw.triton.launcher.server.model.Game;
import it.polimi.ingsw.triton.launcher.server.model.Island;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CardEffect01;
import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.utils.exceptions.CharacterCardWithParametersException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.EndGameException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.LastMoveException;
import it.polimi.ingsw.triton.launcher.utils.message.MessageType;
import it.polimi.ingsw.triton.launcher.utils.message.clientmessage.visitors.ClientMessageModifierVisitor;

import java.util.NoSuchElementException;

/**
 * This message contains the parameters to apply the effect.
 */
public class CharacterCard01Reply extends CharacterCardReply{
    private final Color student;
    private final int islandID;
    public CharacterCard01Reply(String senderUsername, Color student, int islandID) {
        super(senderUsername);
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
