package ureca.ureca_miniproject2.report.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.Report;
import ureca.ureca_miniproject2.post.entity.key.ReportKey;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.report.repository.ReportRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.util.exception.custom.IsAlreadyException;
import ureca.ureca_miniproject2.util.response.FailureMessages;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public boolean hasReported(Integer userId, Integer postId) {
        return reportRepository.existsById(new ReportKey(userId, postId));
    }

    @Override
    public void reportPost(Integer userId, Integer postId, String content) {
        if (!hasReported(userId, postId)) {
            // User와 Post 엔티티 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글입니다."));

            // 연관된 엔티티를 포함하여 Report 생성
            Report report = new Report(user, post, content);
            reportRepository.save(report);
            post.incrementReport();
            postRepository.save(post);
        } else {
            throw new IsAlreadyException(FailureMessages.REPORT_DUPLICATED.getMessage());
        }
    }


}
