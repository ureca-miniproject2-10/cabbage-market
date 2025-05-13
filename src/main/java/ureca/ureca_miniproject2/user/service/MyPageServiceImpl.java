package ureca.ureca_miniproject2.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.user.dto.MyPageDto;
import ureca.ureca_miniproject2.user.dto.ProfileUpdateRequest;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.util.exception.custom.NotFoundException;
import ureca.ureca_miniproject2.util.image.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static ureca.ureca_miniproject2.util.response.FailureMessages.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageService imageService;

    @Override
    public MyPageDto getMyPageInfo(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

        List<Post> userPosts = postRepository.findByUserOrderByCreatedAtDesc(user);
        List<PostResponse> postResponses = userPosts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        return MyPageDto.of(
                user.getUsername(),
                user.getName(),
                user.getProfile_image(),
                postResponses
        );
    }

    @Override
    @Transactional
    public MyPageDto updateProfile(Integer userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

        // 사용자 이름 업데이트
        user.updateName(request.name());

        // 업데이트된 사용자 정보로 MyPageDto 반환
        List<Post> userPosts = postRepository.findByUserOrderByCreatedAtDesc(user);
        List<PostResponse> postResponses = userPosts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        return MyPageDto.of(
                user.getUsername(),
                user.getName(),
                user.getProfile_image(),
                postResponses
        );
    }

    @Override
    @Transactional
    public MyPageDto updateProfileImage(Integer userId, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

        // 기존 프로필 이미지가 있으면 삭제
        String currentImageUrl = user.getProfile_image();
        if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
            try {
                String fileName = imageService.extractFileNameFromUrl(currentImageUrl);
                imageService.deleteFile(fileName);
            } catch (Exception e) {
                // 이미지 삭제 실패 로그 기록
                System.err.println("기존 프로필 이미지 삭제 실패: " + e.getMessage());
                // 삭제 실패해도 계속 진행
            }
        }

        // 새 이미지 업로드
        String newFileName = imageService.uploadFile(image);
        String newImageUrl = imageService.getFileUrl(newFileName);

        // 사용자 프로필 이미지 URL 업데이트
        user.updateProfileImageUrl(newImageUrl);

        // 업데이트된 사용자 정보로 MyPageDto 반환
        List<Post> userPosts = postRepository.findByUserOrderByCreatedAtDesc(user);
        List<PostResponse> postResponses = userPosts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());

        return MyPageDto.of(
                user.getUsername(),
                user.getName(),
                user.getProfile_image(),
                postResponses
        );
    }
}
