package cmdotender.TaskLine.product.exceptions.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "G001", "Validation failed"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G002", "Request is invalid or malformed"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "G003", "Requested resource was not found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G004", "HTTP method is not allowed for this endpoint"),
    CONFLICT(HttpStatus.CONFLICT, "G005", "Request could not be completed due to a conflict"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "Authentication is required to access this resource"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "You do not have permission to access this resource"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G999", "An unexpected error occurred"),

    REQUEST_BODY_MISSING(HttpStatus.BAD_REQUEST, "G006", "Request body is missing"),
    TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "G007", "Parameter type mismatch"),
    NOT_READABLE(HttpStatus.BAD_REQUEST, "G008", "Request body is not readable"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "G009", "Required request parameter is missing"),
    NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "G010", "No handler found for the requested path"),
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "G011", "Media type is not supported"),
    MEDIA_TYPE_NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "G012", "Requested media type is not acceptable"),
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A003", "Invalid authentication credentials"),
    AUTH_USER_DISABLED(HttpStatus.UNAUTHORIZED, "A004", "User account is disabled"),
    RESOURCE_ALREADY_EXISTS(HttpStatus.CONFLICT, "G013" , "Resource already exists" ),;

    private final HttpStatus status;
    private final String errorCode;
    private final String message;
}
