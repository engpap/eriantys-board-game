package it.polimi.ingsw.triton.launcher.network.message.clientmessage;

import it.polimi.ingsw.triton.launcher.network.message.Message;
import it.polimi.ingsw.triton.launcher.network.message.MessageType;

public class ClientMessage extends Message{
    private final String senderUsername;

    public ClientMessage(MessageType messageType, String senderUsername) {
        super(messageType);
        this.senderUsername = senderUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }
}
