package org.api;

import com.typesafe.config.Config;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.api.config.TestEnvFactory;

public class BaseSpec {

    private static final Config CONFIG = TestEnvFactory.getInstance().getConfig();
    private static final String BASE_URL = CONFIG.getString("BASE_URL");
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
