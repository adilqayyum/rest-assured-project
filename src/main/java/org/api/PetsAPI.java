package org.api;
import com.typesafe.config.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.api.config.TestEnvFactory;
import org.api.pets.data.AddPet;

public class PetsAPI {

    private static final Config CONFIG = TestEnvFactory.getInstance().getConfig();
    private static final String ADD_PET = CONFIG.getString("ADD_PET");
    private static final String GET_PET_BY_STATUS = CONFIG.getString("GET_PET_BY_STATUS");

    public static Response addPetEndpoint(AddPet petRequestBody) {
        return RestAssured.given()
                .spec(BaseSpec.getBasicSpec())
                .when()
                .body(petRequestBody)
                .post(ADD_PET)
                .then()
                .log()
                .ifError()
                .extract()
                .response();
    }

    public static Response getPetById(Long Id) {
        return RestAssured.given()
                .spec(BaseSpec.getBasicSpec())
                .when()
                .get(ADD_PET+"/"+Id)
                .then()
                .log()
                .ifError()
                .extract()
                .response();
    }

    public static Response deletePetById(Long Id) {
        return RestAssured.given()
                .spec(BaseSpec.getBasicSpec())
                .when()
                .delete(ADD_PET+"/"+Id)
                .then()
                .log()
                .ifError()
                .extract()
                .response();
    }

    public static Response getPetsByStatus(String status) {
        return RestAssured.given()
                .spec(BaseSpec.getBasicSpec())
                .when()
                .queryParams("status", status)
                .get(GET_PET_BY_STATUS)
                .then()
                .log()
                .ifError()
                .extract()
                .response();
    }
}
