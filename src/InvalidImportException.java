/**
 * Error can occur while importing a job application
 * Error used when a row is missing fields or contains invalid data
 */
public class InvalidImportException extends Exception {
    /**
     * InvalidImportException uses a custom error message
     * @param message error message used to explain why row is invalid
     */
    public InvalidImportException(String message)
    {
        super(message);
    }
}
