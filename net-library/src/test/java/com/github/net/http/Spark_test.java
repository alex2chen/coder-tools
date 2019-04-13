package com.github.net.http;

import org.junit.Test;

import static spark.Spark.*;

/**
 * @Author: alex
 * @Description: a tiny web framework for Java 8：https://github.com/perwendel/spark
 * @Date: created in 2018/9/11.
 */
public class Spark_test {
    @Test
    public void startHello() {
        //http://localhost:4567/hello

        get("/hello", (request, response) -> "Hello World!");
        get("/private", (request, response) -> {
            response.status(401);
            return "Go Away!!!";
        });
        get("/users/:name", (request, response) -> "Selected user: " + request.params(":name"));
        get("/news/:section", (request, response) -> {
            response.type("text/xml");
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news>" + request.params("section") + "</news>";
        });
        get("/protected", (request, response) -> {
            halt(403, "I don't think so!!!");
            return null;
        });
        get("/redirect", (request, response) -> {
            response.redirect("/news/world");
            return null;
        });
        get("/", (request, response) -> "root");
    }
}
