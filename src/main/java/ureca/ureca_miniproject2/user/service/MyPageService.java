package ureca.ureca_miniproject2.user.service;

import org.springframework.web.multipart.MultipartFile;
import ureca.ureca_miniproject2.user.dto.MyPageDto;
import ureca.ureca_miniproject2.user.dto.ProfileUpdateRequest;

import java.io.IOException;

public interface MyPageService {
    MyPageDto getMyPageInfo(Integer userId);
    MyPageDto updateProfile(Integer userId, ProfileUpdateRequest request);
    MyPageDto updateProfileImage(Integer userId, MultipartFile image) throws IOException;
    void resetToDefaultProfileImage(Integer userId);

}
