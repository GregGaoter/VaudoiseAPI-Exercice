package ch.vaudoise.vaudoiseapi.exercice.web.rest.errors;

public class InvalidContractException extends RuntimeException {

    public InvalidContractException(String message) {
        super(message);
    }
}
