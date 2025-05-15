package ureca.ureca_miniproject2.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.post.entity.Apology;
import ureca.ureca_miniproject2.post.entity.ApologyState;

import java.util.List;
import java.util.Optional;

public interface ApologyRepository extends JpaRepository<Apology, Integer> {
    Optional<Apology> findByPostPostId(Integer postId);
    List<Apology> findByState(ApologyState apologyState);
    // 특정 게시글 ID와 상태로 반성문 찾기
    Optional<Apology> findByPostPostIdAndState(Integer postId, ApologyState state);
}
