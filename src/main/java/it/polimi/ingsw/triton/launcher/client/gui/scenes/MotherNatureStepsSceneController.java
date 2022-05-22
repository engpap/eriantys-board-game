package it.polimi.ingsw.triton.launcher.client.gui.scenes;

import it.polimi.ingsw.triton.launcher.client.model.ClientModel;
import it.polimi.ingsw.triton.launcher.utils.message.Message;
import it.polimi.ingsw.triton.launcher.utils.message.clientmessage.MotherNatureReply;
import it.polimi.ingsw.triton.launcher.utils.obs.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class MotherNatureStepsSceneController extends SceneController {

    @FXML
    AnchorPane motherNaturePane;

    @FXML
    ChoiceBox<Integer> stepsChoiceBox;

    @FXML
    Button moveButton;

    @FXML
    Button playCCButton;

    public Button getPlayCCButton() {
        return playCCButton;
    }

    public void move(ActionEvent event){
        notify(new MotherNatureReply(username, stepsChoiceBox.getValue()));
        ((Stage) motherNaturePane.getScene().getWindow()).close();
    }

    @Override
    public <T> void setupScene(ClientModel clientModel, T parameters) {
        ArrayList<Integer> steps = new ArrayList<>();
        for (int i = 0; i <= clientModel.getLastAssistantCardPlayedPerUsername().get(clientModel.getUsername()).getType().getMaxSteps() + (int)parameters; i++) {
            steps.add(i);
        }
        stepsChoiceBox.getItems().addAll(steps);
        stepsChoiceBox.setOnAction(this::activeButton);
    }

    private void activeButton(ActionEvent event){
        moveButton.setDisable(false);
    }

    @Override
    public AnchorPane getAnchorPane() {
        return motherNaturePane;
    }

    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }
}