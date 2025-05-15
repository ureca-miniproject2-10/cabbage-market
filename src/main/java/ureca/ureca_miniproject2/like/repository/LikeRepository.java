package ureca.ureca_miniproject2.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.post.entity.LikeEntity;
import ureca.ureca_miniproject2.post.entity.key.LikeEntityKey;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, LikeEntityKey> {
    Optional<LikeEntity> findByUserIdAndPostId(Integer userId, Integer postId);

    boolean existsByUserIdAndPostId(Integer userId, Integer postId);


}