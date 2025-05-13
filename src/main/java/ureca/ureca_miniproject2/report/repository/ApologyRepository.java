package ureca.ureca_miniproject2.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.post.entity.Apology;
import ureca.ureca_miniproject2.post.entity.ApologyState;
import ureca.ureca_miniproject2.post.entity.Post;

import java.util.Optional;

public interface ApologyRepository extends JpaRepository<Apology, Integer> {
    Optional<Apology> findByPost(Post post);

    Page<Apology> findByState(ApologyState apologyState, Pageable pageable);
}
