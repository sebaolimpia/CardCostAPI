package etraveli.group.common;

public class Constants {

    // Application messages
    public static final String SCOPE_SUFFIX = "SCOPE_SUFFIX";

    public static final String LOCALHOST_URL = "http://localhost:8080";

    public static final String BASE_URL = "/payment-cards-cost";

    public static final String INFO_BIN_BASE_URL = BASE_URL + "/bin-info";

    public static final String AUTHORIZATION_BASE_URL = BASE_URL + "/auth";

    public static final String USERS_BASE_URL = BASE_URL + "/users";

    public static final String BIN_LIST_URL = "https://lookup.binlist.net/%d";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_HEADER_API_KEY = "x-api-key";

    public static final String AUTHORITIES = "authorities";

    // Log messages
    public static final String LOG_ERROR_EXECUTING_NOT_FOUND_EXCEPTION_HANDLER_REST =
            "Executing not found exception handler (REST)";

    public static final String LOG_ERROR_EXECUTING_BAD_REQUEST_EXCEPTION_HANDLER_REST =
            "Executing bad request exception handler (REST)";

    public static final String LOG_ERROR_EXECUTING_METHOD_ARGUMENT_NOT_VALID_EXCEPTION_HANDLER_REST =
            "Executing method argument not valid exception handler (REST).";

    // Validation messages
    public static final String VALIDATION_THE_NUMBER_MUST_BE_BETWEEN_8_AND_19_DIGITS =
            "The card number must be a number between 8 and 19 digits.";

    public static final String VALIDATION_COUNTRY_CODE_MUST_BE_EXACTLY_2_CHARACTERS_LONG =
            "Country code must be exactly 2 characters long.";

    public static final String VALIDATION_BIN_MUST_BE_EXACTLY_6_CHARACTERS_LONG =
            "BIN must be exactly 6 characters long.";

    public static final String VALIDATION_COUNTRY_CODE_API_CALL_MUST_BE_EXACTLY_2_CHARACTERS_LONG =
            "Response country code of API call https://lookup.binlist.net must be exactly 2 characters long.";

    public static final String VALIDATION_THE_CARD_COST_ID_MUST_BE_A_POSITIVE_NUMBER =
            "The card cost id must be a positive number.";

    public static final String VALIDATION_THE_BIN_INFO_ID_MUST_BE_A_POSITIVE_NUMBER =
            "The BIN info id must be a positive number.";

    public static final String VALIDATION_THE_COST_MUST_BE_ZERO_OR_A_POSITIVE_NUMBER =
            "The cost must be zero or a positive number.";

    public static final String VALIDATION_THE_COUNTRY_CANNOT_BE_EMPTY = "The country cannot be empty.";

    public static final String VALIDATION_THE_COST_CANNOT_BE_EMPTY = "The cost cannot be empty.";

    public static final String VALIDATION_THE_USERNAME_CANNOT_BE_EMPTY = "The username cannot be empty.";

    public static final String VALIDATION_THE_PASSWORD_CANNOT_BE_EMPTY = "The password cannot be empty.";

    // Exception messages
    public static final String EXCEPTION_ALREADY_EXIST_USER_WITH_USERNAME = "Already exist user with username %s.";

    public static final String EXCEPTION_NOT_FOUND_CARD_COST_WITH_COUNTRY = "Not found card cost with country %s.";

    public static final String EXCEPTION_NOT_FOUND_CARD_COST_WITH_COST = "Not found card cost with cost %f.";

    public static final String EXCEPTION_NOT_FOUND_CARD_COST_WITH_ID = "Not found card cost with id %d.";

    public static final String EXCEPTION_NOT_FOUND_BIN_INFO_WITH_ID = "Not found BIN info with id %d.";

    public static final String EXCEPTION_NOT_FOUND_COUNTRY_WITH_BIN = "Not found country with BIN %d.";

    public static final String EXCEPTION_NOT_FOUND_COUNTRY_FROM_EXTERNAL_API_CALL =
            "Not found country from https://lookup.binlist.net.";

    public static final String EXCEPTION_SUPER_ADMINISTRATOR_ROLE_NOT_FOUND =
            "Super Administrator role not found, thus cannot create user super administrator.";

    public static final String EXCEPTION_ADMINISTRATOR_ROLE_NOT_FOUND =
            "Administrator role not found, thus cannot create user administrator.";

    public static final String EXCEPTION_USER_ROL_NOT_FOUND = "User role not found, thus cannot create user.";

    public static final String EXCEPTION_INVALID_USERNAME_OR_PASSWORD = "Invalid username or password.";

    public static final String EXCEPTION_USER_NOT_FOUND = "User not found.";

    public static final String EXCEPTION_UNAUTHORIZED = "The client does not have rights to access the content.";

    public static final String EXCEPTION_ALREADY_EXIST_BIN = "BIN %d already exists.";

}
