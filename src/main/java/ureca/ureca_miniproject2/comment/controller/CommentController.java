package ureca.ureca_miniproject2.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.comment.dto.CommentRequestDto;
import ureca.ureca_miniproject2.comment.dto.CommentResponseDto;
import ureca.ureca_miniproject2.comment.service.CommentService;
import ureca.ureca_miniproject2.config.MyUserDetails;
import ureca.ureca_miniproject2.util.exception.custom.UnAuthorizedException;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.FailureMessages;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;
import static ureca.ureca_miniproject2.util.response.SuccessMessages.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable("postId") Integer postId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal MyUserDetails userDetails
            ) {
        if(userDetails == null){
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        Integer currentUserId = userDetails.getUserId();

        CommentResponseDto responseDto = commentService.createComment(postId, currentUserId, requestDto);
        return ApiResponse.success(COMMENT_CREATE, responseDto);
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable("commentId") Integer commentId,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        Integer currentUserId = userDetails.getUserId();

        CommentResponseDto responseDto = commentService.updateComment(commentId,  currentUserId, requestDto);
        return ApiResponse.success(COMMENT_UPDATE, responseDto);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable("commentId") Integer commentId,
            @AuthenticationPrincipal MyUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        Integer currentUserId = userDetails.getUserId();
        commentService.deleteComment(commentId, currentUserId);
        return ApiResponse.success(COMMENT_DELETE);
    }
}
