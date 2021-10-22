package com.app.picture;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.app.common.utils.site.element.HtmlElement;
import com.app.common.utils.site.element.UploadedPicture;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class PictureService {

    private final MessageSource messageSource;

    private final AmazonS3 amazonS3Client;

    private final String bucketName = "ottomoto";

    public PictureService(MessageSource messageSource, AmazonS3 amazonS3Client) {
        this.messageSource = messageSource;
        this.amazonS3Client = amazonS3Client;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        Float height = 200f;
        Float percent = (height / (float) originalImage.getHeight()) * 100f;
        Float width = originalImage.getWidth() * (percent / 100.0f);

        BufferedImage resizedImage = new BufferedImage(width.intValue(), height.intValue(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width.intValue(), height.intValue(), null);
        g.dispose();

        return resizedImage;
    }

    public List<UploadedPicture> uploadPictures(MultipartFile[] uploadedFiles) throws IOException {
        List<UploadedPicture> uploadedPictures = new ArrayList<>();

        for (MultipartFile uploadedFile : uploadedFiles) {
            String baseFileName = new Date().getTime() + RandomStringUtils.randomNumeric(10);

            String fileName = baseFileName + "." + FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
            uploadFileToS3(fileName, uploadedFile.getInputStream());

            String miniatureFileName = baseFileName + "-small." + FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
            InputStream miniatureStream = createImageMiniature(uploadedFile);
            uploadFileToS3(miniatureFileName, miniatureStream);

            uploadedPictures.add(UploadedPicture.of(fileName, miniatureFileName));
        }

        return uploadedPictures;
    }

    private InputStream createImageMiniature(MultipartFile uploadedFile) throws IOException {
        BufferedImage uploadedImageBuffered = ImageIO.read(uploadedFile.getInputStream());
        int type = uploadedImageBuffered.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : uploadedImageBuffered.getType();
        String uploadedFileExtension = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
        BufferedImage resizedImage = resizeImage(uploadedImageBuffered, type);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, uploadedFileExtension, os);

        return new ByteArrayInputStream(os.toByteArray());
    }

    public List<String> convertPicturesToHtml(List<UploadedPicture> uploadedPictures) throws IOException {
        List<String> imagesHtml = new ArrayList<>();

        for (UploadedPicture uploadedPicture : uploadedPictures) {
            imagesHtml.add(prepareHtmlAfterFileUpload(uploadedPicture));
        }

        return imagesHtml;
    }

    private String prepareHtmlAfterFileUpload(UploadedPicture uploadedPicture) {

        return "<li index='LIST_ID' picturetodelete='false'>" +
                generateImageElement(uploadedPicture) +
                generateInputForRepositoryName(uploadedPicture) +
                generateInputForFileName(uploadedPicture) +
                generateInputForMiniatureRepositoryName(uploadedPicture) +
                generateDeleteButton() +
                generateMarkAsMainButton() +
                "</li>";
    }

    private String generateDeleteButton() {
        return HtmlElement.builder().tag("button").
                type("button").
                classStyle("btn btn-secondary btn-sm").
                onclick("deletePictureInAnnouncement(this);").
                html(messageSource.getMessage("delete", null, Locale.getDefault())).build().toHtml();
    }

    private String generateMarkAsMainButton() {
        return HtmlElement.builder().tag("button").
                type("button").
                classStyle("btn btn-secondary btn-sm").
                onclick("markPictureInAnnouncementAsMain(this);").
                html(messageSource.getMessage("mainPhoto", null, Locale.getDefault())).build().toHtml();
    }

    private void uploadFileToS3(String fileName, InputStream fileData) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image");
        objectMetadata.setContentLength(fileData.available());

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, fileData, objectMetadata).
                withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private String generateImageElement(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("img").
                id("pictures[LIST_ID].repositoryName").
                src("/ottomoto/images/" + uploadedPicture.getMiniatureFileName()).
                classStyle("miniatureImageInImageScroller").
                onclick("showImage(this,$('#photoContainerMiniImage'))").
                picture("/ottomoto/images/" + uploadedPicture.getMiniatureFileName()).
                index("LIST_ID").
                build().toHtml();
    }

    private String generateInputForFileName(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("input").
                type("text").
                id("pictures[LIST_ID].fileName").
                name("pictures[LIST_ID].fileName").
                classStyle("displayNone").
                value(uploadedPicture.getUploadedFileName()).build().toHtml();
    }

    private String generateInputForMiniatureRepositoryName(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("input").
                type("text").
                id("pictures[LIST_ID].miniatureRepositoryName").
                name("pictures[LIST_ID].miniatureRepositoryName").
                classStyle("displayNone").
                value(uploadedPicture.getMiniatureFileName()).build().toHtml();
    }

    private String generateInputForRepositoryName(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("input").
                type("text").
                id("pictures[LIST_ID].repositoryName").
                name("pictures[LIST_ID].repositoryName").
                classStyle("displayNone").
                value(uploadedPicture.getUploadedFileName()).build().toHtml();
    }

}
