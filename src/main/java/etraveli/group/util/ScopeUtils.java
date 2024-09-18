package etraveli.group.util;

import static etraveli.group.common.Constants.SCOPE_SUFFIX;
import static org.springframework.util.StringUtils.hasText;

/**
 * Utility class for scope information.
 */
public class ScopeUtils {

    public static final String ENV_SCOPE = "SCOPE";

    public static final String PROD_SCOPE = "prod";

    public static void calculateScopeSuffix() {
        System.setProperty(SCOPE_SUFFIX, getScopeValue());
    }

    /**
     * Gets the name of the scope.
     * If the scope is not set as environment variable, the default value prod is returned.
     *
     * @return the name of the scope
     */
    public static String getScopeValue() {
        String scope = System.getenv(ENV_SCOPE);
        if (hasText(scope)) {
            return scope;
        }
        return PROD_SCOPE;
    }
}
