package ureca.ureca_miniproject2.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ureca.ureca_miniproject2.user.entity.UserRole;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByRole(String role);
}
