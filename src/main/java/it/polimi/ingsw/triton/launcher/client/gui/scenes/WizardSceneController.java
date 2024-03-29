package it.polimi.ingsw.triton.launcher.client.gui.scenes;

import it.polimi.ingsw.triton.launcher.client.model.ClientModel;
import it.polimi.ingsw.triton.launcher.server.model.enums.Wizard;
import it.polimi.ingsw.triton.launcher.utils.message.clientmessage.WizardReply;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WizardSceneController extends SceneController {

    @FXML
    AnchorPane wizardPane;

    @FXML
    Button selectButton;

    @FXML
    Polygon leftSwitch;

    @FXML
    Polygon rightSwitch;

    @FXML
    ImageView wizardImageView;

    private Map<Image, Wizard> wizardsImages;
    private int shownWizard = 0;

    /**
     * The user can scroll through the wizard images
     * When user clicks on selectButton, the currently selected wizard is assigned to him
     * To do this, we have a map between Image and Wizard
     */
    public void select() {
        Wizard selectedWizard = wizardsImages.get(wizardImageView.getImage());
        notify(new WizardReply(selectedWizard));
        selectButton.setDisable(true);
    }

    /**
     * This method permits user to scroll through the wizard images
     * When wizard is last one, so you can't scroll, darts are different colorized
     */
    public void switchLeft() {
        if (shownWizard > 0) {
            wizardImageView.setImage((Image) wizardsImages.keySet().toArray()[shownWizard - 1]);
            shownWizard--;
            rightSwitch.setFill(Color.BLUE);
            rightSwitch.setOpacity(1);
        }
        if (shownWizard == 0) {
            leftSwitch.setFill(Color.GRAY);
            leftSwitch.setOpacity(0.5);
        }

    }

    /**
     * This method permits user to scroll through the wizard images
     * When wizard is last one, so you can't scroll, darts are different colorized
     */
    public void switchRight() {
        if (shownWizard < wizardsImages.size() - 1) {
            wizardImageView.setImage((Image) wizardsImages.keySet().toArray()[shownWizard + 1]);
            shownWizard++;
            leftSwitch.setFill(Color.BLUE);
            leftSwitch.setOpacity(1);
        }
        if (shownWizard == wizardsImages.size() - 1) {
            rightSwitch.setFill(Color.GRAY);
            rightSwitch.setOpacity(0.5);
        }

    }

    /**
     * In this method we create the map that associates Images and Wizards, and we prepare everything for user interaction
     *
     * @param clientModel the client-side model.
     * @param parameters  in this case contains the available wizards' arraylist, that is sent by the server
     * @param <T>         generic type parameter.
     */
    @Override
    public <T> void setupScene(ClientModel clientModel, T parameters) {
        ArrayList<?> wizards = (ArrayList<?>) parameters;
        wizardsImages = new HashMap<>();
        rightSwitch.setFill(Color.BLUE);
        for (Object wizard : wizards) {
            wizardsImages.put(new Image(Objects.requireNonNull(WizardSceneController.class.getResource("/Images/Wizards" + ((Wizard) wizard).getImagePath())).toString()), (Wizard) wizard);
        }
        wizardImageView.setImage((Image) wizardsImages.keySet().toArray()[0]);
        leftSwitch.setFill(Color.GRAY);
        leftSwitch.setOpacity(0.5);
        if (wizards.size() == 1) {
            rightSwitch.setFill(Color.GRAY);
            rightSwitch.setOpacity(0.5);
        }
    }

}
