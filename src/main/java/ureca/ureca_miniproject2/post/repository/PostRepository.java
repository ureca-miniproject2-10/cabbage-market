package ureca.ureca_miniproject2.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.postId = :postId")
    Optional<Post> findByIdFetchComment(@Param("postId") Integer postId);

    List<Post> findByUserOrderByCreatedAtDesc(User user);

    List<Post> findByState(PostState postState);
}
