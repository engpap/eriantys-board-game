package it.polimi.ingsw.triton.launcher.utils.message.servermessage;

import it.polimi.ingsw.triton.launcher.client.ServerMessageVisitor;
import it.polimi.ingsw.triton.launcher.utils.message.Message;

public abstract class ServerMessage implements Message {

    public void accept(BroadcastMessageVisitor messageVisitor){
        messageVisitor.visit(this);
    }
    public abstract void accept(ServerMessageVisitor messageVisitor);
}
