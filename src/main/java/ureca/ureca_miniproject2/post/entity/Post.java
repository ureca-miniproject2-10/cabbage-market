package ureca.ureca_miniproject2.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ureca.ureca_miniproject2.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();
}
