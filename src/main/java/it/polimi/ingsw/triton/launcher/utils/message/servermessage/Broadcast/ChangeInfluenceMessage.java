package it.polimi.ingsw.triton.launcher.utils.message.servermessage.Broadcast;

import it.polimi.ingsw.triton.launcher.client.ClientVisitor;
import it.polimi.ingsw.triton.launcher.server.model.Island;
import it.polimi.ingsw.triton.launcher.utils.message.MessageType;

/**
 * This message communicates to the players that an island has a new dominator.
 */
public class ChangeInfluenceMessage extends BroadcastServerMessage{
    private Island islandWithNewInfluence;
    private String usernameDominator;

    public ChangeInfluenceMessage(Island islandWithNewInfluence, String usernameDominator) {
        this.islandWithNewInfluence = islandWithNewInfluence;
        this.usernameDominator = usernameDominator;
    }

    public Island getIslandWithNewInfluence() {
        return islandWithNewInfluence;
    }

    public String getUsernameDominator() {
        return usernameDominator;
    }

    @Override
    public void accept(ClientVisitor messageVisitor) {

    }
}
