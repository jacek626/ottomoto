package com.app.service;

import com.app.utils.UploadedPicture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PictureServiceTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PictureService pictureService;

    @Test
    public void shouldConvertPicturesToHtml() throws IOException {
        //given
        when(messageSource.getMessage(any(String.class), any(Object[].class), any(Locale.class))).thenReturn("test");

        List<UploadedPicture> uploadedPictures = new ArrayList<>();
        uploadedPictures.add(UploadedPicture.of(new File("uploadedFile"),new File("miniatureFile"), "originalFileName"));
        uploadedPictures.add(UploadedPicture.of(new File("uploadedFile2"),new File("miniatureFile2"), "originalFileName2"));

        //when
        List<String> picturesConvertedToHtml = pictureService.convertPicturesToHtml(uploadedPictures);

        //then
        assertThat(picturesConvertedToHtml).size().isEqualTo(2);
        assertThat(picturesConvertedToHtml).first().asString().isNotBlank();
    }

    //todo
    // deleteFromFileRepository
    // convertPicturesToHtml


}
