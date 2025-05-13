package ureca.ureca_miniproject2.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ureca.ureca_miniproject2.post.entity.Post;
import ureca.ureca_miniproject2.post.entity.Report;
import ureca.ureca_miniproject2.post.entity.key.ReportKey;
import ureca.ureca_miniproject2.post.repository.PostRepository;
import ureca.ureca_miniproject2.report.dto.ReportResponse;
import ureca.ureca_miniproject2.report.repository.ApologyRepository;
import ureca.ureca_miniproject2.report.repository.ReportRepository;
import ureca.ureca_miniproject2.user.entity.User;
import ureca.ureca_miniproject2.user.repository.UserRepository;
import ureca.ureca_miniproject2.util.exception.custom.ForbiddenException;
import ureca.ureca_miniproject2.util.exception.custom.IsAlreadyException;
import ureca.ureca_miniproject2.util.exception.custom.NotFoundException;
import ureca.ureca_miniproject2.util.response.FailureMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ApologyRepository apologyRepository;

    @Override
    public boolean hasReported(Integer userId, Integer postId) {
        return reportRepository.existsById(new ReportKey(userId, postId));
    }

    @Override
    public void reportPost(Integer userId, Integer postId, String content) {
        if (!hasReported(userId, postId)) {
            // User와 Post 엔티티 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage()));

            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND.getMessage()));

            // 연관된 엔티티를 포함하여 Report 생성
            Report report = new Report(user, post, content);
            reportRepository.save(report);
            post.incrementReport();

            if(post.shouldBeRestricted()) post.restrict();

            postRepository.save(post);
        } else {
            throw new IsAlreadyException(REPORT_DUPLICATED.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportResponse> getAllReports() {
        List<Report> reports = reportRepository.findAll();
        return reports.stream()
                .map(ReportResponse::from)
                .toList();
    }
}
