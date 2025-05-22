package ureca.ureca_miniproject2.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Apology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    private String state;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void accept() {
        this.state = "002";
        this.reviewedAt = LocalDateTime.now();
    }

    public void reject() {
        this.state = "003";
        this.reviewedAt = LocalDateTime.now();
    }
}
