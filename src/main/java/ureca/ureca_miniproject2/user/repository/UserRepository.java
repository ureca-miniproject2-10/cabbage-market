package ureca.ureca_miniproject2.user.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ureca.ureca_miniproject2.user.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles r WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

}
