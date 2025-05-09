package ureca.ureca_miniproject2.user.service;

import ureca.ureca_miniproject2.user.dto.UserDto;
import ureca.ureca_miniproject2.user.dto.UserRequestDto;

import java.util.List;

public interface UserService {

    // 사용자 list 조회
    List<UserDto> userList();
    // 사용자 상세 조회 ( id )
    UserDto getUserById(Integer userId);
    // 사용자 등록
    UserDto registerUser(UserRequestDto userRequestDto);
    // 사용자 수정
    UserDto updateUser(Integer userId, UserRequestDto userRequestDto);
    // 사용자 삭제
    void deleteUser(Integer userId);

}
