package com.app.searchform;

import com.app.enums.PaginationPageSize;
import com.app.utils.PaginationDetails;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface SearchStrategy<E extends EntityForSearchStrategy> {
    Page<E> loadData(PageRequest pageRequest, Predicate predicate);

    default Map<String, Object> prepareDataForHtmlElements(E entity) {
        return Collections.emptyMap();
    }

    default Map<String, Object> prepareSearchForm(E entity, PaginationDetails paginationDetails) {
        PageRequest pageRequest = PageRequest.of(paginationDetails.getPage() - 1, paginationDetails.getSize(), Sort.Direction.fromString(paginationDetails.getSort()), paginationDetails.getOrderBy());
        Map<String, Object> model = new HashMap<>();

        Page<E> pages = loadData(pageRequest, entity.preparePredicates());
        model.put("pages", pages);

        model.put("searchArguments", "&" + entity.prepareUrlParams());
        model.putAll(prepareDataForHtmlElements(entity));
        model.putAll(prepareDataForPaginationElements(paginationDetails, pages.getTotalPages()));

        return model;
    }

    default Map<String, Object> prepareDataForPaginationElements(PaginationDetails paginationDetails, int totalPages) {
        Map<String, Object> model = new HashMap<>();

        model.put("page", paginationDetails.getPage());
        model.put("orderBy", paginationDetails.getOrderBy());
        model.put("sort", paginationDetails.getSort());
        model.put("size", paginationDetails.getSize());
        model.put("pageSizes", PaginationPageSize.LIST);
        model.put("totalPages", totalPages);

        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,totalPages).boxed().collect(Collectors.toList());
            model.put("pageNumbers", pageNumbers);
        }

        return model;
    }

}


