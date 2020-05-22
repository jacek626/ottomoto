package com.app.utils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

public interface PredicatePreparer {
    Predicate preparePredicates();

    BooleanBuilder getPredicate();

    default void preparePredicates(BooleanPath booleanPath, Boolean fieldValue) {
        if (fieldValue != null)
            getPredicate().and(booleanPath.eq(fieldValue));
    }

    default void preparePredicates(StringPath stringPath, String fieldValue) {
        if (fieldValue != null)
            getPredicate().and(stringPath.containsIgnoreCase(fieldValue));
    }

    default void preparePredicates(NumberPath<Long> numberPath, Long fieldValue) {
        if (fieldValue != null)
            getPredicate().and(numberPath.eq(fieldValue));
    }

}
