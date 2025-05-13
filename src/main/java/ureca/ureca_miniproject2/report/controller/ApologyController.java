package ureca.ureca_miniproject2.report.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ureca.ureca_miniproject2.config.userdetails.MyUserDetails;
import ureca.ureca_miniproject2.report.dto.ApologyRequest;
import ureca.ureca_miniproject2.report.service.ApologyService;
import ureca.ureca_miniproject2.util.response.ApiResponse;

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

}
