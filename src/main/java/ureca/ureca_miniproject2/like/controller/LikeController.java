package ureca.ureca_miniproject2.like.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.like.service.LikeService;

// 특정 게시글 id 좋아요 누르면 1 증가, 해당 사용자가 이미 좋아요를 누른 상태면 1 감소 토글 버튼
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{userId}/{postId}")
    public ResponseEntity<String> toggleLike(@PathVariable("userId") Integer userId, @PathVariable("postId") Integer postId) {
        likeService.toggleLike(userId, postId);
        return ResponseEntity.ok("좋아요 상태가 변경되었습니다.");
    }

}
