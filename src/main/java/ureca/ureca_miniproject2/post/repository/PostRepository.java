package ureca.ureca_miniproject2.post.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
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

    Page<Post> findByTitleContainingIgnoreCaseAndPriceBetween(
            String keyword, int minPrice, int maxPrice, Pageable pageable
    );

    Page<Post> findByTitleContainingIgnoreCaseAndPriceBetweenAndState(
            String keyword, int minPrice, int maxPrice, String state, Pageable pageable
    );


    List<Post> findByState(String postState);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p from Post p where p.postId = :postId")
    Optional<Post> findByIdWithPessimisticLock(@Param("postId") Integer postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCnt = p.likeCnt+ 1 WHERE p.postId = :postId")
    void incrementLikeCount(@Param("postId") Integer postId);

    @Modifying
    @Query("UPDATE Post p SET p.likeCnt = p.likeCnt- 1 WHERE p.postId = :postId")
    void decrementLikeCount(@Param("postId") Integer postId);

    @Query(
            value = "SELECT p FROM Post p JOIN FETCH p.user",
            countQuery = "SELECT COUNT(p) FROM Post p"
    )
    Page<Post> findAllWithUser(Pageable pageable);



}
