package ureca.ureca_miniproject2.report.dto;

import ureca.ureca_miniproject2.post.entity.Report;
import java.time.LocalDateTime;

public record ReportResponse(
        String username,
        LocalDateTime createdAt,
        String content,
        Integer postId
) {
    public static ReportResponse from(Report report) {
        return new ReportResponse(
                report.getUser().getUsername(),
                report.getCreatedAt(),
                report.getContent(),
                report.getPost().getPostId()
        );
    }
}
