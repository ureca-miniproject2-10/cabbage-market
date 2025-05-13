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
    POST_DELETE(HttpStatus.OK.value(), "게시글 삭제에 성공했습니다."),

    // 댓글 관련 메시지
    COMMENT_CREATE(HttpStatus.CREATED.value(),"댓글이 성공적으로 생성되었습니다." ),
    COMMENT_UPDATE(HttpStatus.OK.value(), "댓글 수정에 성공했습니다."),
    COMMENT_DELETE(HttpStatus.OK.value(), "댓글 삭제에 성공했습니다."),

    // 좋아요 메시지
    LIKE_TOGGLE(HttpStatus.OK.value(), "좋아요 상태가 변경되었습니다."),

    // 신고하기 메시지
    REPORT_OK(HttpStatus.OK.value(), "신고 완료되었습니다.");

    private final int httpStatus;
    private final String message;
}
