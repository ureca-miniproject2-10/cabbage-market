package ureca.ureca_miniproject2.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
