package ureca.ureca_miniproject2.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FailureMessages {
    POST_CREATE(HttpStatus.BAD_REQUEST, "게시글 등록에 실패하였습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,  "게시글이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,  "유저가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatus() {
        return this.httpStatus.value();
    }
}
