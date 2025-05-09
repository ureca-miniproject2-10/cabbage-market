package ureca.ureca_miniproject2.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Integer userId;        // PK
    private String username;       // 아이디
    private String name;           // 이름
    private String profileImage;   // 이미지 경로
    private List<String> roles;    // 역할 목록
}
