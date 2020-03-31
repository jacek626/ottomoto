package com.app.service;

import com.app.entity.Picture;

import java.util.List;

public interface PictureService {
    void deleteFromFileRepository(List<Picture> pictures);
}
