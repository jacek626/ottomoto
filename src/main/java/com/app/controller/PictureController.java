package com.app.controller;

import com.app.service.PictureService;
import com.app.utils.UploadedPicture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/image")
public class PictureController {

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @RequestMapping(value="uploadImage", method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<String>> uploadImage(@RequestParam("file") MultipartFile[] file) {
        try {
            List<UploadedPicture> uploadedPictures = pictureService.uploadPictures(file);
            List<String> picturesAsHtml = pictureService.convertPicturesToHtml(uploadedPictures);

            return new ResponseEntity<>(picturesAsHtml, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
