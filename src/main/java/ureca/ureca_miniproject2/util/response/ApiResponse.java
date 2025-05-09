package ureca.ureca_miniproject2.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private final int status;
    private final boolean success;
    private final String message;
    private T data;

    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessMessages successMessages){
        return ResponseEntity.status(successMessages.getHttpStatus())
                .body(ApiResponse.<T>builder()
                        .status(successMessages.getHttpStatus())
                        .success(true)
                        .message(successMessages.getMessage())
                        .build());
    }
    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessMessages successMessages, T data){
        return ResponseEntity.status(successMessages.getHttpStatus())
                .body(ApiResponse.<T>builder()
                        .status(successMessages.getHttpStatus())
                        .success(true)
                        .message(successMessages.getMessage())
                        .data(data).build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(int status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .status(status)
                        .success(false)
                        .message(message)
                        .build());
    }
}
