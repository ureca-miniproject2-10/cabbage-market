package ureca.ureca_miniproject2.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    private String content;
    private LocalDateTime createdAt;

    public void updateContent(String content) {
        this.content = content;
    }
}
