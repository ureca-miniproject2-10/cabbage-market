package ureca.ureca_miniproject2.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.service.PostService;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> findAllPosts() {
        return ApiResponse.success(SuccessMessages.POST_FIND_ALL, postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> findPost(@PathVariable("postId") Integer postId) {
        return ApiResponse.success(SuccessMessages.POST_FIND, postService.getPost(postId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostCreateRequest request) {
        return ApiResponse.success(SuccessMessages.POST_CREATE, postService.createPost(request));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable("postId") Integer postId,
            @RequestBody PostUpdateRequest request) {
        return ApiResponse.success(SuccessMessages.POST_UPDATE, postService.updatePost(postId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable("postId") Integer postId) {
        postService.deletePost(postId);
        return ApiResponse.success(SuccessMessages.POST_DELETE);
    }
}
