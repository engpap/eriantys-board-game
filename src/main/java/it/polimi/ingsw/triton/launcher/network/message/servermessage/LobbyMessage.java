package it.polimi.ingsw.triton.launcher.network.message.servermessage;

import it.polimi.ingsw.triton.launcher.model.Game;
import it.polimi.ingsw.triton.launcher.network.message.Message;
import it.polimi.ingsw.triton.launcher.network.message.MessageType;

import java.util.ArrayList;

public class LobbyMessage extends BroadcastServerMessage {
    private final ArrayList<String> onlineNicknames;
    private int maxNumberPlayers;
    public LobbyMessage(ArrayList<String> onlineNicknames, int maxNumberPlayers) {
        super(MessageType.LOBBY);
        this.onlineNicknames = onlineNicknames;
        this.maxNumberPlayers = maxNumberPlayers;
    }

    public ArrayList<String> getOnlineNicknames() {
        return onlineNicknames;
    }

    public int getMaxNumberPlayers() {
        return maxNumberPlayers;
    }
}
