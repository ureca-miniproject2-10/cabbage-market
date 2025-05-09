package ureca.ureca_miniproject2.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ureca.ureca_miniproject2.user.entity.key.UserRoleKey;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserRoleKey.class)
public class UserUserRole {

    @Id
    private Integer userId;

    @Id
    private Integer roleId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @MapsId("roleId")
    @JoinColumn(name="role_id")
    private UserRole userRole;
}
