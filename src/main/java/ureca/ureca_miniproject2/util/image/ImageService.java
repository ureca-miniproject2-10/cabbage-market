package ureca.ureca_miniproject2.util.image;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ureca.ureca_miniproject2.util.exception.custom.ImageUploadException;
import ureca.ureca_miniproject2.util.response.FailureMessages;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ureca.ureca_miniproject2.util.response.FailureMessages.*;

@Service
public class ImageService {
    private final Storage storage;
    private final String bucketName;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // 허용되는 이미지 타입
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp"
    );

    public ImageService(Storage storage,
                        @Value("${spring.cloud.gcp.storage.bucket}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    // 파일 업로드
    public String uploadFile(MultipartFile file) throws IOException {
        validateFile(file);
        
        String fileName = generateFileName(file);
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());

        return fileName;
    }

    private void validateFile(MultipartFile file) {
        // 파일이 비어있는지 확인
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_FILE.getMessage());
        }

        // 파일 크기 검사
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageUploadException(IMAGE_SIZE_EXCEEDED.getMessage() + "현재 크기: "
                    + (file.getSize() / 1024 / 1024) + "MB");
        }

        // 파일 타입 검사
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new ImageUploadException(INVALID_IMAGE_TYPE.getMessage());
        }
    }

    // 파일 URL 가져오기
    public String getFileUrl(String fileName) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }

    // 파일 삭제
    public void deleteFile(String fileName) {
        storage.delete(BlobId.of(bucketName, fileName));
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + getExtension(file.getOriginalFilename());
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String extractFileNameFromUrl(String fileUrl) {
        String regex = ".*/([^/?]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileUrl);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
