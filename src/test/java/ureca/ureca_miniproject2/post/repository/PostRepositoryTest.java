package ureca.ureca_miniproject2.post.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.user.entity.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("게시글 생성 테스트")
    @Test
    void savePost() {
        // given
        User user = User.builder()
                .name("testUser")
                .password("1234")
                .roles(Collections.emptyList())
                .build();

        // TestEntityManager를 사용하여 User 엔티티 저장
        User savedUser = entityManager.persist(user);

        Post post = Post.builder()
                .title("test1")
                .content("test1")
                .price(1000)
                .imageUrl("local")
                .createdAt(LocalDateTime.now())
                .state(PostState.SALE)
                .user(savedUser)
                .build();

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertThat(savedPost.getPostId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getContent()).isEqualTo(post.getContent());
    }

    @DisplayName("게시글 조회 테스트")
    @Test
    void findPost() {
        // given
        User user = User.builder()
                .name("testUser")
                .password("1234")
                .roles(Collections.emptyList())
                .build();

        User savedUser = entityManager.persist(user);

        Post post = Post.builder()
                .title("test1")
                .content("test1")
                .price(1000)
                .imageUrl("local")
                .createdAt(LocalDateTime.now())
                .state(PostState.SALE)
                .user(savedUser)
                .build();

        Post savedPost = entityManager.persist(post);

        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();

        // when
        Optional<Post> foundPost = postRepository.findById(savedPost.getPostId());

        // then
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo("test1");
    }

    @DisplayName("전체 게시글 조회 테스트")
    @Test
    void findAllPosts() {
        // given
        User user = User.builder()
                .name("testUser")
                .password("1234")
                .roles(Collections.emptyList())
                .build();

        User savedUser = entityManager.persist(user);

        Post post1 = Post.builder()
                .title("게시글1")
                .content("내용1")
                .price(1000)
                .imageUrl("local")
                .createdAt(LocalDateTime.now())
                .state(PostState.SALE)
                .user(savedUser)
                .build();

        Post post2 = Post.builder()
                .title("게시글2")
                .content("내용2")
                .price(2000)
                .imageUrl("local")
                .createdAt(LocalDateTime.now())
                .state(PostState.SALE)
                .user(savedUser)
                .build();

        entityManager.persist(post1);
        entityManager.persist(post2);

        entityManager.flush();
        entityManager.clear();

        // when
        List<Post> posts = postRepository.findAll();

        // then
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting("title").containsExactlyInAnyOrder("게시글1", "게시글2");
    }

    @DisplayName("게시글 삭제 테스트")
    @Test
    void deletePost() {
        // given
        User user = User.builder()
                .name("testUser")
                .password("1234")
                .roles(Collections.emptyList())
                .build();

        User savedUser = entityManager.persist(user);

        Post post = Post.builder()
                .title("삭제할 게시글")
                .content("내용")
                .price(1000)
                .imageUrl("local")
                .createdAt(LocalDateTime.now())
                .state(PostState.SALE)
                .user(savedUser)
                .build();

        Post savedPost = entityManager.persist(post);
        Integer postId = savedPost.getPostId();

        // when
        postRepository.deleteById(postId);

        // then
        Optional<Post> deletedPost = postRepository.findById(postId);
        assertThat(deletedPost).isEmpty();
    }
}
