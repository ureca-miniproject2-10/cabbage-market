package ureca.ureca_miniproject2.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserUserRole> role = new ArrayList<>();


}


