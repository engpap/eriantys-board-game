package it.polimi.ingsw.triton.launcher.utils.message.clientmessage;

import it.polimi.ingsw.triton.launcher.server.model.Island;
import it.polimi.ingsw.triton.launcher.utils.message.MessageType;

/**
 * This message contains the parameter to apply the effect.
 */
public class CharacterCard05Reply extends ClientMessage{
    private final Island island;
    public CharacterCard05Reply(String senderUsername, Island island) {
        super(MessageType.CHARACTER_CARD_05_PARAMETER, senderUsername);
        this.island = island;
    }

    public Island getIsland() {
        return island;
    }
}