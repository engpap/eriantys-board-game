package it.polimi.ingsw.triton.launcher.utils.message;

/**
 * Contains some types of errors that can occurred during the execution of the game.
 */
public enum ErrorTypeID {
    USERNAME_ALREADY_CHOSEN("This username is already used!"),
    FORBIDDEN_USERNAME("Username is not correct!"),
    WRONG_PLAYERS_NUMBER("The number of players must be 2 or 3!"),
    FULL_LOBBY("Lobby is full!"),
    TOWER_COLOR_ALREADY_CHOSEN("This tower color is already chosen!"),
    WIZARD_ALREADY_CHOSEN("This wizard is already chosen!"),
    ASSISTANTCARD_ALREADY_CHOSEN("This assistant card is already used!"),
    INCORRECT_MOTHERNATURE_STEPS("The number of steps is incorrect!"),
    CLOUD_TILE_ALREADY_CHOSEN("The selected cloud tile is already selected!"),
    NOT_ENOUGH_COINS("You don't have enough coins to buy this character card!"),
    CHARACTER_CARD_NOT_AVAILABLE("The character card selected is not available!"),
    NO_STUDENT_WITH_COLOR_ENTRANCE("There aren't students with that color in your entrance!"),
    DINING_ROOM_OF_COLOR_IS_FULL("Your dining room corresponding of the color you choose is already full!"),
    NO_ISLAND_WITH_THAT_ID("There aren't existing islands with that id!"),
    GENERIC_CLIENT_INPUT_ERROR("Client has entered a wrong input!"),
    CHARACTER_CARD_ALREADY_PLAYED("Cannot use a Character Card ability more than once per turn!"),
    NULL_VALUE("Entered a null value"),
    COMMAND_CANCELLED("Command successfully canceled!"),
    ILLEGAL_MOVE_FOR_MODE("This move cannot be done in this game mode!"),
    ILLEGAL_MOVE("This move cannot be done"),
    ILLEGAL_MOVE_FOR_PHASE("This move cannot be done in this phase!"),
    COMPROMISED("Game rules or code has been compromised!");

    private final String description;

    ErrorTypeID(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
