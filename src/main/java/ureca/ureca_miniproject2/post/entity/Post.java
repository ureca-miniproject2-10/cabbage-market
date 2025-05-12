package ureca.ureca_miniproject2.post.entity;

import jakarta.persistence.*;
import lombok.*;
import ureca.ureca_miniproject2.comment.entity.Comment;
import ureca.ureca_miniproject2.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;
    private String title;
    private String content;
    private Integer price;
    private String imageUrl;
    private int likeCnt;
    private int viewCnt;
    private int reportCnt;

    private LocalDateTime createdAt;

    @Enumerated(value=EnumType.STRING)
    private PostState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void update(String title, String content, Integer price, String imageUrl, PostState state) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.imageUrl = imageUrl;
        this.state = state;
    }
}
