package it.polimi.ingsw.triton.launcher.client.cli;

import it.polimi.ingsw.triton.launcher.client.model.ClientModel;
import it.polimi.ingsw.triton.launcher.server.model.AssistantCard;
import it.polimi.ingsw.triton.launcher.server.model.CloudTile;
import it.polimi.ingsw.triton.launcher.server.model.Island;
import it.polimi.ingsw.triton.launcher.server.model.cardeffects.CharacterCard;
import it.polimi.ingsw.triton.launcher.server.model.enums.*;
import it.polimi.ingsw.triton.launcher.server.model.player.AssistantDeck;
import it.polimi.ingsw.triton.launcher.server.model.player.SchoolBoard;
import it.polimi.ingsw.triton.launcher.utils.message.ErrorTypeID;
import it.polimi.ingsw.triton.launcher.utils.message.clientmessage.*;
import it.polimi.ingsw.triton.launcher.utils.obs.Observable;
import it.polimi.ingsw.triton.launcher.client.Client;
import it.polimi.ingsw.triton.launcher.utils.message.Message;
import it.polimi.ingsw.triton.launcher.client.view.ClientView;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class Cli extends Observable<Message> implements ClientView{
    private final PrintStream out;
    private ClientModel clientModel;
    private static final String TRY_AGAIN = "Try again...";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BOLDGREEN = "\u001B[1;32m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BOLDYELLOW = "\u001B[1;33m";
    public static final String ANSI_PINK = "\u001B[35m";
    private static final String commandForCharacterCard="--playCC";


    /**
     * Instantiates a new Cli;
     * The PrintStream out variable is set to System.out, by this way System.out.println() is not replicated multiple times.
     */
    public Cli() {
        out = System.out;
    }

    /**
     * Print the logo and welcome the player.
     */
    public void start() {
        out.print("\n" +
                "  _____   ____    ___      _      _   _   _____  __   __  ____  \n" +
                " | ____| |  _ \\  |_ _|    / \\    | \\ | | |_   _| \\ \\ / / / ___| \n" +
                " |  _|   | |_) |  | |    / _ \\   |  \\| |   | |    \\ V /  \\___ \\ \n" +
                " | |___  |  _ <   | |   / ___ \\  | |\\  |   | |     | |    ___) |\n" +
                " |_____| |_| \\_\\ |___| /_/   \\_\\ |_| \\_|   |_|     |_|   |____/ \n");
        out.println("Welcome to Eriantys!");
        init();
    }

    /**
     * Creates the Client to communicate with the Server, then sets the latter as an Observer of the Cli.
     * Eventually, it asks the username to the player.
     */
    public void init(){
        this.addObserver(new Client(this));
        this.clientModel=new ClientModel();
        askUsername();
    }

    @Override
    public void askUsername() {
        out.print(ANSI_BOLDGREEN + "Enter your username: " + ANSI_RESET);
        try {
            String username = readLine();
            notify(new LoginRequest(username));
        } catch (ExecutionException e) {
            out.println(TRY_AGAIN);
        }
    }

    @Override
    public void showLoginReply() {
        // to implement when we redo lobby and decouple info message between server and game
    }

    public void askGameMode() {
        String input="";
        do{
            try {
                out.print("You are the first player\n" + ANSI_BOLDGREEN + "Please, select a game mode [N for normal mode, E for expert mode]: " + ANSI_RESET);
                input = readLine();
            } catch (ExecutionException e) {
                out.println("You should type N or E. Try again...");
            }

        }while(!(input.equalsIgnoreCase("E") || input.equalsIgnoreCase("N")));
        // if input is E, expertMode is true
        boolean expertMode= input.equalsIgnoreCase("E");
        notify(new GameModeReply(clientModel.getUsername(), expertMode));
    }



    @Override
    public void askNumOfPlayers() {
        try {
                out.print(ANSI_BOLDGREEN + "Enter number of players [2 or 3]: " + ANSI_RESET);
                String input = readLine();
                int numOfPlayers = Integer.parseInt(input);
                notify(new PlayersNumberReply(clientModel.getUsername(), numOfPlayers));
        } catch (ExecutionException e) {
            out.println(TRY_AGAIN);
        }catch(NumberFormatException e){
            out.println(TRY_AGAIN);
            askNumOfPlayers();
        }
    }

    public void showLobbyMessage(ArrayList<String> onlineNicknames, int maxNumberPlayers ) {
        out.println("ONLINE PLAYERS:");
        for(String username: onlineNicknames){
            System.out.println("- " + username);
        }
        out.println("There are " + onlineNicknames.size() +
                " out of " + maxNumberPlayers + " players connected; Waiting for " + (maxNumberPlayers-onlineNicknames.size()) + " players...");
    }


    @Override
    public void askTowerColor(boolean[] chosenTowerColors) {
        try {
            out.print(ANSI_BOLDGREEN + "Choose a tower color [ ");
            for(int i=0;i< chosenTowerColors.length;i++){
                if(!chosenTowerColors[i]) {
                    out.print(i + " for " + TowerColor.values()[i]);
                    if(i<chosenTowerColors.length-1)
                        out.print(", ");
                }
            }
            out.print(" ]: " + ANSI_RESET);
            String input = readLine();
            notify(new TowerColorReply(clientModel.getUsername(), TowerColor.values()[Integer.parseInt(input)]));
        } catch (ExecutionException | NullPointerException | NumberFormatException e) {
            out.println(TRY_AGAIN);
            askTowerColor(chosenTowerColors);
        }
    }


    @Override
    public void askWizard(ArrayList<Wizard> wizards) {
        try {
            out.print(ANSI_BOLDGREEN + "Choose a Wizard [ ");
            for(Wizard wizard: wizards){
                out.print(wizard+" ");
            }
            out.print("]: " + ANSI_RESET);
            String input = readLine();
            notify(new WizardReply(clientModel.getUsername(), Wizard.valueOf(input.toUpperCase())));
        } catch (ExecutionException | NullPointerException e) {
            out.println(TRY_AGAIN);
            askWizard(wizards);
        }
    }


    @Override
    public void showGameInfo(ArrayList<CharacterCard> availableCharacterCards, ArrayList<Island> islands, Map<String, SchoolBoard> schoolBoards, ArrayList<CloudTile> cloudTiles, Island motherNaturePosition) {
        clientModel.setAvailableCharacterCards(availableCharacterCards);
        clientModel.setIslands(islands);
        clientModel.setSchoolBoards(schoolBoards);
        clientModel.setCloudTiles(cloudTiles);
        clientModel.setMotherNaturePosition(motherNaturePosition);
        out.println(clientModel.toString());
    }

    public void showChangePhase(GameState gameState){
        out.println(ANSI_BOLDYELLOW+" ---"+gameState.name()+"---"+ANSI_RESET);
    }


    @Override
    public void askAssistantCard() {
        AssistantDeck assistantDeck= clientModel.getAssistantDeck();
        try {
            out.print(ANSI_BOLDGREEN + "Draw an Assistant Card\n[ " + ANSI_RESET + ANSI_GREEN);
            for (AssistantCard assistantCard : assistantDeck.getAssistantDeck()) {
                out.print(assistantCard.toString());
            }
            out.print(ANSI_BOLDGREEN + " ]: " + ANSI_RESET);
            String input = readLine();
            AssistantCard assistantCardReply=null;
            for (AssistantCard assistantCard : assistantDeck.getAssistantDeck()){
                if ((assistantCard.getType()).equals(AssistantCardType.valueOf(input.toUpperCase())))
                    assistantCardReply = assistantCard;
            }
            notify(new AssistantCardReply(clientModel.getUsername(), assistantCardReply));
        } catch (ExecutionException | NullPointerException e) {
            out.println(TRY_AGAIN);
            askAssistantCard();
        }
    }

    @Override
    public void showInfoAssistantCardPlayed(String username, AssistantCard assistantCard) {
        out.println("Player: "+username+ " has played " + assistantCard.toString());
    }

    @Override
    public void askMoveStudentFromEntrance() {
        try {
            out.println(ANSI_GREEN + "Choose three students to move from entrance to dining room or an island");
            out.print("Islands:\n");
            out.println(clientModel.getIslands());
            out.print("\n");
            SchoolBoard schoolBoard= clientModel.getSchoolBoards().get(clientModel.getUsername());
            out.print("Your SchoolBoard:");
            out.println(schoolBoard.toString());
            out.println("To do so, type on each line [color of student, d (for dining room) ] or [color of student, island id]");
            out.println(ANSI_BOLDGREEN + "Please, enter data: " + ANSI_RESET);
            String input = readLine();
            if(input.equals(commandForCharacterCard))
                showAndPlayCharacterCard();
            else {
                String[] splittedInput = input.split(",");
                Color color = Color.valueOf(splittedInput[0].toUpperCase());
                if (removeSpaces(splittedInput[1]).equals("d"))
                    notify(new MoveStudentOntoDiningRoomMessage(clientModel.getUsername(), color));
                else
                    notify(new MoveStudentOntoIslandMessage(clientModel.getUsername(), Integer.parseInt(removeSpaces(splittedInput[1])), color));
            }
        } catch (ExecutionException | NullPointerException | IllegalArgumentException e) {
            out.println(TRY_AGAIN);
            askMoveStudentFromEntrance();
        }
    }


    @Override
    public void askNumberStepsMotherNature() {
        try {
            out.println("Mother nature is on the island: " + clientModel.getMotherNaturePosition().getId());
            out.print(ANSI_BOLDGREEN + "Insert the number of steps that mother nature has to do: " + ANSI_RESET);
            String input = readLine();
            if(input.equals(commandForCharacterCard))
                showAndPlayCharacterCard();
            else
                notify(new MotherNatureReply(clientModel.getUsername(), Integer.parseInt(input)));
        } catch (ExecutionException | NumberFormatException | NullPointerException e) {
            out.println(TRY_AGAIN);
            askNumberStepsMotherNature();
        }
    }


    @Override
    public void askCloudTile() {
        try {
            out.println(ANSI_GREEN + "Choose a cloud tile to withdraw the students");
            out.print("CloudTiles:");
            out.println(clientModel.printCloudTiles());
            out.println(ANSI_BOLDGREEN + "Select the id of the cloud tile you choose:" + ANSI_RESET);
            String input = readLine();
            if(input.equals(commandForCharacterCard))
                showAndPlayCharacterCard();
            else
                notify(new CloudTileReply(clientModel.getUsername(), Integer.parseInt(input)));
        } catch (ExecutionException | NumberFormatException | NullPointerException e) {
            out.println(TRY_AGAIN);
            askCloudTile();
        }
    }

    @Override
    public void askCharacterCardParameters(int id) {

    }

    @Override
    public void showErrorMessage(ErrorTypeID errorTypeID) {
        out.println(errorTypeID.getDescription());
        if(errorTypeID == ErrorTypeID.FULL_LOBBY)
            System.exit(1);
    }

    @Override
    public void showDisconnectionMessage() {
        out.println("A player has disconnected! The game is finished.");
        System.exit(1);
    }

    @Override
    public void showEmptyBagMessage() {
        out.println("The bag is empty! The game will finish at the end of last player's turn");
        out.println("Every player will not draw any students from the cloud tiles");
    }

    @Override
    public void showMoveTowerOntoIsland(int islandId) {
        out.println("A tower has been moved onto island "+islandId);
        clientModel.printIslands();
    }

    @Override
    public void showMoveTowerOntoSchoolBoard(String username,SchoolBoard schoolBoard) {
        out.println("A tower has been moved back onto "+username+"'s school board");
        out.println(clientModel.getSchoolBoards().get(username).toString());
    }




    @Override
    public void showTieMessage() {
        out.println("You have tied");
    }



    @Override
    public void showWinMessage() {
        out.println("Congratulations! You're the winner!");
    }

    @Override
    public void showLoseMessage(String winnerUsername) {
        out.println("You Lose! The winner is: " + winnerUsername);
    }




    @Override
    public void showYourTurnMessage() {

    }

    @Override
    public void showAvailableCharacterCard() {

    }



    @Override
    public void showGenericMessage(String genericMessage) {
        out.println(genericMessage);
    }

    private void showAndPlayCharacterCard(){
        try {
            out.println(clientModel.getAvailableCharacterCards().toString());
            out.print("Please, choose a character card id to play: ");
            String input=readLine();
            notify(new UseCharacterCardRequest(clientModel.getUsername(),Integer.parseInt(removeSpaces(input))));
        } catch (ExecutionException | NumberFormatException e) {
            out.println(TRY_AGAIN);
            showAndPlayCharacterCard();
        }
    }

    private String removeSpaces(String string){
        return string.replace(" ", "");
    }

    /**
     * Read a string line using a separated thread
     *
     * @return the string
     * @throws ExecutionException   the execution exception
     * @throws NullPointerException the null pointer exception
     */
    public String readLine() throws ExecutionException, NullPointerException {
        FutureTask<String> futureTask = new FutureTask<>(new InputReadTask());
        new Thread(futureTask).start();
        try {
            return futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        throw new NullPointerException("The method had read a null string");
    }


    public ClientModel getClientModel(){
        return clientModel;
    }

}


