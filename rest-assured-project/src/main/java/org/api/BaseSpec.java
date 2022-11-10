package org.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseSpec {

    private static final String BASE_URL = "https://the-one-api.dev/v2";
    public static RequestSpecification getBasicSpec() {
        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .build()
                .config(
                        RestAssuredConfig.config()
                                .logConfig(LogConfig.logConfig().blacklistHeader("Authorization")));
    }
}
