package ureca.ureca_miniproject2.post.entity;

import java.time.LocalDateTime;

public class Report {
    // 복합키 설정 필요
    // 유저id FK
    // 게시글id FK

    private LocalDateTime createdAt;
    private String content;
}
