package ureca.ureca_miniproject2.post.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import ureca.ureca_miniproject2.comment.dto.UserSimpleDto;
import ureca.ureca_miniproject2.config.MyUserDetails;
import ureca.ureca_miniproject2.post.dto.PostDetailResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.post.service.PostService;
import ureca.ureca_miniproject2.util.image.ImageService;
import ureca.ureca_miniproject2.util.response.ApiResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private ImageService imageService;

    @Mock
    private MyUserDetails userDetails;

    @InjectMocks
    private PostController postController;

    @Test
    @DisplayName("기존 이미지가 있는 게시글에 새 이미지로 수정 시 GCS에서 기존 이미지 삭제 및 새 이미지 업로드 확인")
    void updatePost_ReplaceExistingImage() throws IOException {
        // Given
        Integer postId = 1;
        Integer userId = 1;
        String originalImageUrl = "https://storage.googleapis.com/test-bucket/original-image-20240512.jpg";
        String originalFileName = "original-image-20240512.jpg";

        String newFileName = "new-image-20240512.jpg";
        String newImageUrl = "https://storage.googleapis.com/test-bucket/new-image-20240512.jpg";

        PostUpdateRequest request = new PostUpdateRequest(
                "수정된 제목",
                "수정된 내용",
                15000,
                originalImageUrl, // 기존 이미지 URL
                PostState.SALE
        );

        MockMultipartFile newImage = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "새 이미지 데이터".getBytes()
        );

        PostDetailResponse existingPost = new PostDetailResponse(
                postId,
                "원래 제목",
                "원래 내용",
                10000,
                originalImageUrl,
                0,
                0,
                0,
                LocalDateTime.now(),
                PostState.SALE,
                UserSimpleDto.builder().userId(userId).username("testUser").build(),
                new ArrayList<>()
        );

        PostDetailResponse expectedResponse = new PostDetailResponse(
                postId,
                "수정된 제목",
                "수정된 내용",
                15000,
                newImageUrl, // 새 이미지 URL이 반환됨
                0,
                0,
                0,
                LocalDateTime.now(),
                PostState.SALE,
                UserSimpleDto.builder().userId(userId).username("testUser").build(),
                new ArrayList<>()
        );

        // Mock 설정
        when(userDetails.getUserId()).thenReturn(userId);
        when(postService.getPost(postId)).thenReturn(existingPost);
        when(imageService.extractFileNameFromUrl(originalImageUrl)).thenReturn(originalFileName);
        when(imageService.uploadFile(newImage)).thenReturn(newFileName); // 새 이미지 업로드 성공
        when(imageService.getFileUrl(newFileName)).thenReturn(newImageUrl);
        when(postService.updatePost(eq(postId), any(PostUpdateRequest.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponse<PostDetailResponse>> response = postController.updatePost(
                postId,
                request,
                newImage,
                userDetails
        );

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(newImageUrl, response.getBody().getData().imageUrl());

        // 이미지 서비스의 메소드 호출 검증 - 순서대로
        verify(imageService).extractFileNameFromUrl(originalImageUrl); // 1. URL에서 파일명 추출
        verify(imageService).deleteFile(originalFileName); // 2. 기존 이미지 삭제
        verify(imageService).uploadFile(newImage); // 3. 새 이미지 업로드
        verify(imageService).getFileUrl(newFileName); // 4. 새 이미지 URL 생성

        // PostService 호출 검증 - 올바른 이미지 URL을 포함한 요청으로 호출되었는지
        verify(postService).updatePost(eq(postId), argThat(req ->
                req.imageUrl().equals(newImageUrl) &&
                        req.title().equals("수정된 제목") &&
                        req.content().equals("수정된 내용")
        ));
    }

    @Test
    @DisplayName("이미지가 있는 게시글 삭제 시 GCS에서 이미지도 함께 삭제 확인")
    void deletePost_WithImageDeletesImageFromGCS() {
        // Given
        Integer postId = 1;
        Integer userId = 1;
        String imageUrl = "https://storage.googleapis.com/test-bucket/sample-image-20240512.jpg";
        String fileName = "sample-image-20240512.jpg";

        PostDetailResponse existingPost = new PostDetailResponse(
                postId,
                "제목",
                "내용",
                15000,
                imageUrl,
                0,
                0,
                0,
                LocalDateTime.now(),
                PostState.SALE,
                UserSimpleDto.builder().userId(userId).username("testUser").build(),
                new ArrayList<>()
        );

        // Mock 설정
        when(userDetails.getUserId()).thenReturn(userId);
        when(postService.getPost(postId)).thenReturn(existingPost);
        when(imageService.extractFileNameFromUrl(imageUrl)).thenReturn(fileName);
        doNothing().when(postService).deletePost(postId);

        // When
        ResponseEntity<ApiResponse<Void>> response = postController.deletePost(postId, userDetails);

        // Then
        assertEquals(200, response.getStatusCodeValue());

        // 호출 순서 검증
        InOrder inOrder = inOrder(postService, imageService);
        inOrder.verify(postService).getPost(postId); // 1. 게시글 조회
        inOrder.verify(imageService).extractFileNameFromUrl(imageUrl); // 2. 이미지 URL에서 파일명 추출
        inOrder.verify(imageService).deleteFile(fileName); // 3. GCS에서 이미지 삭제
        inOrder.verify(postService).deletePost(postId); // 4. 게시글 삭제

        // 전체 호출 횟수 검증
        verify(imageService, times(1)).extractFileNameFromUrl(anyString());
        verify(imageService, times(1)).deleteFile(anyString());
        verify(postService, times(1)).deletePost(anyInt());
    }
}
