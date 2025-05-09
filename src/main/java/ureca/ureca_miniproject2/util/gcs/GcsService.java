package ureca.ureca_miniproject2.util.gcs;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GcsService {
    private final Storage storage;
    private final String bucketName;

    public GcsService(Storage storage,
                      @Value("${spring.cloud.gcp.storage.bucket}") String bucketName){
        this.storage = storage;
        this.bucketName = bucketName;
    }

    // 파일 업로드
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file);
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
        storage.create(blobInfo, file.getBytes());

        return fileName;
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
}
