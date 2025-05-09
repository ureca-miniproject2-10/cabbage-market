package ureca.ureca_miniproject2.post.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostState {
    SALE("판매중"),
    SOLD_OUT("거래종료"),
    RESTRICT("거래제한");

    private final String state;
}
