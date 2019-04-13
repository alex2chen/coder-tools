package com.github.common;

import com.google.common.base.Stopwatch;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
import static org.hamcrest.core.Is.is;

/**
 * @author alex.chen
 * @version 1.0.0
 * @date 2017/4/28
 */
public class JsonPath_test {
    private String json = "{ \"store\": {\n" +
            "    \"book\": [ \n" +
            "      { \"category\": \"reference\",\n" +
            "        \"author\": \"Nigel Rees\",\n" +
            "        \"title\": \"Sayings of the Century\",\n" +
            "        \"price\": 8.95\n" +
            "      },\n" +
            "      { \"category\": \"fiction\",\n" +
            "        \"author\": \"Evelyn Waugh\",\n" +
            "        \"title\": \"Sword of Honour\",\n" +
            "        \"price\": 12.99,\n" +
            "        \"isbn\": \"0-553-21311-3\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"bicycle\": {\n" +
            "      \"color\": \"red\",\n" +
            "      \"price\": 19.95\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Test
    public void readNode() {
        Stopwatch stopwatch= Stopwatch.createStarted();
        List<String> authors = JsonPath.read(json, "$.store.book[*].author");
//        System.out.println(authors);
        Assert.assertThat(authors.size(),is(2));
        System.out.println(stopwatch);
        String author = JsonPath.read(json, "$.store.book[1].author");
//        System.out.println(author);
        Assert.assertThat(author,is("Evelyn Waugh"));
        System.out.println(stopwatch);
        List<Object> books = JsonPath.read(json, "$.store.book[?]", filter(where("category").is("reference")));
        Assert.assertThat(books.size(),is(1));
//        System.out.println(books);
        System.out.println(stopwatch);
    }
}
