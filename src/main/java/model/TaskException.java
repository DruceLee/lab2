package model;

/**
 * Class to generate exceptions
 * @author Andrey Sherstyuk
 */
public class TaskException extends Exception {

    /**
     * Constructor with parameter
     * @param message - massage
     */
    public TaskException(String message) {
        super(message);
    }

    public TaskException() {
        super();
    }
}
