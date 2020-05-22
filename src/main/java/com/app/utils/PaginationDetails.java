package com.app.utils;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Builder
@Data
public class PaginationDetails {
    private int page;
    private int size;
    private String orderBy;
    private String sort;

    public PageRequest convertToPageRequest() {
        return PageRequest.of(page, size, Sort.Direction.fromString(sort), orderBy);
    }
}
