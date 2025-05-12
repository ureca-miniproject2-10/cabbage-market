package ureca.ureca_miniproject2.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
