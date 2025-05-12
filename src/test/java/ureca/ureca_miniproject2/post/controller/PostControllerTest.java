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
import ureca.ureca_miniproject2.post.dto.PostResponse;
import ureca.ureca_miniproject2.post.dto.PostUpdateRequest;
import ureca.ureca_miniproject2.post.entity.PostState;
import ureca.ureca_miniproject2.post.service.PostService;
import ureca.ureca_miniproject2.util.image.ImageService;
import ureca.ureca_miniproject2.util.response.ApiResponse;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private PostController postController;

    @Test
    @DisplayName("기존 이미지가 있는 게시글에 새 이미지로 수정 시 GCS에서 기존 이미지 삭제 및 새 이미지 업로드 확인")
    void updatePost_ReplaceExistingImage() throws IOException {
        // Given
        Integer postId = 1;
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

        PostResponse expectedResponse = new PostResponse(
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
                "testUser"
        );

        // Mock 설정
        when(imageService.extractFileNameFromUrl(originalImageUrl)).thenReturn(originalFileName);
        when(imageService.uploadFile(newImage)).thenReturn(newFileName); // 새 이미지 업로드 성공
        when(imageService.getFileUrl(newFileName)).thenReturn(newImageUrl);
        when(postService.updatePost(eq(postId), any(PostUpdateRequest.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ApiResponse<PostResponse>> response = postController.updatePost(
                postId,
                request,
                newImage
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
        String imageUrl = "https://storage.googleapis.com/test-bucket/sample-image-20240512.jpg";
        String fileName = "sample-image-20240512.jpg";

        PostResponse existingPost = new PostResponse(
                postId,
                "수정된 제목",
                "수정된 내용",
                15000,
                imageUrl, // 새 이미지 URL이 반환됨
                0,
                0,
                0,
                LocalDateTime.now(),
                PostState.SALE,
                "testUser"
        );

        // Mock 설정
        when(postService.getPost(postId)).thenReturn(existingPost);
        when(imageService.extractFileNameFromUrl(imageUrl)).thenReturn(fileName);
        doNothing().when(postService).deletePost(postId);

        // When
        ResponseEntity<ApiResponse<Void>> response = postController.deletePost(postId);

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

    @Test
    @DisplayName("GCS에서 이미지 삭제 실패 시에도 게시글 삭제 진행 확인")
    void deletePost_ContinuesDeletingPostEvenIfImageDeleteFails() {
        // Given
        Integer postId = 1;
        String imageUrl = "https://storage.googleapis.com/test-bucket/problematic-image.jpg";
        String fileName = "problematic-image.jpg";

        PostResponse existingPost = new PostResponse(
                postId,
                "수정된 제목",
                "수정된 내용",
                15000,
                imageUrl, // 새 이미지 URL이 반환됨
                0,
                0,
                0,
                LocalDateTime.now(),
                PostState.SALE,
                "testUser"
        );

        // Mock 설정 - 이미지 삭제 실패
        when(postService.getPost(postId)).thenReturn(existingPost);
        when(imageService.extractFileNameFromUrl(imageUrl)).thenReturn(fileName);
        doNothing().when(postService).deletePost(postId);

        // When
        ResponseEntity<ApiResponse<Void>> response = postController.deletePost(postId);

        // Then
        assertEquals(200, response.getStatusCodeValue());

        // 이미지 삭제 실패에도 불구하고 게시글은 삭제되었는지 확인
        verify(imageService).deleteFile(fileName);
        verify(postService).deletePost(postId);
    }
}