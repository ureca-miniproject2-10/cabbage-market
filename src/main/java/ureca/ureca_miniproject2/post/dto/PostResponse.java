package ureca.ureca_miniproject2.post.dto;

import ureca.ureca_miniproject2.comment.dto.CommentResponseDto;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.PostState;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Integer postId,
        String title,
        Integer price,
        String imageUrl,
        int likeCnt,
        int viewCnt,
        int reportCnt,
        LocalDateTime createdAt,
        String state,
        String name
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getTitle(),
                post.getPrice(),
                post.getImageUrl(),
                post.getLikeCnt(),
                post.getViewCnt(),
                post.getReportCnt(),
                post.getCreatedAt(),
                post.getState(),
                post.getUser().getName()
        );
    }
}
