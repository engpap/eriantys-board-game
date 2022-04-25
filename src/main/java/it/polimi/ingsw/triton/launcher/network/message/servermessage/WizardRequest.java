package it.polimi.ingsw.triton.launcher.network.message.servermessage;

import it.polimi.ingsw.triton.launcher.model.enums.Wizard;
import it.polimi.ingsw.triton.launcher.network.message.MessageType;

import java.util.ArrayList;

public class WizardRequest extends ServerMessage {
    private final ArrayList<Wizard> availableWizards;
    private String receiverUsername;
    public WizardRequest(ArrayList<Wizard> availableWizards, String receiverUsername) {
        super(MessageType.WIZARD_REQUEST, receiverUsername);
        this.availableWizards = availableWizards;
    }

    public ArrayList<Wizard> getAvailableWizards() {
        return availableWizards;
    }
}
