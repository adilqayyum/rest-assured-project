package org.api;

import com.typesafe.config.Config;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.api.config.TestEnvFactory;
import tech.grasshopper.filter.ExtentRestAssuredFilter;

public class BaseSpec {

    private static final Config CONFIG = TestEnvFactory.getInstance().getConfig();
    private static final String BASE_URL = CONFIG.getString("BASE_URL");
    private static final boolean SHOULD_LOG_REQUEST_AND_RESPONSE_DETAILS_TO_CONSOLE =
            CONFIG.getBoolean("SHOULD_LOG_REQUEST_AND_RESPONSE_DETAILS_TO_CONSOLE");

    static {
        RestAssured.filters(new ExtentRestAssuredFilter());
        if (SHOULD_LOG_REQUEST_AND_RESPONSE_DETAILS_TO_CONSOLE) {
            RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
    }

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
