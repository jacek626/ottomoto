package com.app.searchform;

import com.app.common.utils.search.PredicatePreparer;
import com.app.common.utils.search.UrlParamsPreparer;

public interface EntityForSearchStrategy<E> extends UrlParamsPreparer, PredicatePreparer {
}
