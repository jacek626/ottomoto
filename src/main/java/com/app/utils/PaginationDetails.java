package com.app.utils;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PaginationDetails {
    int page;
    int size;
    String orderBy;
    String sort;
}
