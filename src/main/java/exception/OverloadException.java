package exception;

public class OverloadException extends RuntimeException {
    public OverloadException() {
    }

    public OverloadException(String message) {
        super(message);
    }

    public OverloadException(String message, Throwable cause) {
        super(message, cause);
    }
}
