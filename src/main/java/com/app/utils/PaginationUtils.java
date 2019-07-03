package com.app.utils;

import java.util.Collections;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationUtils {

/*    public <T extends Object> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Book> list;
 
        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }
 
        Page<Book> bookPage
          = new PageImpl<Book>(list, PageRequest.of(currentPage, pageSize), books.size());
 
        return bookPage;
    }*/
}
