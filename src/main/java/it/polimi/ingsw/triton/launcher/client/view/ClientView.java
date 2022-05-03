package it.polimi.ingsw.triton.launcher.client.view;

import it.polimi.ingsw.triton.launcher.server.model.CloudTile;
import it.polimi.ingsw.triton.launcher.server.model.Island;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CharacterCard;
import it.polimi.ingsw.triton.launcher.server.model.player.SchoolBoard;
import it.polimi.ingsw.triton.launcher.utils.View;

import java.util.ArrayList;
import java.util.Map;

public interface ClientView extends View {
    // Ask methods
    void askUsername();

    // Show methods
    void showGenericMessage(String genericMessage);
    void showLobbyMessage(ArrayList<String> onlineNicknames, int maxNumberPlayers );
    void showGameInfo(ArrayList<CharacterCard> availableCharacterCards, ArrayList<Island> islands, Map<String, SchoolBoard> schoolBoards, ArrayList<CloudTile> cloudTiles);
}
