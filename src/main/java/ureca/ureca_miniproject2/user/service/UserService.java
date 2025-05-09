package ureca.ureca_miniproject2.user.service;

import ureca.ureca_miniproject2.user.dto.UserDto;

import java.util.List;

public interface UserService {

    // 사용자 list 조회
    List<UserDto> userList();
    // 사용자 상세 조회 ( id )
    UserDto getUserByUsername(String username);
    // 사용자 등록

    // 사용자 수정

    // 사용자 삭제

}
