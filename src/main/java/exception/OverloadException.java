package exception;


public class OverloadException  extends RuntimeException {
    public OverloadException() {
    }

    public OverloadException(String message) {
        super(message);
    }
}
