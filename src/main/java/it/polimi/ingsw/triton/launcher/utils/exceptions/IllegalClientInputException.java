package it.polimi.ingsw.triton.launcher.utils.exceptions;

import it.polimi.ingsw.triton.launcher.utils.message.ErrorTypeID;

/**
 * This exception is thrown when the player input is uncorrected.
 */
public class IllegalClientInputException extends Exception {
    private final ErrorTypeID typeError;

    public IllegalClientInputException() {
        super(ErrorTypeID.GENERIC_CLIENT_INPUT_ERROR.getDescription());
        typeError = ErrorTypeID.GENERIC_CLIENT_INPUT_ERROR;
    }

    public IllegalClientInputException(ErrorTypeID typeError) {
        super(typeError.getDescription());
        this.typeError = typeError;
    }

    public ErrorTypeID getTypeError() {
        return typeError;
    }
}

