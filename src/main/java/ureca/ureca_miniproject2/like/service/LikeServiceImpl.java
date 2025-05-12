package ureca.ureca_miniproject2.like.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional
    public void toggleLike(Integer userId, Integer postId) {
        LikeEntityKey likeKey = new LikeEntityKey(userId, postId);
        LikeEntity likeEntity = likeRepository.findById(likeKey).orElse(null);

        if (likeEntity == null) {
            // 좋아요 추가
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

            LikeEntity newLike = new LikeEntity(user, post);
            likeRepository.save(newLike);
            post.incrementLike();
            postRepository.save(post);
        } else {
            // 좋아요 취소
            likeRepository.delete(likeEntity);
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
            post.decrementLike();
            postRepository.save(post);
        }
    }

}
