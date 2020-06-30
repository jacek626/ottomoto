package com.app.searchform;

import com.app.utils.PredicatePreparer;
import com.app.utils.UrlParamsPreparer;

public interface EntityForSearchStrategy<E> extends UrlParamsPreparer, PredicatePreparer {
}
