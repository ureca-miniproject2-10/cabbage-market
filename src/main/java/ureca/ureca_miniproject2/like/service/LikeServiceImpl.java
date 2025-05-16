package ureca.ureca_miniproject2.like.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.like.repository.LikeRepository;
import ureca.ureca_miniproject2.post.entity.LikeEntity;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.key.LikeEntityKey;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 1. 비관적 락 적용
     */
//    @Override
//    @Transactional
//    public boolean toggleLike(Integer userId, Integer postId) {
//        LikeEntityKey likeKey = new LikeEntityKey(userId, postId);
//
//        Post post = postRepository.findByIdWithPessimisticLock(postId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
//
//        LikeEntity likeEntity = likeRepository.findById(likeKey).orElse(null);
//
//
//        if (likeEntity == null) {
//            // 좋아요 추가
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
//            LikeEntity newLike = new LikeEntity(user, post);
//            likeRepository.save(newLike);
//            post.incrementLike();
//
//            return true; // 좋아요된 상태
//        } else {
//            // 좋아요 취소
//            likeRepository.delete(likeEntity);
//            post.decrementLike();
//
//            return false; // 좋아요 취소된 상태
//        }
//    }

    /**
     * 2. 원자적 업데이트
     */
    @Override
    @Transactional
    public boolean toggleLike(Integer userId, Integer postId) {
        LikeEntityKey likeKey = new LikeEntityKey(userId, postId);
        LikeEntity likeEntity = likeRepository.findById(likeKey).orElse(null);

        if (likeEntity == null) {
            // 좋아요 추가
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

            LikeEntity newLike = new LikeEntity(user, post);
            likeRepository.save(newLike);
            postRepository.incrementLikeCount(postId);
            System.out.println("현재 좋아요 수 " + post.getLikeCnt());

            return true;
        } else {
            // 좋아요 취소
            likeRepository.delete(likeEntity);
            postRepository.decrementLikeCount(postId);

            return false;
        }
    }

//    @Override
//    @Transactional
//    public boolean toggleLike(Integer userId, Integer postId) {
//        LikeEntityKey likeKey = new LikeEntityKey(userId, postId);
//
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
//
//        LikeEntity likeEntity = likeRepository.findById(likeKey).orElse(null);
//
//        if (likeEntity == null) {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
//            LikeEntity newLike = new LikeEntity(user, post);
//            likeRepository.save(newLike);
//            post.incrementLike();
//        } else {
//            likeRepository.delete(likeEntity);
//            post.decrementLike();
//        }
//
//        // 낙관적 락을 적용하기 위해 저장 시 version 비교
//        postRepository.save(post); // 충돌 시 OptimisticLockingFailureException 발생
//
//        return likeEntity == null;
//    }
//
//    public boolean toggleLikeWithRetry(Integer userId, Integer postId) {
//        int maxRetries = 5; // 재시도 횟수 감소
//        for (int i = 0; i < maxRetries; i++) {
//            try {
//                return toggleLike(userId, postId);
//            } catch (ObjectOptimisticLockingFailureException e) {
//                if (i == maxRetries - 1) {
//                    throw e;
//                }
//                try {
//                    // 지수 백오프 방식으로 대기 시간 증가
//                    Thread.sleep((long) (Math.pow(2, i) * 50));
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                    throw new RuntimeException("좋아요 처리 중 인터럽트 발생", ie);
//                }
//            }
//        }
//        throw new IllegalStateException("낙관적 락 충돌로 인해 좋아요 토글 실패");
//    }

}
