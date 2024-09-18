package etraveli.group.config;

import static etraveli.group.common.Constants.EXCEPTION_USER_NOT_FOUND;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import etraveli.group.repository.IUsersRepository;
import etraveli.group.security.ApiKeyAuthorizationFilter;
import etraveli.group.security.JwtAuthorizationFilter;
import etraveli.group.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Web security configuration.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtService jwtService;

    private final IUsersRepository userRepository;

    @Qualifier("delegatedAuthenticationEntryPoint")
    private final AuthenticationEntryPoint authEntryPoint;

    @Value("${spring.app.api-key}")
    private String internalApiKey;

    @Autowired
    public WebSecurityConfig(
            JwtService jwtService,
            IUsersRepository userRepository,
            AuthenticationEntryPoint authEntryPoint) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(userDetailsService(), jwtService);
    }

    @Bean
    public ApiKeyAuthorizationFilter apiKeyAuthorizationFilter() {
        return new ApiKeyAuthorizationFilter(internalApiKey);
    }

    /**
     * Password encoder.
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Perform the authentication by finding the users in our database.
     * The userDetailsService() defines how to retrieve the users using the UserRepository that is injected.
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(EXCEPTION_USER_NOT_FOUND));
    }

    /**
     * Endpoints authorization configuration using JWT.
     *
     * @param http HttpSecurity object
     * @throws Exception Exception if any error occurs.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/payment-cards-cost/auth/**").permitAll()
                                .requestMatchers("/payment-cards-cost/users/admin").hasRole("SUPER_ADMIN")
                                .requestMatchers("/payment-cards-cost/users/all").hasAnyRole("SUPER_ADMIN", "ADMIN")
                                .requestMatchers(antMatcher(GET, "/payment-cards-cost/**")).hasAnyRole("SUPER_ADMIN", "ADMIN", "USER")
                                .requestMatchers(antMatcher(POST, "/payment-cards-cost")).hasRole("ADMIN")
                                .requestMatchers(antMatcher(PUT, "/payment-cards-cost/**")).hasRole("ADMIN")
                                .requestMatchers(antMatcher(DELETE, "/payment-cards-cost/**")).hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(authEntryPoint)
                );
        http.addFilterBefore(new ApiKeyAuthorizationFilter(internalApiKey), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Configuration to exclude endpoints.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/configuration/security",
                "/h2-console/**");
    }
}
