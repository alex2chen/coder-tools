package com.github.search.es;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import java.util.Map;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/11/4
 */
public class Search_test {
    @Test
    public void boolQuery() {
        Map<String, Object> where = Maps.newHashMap();
        where.put("key", "abc");
        where.put("key2", "abc");
        where.put("key3", "abc");
        where.put("key8", "abc");
        Map<String, Object> romoves = Maps.newHashMap();
        romoves.put("key2", "abc");
        Iterables.removeIf(where.entrySet(), x -> !romoves.containsKey(x.getKey()));
        System.out.println(where);
        BoolQueryBuilder batchBoolQuery = QueryBuilders.boolQuery();
        where.keySet().forEach(x ->
                batchBoolQuery.must(QueryBuilders.termQuery(x, where.get(x)))
        );
        System.out.println(batchBoolQuery.toString());
    }
}
