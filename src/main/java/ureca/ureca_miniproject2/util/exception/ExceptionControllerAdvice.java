package ureca.ureca_miniproject2.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ureca.ureca_miniproject2.util.exception.custom.*;
import ureca.ureca_miniproject2.util.response.ApiResponse;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        return ApiResponse.fail(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<?> handleImageUploadException(ImageUploadException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<?> handleUnAuthorizedException(UnAuthorizedException e) {
        return ApiResponse.fail(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException e) {
        return ApiResponse.fail(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    @ExceptionHandler(IsAlreadyException.class)
    public ResponseEntity<?> handleIsAlreadyException(IsAlreadyException e) {
        return ApiResponse.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

}
