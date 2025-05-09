package ureca.ureca_miniproject2.post.service;

import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;

import java.util.List;

public interface PostService {

    // 게시글 등록
    PostResponse createPost(PostCreateRequest request);

    // 게시글 전체조회
    List<PostResponse> getAllPosts();

    // 게시글 상세조회
    PostResponse getPost(Integer postId);

    // 게시글 수정
    PostResponse updatePost(Integer postId, PostUpdateRequest request);

    // 게시글 삭제
    void deletePost(Integer postId);
}
