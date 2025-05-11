package ureca.ureca_miniproject2.util.image;

import static org.junit.jupiter.api.Assertions.*;

import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import ureca.ureca_miniproject2.util.exception.custom.ImageUploadException;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private Storage storage;

    @InjectMocks
    private ImageService imageService;

    private final String bucketName = "test-bucket";

    @BeforeEach
    void setup() {
        imageService = new ImageService(storage, bucketName);
    }

    @Test
    @DisplayName("이미지 업로드 성공 시 파일명 반환 및 저장소 호출 확인")
    void uploadFile_Success() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test-data".getBytes()
        );

        // When
        String fileName = imageService.uploadFile(file);

        // Then
        assertNotNull(fileName);
        verify(storage).create(any(), any(byte[].class));
    }

    @Test
    @DisplayName("빈 파일 업로드 시 IllegalArgumentException 발생")
    void validateFile_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "empty", "empty.jpg", "image/jpeg", new byte[0]
        );

        assertThrows(IllegalArgumentException.class,
                () -> imageService.uploadFile(emptyFile));
    }

    @Test
    @DisplayName("10MB 초과 파일 업로드 시 ImageUploadException 발생 및 오류 메시지 검증")
    void validateFile_ExceedSize() {
        byte[] oversizeData = new byte[11 * 1024 * 1024]; // 11MB
        MockMultipartFile largeFile = new MockMultipartFile(
                "large", "large.jpg", "image/jpeg", oversizeData
        );

        ImageUploadException exception = assertThrows(ImageUploadException.class,
                () -> imageService.uploadFile(largeFile));

        assertTrue(exception.getMessage().contains("10MB"));
    }
}
