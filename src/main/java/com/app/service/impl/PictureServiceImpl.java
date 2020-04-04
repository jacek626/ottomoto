package com.app.service.impl;

import com.app.entity.Picture;
import com.app.repository.PictureRepository;
import com.app.service.PictureService;
import com.app.utils.HtmlElement;
import com.app.utils.UploadedPicture;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class PictureServiceImpl implements PictureService {

    private final Environment environment;

    private final PictureRepository pictureRepository;

    private final MessageSource messageSource;

    private String repositoryLocation;

    public PictureServiceImpl(Environment environment, PictureRepository pictureRepository, MessageSource messageSource) {
        this.environment = environment;
        this.pictureRepository = pictureRepository;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        repositoryLocation = environment.getProperty("spring.repository.location");
    }

    @Override
    public void deleteFromFileRepository(List<Picture> pictures) {
        for (Picture picture : pictures) {
            Paths.get(repositoryLocation, picture.getRepositoryName()).toFile().delete();
        }

        pictureRepository.deleteAll(pictures);
    }

    @Override
    public List<UploadedPicture> uploadPictures(MultipartFile[] uploadedFiles) throws IOException {
        List<UploadedPicture> savedImages = new ArrayList<>();

        for (MultipartFile uploadedFile : uploadedFiles) {
            File uploadedImage = saveUploadedFileInRepository(uploadedFile);
            File convertedImageToMiniature = prepareImageMiniature(uploadedImage);
            savedImages.add(UploadedPicture.of(uploadedImage, convertedImageToMiniature, uploadedFile.getOriginalFilename()));
        }

        return savedImages;
    }

    @Override
    public List<String> convertPicturesToHtml(List<UploadedPicture> uploadedPictures) throws IOException {
        List<String> imagesHtml = new ArrayList<>();

        for (UploadedPicture uploadedPicture : uploadedPictures) {
            imagesHtml.add(prepareHtmlAfterFileUpload(uploadedPicture));
        }

        return imagesHtml;
    }

    private File prepareImageMiniature(File uploadedImage) throws IOException {
        BufferedImage uploadedImageBuffered = ImageIO.read(uploadedImage);
        int type = uploadedImageBuffered.getType() == 0? BufferedImage.TYPE_INT_ARGB : uploadedImageBuffered.getType();

        String uploadedFileExtension = FilenameUtils.getExtension(uploadedImage.getName());
        BufferedImage resizeImageJpg = resizeImage(uploadedImageBuffered, type);
        String imageMiniaturePath = FilenameUtils.concat(repositoryLocation,  FilenameUtils.getBaseName(uploadedImage.getName()) + "-small." + uploadedFileExtension);
        File imageMiniature = new File(imageMiniaturePath);
        ImageIO.write(resizeImageJpg, uploadedFileExtension, imageMiniature);

        return imageMiniature;
    }

    private File saveUploadedFileInRepository(MultipartFile uploadedFile) throws IOException {
        String randomFileName = generateRandomFileNameWithSameExtension(uploadedFile);
        String filePath = Paths.get(repositoryLocation, randomFileName).toString();
        File uploadedImage = new File(filePath);
        BufferedOutputStream uploadedImageStream =  new BufferedOutputStream(new FileOutputStream(uploadedImage));
        uploadedImageStream.write(uploadedFile.getBytes());
        uploadedImageStream.close();

        return uploadedImage;
    }

    private String generateRandomFileNameWithSameExtension(MultipartFile uploadedFile) {
        return new Date().getTime() + RandomStringUtils.randomNumeric(10) + "." + FilenameUtils.getExtension(uploadedFile.getOriginalFilename());
    }

    private String prepareHtmlAfterFileUpload(UploadedPicture uploadedPicture) {
        StringBuilder elementsToReturn = new StringBuilder();

        elementsToReturn.append("<li index='LIST_ID' picturetodelete='false'>");
        elementsToReturn.append(generateImageElement(uploadedPicture));
        elementsToReturn.append(generateInputForRepositoryName(uploadedPicture));
        elementsToReturn.append(generateInputForFileName(uploadedPicture));
        elementsToReturn.append(generateInputForMiniatureRepositoryName(uploadedPicture));
        elementsToReturn.append(generateDeleteButton());
        elementsToReturn.append("</li>");

        return elementsToReturn.toString();
    }

    private String generateDeleteButton() {
        return HtmlElement.builder().tag("button").
                type("button").
                onclick("deletePictureInAnnouncement(this);").
                html( messageSource.getMessage("delete", null, Locale.getDefault())).build().toHtml();
    }

    private String generateImageElement(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("img").
                id("pictures[LIST_ID].repositoryName").
                src("/otomoto/images/" + uploadedPicture.getMiniatureFile().getName()).
                classStyle("miniatureImageInImageScroller").
                onclick("showImage(this,$('#photoContainerMiniImage'))").
                picture("/otomoto/images/"+ uploadedPicture.getMiniatureFile().getName()).
                index("LIST_ID").
                build().toHtml();
    }

    private String generateInputForFileName(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("input").
                type("text").
                id("pictures[LIST_ID].fileName").
                name("pictures[LIST_ID].fileName").
                classStyle("displayNone").
                value(uploadedPicture.getOriginalFilename()).build().toHtml();
    }

    private String generateInputForMiniatureRepositoryName(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("input").
                type("text").
                id("pictures[LIST_ID].miniatureRepositoryName").
                name("pictures[LIST_ID].miniatureRepositoryName").
                classStyle("displayNone").
                value(uploadedPicture.getMiniatureFile().getName()).build().toHtml();
    }

    private String generateInputForRepositoryName(UploadedPicture uploadedPicture) {
        return HtmlElement.builder().tag("input").
                type("text").
                id("pictures[LIST_ID].repositoryName").
                name("pictures[LIST_ID].repositoryName").
                classStyle("displayNone").
                value(uploadedPicture.getUploadedFile().getName()).build().toHtml();
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        Float height = 200f;
        Float percent =  (height / (float) originalImage.getHeight()) * 100f;
        Float width = originalImage.getWidth()  * (percent/100.0f);

        BufferedImage resizedImage = new BufferedImage(width.intValue(), height.intValue(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width.intValue(), height.intValue(), null);
        g.dispose();

        return resizedImage;
    }

}
