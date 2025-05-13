package ureca.ureca_miniproject2.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId; // pk

    private String username; // 아이디
    private String password; // 비밀번호
    private String profile_image; // 이미지 경로
    private String name; // 이름

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserUserRole> roles = new ArrayList<>();

    // 사용자 생성시, 기본 역할 ROLE_USER 부여
    public void addRole(UserRole userRole) {

        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        UserUserRole userUserRole = UserUserRole.builder()
                .user(this)
                .userRole(userRole)
                .userId(this.userId)
                .roleId(userRole.getRoleId())
                .build();
        this.roles.add(userUserRole);
    }

    public void updateName(String name) {
        this.name=name;
    }

    public void updateProfileImageUrl(String newImageUrl) {
        this.profile_image=newImageUrl;
    }
}


