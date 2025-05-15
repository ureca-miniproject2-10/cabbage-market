package ureca.ureca_miniproject2.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.entity.Apology;
import ureca.ureca_miniproject2.post.service.PostService;
import ureca.ureca_miniproject2.report.dto.ApologyResponse;
import ureca.ureca_miniproject2.report.dto.ReportResponse;
import ureca.ureca_miniproject2.report.service.ApologyService;
import ureca.ureca_miniproject2.report.service.ReportService;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final PostService postService;
    private final ApologyService apologyService;
    private final ReportService reportService;

    // 제한된 게시글 목록
    @GetMapping("/posts/restricted")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getRestrictedPosts() {
        List<PostResponse> restrictedPost = postService.findRestrictedPost();
        return ApiResponse.success(SuccessMessages.POST_FIND_ALL, restrictedPost);
    }

    // 반성문 목록 조회
    @GetMapping("/apologies")
    public ResponseEntity<ApiResponse<List<?>>> getApologies() {
        return ApiResponse.success(SuccessMessages.APOLOGY_READ, apologyService.getPendingApology());
    }

    // 신고 내역 조회
    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReports() {
        List<ReportResponse> allReports = reportService.getAllReports();
        return ApiResponse.success(SuccessMessages.REPORT_READ, allReports);
    }


    // 반성문 상세 조회
    @GetMapping("/apologies/{id}")
    public ResponseEntity<ApiResponse<ApologyResponse>> getApology(@PathVariable("id") Integer id) {
        ApologyResponse apologyById = apologyService.getApologyById(id);
        return ApiResponse.success(SuccessMessages.APOLOGY_READ, apologyById);
    }


    // 반성문 검토 처리 (승인 또는 거절)
    @PostMapping("/apologies/{id}/review")
    public ResponseEntity<ApiResponse<Void>> reviewApology(
            @PathVariable("id") Integer id,
            @RequestParam("accept") boolean accept) {
        apologyService.reviewApology(id, accept);
        return ApiResponse.success(SuccessMessages.APPEAL_REVIEW);
    }
}
