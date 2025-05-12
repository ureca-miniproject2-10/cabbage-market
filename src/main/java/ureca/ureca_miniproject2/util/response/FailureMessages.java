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

    // 유저 관련 메시지
    USER_NOT_FOUND("유저가 존재하지 않습니다."),

    // 이미지 관련 메시지
    // 이미지 관련 메시지 추가
    IMAGE_SIZE_EXCEEDED("이미지 크기는 10MB를 초과할 수 없습니다."),
    INVALID_IMAGE_TYPE( "지원하지 않는 이미지 형식입니다. 지원 형식: JPEG, PNG, GIF, BMP, WEBP"),
    EMPTY_FILE("파일이 비어있습니다.");

    private final String message;
}
