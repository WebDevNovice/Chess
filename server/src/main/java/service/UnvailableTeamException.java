package service;

/**
 * Indicates there was an error connecting to the database
 */
public class UnvailableTeamException extends Exception{
    public UnvailableTeamException(String message) {
        super(message);
    }
}
