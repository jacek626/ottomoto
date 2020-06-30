package com.app.searchform;

import com.app.dto.AnnouncementDto;
import com.app.entity.Announcement;
import com.querydsl.core.types.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HomeSearchStrategyDecorator implements SearchStrategy<Announcement, AnnouncementDto> {

    private final AnnouncementSearchStrategy announcementSearchFormStrategy;
    private final ModelMapper modelMapper;


    public HomeSearchStrategyDecorator(AnnouncementSearchStrategy announcementSearchFormStrategy, ModelMapper modelMapper) {
        this.announcementSearchFormStrategy = announcementSearchFormStrategy;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<Announcement> loadData(PageRequest pageRequest, Predicate predicate) {
        return announcementSearchFormStrategy.loadData(pageRequest, predicate);
    }

    @Override
    public Map<String, Object> prepareDataForHtmlElements(Announcement entity) {
        return announcementSearchFormStrategy.prepareDataForHtmlElements(entity);
    }

    @Override
    public AnnouncementDto convertToDto(Announcement entity) {
        return modelMapper.map(entity, AnnouncementDto.class);
    }
}


