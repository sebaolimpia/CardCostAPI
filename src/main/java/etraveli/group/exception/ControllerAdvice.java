package etraveli.group.exception;

import static etraveli.group.common.Constants.*;
import static org.springframework.http.HttpStatus.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import etraveli.group.dto.ApiErrorDto;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Handler for authentication errors.
     *
     * @param ex Exception.
     * @return Authentication error.
     */
    @ExceptionHandler({
            UsernameNotFoundException.class
    })
    public ResponseEntity<ApiErrorDto> handleAuthenticationRequest(Exception ex) {
        log.warn("Executing authentication request exception handler (REST)");
        ApiErrorDto apiError = ApiErrorDto.builder()
                .error(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .status(UNAUTHORIZED.value())
                .build();
        return new ResponseEntity<>(apiError, UNAUTHORIZED);
    }

    /**
     * Handler for unauthorized errors.
     *
     * @param ex Exception.
     * @return Authorization error.
     */
    @ExceptionHandler({
            AuthenticationException.class,
            UnauthorizedException.class,
            org.springframework.security.access.AccessDeniedException.class
    })
    public ResponseEntity<ApiErrorDto> handleUnAuthorizedRequest(Exception ex) {
        log.warn("Executing unauthorized request exception handler (REST)");
        ApiErrorDto apiError = ApiErrorDto.builder()
                .error(UnauthorizedException.class.getSimpleName())
                .message(EXCEPTION_UNAUTHORIZED)
                .status(FORBIDDEN.value())
                .build();
        return new ResponseEntity<>(apiError, FORBIDDEN);
    }

    /**
     * Handler for bad requests.
     * @param ex Exception.
     * @return Bad request error.
     */
    @ExceptionHandler({
            BadRequestException.class,
            DuplicateKeyException.class,
            SQLIntegrityConstraintViolationException.class,
            WebExchangeBindException.class,
            ServerWebInputException.class,
            InvalidInputException.class,
            JsonProcessingException.class
    })
    public ResponseEntity<ApiErrorDto> handleBadRequest(Exception ex) {
        log.warn(LOG_ERROR_EXECUTING_BAD_REQUEST_EXCEPTION_HANDLER_REST);
        ApiErrorDto apiError = ApiErrorDto.builder()
                .error(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .status(BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handler for not found requests.
     * @param ex Exception.
     * @return Not found error.
     */
    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ApiErrorDto> handleNotFoundRequest(NotFoundException ex) {
        log.warn(LOG_ERROR_EXECUTING_NOT_FOUND_EXCEPTION_HANDLER_REST);
        ApiErrorDto apiError = ApiErrorDto.builder()
                .error(ex.getClass().getSimpleName())
                .message(ex.getMessage())
                .status(NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    /**
     * Handler for not found requests.
     * @param ex Exception.
     * @return Not found error.
     */
    @ExceptionHandler({
            ConstraintViolationException.class
    })
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn(LOG_ERROR_EXECUTING_METHOD_ARGUMENT_NOT_VALID_EXCEPTION_HANDLER_REST);
        List<ApiErrorDto> apiError = ex.getConstraintViolations()
                .stream()
                .map(constraintViolation -> ApiErrorDto.builder()
                        .error(constraintViolation.getClass().getSimpleName())
                        .message(constraintViolation.getMessage())
                        .status(BAD_REQUEST.value())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    /**
     * Handler for method argument not valid.
     * @param ex Exception.
     * @param headers Headers.
     * @param status Status.
     * @param request Request.
     * @return Method argument not valid error.
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        log.warn(LOG_ERROR_EXECUTING_METHOD_ARGUMENT_NOT_VALID_EXCEPTION_HANDLER_REST);
        List<ApiErrorDto> apiError = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::mapError)
                .collect(Collectors.toList());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    private ApiErrorDto mapError(FieldError fieldError) {
        return ApiErrorDto.builder()
                .error(fieldError.getClass().getSimpleName())
                .message(fieldError.getDefaultMessage())
                .status(BAD_REQUEST.value())
                .build();
    }
}
