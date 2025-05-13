package ureca.ureca_miniproject2.post.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import ureca.ureca_miniproject2.post.entity.key.LikeEntityKey;
import ureca.ureca_miniproject2.user.entity.User;

@Entity
@NoArgsConstructor
@IdClass(LikeEntityKey.class)
public class LikeEntity {
    @Id
    private Integer userId;
    @Id
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name="post_id")
    private Post post;

    public LikeEntity(User user, Post post) {
        this.user = user;
        this.post = post;
        this.userId = user.getUserId();
        this.postId = post.getPostId();
    }

}
