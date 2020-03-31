package com.app.searchForm;

import com.app.utils.PaginationDetails;
import com.app.utils.PredicatesAndUrlParams;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface SearchFormStrategy<E> {
    PredicatesAndUrlParams preparePredicatesAndUrlParams(E entity);
    Page<E> loadData(PageRequest pageRequest, List<Predicate> predicates);
    Map<String, Object> prepareDataForHtmlElements(E entity);

    default Map<String, Object> prepareSearchForm(E entity, PaginationDetails paginationDetails) {
        PageRequest pageRequest = PageRequest.of(paginationDetails.getPage() - 1, paginationDetails.getSize(), Sort.Direction.fromString(paginationDetails.getSort()), paginationDetails.getOrderBy());
        Map<String, Object> model = new HashMap<>();

        PredicatesAndUrlParams predicatesAndUrlParams = preparePredicatesAndUrlParams(entity);
        model.put("searchArguments", predicatesAndUrlParams.getUrlParams());

        Page<E> pages = loadData(pageRequest, predicatesAndUrlParams.getPredicates());
        model.put("pages", pages);

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

        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1 ,totalPages).boxed().collect(Collectors.toList());
            model.put("pageNumbers", pageNumbers);
        }

        return model;
    }

}


