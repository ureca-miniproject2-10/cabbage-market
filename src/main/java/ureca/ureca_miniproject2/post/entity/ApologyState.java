package ureca.ureca_miniproject2.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApologyState {
    PENDING("검토중"),
    ACCEPTED("승인됨"),
    REJECTED("거부됨");

    private final String state;
}
