package com.app.searchform;

import com.app.utils.search.PredicatePreparer;
import com.app.utils.search.UrlParamsPreparer;

public interface EntityForSearchStrategy<E> extends UrlParamsPreparer, PredicatePreparer {
}
