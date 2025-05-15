package ureca.ureca_miniproject2.user.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.user.dto.UserDto;
import ureca.ureca_miniproject2.user.dto.UserRequestDto;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.user.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 사용자 리스트 조회
    @GetMapping
    public ResponseEntity<List<UserDto>> getUserList() {
        List<UserDto> userList = userService.userList();
        return ResponseEntity.ok(userList);
    }

    // 사용자 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Integer id) {
        UserDto dto = userService.getUserById(id);
        return ResponseEntity.ok(dto);
    }


    // 사용자 수정
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Integer id, @RequestBody UserRequestDto userRequestDto) {
        UserDto dto = userService.updateUser(id,userRequestDto);
        return ResponseEntity.ok(dto);
    }

    // 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
