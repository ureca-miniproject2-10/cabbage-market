package ureca.ureca_miniproject2.post.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostDetailResponse;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.util.client.ClientIpUtil;
import ureca.ureca_miniproject2.util.exception.custom.DuplicatedViewException;
import ureca.ureca_miniproject2.util.exception.custom.NotFoundException;
import ureca.ureca_miniproject2.util.response.FailureMessages;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Override
    public PostDetailResponse createPost(PostCreateRequest request, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .likeCnt(0)
                .viewCnt(0)
                .reportCnt(0)
                .createdAt(LocalDateTime.now())
                .state(PostState.SALE)
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        return PostDetailResponse.from(savedPost);
    }

    @Override
    public Page<PostResponse> getAllPosts(int pageNumber, int pageSize) {
        // 페이지 번호는 0부터 시작
        int correctedPageNumber = Math.max(0, pageNumber);

        // Pageable 객체 생성
        PageRequest pageable = PageRequest.of(correctedPageNumber, pageSize);
        return postRepository.findAll(pageable)
                .map(PostResponse::from);
    }

    @Override
    public PostDetailResponse getPost(Integer postId) {
        Post post = postRepository.findByIdFetchComment(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND.getMessage()));

        return PostDetailResponse.from(post);
    }

    @Override
    public PostDetailResponse updatePost(Integer postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND.getMessage()));

        post.update(request.title(),
                request.content(),
                request.price(),
                request.imageUrl(),
                request.state());

        return PostDetailResponse.from(post);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));
        postRepository.delete(post);
    }


    // 조회수 증가
    @Override
    public void increaseViewCount(Integer postId, HttpServletRequest request) {
        String clientIp = ClientIpUtil.getClientIp(request);
        String cacheKey = postId + ":" + clientIp;

        Cache cache = cacheManager.getCache("postViews");
        if (cache == null) {
            throw new IllegalStateException("CacheManager 설정이 잘못되었습니다.");
        }
        // 이미 조회한 경우 예외 던지기
        if (cache.get(cacheKey) != null) {
            throw new DuplicatedViewException(POST_DUPLICATED_VIEW.getMessage());
        }

        // Post 엔티티 조회
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.incrementView(); // 조회수 증가
            postRepository.save(post); // DB 저장
            cache.put(cacheKey, true); // 캐시에 기록
            System.out.println("조회수 증가 - Post ID: " + postId + ", IP: " + clientIp);
        } else {
            throw new NotFoundException(POST_NOT_FOUND.getMessage());
        }
    }


    // 제목으로 게시글 검색
    @Override
    public Page<PostResponse> searchPostsByTitle(String keyword, int pageNumber, int pageSize) {
        int correctedPageNumber = Math.max(0, pageNumber);
        PageRequest pageable = PageRequest.of(correctedPageNumber, pageSize);

        return postRepository.findByTitleContainingIgnoreCase(keyword, pageable)
                .map(PostResponse::from);
    }




    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> findRestrictedPost() {
        List<Post> posts = postRepository.findByState(PostState.RESTRICT);
        return posts.stream().map(PostResponse::from).toList();
    }
}
