package softuni.project.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String uploadImg(MultipartFile multipartFile) throws IOException;
}
