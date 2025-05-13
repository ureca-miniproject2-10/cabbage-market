package ureca.ureca_miniproject2.report.dto;

import lombok.Builder;
import lombok.Getter;
import ureca.ureca_miniproject2.post.entity.Apology;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApologyResponse {
    private Integer id;
    private Integer postId;
    private String postTitle;
    private String content;
    private LocalDateTime createdAt;
    private String state;
    private String username;

    public static ApologyResponse from(Apology letter) {
        return ApologyResponse.builder()
                .id(letter.getId())
                .postId(letter.getPost().getPostId())
                .postTitle(letter.getPost().getTitle())
                .content(letter.getContent())
                .createdAt(letter.getCreatedAt())
                .state(letter.getState().getState())
                .username(letter.getPost().getUser().getUsername())
                .build();
    }
}
