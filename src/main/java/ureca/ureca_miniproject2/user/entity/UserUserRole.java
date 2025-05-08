package ureca.ureca_miniproject2.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@IdClass(UserRoleKey.class)
public class UserUserRole {

    @Id
    private Integer userId;

    @Id
    private Integer roleId;

}
