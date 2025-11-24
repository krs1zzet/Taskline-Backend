package cmdotender.TaskLine.product.exceptions;

import cmdotender.TaskLine.product.exceptions.customException.ApiException;
import cmdotender.TaskLine.product.exceptions.errorCode.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==== ORTAK BASE METODLAR ==== //

    private ProblemDetail base(ErrorCode code,
                               HttpServletRequest request,
                               String overrideMessage) {

        String detail = overrideMessage != null
                ? overrideMessage
                : code.getMessage();

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(code.getStatus(), detail);
        pd.setTitle(code.getStatus().getReasonPhrase());
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("errorCode", code.getErrorCode());
        return pd;
    }

    private ProblemDetail base(ErrorCode code, HttpServletRequest request) {
        return base(code, request, null);
    }

    private ProblemDetail base(HttpStatus status,
                               String detail,
                               HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status.getReasonPhrase());
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("path", request.getRequestURI());
        return pd;
    }

    // ==== CUSTOM / DOMAIN EXCEPTION ==== //

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ProblemDetail> handleApi(ApiException ex,
                                                   HttpServletRequest request) {
        ErrorCode code = ex.getErrorCode();
        ProblemDetail pd = base(code, request, ex.getMessage());

        if (ex.getDetails() != null) {
            pd.setProperty("details", ex.getDetails());
        }

        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // ==== VALIDATION ==== //

    // @Valid body validation (DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {

        ErrorCode code = ErrorCode.VALIDATION_ERROR;
        ProblemDetail pd = base(code, request);

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage(),
                        (oldVal, newVal) -> newVal,
                        LinkedHashMap::new
                ));

        pd.setProperty("errors", fieldErrors);

        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // @Validated parametreler için (ConstraintViolationException)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex,
                                                                   HttpServletRequest request) {
        ErrorCode code = ErrorCode.VALIDATION_ERROR;
        ProblemDetail pd = base(code, request);

        Map<String, String> violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage() == null ? "invalid" : v.getMessage(),
                        (oldVal, newVal) -> newVal,
                        LinkedHashMap::new
                ));

        pd.setProperty("errors", violations);

        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // BindException (özellikle form-data / query param bind hataları)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindException(BindException ex,
                                                             HttpServletRequest request) {
        ErrorCode code = ErrorCode.VALIDATION_ERROR;
        ProblemDetail pd = base(code, request);

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() == null ? "invalid" : fe.getDefaultMessage(),
                        (oldVal, newVal) -> newVal,
                        LinkedHashMap::new
                ));

        pd.setProperty("errors", fieldErrors);

        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // ==== REQUEST / PARAM / BODY HATALARI ==== //

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        ErrorCode code = ErrorCode.TYPE_MISMATCH;
        ProblemDetail pd = base(code, request,
                "Parameter type mismatch: " + ex.getName());

        pd.setProperty("expectedType",
                ex.getRequiredType() != null
                        ? ex.getRequiredType().getSimpleName()
                        : "unknown");

        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // Body okunamıyor (bozuk JSON, boş body vs.)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleNotReadable(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        ErrorCode code = ErrorCode.NOT_READABLE;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // Required request parameter yok
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingParam(MissingServletRequestParameterException ex,
                                                            HttpServletRequest request) {
        ErrorCode code = ErrorCode.MISSING_PARAMETER;
        ProblemDetail pd = base(code, request,
                "Missing required parameter: " + ex.getParameterName());
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // ==== HTTP / ROUTING ==== //

    // Method not allowed (GET beklerken POST vs.)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                                HttpServletRequest request) {
        ErrorCode code = ErrorCode.METHOD_NOT_ALLOWED;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // Media type desteklenmiyor (Content-Type)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpServletRequest request) {
        ErrorCode code = ErrorCode.MEDIA_TYPE_NOT_SUPPORTED;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // Accept header ile uyuşmuyor
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ProblemDetail> handleMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpServletRequest request) {
        ErrorCode code = ErrorCode.MEDIA_TYPE_NOT_ACCEPTABLE;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // No handler found (404 - mapping yok)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoHandlerFound(NoHandlerFoundException ex,
                                                              HttpServletRequest request) {
        ErrorCode code = ErrorCode.NO_HANDLER_FOUND;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // ResponseStatusException (Spring'in kendi fırlattığı durumlar)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ProblemDetail> handleResponseStatus(ResponseStatusException ex,
                                                              HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String reason = ex.getReason() != null
                ? ex.getReason()
                : status.getReasonPhrase();

        ProblemDetail pd = base(status, reason, request);
        return ResponseEntity.status(status).body(pd);
    }

    // ==== SECURITY ==== //

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthentication(AuthenticationException ex,
                                                              HttpServletRequest request) {
        ErrorCode code = ErrorCode.UNAUTHORIZED;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex,
                                                            HttpServletRequest request) {
        ErrorCode code = ErrorCode.FORBIDDEN;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // ==== DATA / DB ==== //

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrity(DataIntegrityViolationException ex,
                                                             HttpServletRequest request) {
        ErrorCode code = ErrorCode.CONFLICT;
        ProblemDetail pd = base(code, request);
        return ResponseEntity.status(code.getStatus()).body(pd);
    }

    // ==== EN GENEL FALLBACK ==== //

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleAny(Exception ex,
                                                   HttpServletRequest request) {
        log.error("Unhandled exception", ex);

        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        ProblemDetail pd = base(code, request);

        return ResponseEntity.status(code.getStatus()).body(pd);
    }
}
