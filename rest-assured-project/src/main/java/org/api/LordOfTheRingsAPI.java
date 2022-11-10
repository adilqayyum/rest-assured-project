package org.api;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class LordOfTheRingsAPI {

    private static final String GET_BOOK = "/book";

    public static Response getBooks() {
        return RestAssured.given()
                .spec(BaseSpec.getBasicSpec())
                .when()
                .get(GET_BOOK)
                .then()
                .log()
                .ifError()
                .extract()
                .response();
    }
}
