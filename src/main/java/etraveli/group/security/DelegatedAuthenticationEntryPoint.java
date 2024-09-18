package etraveli.group.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Spring security exceptions can be directly handled by adding custom filters and
 * constructing the response body. To handle these exceptions at a global level
 * via @ExceptionHandler and @ControllerAdvice, we need a custom implementation of AuthenticationEntryPoint.
 * AuthenticationEntryPoint is used to send an HTTP response that requests credentials from a client.
 * Although there are multiple built-in implementations for the security entry point, we need to write
 * a custom implementation for sending a custom response message.
 * We’ll implement AuthenticationEntryPoint and then delegate the exception handler to HandlerExceptionResolver.
 * */
@Component("delegatedAuthenticationEntryPoint")
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        resolver.resolveException(request, response, null, authException);
    }
}
