package ureca.ureca_miniproject2.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.service.PostService;
import ureca.ureca_miniproject2.util.gcs.GcsService;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final GcsService gcsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> findAllPosts() {
        return ApiResponse.success(SuccessMessages.POST_FIND_ALL, postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> findPost(@PathVariable("postId") Integer postId) {
        return ApiResponse.success(SuccessMessages.POST_FIND, postService.getPost(postId));
    }

    // 이미지가 없는 기존 JSON 만 받는 메서드
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostCreateRequest request) {
        return ApiResponse.success(SuccessMessages.POST_CREATE, postService.createPost(request));
    }

    // 이미지와 함께 게시글 생성 (Multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponse>> createPostWithImage(
            @RequestPart("request") PostCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // 이미지가 있으면 업로드하고 URL 설정
        if (image != null && !image.isEmpty()) {
            String fileName = gcsService.uploadFile(image);
            String imageUrl = gcsService.getFileUrl(fileName);

            // request 객체에 이미지 URL 설정 (record 타입이므로 새로 생성)
            request = new PostCreateRequest(
                    request.title(),
                    request.content(),
                    request.price(),
                    imageUrl,
                    request.userId()
            );
        }

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
