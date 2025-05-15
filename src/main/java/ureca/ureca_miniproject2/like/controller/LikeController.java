package ureca.ureca_miniproject2.like.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.config.userdetails.MyUserDetails;
import ureca.ureca_miniproject2.like.repository.LikeRepository;
import ureca.ureca_miniproject2.like.service.LikeService;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.util.exception.custom.UnAuthorizedException;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import java.util.HashMap;
import java.util.Map;

import static ureca.ureca_miniproject2.util.response.FailureMessages.UNAUTHORIZED;

// 특정 게시글 id 좋아요 누르면 1 증가, 해당 사용자가 이미 좋아요를 누른 상태면 1 감소 토글 버튼
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggleLike(
            @PathVariable("postId") Integer postId,
            @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails == null) {
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        Integer userId = userDetails.getUserId();
        boolean isLiked = likeService.toggleLike(userId, postId);

        int likeCnt = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."))
                .getLikeCnt();

        Map<String, Object> result = new HashMap<>();
        result.put("isLiked", isLiked);
        result.put("likeCnt", likeCnt);

        return ApiResponse.success(SuccessMessages.LIKE_TOGGLE, result);
    }


    // 현재 게시글 좋아요 상태
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> isLiked(
            @PathVariable("postId") Integer postId,
            @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails == null) {
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        boolean isLiked = likeRepository.existsByUserIdAndPostId(userDetails.getUserId(), postId);

        Map<String, Boolean> result = new HashMap<>();
        result.put("isLiked", isLiked);

        return ApiResponse.success(SuccessMessages.LIKE_TOGGLE, result);
    }





}
