package ureca.ureca_miniproject2.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ureca.ureca_miniproject2.util.exception.custom.NotFoundException;
import ureca.ureca_miniproject2.util.response.ApiResponse;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException e) {
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
}
