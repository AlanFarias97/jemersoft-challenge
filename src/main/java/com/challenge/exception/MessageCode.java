package com.challenge.exception;

public enum MessageCode implements GeneralMessageCode{


    POKEMON_NOT_FOUND("Error, Pokemon not found"),
    ERROR_AT_PROCESS_JSON("Error at process json"),

    ERROR_CALLING_POKE_API("Error invoking pokeapi"),
    ERROR_AT_LOADING_DATA("Error at loading data"),
    MOVEMENT_NOT_FOUND("Error, Movement not found"),
    ;

    private final String message;


    MessageCode(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }


    public String getExceptionCode() {
        return this.name();
    }
}
