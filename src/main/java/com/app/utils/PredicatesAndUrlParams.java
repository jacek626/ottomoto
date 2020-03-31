package com.app.utils;

import com.querydsl.core.types.Predicate;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PredicatesAndUrlParams {
    @Getter
    List<Predicate> predicates;
    @Getter
    String urlParams;

    public static PredicatesAndUrlParams of(@NotNull List<Predicate> predicates, String urlParams) {
        return new PredicatesAndUrlParams(predicates, urlParams);
    }

    private PredicatesAndUrlParams(List<Predicate> predicates, String urlParams) {
        this.predicates = predicates;
        this.urlParams = urlParams;
    }
}