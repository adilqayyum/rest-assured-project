package org.api.pets;

import io.restassured.response.Response;
import org.api.PetsAPI;
import org.api.assertutils.ResponseAssert;
import org.api.pets.data.AddPet;
import org.api.pets.data.DataSet;
import org.api.pets.data.PetsData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class APIAutomationTests {

    private static AddPet addPet;
    private static Long petId;

    @BeforeAll
    public static void setAddPet() {
        addPet = PetsData.getAddPetRequestBody();
    }

    @Test
    @Order(1)
    @DisplayName("Verify that Add Pets works correctly for Pending")
    void testAddPetEndpointForPending() {

        addPet.setStatus("pending");

        Response response = PetsAPI.addPetEndpoint(addPet);
        Logger.getLogger("Logger").log(Level.INFO, response.getBody().asString());

        petId = response.then().extract().path("id");
        ResponseAssert.assertThat(response)
                .statusCodeIs(SC_OK)
                .hasKeyWithValue("name","test_doggie")
                .assertAll();
    }

    @Test
    @Order(1)
    @DisplayName("Verify that Add Pets works correctly for Sold")
    void testAddPetEndpointForSold() {

        DataSet soldData = DataSet.builder()
                .setId(1)
                .setName("demo_sold")
                .build();

        addPet.setStatus("sold");
        addPet.setCategory(soldData);

        Response response = PetsAPI.addPetEndpoint(addPet);
        Logger.getLogger("Logger").log(Level.INFO, response.getBody().asString());

        ResponseAssert.assertThat(response)
                .statusCodeIs(SC_OK)
                .hasKeyWithValue("name","test_dog")
                .assertAll();
    }

    @ParameterizedTest
    @Order(2)
    @CsvSource({"pending","sold"})
    @DisplayName("Verify that Get Pets by Status works correctly")
    void testGetPetsByValidStatus(String status) {

        Response response = PetsAPI.getPetsByStatus(status);
        Logger.getLogger("Logger").log(Level.INFO, response.getBody().asString());

        ResponseAssert.assertThat(response)
                .statusCodeIs(SC_OK)
                .hasKeyWithValue("name","test_dog")
                .hasKeyWithValue("category.name", "demo")
                .assertAll();
    }

    @ParameterizedTest
    @Order(2)
    @CsvSource({"-1","-2"})
    @DisplayName("Verify that Get Pet returns 404 for invalid IDs")
    void testGetPetsByID(Long Id) {

        Response response = PetsAPI.getPetById(Id);
        Logger.getLogger("Logger").log(Level.INFO, response.getBody().asString());

        ResponseAssert.assertThat(response)
                .statusCodeIs(SC_NOT_FOUND)
                .hasKeyWithValue("message", "Pet not found")
                .assertAll();
    }

    @Order(3)
    @Test
    @DisplayName("Verify that Delete Pet by ID works correctly")
    void testDeletePetsByID() {

        Response response = PetsAPI.deletePetById(petId);
        Logger.getLogger("Logger").log(Level.INFO, response.getBody().asString());

        ResponseAssert.assertThat(response)
                .statusCodeIs(SC_OK)
                .hasKeyWithValue("message", petId.toString())
                .assertAll();
    }
}
