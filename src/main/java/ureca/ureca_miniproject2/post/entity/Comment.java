package ureca.ureca_miniproject2.post.entity;

import java.time.LocalDateTime;

public class Comment {
    private int commentId;

    // 유저id FK
    // 게시글id FK

    private String content;
    private LocalDateTime createdAt;
}
