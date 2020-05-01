package com.app.utils;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaginationDetails {
    private int page;
    private int size;
    private String orderBy;
    private String sort;
}
