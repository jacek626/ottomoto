package com.app.service;

import com.app.entity.Picture;
import com.app.utils.UploadedPicture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PictureService {
    void deleteFromFileRepository(List<Picture> pictures);
    List<UploadedPicture> uploadPictures(MultipartFile[] uploadedFiles) throws IOException;
    List<String> convertPicturesToHtml(List<UploadedPicture> uploadedPictures) throws IOException;
}
