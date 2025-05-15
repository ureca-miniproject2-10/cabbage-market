package ureca.ureca_miniproject2.post.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ureca.ureca_miniproject2.config.userdetails.MyUserDetails;
import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostDetailResponse;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.post.service.PostService;
import ureca.ureca_miniproject2.util.exception.custom.ForbiddenException;
import ureca.ureca_miniproject2.util.image.ImageService;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import java.io.IOException;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> findAllPosts(
            @RequestParam(name="pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name="pageSize",defaultValue = "10") int pageSize
    ) {
        Page<PostResponse> postPage = postService.getAllPosts(pageNumber, pageSize);
        return ApiResponse.success(SuccessMessages.POST_FIND_ALL, postPage);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponse>> findPost(@PathVariable("postId") Integer postId, @AuthenticationPrincipal MyUserDetails userDetails) {

        PostDetailResponse post = postService.getPost(postId);

        // 제한된 게시글 처리
        if (post.state() == PostState.RESTRICT) {
            // 작성자가 아닌 경우 접근 거부
            if (userDetails == null || !hasAdminRole(userDetails) && !post.userSimpleDto().userId().equals(userDetails.getUserId())) {
                throw new ForbiddenException(ACCESS_DENY.getMessage());
            }

            // 작성자인 경우 반성문 제출 안내 메시지 반환
            if(!hasAdminRole(userDetails) && post.userSimpleDto().userId().equals(userDetails.getUserId()))
                return ApiResponse.success(SuccessMessages.REQUIRE_APOLOGY);
        }

        // 일반 게시글은 기존대로 처리
        return ApiResponse.success(SuccessMessages.POST_FIND, post);
    }


    // 이미지와 함께 게시글 생성 (Multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostDetailResponse>> createPostWithImage(
            @RequestPart("request") PostCreateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {

        // 이미지가 있으면 업로드하고 URL 설정
        String imageUrl = request.imageUrl();
        if (image != null && !image.isEmpty()) {
            String fileName = imageService.uploadFile(image);
            imageUrl = imageService.getFileUrl(fileName);
        }

        // 인증된 사용자 ID로 요청 객체 생성
        request = new PostCreateRequest(
                request.title(),
                request.content(),
                request.price(),
                imageUrl
        );

        return ApiResponse.success(SuccessMessages.POST_CREATE, postService.createPost(request, userDetails.getUserId()));
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostDetailResponse>> updatePost(
            @PathVariable("postId") Integer postId,
            @RequestPart("request") PostUpdateRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {

        PostDetailResponse post = postService.getPost(postId);

        if (!isPostAuthor(post, userDetails.getUserId())) {
            throw new ForbiddenException(POST_FORBIDDEN.getMessage());
        }

        // 제한된 게시글은 수정 불가
        if (post.state() == PostState.RESTRICT) {
            throw new ForbiddenException(UPDATE_DENY.getMessage());
        }

        String imageUrl = request.imageUrl();
        if (image != null && !image.isEmpty()) {
            //기존 이미지가 있으면 삭제
            if (imageUrl != null && !imageUrl.isEmpty()) {
                String fileName = imageService.extractFileNameFromUrl(imageUrl);
                imageService.deleteFile(fileName);
            }

            //새로운 이미지 업로드
            String newFileName = imageService.uploadFile(image);
            imageUrl = imageService.getFileUrl(newFileName);
        }

        // PostUpdateRequest 객체 업데이트
        PostUpdateRequest updatedRequest = new PostUpdateRequest(
                request.title(),
                request.content(),
                request.price(),
                imageUrl,
                request.state()
        );

        return ApiResponse.success(SuccessMessages.POST_UPDATE, postService.updatePost(postId, updatedRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable("postId") Integer postId, @AuthenticationPrincipal MyUserDetails userDetails) {
        PostDetailResponse postResponse = postService.getPost(postId);
        String imageUrl = postResponse.imageUrl();

        if (!isPostAuthor(postResponse, userDetails.getUserId())) {
            throw new ForbiddenException(POST_FORBIDDEN.getMessage());
        }

        //이미지가 존재하면 삭제
        if(imageUrl != null && !imageUrl.isEmpty()){
            String fileName = imageService.extractFileNameFromUrl(imageUrl);
            imageService.deleteFile(fileName);
        }

        postService.deletePost(postId);
        return ApiResponse.success(SuccessMessages.POST_DELETE);
    }

    private boolean isPostAuthor(PostDetailResponse post, Integer userId) {
        return post.userSimpleDto().userId().equals(userId);
    }

    @PostMapping("/{postId}/view")
    public ResponseEntity<ApiResponse<Void>> increaseView(@PathVariable("postId") Integer postId, HttpServletRequest request) {
            postService.increaseViewCount(postId, request);
            return ApiResponse.success(SuccessMessages.POST_VIEW);
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PostResponse>>> searchPosts(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "minPrice", required = false) Integer minPrice,
            @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(name = "state", required = false) PostState state  // ✅ enum 받기
    ) {
        String safeKeyword = (keyword != null) ? keyword : "";
        Page<PostResponse> postPage = postService.searchPostsByTitle(safeKeyword, pageNumber, pageSize, minPrice, maxPrice, state);
        return ApiResponse.success(SuccessMessages.POST_FIND_ALL, postPage);
    }

    private boolean hasAdminRole(MyUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }
}
