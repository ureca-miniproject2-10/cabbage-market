package ureca.ureca_miniproject2.report.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.post.entity.Apology;
import ureca.ureca_miniproject2.post.entity.ApologyState;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.report.dto.ApologyResponse;
import ureca.ureca_miniproject2.report.repository.ApologyRepository;
import ureca.ureca_miniproject2.report.repository.ReportRepository;
import ureca.ureca_miniproject2.util.exception.custom.ForbiddenException;
import ureca.ureca_miniproject2.util.exception.custom.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@Service
@RequiredArgsConstructor
public class ApologyService {
    private final PostRepository postRepository;
    private final ApologyRepository apologyRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void submitApology(Integer postId, String content, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

        // 게시글 작성자만 반성문 제출 가능
        if (!post.getUser().getUserId().equals(userId)) {
            throw new ForbiddenException(APOLOGY_FORBIDDEN.getMessage());
        }


        Optional<Apology> existingLetter = apologyRepository.findByPostPostIdAndState(post.getPostId(), ApologyState.PENDING);

        if (existingLetter.isPresent()) {
            throw new IllegalStateException("이미 검토 중인 반성문이 있습니다. 관리자의 검토 결과를 기다려주세요.");
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

        if (apology.getReviewedAt() != null) {
            throw new IllegalStateException("이미 처리된 반성문입니다. 현재 상태: " + apology.getState().getState());
        }

        Post post = apology.getPost();

        if (isAccepted) {
            // 게시글 제한 해제 및 신고 관련 데이터 초기화
            post.unrestrict();
            post.resetReportCount();

            // 게시글에 연관된 모든 신고 기록 삭제
            reportRepository.deleteAllByPost(post);

            // 승인된 반성문 삭제
            apologyRepository.delete(apology);

            postRepository.save(post);
        } else {
            apology.reject();
            apologyRepository.save(apology);
        }
    }

    @Transactional
    public List<ApologyResponse> getPendingApology() {
        List<Apology> apologies = apologyRepository.findByState(ApologyState.PENDING);
        return apologies.stream().map(ApologyResponse::from).toList();
    }

    @Transactional
    public ApologyResponse getApologyById(Integer id) {
        Apology apology = apologyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 반성문입니다."));

        return ApologyResponse.from(apology);
    }
}
