package etraveli.group.security;

import static etraveli.group.common.Constants.AUTHORITIES;
import static etraveli.group.common.Constants.AUTHORIZATION_HEADER;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import etraveli.group.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter to request validation by JWT.
 */
@Order(2)
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtService jwtService;

    /**
     * Use of @Lazy annotation to avoid circular dependency.
     * */
    @Autowired
    public JwtAuthorizationFilter(@Lazy UserDetailsService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    /**
     * Filter to request validation by JWT.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String jwtHeader = request.getHeader(AUTHORIZATION_HEADER);

        try {
            if (jwtService.existJWT(jwtHeader)) {
                final String username = jwtService.extractUsername(request);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Claims claims = jwtService.getClaims(request);
                if (jwtService.isValidJWT(request, userDetails) && claims.get(AUTHORITIES) != null) {
                    setUpSpringAuthentication(claims);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.sendError(SC_UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Method to authenticate us within the Spring flow.
     */
    private void setUpSpringAuthentication(Claims claims) {
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.get(AUTHORITIES);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(),
                null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        getContext().setAuthentication(auth);
    }
}
