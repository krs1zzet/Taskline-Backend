package cmdotender.TaskLine.product.exceptions.customException;

import cmdotender.TaskLine.product.exceptions.errorCode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String,Object> details;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApiException(ErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApiException(ErrorCode errorCode, String message, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }


}
