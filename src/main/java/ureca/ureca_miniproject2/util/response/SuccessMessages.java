package ureca.ureca_miniproject2.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessMessages {
    // POST 관련 성공 메시지
    POST_CREATE(HttpStatus.CREATED.value(), "게시글이 성공적으로 생성되었습니다."),
    POST_FIND(HttpStatus.OK.value(), "게시글 상세조회에 성공했습니다."),
    POST_FIND_ALL(HttpStatus.OK.value(), "전체 게시글 조회에 성공했습니다."),
    POST_UPDATE(HttpStatus.OK.value(), "게시글 수정에 성공했습니다."),
    POST_DELETE(HttpStatus.OK.value(), "게시글 삭제에 성공했습니다.");
    private final int httpStatus;
    private final String message;
}
