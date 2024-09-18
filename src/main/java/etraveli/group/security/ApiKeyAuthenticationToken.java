package etraveli.group.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private final String apiKey;
    private final Object principal;

    public ApiKeyAuthenticationToken(String apiKey) {
        // No authorities/roles yet
        super(null);
        this.apiKey = apiKey;
        this.principal = null;
        // Initially unauthenticated
        setAuthenticated(false);
    }

    public ApiKeyAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = null;
        this.principal = principal;
        // Authenticated with authorities
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        // In this case, the API key is the credential
        return apiKey;
    }

    @Override
    public Object getPrincipal() {
        // The principal is usually the user or identifier
        return this.principal;
    }
}
