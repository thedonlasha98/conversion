package ge.bog.conversion.exception;

public enum ErrorMessage {

    GENERAL_ERROR("General Error!"),
    USER_NOT_EXISTS("User Not Exists!"),
    INCORRECT_CREDENTIALS("Incorrect Credentials!"),
    INCORRECT_PARAMETERS("Incorrect Parameters!"),
    INCORRECT_STATUS_OR_USER("Incorrect Status Or User!"),
    ACCOUNT_FROM_NOT_FOUND("AccountFrom Not Found!"),
    ACCOUNT_NOT_EXISTS("Account Not Exists!"),
    ACCOUNT_TO_NOT_FOUND("AccountTo Not Found!"),
    SAME_CURRENCY_ERROR("Same Currency Error!"),
    COULD_NOT_FIND_RATE("Couldn't find Rate!"),
    CONVERSION_NOT_FOUND("Conversion Not Found!"),
    NOT_ENOUGH_BALANCE_FOR_ACCOUNT_FROM("Not Enough Balance!"),
    NOT_ENOUGH_BALANCE_FOR_ACCOUNT_TO("Not Enough Balance For AccountTo!"),
    USER_NOT_EQUALS_INP_USER("User Not Equals InpUser!"),
    ONE_CCY_MUST_BE_GEL("One ccy must be GEL");

    private String description;

    public String getDescription() {
        return description;
    }

    ErrorMessage(String description) {
        this.description = description;
    }
}
