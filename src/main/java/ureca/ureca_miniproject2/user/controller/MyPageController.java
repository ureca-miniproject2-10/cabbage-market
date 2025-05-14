package ureca.ureca_miniproject2.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ureca.ureca_miniproject2.config.userdetails.MyUserDetails;
import ureca.ureca_miniproject2.user.dto.MyPageDto;
import ureca.ureca_miniproject2.user.dto.ProfileUpdateRequest;
import ureca.ureca_miniproject2.user.service.MyPageService;
import ureca.ureca_miniproject2.util.exception.custom.UnAuthorizedException;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import java.io.IOException;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<MyPageDto>> getMyPage(@AuthenticationPrincipal MyUserDetails userDetails) {
        if (userDetails == null) {
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        Integer userId = userDetails.getUserId();
        MyPageDto myPageDto = myPageService.getMyPageInfo(userId);

        return ApiResponse.success(SuccessMessages.USER_MYPAGE_FIND, myPageDto);
    }

    // 이름 변경
    @PutMapping("/mypage/profile")
    public ResponseEntity<ApiResponse<MyPageDto>> updateProfile(
            @RequestBody ProfileUpdateRequest request,
            @AuthenticationPrincipal MyUserDetails userDetails) {

        if (userDetails == null) {
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        Integer userId = userDetails.getUserId();
        MyPageDto updatedProfile = myPageService.updateProfile(userId, request);

        return ApiResponse.success(SuccessMessages.USER_PROFILE_UPDATE, updatedProfile);
    }

    @PutMapping(value = "/mypage/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<MyPageDto>> updateProfileImage(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal MyUserDetails userDetails) throws IOException {

        if (userDetails == null) {
            throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
        }

        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("프로필 이미지가 제공되지 않았습니다.");
        }

        Integer userId = userDetails.getUserId();
        MyPageDto updatedProfile = myPageService.updateProfileImage(userId, image);

        return ApiResponse.success(SuccessMessages.USER_PROFILE_IMAGE_UPDATE, updatedProfile);
    }
}
