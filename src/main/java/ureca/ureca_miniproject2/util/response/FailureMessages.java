package ureca.ureca_miniproject2.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FailureMessages {
    POST_REGISTER_SUCCESS(HttpStatus.BAD_REQUEST, "게시글 등록에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatus() {
        return this.httpStatus.value();
    }
}
