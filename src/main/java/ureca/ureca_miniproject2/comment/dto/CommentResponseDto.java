package ureca.ureca_miniproject2.comment.dto;

import lombok.Builder;
import ureca.ureca_miniproject2.comment.entity.Comment;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDto(
        Integer commentId,
        String content,
        LocalDateTime createdAt,
        UserSimpleDto user,
        Integer postId
) {
    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .user(UserSimpleDto.from(comment.getUser())) // User 엔티티를 UserSimpleDto로 변환
                .postId(comment.getPost().getPostId())
                .build();
    }
}
