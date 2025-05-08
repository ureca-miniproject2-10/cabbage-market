package ureca.ureca_miniproject2.post.entity;

import java.time.LocalDateTime;

public class Post {
    private Integer postId;
    private String title;
    private String content;
    private Integer price;
    private String imageUrl;
    private int likeCnt;
    private int viewCnt;
    private int reportCnt;

    private LocalDateTime createdAt;
    private PostState state;

    // FK 유저
}
