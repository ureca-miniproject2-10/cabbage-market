package ureca.ureca_miniproject2.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import ureca.ureca_miniproject2.comment.dto.CommentRequestDto;
import ureca.ureca_miniproject2.comment.dto.CommentResponseDto;
import ureca.ureca_miniproject2.comment.entity.Comment;
import ureca.ureca_miniproject2.comment.repository.CommentRepository;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.util.exception.custom.ForbiddenException;
import ureca.ureca_miniproject2.util.exception.custom.NotFoundException;
import ureca.ureca_miniproject2.util.response.FailureMessages;

import java.time.LocalDateTime;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDto createComment(Integer postId, Integer userId, CommentRequestDto commentRequestDto) {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND.getMessage()));

        // 사용자 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

        // 댓글 생성
        Comment comment = Comment.builder()
                .content(commentRequestDto.content())
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.from(savedComment);
    }

    @Override
    public CommentResponseDto updateComment(Integer commentId, Integer userId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND.getMessage()));

        // 댓글 작성자만 수정 가능
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException(COMMENT_NO_AUTH.getMessage());
        }

        comment.updateContent(commentRequestDto.content());

        return CommentResponseDto.from(comment);
    }

    @Override
    public void deleteComment(Integer commentId, Integer userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND.getMessage()));

        // 댓글 작성자만 삭제 가능
        if (!comment.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException(COMMENT_NO_AUTH.getMessage());
        }

        commentRepository.delete(comment);
    }
}
