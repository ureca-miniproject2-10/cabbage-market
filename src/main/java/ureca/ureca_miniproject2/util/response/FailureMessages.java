package ureca.ureca_miniproject2.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FailureMessages {
    // 게시글 관련 메시지
    POST_CREATE("게시글 등록에 실패하였습니다."),
    POST_NOT_FOUND("게시글이 존재하지 않습니다."),
    POST_FORBIDDEN("게시글을 수정, 삭제는 작성자만 가능합니다."),


    // 유저 관련 메시지
    USER_NOT_FOUND("유저가 존재하지 않습니다."),
    UNAUTHORIZED("로그인이 필요한 기능입니다."),

    // 이미지 관련 메시지
    // 이미지 관련 메시지 추가
    IMAGE_SIZE_EXCEEDED("이미지 크기는 10MB를 초과할 수 없습니다."),
    INVALID_IMAGE_TYPE( "지원하지 않는 이미지 형식입니다. 지원 형식: JPEG, PNG, GIF, BMP, WEBP"),
    EMPTY_FILE("파일이 비어있습니다."),

    // 댓글 관련 메시지
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다."),
    COMMENT_NO_AUTH("댓글을 수정, 삭제할 권한이 없습니다."),

    // 권한 관련 메시지;

    // 신고 관련 메시지
    REPORT_DUPLICATED("이미 신고한 게시글입니다.");

    private final String message;
}
