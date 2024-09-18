package etraveli.group.service;

import static etraveli.group.common.Constants.AUTHORITIES;
import static etraveli.group.common.Constants.AUTHORIZATION_HEADER;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import etraveli.group.config.JwtConfig;
import etraveli.group.model.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtService {

    private static final String PREFIX = "Bearer ";

    @Autowired
    private JwtConfig jwtConfig;

    /**
     * Verify if the "Authorization" header exists and its value starts with "Bearer ".
     */
    public boolean existJWT(String authenticationHeader) {
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
    }

    private <T> T extractClaim(Function<Claims, T> claimsResolver, Claims claims) {
        return claimsResolver.apply(claims);
    }

    private Date extractExpirationOfJWT(Claims claims) {
        return extractClaim(Claims::getExpiration, claims);
    }

    private boolean isTokenExpired(Claims claims) {
        return extractExpirationOfJWT(claims).before(new Date());
    }

    /**
     * Extract the username from the token.
     */
    public String extractUsername(HttpServletRequest request) {
        return extractClaim(Claims::getSubject, getClaims(request));
    }

    private String extractUsernameOfJWT(Claims claims) {
        return extractClaim(Claims::getSubject, claims);
    }

    private boolean isTokenUsernameCorrect(Claims claims, UserDetails user) {
        return user.getUsername().equals(extractUsernameOfJWT(claims));
    }

    /**
     * Get the claims from the token.
     */
    public Claims getClaims(HttpServletRequest request) {
        String jwt = request.getHeader(AUTHORIZATION_HEADER).replace(PREFIX, "");
        SecretKey secretKey = hmacShaKeyFor(jwtConfig.getKey().getBytes());
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
    }

    /**
     * Validate the token.
     */
    public boolean isValidJWT(HttpServletRequest request, UserDetails user) {
        Claims claims = getClaims(request);
        return isTokenUsernameCorrect(claims, user) && !isTokenExpired(claims);
    }

    /**
     * Create bearer token for a specific user, valid for 10 minutes.
     *
     * @param user User to create token for.
     * @return Token created.
     */
    public String createJWT(Users user) {
        String token = Jwts
                .builder()
                .subject(user.getUsername())
                .claim(AUTHORITIES,
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationTime()))
                .signWith(getSigningKey()).compact();
        return PREFIX + token;
    }

    /**
     * Get secret key to sign the token.
     */
    private SecretKey getSigningKey() {
        String secretString = Encoders.BASE64.encode(jwtConfig.getKey().getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        return hmacShaKeyFor(keyBytes);
    }
}
