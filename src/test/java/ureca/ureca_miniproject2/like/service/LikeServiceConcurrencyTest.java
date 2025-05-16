package ureca.ureca_miniproject2.like.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ureca.ureca_miniproject2.like.repository.LikeRepository;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LikeServiceConcurrencyTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    void 백명의_유저가_동시에_좋아요누르기() throws InterruptedException {
        // given
        // 테스트 게시글
        Post post = postRepository.save(Post.builder().title("test post").likeCnt(0).build());

        int numberOfUsers = 100;
        // 테스트 사용자 100명
        List<User> users = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++) {
            users.add(userRepository.save(User.builder().username("user" + i).build()));
        }

        ExecutorService executor = Executors.newFixedThreadPool(20); // 병렬 처리 준비
        CountDownLatch latch = new CountDownLatch(numberOfUsers);

        // 사용자마다 별도 스레드에서 likeService.toggleLike() 호출
        for (User user : users) {
            executor.submit(() -> {
                try {
                    likeService.toggleLike(user.getUserId(), post.getPostId());
                } catch (Exception e) {
                    e.printStackTrace(); // 예외 발생 확인용
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기

        // when
        Post updatedPost = postRepository.findById(post.getPostId()).orElseThrow();

        // then
        int likeCnt = updatedPost.getLikeCnt();
        System.out.println("최종 좋아요 수: " + likeCnt);
        assertThat(likeCnt).isEqualTo(numberOfUsers); // 100 개인지 확인
    }


}

