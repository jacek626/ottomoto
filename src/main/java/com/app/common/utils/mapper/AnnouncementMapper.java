package com.app.common.utils.mapper;

import com.app.announcement.dto.AnnouncementDto;
import com.app.announcement.entity.Announcement;
import com.app.picture.entity.Picture;
import com.app.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementMapper {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public AnnouncementMapper(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public AnnouncementDto convertToDto(Announcement announcement) {
        AnnouncementDto announcementDto = modelMapper.map(announcement, AnnouncementDto.class);

        return announcementDto;
    }

    public Announcement convertToEntity(AnnouncementDto announcementDto) {
        announcementDto.preparePictures();
        Announcement announcement = modelMapper.map(announcementDto, Announcement.class);
        announcement.setUser(userRepository.findById(announcementDto.getUser().getId()).get());
        List<Picture> pictures = announcementDto.getPictures().stream().map(e -> modelMapper.map(e, Picture.class)).collect(Collectors.toList());
        pictures.forEach(e -> e.setAnnouncement(announcement));

        if (pictures.stream().noneMatch(e -> e.isMainPhotoInAnnouncement()))
            pictures.stream().findAny().ifPresent(e -> e.setMainPhotoInAnnouncement(true));

        announcement.setPictures(pictures);
        announcement.setUser(userRepository.findById(announcementDto.getUser().getId()).get());

        return announcement;
    }
}
