package it.polimi.ingsw.triton.launcher.client.gui;

import it.polimi.ingsw.triton.launcher.client.Client;
import it.polimi.ingsw.triton.launcher.client.gui.scenes.*;
import it.polimi.ingsw.triton.launcher.client.model.ClientModel;
import it.polimi.ingsw.triton.launcher.client.view.ClientView;
import it.polimi.ingsw.triton.launcher.server.model.AssistantCard;
import it.polimi.ingsw.triton.launcher.server.model.enums.GameState;
import it.polimi.ingsw.triton.launcher.server.model.enums.Wizard;
import it.polimi.ingsw.triton.launcher.server.model.player.SchoolBoard;
import it.polimi.ingsw.triton.launcher.utils.message.ErrorTypeID;
import it.polimi.ingsw.triton.launcher.utils.message.Message;
import it.polimi.ingsw.triton.launcher.utils.obs.Observable;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class Gui extends Observable<Message> implements ClientView {
    private ClientModel clientModel;
    private Client client;
    private Stage mainStage;
    private final Stage activeStage;
    private GameState actualGamePhase = GameState.SETUP;
    private MainSceneController mainController;
    private FXMLLoader activeLoader;
    private Method lastCalledMethod;


    public Gui(Stage activeStage) {
        this.activeStage = activeStage;
    }

    @Override
    public ClientModel getClientModel() {
        return clientModel;
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        //showAlert(Alert.AlertType.INFORMATION, "Information Message", genericMessage);
    }

    @Override
    public void showLobbyMessage(List<String> onlineNicknames) {
        String usernames = "";
        for (String name : onlineNicknames) {
            usernames += name + "\n";
        }
        usernames = "A new player has been connected.\nNow players online are:\n" + usernames;
        showAlert(Alert.AlertType.INFORMATION, "New player online", usernames);
    }

    @Override
    public void showGameInfo(int characterCardId) {
        switch (characterCardId){
            case 1:
                Platform.runLater(() -> {
                    mainController.showCCModifies01(clientModel);
                });
                break;
            case 7:
                Platform.runLater(() -> {
                    mainController.showCCModifies07(clientModel);
                });
                //mainController.showCCModifies07(clientModel);
                break;
            case 10:
                Platform.runLater(() -> {
                    mainController.showCCModifies10(clientModel);
                });
                //mainController.showCCModifies10(clientModel);
                break;
            case 11:
                Platform.runLater(() -> {
                    mainController.showCCModifies11(clientModel);
                });
                //mainController.showCCModifies11(clientModel);
                break;
            case 12:
                Platform.runLater(() -> {
                    mainController.showCCModifies12(clientModel);
                });
                //mainController.showCCModifies12(clientModel);
                break;
        }
    }

    @Override
    public void showGameInfo() {

    }

    @Override
    public void showChangePhase(GameState gameState) {
        if (gameState == (GameState.PLANNING_PHASE) && actualGamePhase == (GameState.SETUP)) {
            initializeMainStage();
        }
        actualGamePhase = gameState;
        //showAlert(Alert.AlertType.INFORMATION, "Game phase info", "New game phase:\n" + gameState + " is beginning..");

    }

    @Override
    public void showDisconnectionMessage() {
        showAlert(Alert.AlertType.ERROR, "Disconnection", "A player has been disconnected. The game is over!");
        closeGui();
    }


    @Override
    public void showEmptyBagMessage() {
        showAlert(Alert.AlertType.WARNING, "Empty bag", "The bag in empty! Game will finish at the end of this turn");
    }

    @Override
    public void showLoginReply() {
        //showAlert(Alert.AlertType.INFORMATION, "Login Reply", "Username Accepted. Your username will be \"" + clientModel.getUsername() + "\"");
    }

    @Override
    public void showErrorMessage(ErrorTypeID fullLobby) {
        //showAlert(Alert.AlertType.WARNING, "Incorrect input", fullLobby.getDescription());
    }


    @Override
    public void showTieMessage() {
        showAlert(Alert.AlertType.INFORMATION, "Tie", "Nobody won the game: it was a tie");
        askPlayAgain();
    }


    @Override
    public void showAbortMessage() {
        showAlert(Alert.AlertType.ERROR, "Disconnection", "Server error! You will be disconnected!");
        closeGui();
    }

    @Override
    public void showWinMessage() {
        showAlert(Alert.AlertType.INFORMATION, "You won", "Congratulations!! You won the game");
        askPlayAgain();
    }

    @Override
    public void showLoseMessage(String winnerUsername) {
        showAlert(Alert.AlertType.INFORMATION, "You lost", "You lost!\n" + winnerUsername + " won!");
        askPlayAgain();
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(contentText);
            alert.showAndWait();
        });
    }

    private void closeGui() {
        Platform.runLater(() -> {
            activeStage.close();
            mainStage.close();
        });
    }

    public void startGui() {
        client = new Client(this);
        this.addObserver(client);
        this.clientModel = new ClientModel();
        askIpAddress();
    }

    private <T> void prepareController(String path, T parameters) {
        Platform.runLater(() -> {
            activeLoader = new FXMLLoader(getClass().getResource(path));
            try {
                Parent root = activeLoader.load();
                SceneController controller = activeLoader.getController();
                controller.addObserver(client);
                controller.setUsername(clientModel.getUsername());
                controller.setupScene(clientModel, parameters);
                setGenericBackground(controller, "src/main/resources/Images/blueCloud.jpg");
                if(controller instanceof CharacterCardSceneController){
                    backButton(((CharacterCardSceneController)controller).getBackButton());
                }
                activeStage.setScene(new Scene(root));
                activeStage.setResizable(false);
                activeStage.setTitle(clientModel.getUsername());
                //setGenericButton(controller, controller.getPath());
                activeStage.show();
                activeStage.setOnCloseRequest(event -> {
                    event.consume();
                    logout(activeStage);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void askIpAddress() {
        prepareController("/ipAddress-scene.fxml", null);
    }

    @Override
    public void askUsername() {
        prepareController("/login-scene.fxml", null);
    }

    @Override
    public void askTowerColor(boolean[] towerColorChosen) {
        prepareController("/towerColor-scene.fxml", towerColorChosen);
    }

    @Override
    public void askNumPlayersAndGameMode() {
        prepareController("/gameModeAndNumOfPlayers-scene.fxml", null);
    }


    @Override
    public void askWizard(List<Wizard> wizards) {
        prepareController("/wizard-scene.fxml", wizards);
    }

    @Override
    public void askAssistantCard() {
        prepareController("/assistantCard-scene.fxml", null);
    }

    @Override
    public void askCloudTile() {
        if(clientModel.getSchoolBoards().size() == 2){
            prepareController("/chooseCloudTile2player-scene.fxml", null);
        }
        else{
            prepareController("/chooseCloudTile-scene.fxml", null);
        }
        Platform.runLater(() -> {
            if (clientModel.isExpertMode()) {
                ChooseCloudTileSceneController controller = activeLoader.getController();
                controller.getPlayCCButton().setVisible(true);
                playCharacterCard(controller.getPlayCCButton());
            }
        });
        try {
            lastCalledMethod = getClass().getDeclaredMethod("askCloudTile");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askMoveStudentFromEntrance() {
        prepareController("/moveStudentFromEntrance-scene.fxml", null);
        Platform.runLater(() -> {
            if (clientModel.isExpertMode()) {
                MoveStudentFromEntranceSceneController controller = activeLoader.getController();
                controller.getPlayCCButton().setVisible(true);
                playCharacterCard(controller.getPlayCCButton());
            }
        });
        try {
            lastCalledMethod = getClass().getDeclaredMethod("askMoveStudentFromEntrance");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askNumberStepsMotherNature() {
        int additionalSteps = 0;
        if (clientModel.getLastCharacterCardPlayed() != null && clientModel.getLastCharacterCardPlayed().getId() == 4)
            additionalSteps = 2;
        prepareController("/motherNatureSteps-scene.fxml", additionalSteps);
        Platform.runLater(() -> {
            if (clientModel.isExpertMode()) {
                MotherNatureStepsSceneController controller = activeLoader.getController();
                controller.getPlayCCButton().setVisible(true);
                playCharacterCard(controller.getPlayCCButton());
            }
        });
        try {
            lastCalledMethod = getClass().getDeclaredMethod("askNumberStepsMotherNature");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void askCharacterCardParameters(int id) {
        switch (id) {
            case 1:
                prepareController("/charCard01-scene.fxml", 1);
                break;
            case 3:
                prepareController("/charCard03-05-scene.fxml", 3);
                break;
            case 5:
                prepareController("/charCard03-05-scene.fxml", 5);
                break;
            case 7:
                prepareController("/charCard07-scene.fxml", 7);
                break;
            case 9:
                prepareController("/charCard09-11-12-scene.fxml", 9);
                break;
            case 10:
                prepareController("/charCard10-scene.fxml", 10);
                break;
            case 11:
                prepareController("/charCard09-11-12-scene.fxml", 11);
                break;
            case 12:
                prepareController("/charCard09-11-12-scene.fxml", 12);
                break;
        }
    }


    @Override
    public void showMyInfoAssistantCardPlayed (AssistantCard assistantCard){
        Platform.runLater(() -> {
            mainController.showMyInfoAssistantCardPlayed(assistantCard, clientModel);
        });
    }

    @Override
    public void showInfoAssistantCardPlayed (String username, AssistantCard assistantCard){
        Platform.runLater(() -> {
            mainController.showInfoAssistantCardPlayed(username, assistantCard);
        });
    }

    @Override
    public void showInfoStudentIntoDiningRoom (String username, String moveDescription){
        Platform.runLater(() -> {
            mainController.showInfoStudentIntoDiningRoom(username, clientModel);
        });
    }

    @Override
    public void showInfoStudentOntoIsland (String username, String moveDescription){
        Platform.runLater(() -> {
            mainController.showInfoStudentOntoIsland(username, clientModel);
        });
    }

    @Override
    public void showMotherNaturePosition ( int islandId){
        Platform.runLater(() -> {
            mainController.showMotherNaturePosition(clientModel);
        });
    }

    @Override
    public void showChangeInfluenceMessage (String username,int islandId){
        Platform.runLater(() -> {
            mainController.showChangeInfluenceMessage(clientModel);
        });
    }

    @Override
    public void showMergeIslandsMessage ( int island1Id, int island2Id){
        Platform.runLater(() -> {
            mainController.showMergeIslandsMessage(clientModel);
        });
    }

    @Override
    public void showMoveTowerOntoIsland ( int islandId){
        Platform.runLater(() -> {
            mainController.showMoveTowerOntoIsland(clientModel);
        });
    }

    @Override
    public void showMoveTowerOntoSchoolBoard (String username, SchoolBoard schoolBoard){
        Platform.runLater(() -> {
            mainController.showMoveTowerOntoSchoolBoard(clientModel);
        });
    }

    @Override
    public void showInfoChosenCloudTile(String username, String choiceDescription){
        Platform.runLater(() -> {
            mainController.showInfoChosenCloudTile(username, clientModel);
        });
    }

    @Override
    public void showUpdateWallet() {
        Platform.runLater(() -> {
            if(mainController != null)
                mainController.showUpdatedWallet(clientModel);
        });
    }

        private void initializeMainStage() {
            Platform.runLater(() -> {
                FXMLLoader mainLoader;
                if (clientModel.getSchoolBoards().size() == 2)
                    mainLoader = new FXMLLoader(getClass().getResource("/main-scene.fxml"));
                else mainLoader = new FXMLLoader(getClass().getResource("/main3players-scene.fxml"));
                try {
                    Parent root = mainLoader.load();
                    Scene scene = new Scene(root);
                    mainStage = new Stage();
                    mainStage.setTitle(clientModel.getUsername());
                    mainStage.setResizable(false);
                    mainController = mainLoader.getController();
                    mainController.initializeMainScene(clientModel);
                    setGenericBackground(mainController, "src/main/resources/Images/backgroundmainscene.jpg");
                    mainStage.setScene(scene);
                    mainStage.show();
                    activeStage.close();
                    mainStage.setOnCloseRequest(event -> {
                        event.consume();
                        logout(mainStage);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    private void playCharacterCard(Button button){
        button.setOnAction(event -> {
            prepareController("/characterCard-scene.fxml", null);
        });
    }

    private void backButton(Button button){
        button.setOnAction(event -> {
            try {
                lastCalledMethod.invoke(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void askPlayAgain() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to play again?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                mainStage.close();
                askUsername();
            } else {
                closeGui();
            }
        });
    }

        private void setGenericBackground(SceneController controller, String path){
            String currentPath = new java.io.File(path).getAbsolutePath().replace('\\','/');
            Image img = new Image("file:" + currentPath);
            BackgroundImage bImg = new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(500,500,true,true,false,true));
            Background bGround = new Background(bImg);
            controller.getAnchorPane().setBackground(bGround);
        }

        private void setGenericButton(SceneController controller, String path){
            String currentPath = new java.io.File(path).getAbsolutePath().replace('\\','/');
            Image img = new Image("file:" + currentPath);
            BackgroundImage bImg = new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(500,500,true,true,false,true));
            Background bGround = new Background(bImg);
            try {
                controller.getButton().setBackground(bGround);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    public void logout(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure?");
        if (alert.showAndWait().get() == ButtonType.OK){
            System.exit(1);
            stage.close();
        }
    }

}
