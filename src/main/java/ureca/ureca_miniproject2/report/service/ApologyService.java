package ureca.ureca_miniproject2.report.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.post.entity.Apology;
import ureca.ureca_miniproject2.post.entity.ApologyState;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.report.repository.ApologyRepository;
import ureca.ureca_miniproject2.util.exception.custom.ForbiddenException;
import ureca.ureca_miniproject2.util.exception.custom.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@Service
@RequiredArgsConstructor
public class ApologyService {
    private final PostRepository postRepository;
    private final ApologyRepository apologyRepository;

    @Transactional
    public void submitApology(Integer postId, String content, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

        // 게시글 작성자만 반성문 제출 가능
        if (!post.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException(APOLOGY_FORBIDDEN.getMessage());
        }

        // 이미 반성문이 제출된 경우 확인
        Optional<Apology> existingLetter = apologyRepository.findByPost(post);
        if (existingLetter.isPresent()) {
            throw new IllegalStateException("이미 반성문이 제출되었습니다.");
        }

        // 반성문 생성 및 저장
        Apology apology = Apology.builder()
                .post(post)
                .content(content)
                .createdAt(LocalDateTime.now())
                .state(ApologyState.PENDING)
                .build();

        apologyRepository.save(apology);
    }

    @Transactional
    public void reviewApology(Integer apologyId, boolean isAccepted) {
        Apology apology = apologyRepository.findById(apologyId)
                .orElseThrow(() -> new NotFoundException(APOLOGY_NOT_FOUND.getMessage()));

        if (isAccepted) {
            apology.accept();
            // 게시글 제한 해제
            Post post = apology.getPost();
            post.unrestrict();
            postRepository.save(post);
        } else {
            apology.reject();
        }

        apologyRepository.save(apology);
    }

    public Page<Apology> getPendingReflectionLetters(Pageable pageable) {
        return apologyRepository.findByState(ApologyState.PENDING, pageable);
    }
}
