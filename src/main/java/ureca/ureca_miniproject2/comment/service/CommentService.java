package ureca.ureca_miniproject2.comment.service;

import ureca.ureca_miniproject2.comment.dto.CommentRequestDto;
import ureca.ureca_miniproject2.comment.dto.CommentResponseDto;

public interface CommentService {
    CommentResponseDto createComment(Integer postId,Integer userId, CommentRequestDto commentRequestDto);
    CommentResponseDto updateComment(Integer commentId,Integer userId, CommentRequestDto commentRequestDto);
    void deleteComment(Integer commentId,Integer userId);
}
