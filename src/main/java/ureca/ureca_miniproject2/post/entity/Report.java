package ureca.ureca_miniproject2.post.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import ureca.ureca_miniproject2.post.entity.key.ReportKey;
import ureca.ureca_miniproject2.user.entity.User;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@IdClass(ReportKey.class)
public class Report {
    @Id
    private Integer userId;
    @Id
    private Integer postId;

    private LocalDateTime createdAt;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name="post_id")
    private Post post;

    public Report(User user, Post post, String content) {
        this.user = user;
        this.post = post;
        this.userId = user.getUserId();
        this.postId = post.getPostId();
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }
}
