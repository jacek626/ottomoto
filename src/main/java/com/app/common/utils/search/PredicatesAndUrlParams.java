package com.app.common.utils.search;

import com.querydsl.core.types.Predicate;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public class PredicatesAndUrlParams {
    @Getter
    private final Predicate predicate;
    @Getter
    private final String urlParams;

    private PredicatesAndUrlParams(Predicate predicate, String urlParams) {
        this.predicate = predicate;
        this.urlParams = urlParams;
    }

    public static PredicatesAndUrlParams of(@NotNull Predicate predicate, String urlParams) {
        return new PredicatesAndUrlParams(predicate, urlParams);
    }
}
