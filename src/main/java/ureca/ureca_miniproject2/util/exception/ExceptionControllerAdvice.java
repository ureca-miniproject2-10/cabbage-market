package ureca.ureca_miniproject2.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ureca.ureca_miniproject2.util.exception.custom.UserNotFoundException;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.FailureMessages;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> userNotFoundException(UserNotFoundException e) {
        return ApiResponse.fail(USER_NOT_FOUND.getHttpStatus(), e.getMessage());
    }
}
