package ureca.ureca_miniproject2.report.controller;


// 특정 게시글의 신고하기 버튼을 통해 텍스트 입력 및 제출
// 한번 신고하면 비활성화 되도록

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.config.userdetails.MyUserDetails;
import ureca.ureca_miniproject2.report.dto.ReportRequestDto;
import ureca.ureca_miniproject2.report.service.ReportService;
import ureca.ureca_miniproject2.util.exception.custom.UnAuthorizedException;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import static ureca.ureca_miniproject2.util.response.FailureMessages.UNAUTHORIZED;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> reportPost(
            @PathVariable("postId") Integer postId,
            @RequestBody ReportRequestDto reportDto,
            @AuthenticationPrincipal MyUserDetails userDetails) {

            if(userDetails == null){
                throw new UnAuthorizedException(UNAUTHORIZED.getMessage());
            }
            Integer currentUserId = userDetails.getUserId();
            reportService.reportPost(currentUserId, postId, reportDto.getContent());

            return ApiResponse.success(SuccessMessages.REPORT_OK);
    }



}
