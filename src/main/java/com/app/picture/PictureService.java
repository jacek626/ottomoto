package com.app.picture;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.app.common.utils.site.element.HtmlElement;
import com.app.common.utils.site.element.UploadedPicture;
import lombok.AllArgsConstructor;
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

import static java.lang.String.join;
import static org.apache.commons.io.FilenameUtils.getExtension;

@Service
@AllArgsConstructor
public class PictureService {
    private final MessageSource messageSource;
    private final AmazonS3 amazonS3;
    private final String bucketName = "ottomoto";

    public List<UploadedPicture> uploadPictures(MultipartFile[] uploadedFiles) throws IOException {
        List<UploadedPicture> uploadedPictures = new ArrayList<>();

        for (MultipartFile uploadedFile : uploadedFiles) {
            var baseFileName = join(new Date().getTime() + RandomStringUtils.randomNumeric(10));
            var fileName = join(baseFileName, ".", getExtension(uploadedFile.getOriginalFilename()));
            uploadFileToS3(fileName, uploadedFile.getInputStream());
            var miniatureFileName = join(baseFileName, "-small.", getExtension(uploadedFile.getOriginalFilename()));
            uploadFileToS3(miniatureFileName, createImageMiniature(uploadedFile));

            uploadedPictures.add(UploadedPicture.of(fileName, miniatureFileName));
        }

        return uploadedPictures;
    }

    public List<String> convertPicturesToHtml(List<UploadedPicture> uploadedPictures) {
        List<String> imagesHtml = new ArrayList<>();

        for (UploadedPicture uploadedPicture : uploadedPictures) {
            imagesHtml.add(prepareHtmlAfterFileUpload(uploadedPicture));
        }

        return imagesHtml;
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        float height = 200f;
        float percent = (height / (float) originalImage.getHeight()) * 100f;
        float width = originalImage.getWidth() * (percent / 100.0f);

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width.intValue(), height.intValue(), null);
        g.dispose();

        return resizedImage;
    }

    private InputStream createImageMiniature(MultipartFile uploadedFile) throws IOException {
        BufferedImage uploadedImageBuffered = ImageIO.read(uploadedFile.getInputStream());
        int type = uploadedImageBuffered.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : uploadedImageBuffered.getType();
        var uploadedFileExtension = getExtension(uploadedFile.getOriginalFilename());
        BufferedImage resizedImage = resizeImage(uploadedImageBuffered, type);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, uploadedFileExtension, os);

        return new ByteArrayInputStream(os.toByteArray());
    }

    private String prepareHtmlAfterFileUpload(UploadedPicture uploadedPicture) {
        return join("<li index='LIST_ID' picturetodelete='false'>",
                generateImageElement(uploadedPicture),
                generateInputForRepositoryName(uploadedPicture),
                generateInputForFileName(uploadedPicture),
                generateInputForMiniatureRepositoryName(uploadedPicture),
                generateDeleteButton(),
                generateMarkAsMainButton(),
                "</li>");
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

        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, fileData, objectMetadata).
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
