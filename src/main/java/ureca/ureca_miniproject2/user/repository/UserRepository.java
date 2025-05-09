package ureca.ureca_miniproject2.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
