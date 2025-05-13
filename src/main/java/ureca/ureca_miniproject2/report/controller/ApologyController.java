package ureca.ureca_miniproject2.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.config.MyUserDetails;
import ureca.ureca_miniproject2.post.entity.Apology;
import ureca.ureca_miniproject2.report.dto.ApologyRequest;
import ureca.ureca_miniproject2.report.dto.ApologyResponse;
import ureca.ureca_miniproject2.report.dto.ApologyReviewRequest;
import ureca.ureca_miniproject2.report.service.ApologyService;
import ureca.ureca_miniproject2.util.response.ApiResponse;
import ureca.ureca_miniproject2.util.response.SuccessMessages;

import static ureca.ureca_miniproject2.util.response.SuccessMessages.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/apology")
public class ApologyController {
    private final ApologyService apologyService;

    @PostMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> submitApology(
            @PathVariable("postId") Integer postId,
            @RequestBody ApologyRequest request,
            @AuthenticationPrincipal MyUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        apologyService.submitApology(postId, request.getContent(), userId);
        return ApiResponse.success(APOLOGY_CREATE);
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<Page<ApologyResponse>>> getPendingReflectionLetters(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Apology> letters = apologyService.getPendingReflectionLetters(pageable);
        Page<ApologyResponse> responses = letters.map(ApologyResponse::from);
        return ApiResponse.success(APOLOGY_READ, responses);
    }

    @PatchMapping("/{letterId}/review")
    public ResponseEntity<Void> reviewReflectionLetter(
            @PathVariable("letterId") Integer letterId,
            @RequestBody ApologyReviewRequest request) {
        apologyService.reviewApology(letterId, request.isAccepted());
        return ResponseEntity.ok().build();
    }
}
