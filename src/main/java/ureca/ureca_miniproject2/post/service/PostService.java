package ureca.ureca_miniproject2.post.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostDetailResponse;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;

import java.awt.print.Pageable;
import java.util.List;

public interface PostService {

    // 게시글 등록
    PostDetailResponse createPost(PostCreateRequest request, Integer userId);

    // 게시글 전체조회
    Page<PostResponse> getAllPosts(int pageNumber, int pageSize);

    // 게시글 상세조회
    PostDetailResponse getPost(Integer postId);

    // 게시글 수정
    PostDetailResponse updatePost(Integer postId, PostUpdateRequest request);

    // 게시글 삭제
    void deletePost(Integer postId);

    // Ip 기반 조회수 증가
    void increaseViewCount(Integer postId, HttpServletRequest request);


}
