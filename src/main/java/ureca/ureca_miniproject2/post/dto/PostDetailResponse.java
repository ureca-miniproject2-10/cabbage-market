package ureca.ureca_miniproject2.post.dto;

import ureca.ureca_miniproject2.comment.dto.CommentResponseDto;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.PostState;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResponse(
        Integer postId,
        String title,
        String content,
        Integer price,
        String imageUrl,
        int likeCnt,
        int viewCnt,
        int reportCnt,
        LocalDateTime createdAt,
        PostState state,
        String name,
        List<CommentResponseDto> comments
) {
    public static PostDetailResponse from(Post post) {
        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getPrice(),
                post.getImageUrl(),
                post.getLikeCnt(),
                post.getViewCnt(),
                post.getReportCnt(),
                post.getCreatedAt(),
                post.getState(),
                post.getUser().getName(),
                post.getComments().stream().map(CommentResponseDto::from).toList()
        );
    }
}
