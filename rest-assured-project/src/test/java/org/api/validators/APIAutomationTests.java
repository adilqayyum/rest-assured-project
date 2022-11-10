package org.api.validators;

import io.restassured.response.Response;
import org.api.LordOfTheRingsAPI;
import org.api.assertutils.ResponseAssert;
import org.junit.jupiter.api.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.http.HttpStatus.SC_OK;

public class APIAutomationTests {

    @Test
    void testGetFacilityUserByEmail() {

        Response response = LordOfTheRingsAPI.getBooks();
        Logger.getLogger("Logger").log(Level.INFO, response.getBody().toString());

        ResponseAssert.assertThat(response)
                .statusCodeIs(SC_OK)
                .hasKeyWithValue("total", 3)
                .assertAll();
    }
}
