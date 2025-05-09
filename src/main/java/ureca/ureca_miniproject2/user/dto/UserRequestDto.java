package ureca.ureca_miniproject2.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String username;       // 아이디
    private String password;       // 비밀번호
    private String name;           // 이름
    private String profileImage;   // 프로필 이미지 (선택)
    private List<String> roles;    // 역할 목록
}
