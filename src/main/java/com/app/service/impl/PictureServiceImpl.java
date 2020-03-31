package com.app.service.impl;

import com.app.entity.Picture;
import com.app.repository.PictureRepository;
import com.app.service.PictureService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private final Environment environment;

    private final PictureRepository pictureRepository;

    private String repositoryLocation;


    public PictureServiceImpl(Environment environment, PictureRepository pictureRepository) {
        this.environment = environment;
        this.pictureRepository = pictureRepository;
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

}
