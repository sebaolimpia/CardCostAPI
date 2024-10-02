package etraveli.group.util;

public class Constants {

    // Credentials
    public static final String ADMIN = "admin";

    public static final String USER = "user";

    public static final String PASSWORD = "123456";

    // URLs
    public static final String URL_ID_NOT_FOUND = "/1111";

    public static final String URL_ID_NON_POSITIVE = "/0";

    public static final String URL_ID_1 = "/1";

    public static final String COUNTRY_QUERY_PARAM = "/country?country=";

    public static final String COST_QUERY_PARAM = "/cost?cost=";

    // Error messages
    public static final String ERROR_CARD_COST_NOT_FOUND_BY_ID =
            "{\"error\":\"NotFoundException\",\"message\":\"Not found card cost with id 1111.\",\"status\":404}";

    public static final String ERROR_CARD_COST_NOT_FOUND_BY_COUNTRY =
            "{\"error\":\"NotFoundException\",\"message\":\"Not found card cost with country XX.\",\"status\":404}";

    public static final String ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_ID =
            "[{\"error\":\"ConstraintViolationImpl\",\"message\":\"The card cost id must be a positive number.\"" +
                    ",\"status\":400}]";

    public static final String ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_COUNTRY =
            "[{\"error\":\"ConstraintViolationImpl\",\"message\":\"Country code must be exactly 2 characters long.\"" +
                    ",\"status\":400}]";

    public static final String ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_COST =
            "[{\"error\":\"ConstraintViolationImpl\",\"message\":\"The cost must be zero or a positive number.\"" +
                    ",\"status\":400}]";

    public static final String ERROR_CARD_COST_VIOLATION_FIELD_COUNTRY =
            "[{\"error\":\"ViolationFieldError\",\"message\":\"Country code must be exactly 2 characters long.\"" +
                    ",\"status\":400}]";

    public static final String ERROR_VIOLATION_FIELD_COST =
            "[{\"error\":\"ViolationFieldError\",\"message\":\"The cost must be zero or a positive number.\"" +
                    ",\"status\":400}]";

    public static final String ERROR_NULL_COST =
            "[{\"error\":\"ViolationFieldError\",\"message\":\"The cost cannot be empty.\",\"status\":400}]";

    public static final String ERROR_NULL_COUNTRY =
            "[{\"error\":\"ViolationFieldError\",\"message\":\"The country cannot be empty.\",\"status\":400}]";

    public static final String ERROR_CARD_NUMBER_VIOLATION_FIELD_LENGTH_DIGITS =
            "[{\"error\":\"ViolationFieldError\",\"message\":\"The card number must be a number between "
                    + "8 and 19 digits.\",\"status\":400}]";

    public static final String ERROR_BIN_VIOLATION_FIELD_LENGTH_DIGITS =
            "[{\"error\":\"ViolationFieldError\",\"message\":\"BIN must be exactly 6 characters long.\","
                    + "\"status\":400}]";

    public static final String ERROR_BIN_CONSTRAINT_VIOLATION_FIELD_LENGTH_DIGITS =
            "[{\"error\":\"ConstraintViolationImpl\",\"message\":\"BIN must be exactly 6 characters long.\","
                    + "\"status\":400}]";


    public static final String ERROR_BIN_INFO_CONSTRAINT_VIOLATION_BY_ID =
            "[{\"error\":\"ConstraintViolationImpl\",\"message\":\"The BIN info id must be a positive number.\"" +
                    ",\"status\":400}]";

    public static final String ERROR_BIN_INFO_ALREADY_EXIST =
            "{\"error\":\"BadRequestException\",\"message\":\"BIN 421821 already exists.\",\"status\":400}";

    public static final String ERROR_BIN_INFO_NOT_FOUND_BY_ID =
            "{\"error\":\"NotFoundException\",\"message\":\"Not found BIN info with id 1111.\",\"status\":404}";

    // Regex messages
    public static final String REGEX_CARD_COST_NULL_COST = ".*The cost cannot be empty.*";

    public static final String REGEX_CARD_COST_NULL_COUNTRY = ".*The country cannot be empty.*";

    public static final String REGEX_POSITIVE_COST = ".*The cost must be zero or a positive number.*";

    public static final String REGEX_LENGTH_COUNTRY = ".*Country code must be exactly 2 characters long.*";

    public static final String REGEX_LENGTH_BIN = ".*BIN must be exactly 6 characters long.*";
}
