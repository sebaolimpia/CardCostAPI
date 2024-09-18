package etraveli.group.security;

import static etraveli.group.common.Constants.*;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

/**
 * Filter to request validation by API key.
 */
@Order(1)
public class ApiKeyAuthorizationFilter extends OncePerRequestFilter {

    private final String internalApiKey;

    public ApiKeyAuthorizationFilter(String internalApiKey) {
        this.internalApiKey = internalApiKey;
    }

    /**
     * Filter to request validation by API key.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String apiKey = request.getHeader(AUTHORIZATION_HEADER_API_KEY);

        if (internalApiKey.equals(apiKey)) {
            ApiKeyAuthenticationToken authToken = new ApiKeyAuthenticationToken(
                    "user_1",
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
            getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }
}
