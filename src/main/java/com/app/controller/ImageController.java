package com.app.controller;

import com.app.utils.HtmlElement;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private Environment environment;

    String repositoryLocation;

    @PostConstruct
    public void init() {
        repositoryLocation = environment.getProperty("spring.repository.location");
    }

    @RequestMapping(value="uploadImage", method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<String>> uploadImage(@RequestParam("file") MultipartFile[] file, Model model,
                                                    Authentication authentication, ModelMap map) {
        StringBuilder elementsToReturn;
        List<String> resultList = new ArrayList<>();
        /* <li index='"+ currentMaxElementId +"' picturetodelete='false'>"+ result +"</li>*/
        try {

            for (MultipartFile uploadfile : file) {
                elementsToReturn = new StringBuilder();
                elementsToReturn.append("<li index='LIST_ID' picturetodelete='false'>");
                String randomFileName = new Date().getTime() + RandomStringUtils.randomNumeric(10) + "." + FilenameUtils.getExtension(uploadfile.getOriginalFilename());
                String filepath = Paths.get(repositoryLocation, randomFileName).toString();


                // Save the file locally
                File originalImageFile = new File(filepath);
                BufferedOutputStream stream =  new BufferedOutputStream(new FileOutputStream(originalImageFile));
                stream.write(uploadfile.getBytes());
                stream.close();

                BufferedImage originalImage = ImageIO.read(originalImageFile);
                int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                BufferedImage resizeImageJpg = resizeImage(originalImage, type);
                String convertedImageToMiniaturePath = FilenameUtils.concat(repositoryLocation,  FilenameUtils.getBaseName(randomFileName) + "-small." + FilenameUtils.getExtension(randomFileName));
                File convertedImageToMiniature = new File(convertedImageToMiniaturePath);
                ImageIO.write(resizeImageJpg, FilenameUtils.getExtension(randomFileName), convertedImageToMiniature);


                elementsToReturn.append(new HtmlElement.Builder("img").
                        id("pictures[LIST_ID].repositoryName").
                        src("/otomoto/images/" + convertedImageToMiniature.getName()).
                        classStyle("miniatureImageInImageScroller").
                        onclick("showImage(this,$('#photoContainerMiniImage'))").
                        picture("/otomoto/images/"+randomFileName).
                        index("LIST_ID").
                        build().returnHtml());

                elementsToReturn.append(new HtmlElement.Builder("input").
                        type("text").
                        id("pictures[LIST_ID].repositoryName").
                        name("pictures[LIST_ID].repositoryName").
                        classStyle("displayNone").
                        value(randomFileName).build().returnHtml());


                elementsToReturn.append(new HtmlElement.Builder("input").
                        type("text").
                        id("pictures[LIST_ID].fileName").
                        name("pictures[LIST_ID].fileName").
                        classStyle("displayNone").
                        value(uploadfile.getOriginalFilename()).build().returnHtml());

                elementsToReturn.append(new HtmlElement.Builder("input").
                        type("text").
                        id("pictures[LIST_ID].miniatureRepositoryName").
                        name("pictures[LIST_ID].miniatureRepositoryName").
                        classStyle("displayNone").
                        value(convertedImageToMiniature.getName()).build().returnHtml());

                elementsToReturn.append(new HtmlElement.Builder("button").
                        type("button").
                        onclick("deletePictureInAnnouncement(this);").
                        html("usun").build().returnHtml());

                elementsToReturn.append("</li>");

                resultList.add(elementsToReturn.toString());

            }




        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<List<String>>(resultList, HttpStatus.OK);
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type) {
        Float height = 200f;
        Float procent =  (height / (float) originalImage.getHeight()) * 100f;
        Float width = originalImage.getWidth()  * (procent/100.0f);

        BufferedImage resizedImage = new BufferedImage(width.intValue(), height.intValue(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width.intValue(), height.intValue(), null);
        g.dispose();

        return resizedImage;
    }


}
