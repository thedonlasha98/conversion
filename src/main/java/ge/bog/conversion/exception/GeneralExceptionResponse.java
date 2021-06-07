package ge.bog.conversion.exception;

public class GeneralExceptionResponse {
    String message;

    public GeneralExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
