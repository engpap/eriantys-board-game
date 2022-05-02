package it.polimi.ingsw.triton.launcher.utils.message.clientmessage;

import it.polimi.ingsw.triton.launcher.server.model.Game;
import it.polimi.ingsw.triton.launcher.server.view.VirtualView;
import it.polimi.ingsw.triton.launcher.utils.exceptions.EndGameException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.LastMoveException;
import it.polimi.ingsw.triton.launcher.utils.message.MessageType;

import java.util.NoSuchElementException;

public class CloudTileReply extends ClientMessage {
    private final int selectedCloudTileID;

    public CloudTileReply(String username, int selectedCloudTileID) {
        super(MessageType.CLOUD_TILE_REPLY, username);
        this.selectedCloudTileID = selectedCloudTileID;
    }

    public int getSelectedCloudTileID() {
        return selectedCloudTileID;
    }

    @Override
    public void modifyModel(Game game) throws IllegalClientInputException, NoSuchElementException, EndGameException {
        game.chooseCloudTile(game.getCloudTileById(selectedCloudTileID));
        game.nextGameTurn();
    }

    @Override
    public void createStandardNextMessage(Game game, VirtualView virtualView) {
        virtualView.askMoveStudentFromEntrance();
    }

    @Override
    public void createExceptionalNextMessage(Game game, VirtualView virtualView) {
        virtualView.askAssistantCard();
    }

    @Override
    public void createInputErrorMessage(Game game, VirtualView virtualView) {
        virtualView.askCloudTile();
    }
}
