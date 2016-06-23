package com.wuma.LuceneInAction.util;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.io.IOException;

/**
 * Created by liwujun
 * on 2016/6/23 at 10:31
 */
public class TestUtil {
    public static int hitCount(IndexSearcher searcher, Query query) throws IOException {
        return searcher.search(query, 1).totalHits;
    }
}
