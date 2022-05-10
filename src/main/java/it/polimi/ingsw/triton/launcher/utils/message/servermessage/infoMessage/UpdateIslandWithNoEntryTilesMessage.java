package it.polimi.ingsw.triton.launcher.utils.message.servermessage.infoMessage;

import it.polimi.ingsw.triton.launcher.client.ServerMessageVisitor;
import it.polimi.ingsw.triton.launcher.server.model.Island;
import it.polimi.ingsw.triton.launcher.utils.message.servermessage.InfoMessage;

public class UpdateIslandWithNoEntryTilesMessage extends InfoMessage {
    private final Island islandToUpdate;

    public UpdateIslandWithNoEntryTilesMessage(Island islandToUpdate){
        this.islandToUpdate = islandToUpdate;
    }

    @Override
    public void accept(ServerMessageVisitor messageVisitor) {
        messageVisitor.visit(this);
    }

    public Island getIslandToUpdate() {
        return islandToUpdate;
    }
}