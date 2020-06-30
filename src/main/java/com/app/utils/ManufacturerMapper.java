package com.app.utils;

import com.app.dto.ManufacturerDto;
import com.app.entity.Manufacturer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerMapper {

    private final ModelMapper modelMapper;

    public ManufacturerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ManufacturerDto convertToDto(Manufacturer manufacturer) {
        ManufacturerDto manufacturerDto = modelMapper.map(manufacturer, ManufacturerDto.class);

        return manufacturerDto;
    }

    public Manufacturer convertToEntity(ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = modelMapper.map(manufacturerDto, Manufacturer.class);

/*        announcement.setUser(userRepository.findById(announcementDto.getUser().getId()).get());
        List<Picture> pictures = announcementDto.getPictures().stream().map(e -> modelMapper.map(e, Picture.class)).collect(Collectors.toList());
        pictures.forEach(e -> e.setAnnouncement(announcement));

        if (pictures.stream().noneMatch(e -> e.isMainPhotoInAnnouncement()))
            pictures.stream().findAny().ifPresent(e -> e.setMainPhotoInAnnouncement(true));

        announcement.setPictures(pictures);
        announcement.setUser(userRepository.findById(announcementDto.getUser().getId()).get());*/

        return manufacturer;
    }

}
