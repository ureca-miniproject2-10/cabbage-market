package ureca.ureca_miniproject2.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String role; // ROLE_USER , ROLE_ADMIN , ROLE_WARNING , 일반 유저 , 관리자 , 제한적인 유저

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserUserRole> userRoles = new ArrayList<>();

    public UserRole(String role) {
        this.role = role;
    }

}
