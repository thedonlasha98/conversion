package ge.bog.conversion.exception;

public class GeneralException extends RuntimeException {

    private String errorMessage;

    public GeneralException(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage.getDescription();
    }

    public GeneralException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
