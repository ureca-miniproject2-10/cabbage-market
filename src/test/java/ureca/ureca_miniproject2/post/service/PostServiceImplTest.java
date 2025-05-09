package ureca.ureca_miniproject2.post.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ureca.ureca_miniproject2.post.dto.PostCreateRequest;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class PostServiceImplTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private PostCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        // 테스트 사용자 생성 및 저장
        testUser = userRepository.save(User.builder()
                .username("testUser")
                .password("1234")
                .name("테스트 유저1")
                .build());

        userRepository.flush();

        // 게시글 생성 요청 객체 준비
        createRequest = new PostCreateRequest(
                "Test Title",
                "Test Content",
                10000,
                "test.jpg",
                testUser.getUserId()
        );
    }

    @Test
    @DisplayName("게시글 생성 통합 테스트")
    void createPostIntegrationTest() {
        // when
        PostResponse response = postService.createPost(createRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("Test Title");
        assertThat(response.content()).isEqualTo("Test Content");

        // 실제로 저장되었는지 확인
        Post savedPost = postRepository.findById(response.postId()).orElse(null);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo("Test Title");
    }

    @Test
    @DisplayName("게시글 수정 통합 테스트")
    void updatePostIntegrationTest() {
        // given
        PostResponse created = postService.createPost(createRequest);

        PostUpdateRequest updateRequest = new PostUpdateRequest(
                "Updated Title",
                "Updated Content",
                20000,
                "updated.jpg",
                PostState.RESTRICT
        );

        // when
        PostResponse updated = postService.updatePost(created.postId(), updateRequest);

        // then
        assertThat(updated.title()).isEqualTo("Updated Title");
        assertThat(updated.content()).isEqualTo("Updated Content");
        assertThat(updated.price()).isEqualTo(20000);
    }

    @Test
    @DisplayName("게시글 삭제 통합 테스트")
    void deletePostIntegrationTest() {
        // given
        PostResponse created = postService.createPost(createRequest);
        Integer postId = created.postId();

        // when
        postService.deletePost(postId);

        // then
        assertThat(postRepository.findById(postId)).isEmpty();
    }
}