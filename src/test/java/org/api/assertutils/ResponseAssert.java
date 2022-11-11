package org.api.assertutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.typesafe.config.Config;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.api.config.TestEnvFactory;

@Slf4j
public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

    private final SoftAssertions softAssertions;

    private static final Config CONFIG = TestEnvFactory.getInstance().getConfig();
    private static final String SCHEMA_RESOURCES_PATH = CONFIG.getString("SCHEMA_RESOURCES_PATH");
    private static final String RELATIVE_PATH_SCHEMAS_FROM_RESOURCES_DIR =
            CONFIG.getString("RELATIVE_PATH_SCHEMAS_FROM_RESOURCES_DIR");

    private ResponseAssert(Response response) {
        super(response, ResponseAssert.class);
        this.softAssertions = new SoftAssertions();
    }

    public static ResponseAssert assertThat(Response response) {
        return new ResponseAssert(response);
    }

    public ResponseAssert statusCodeIs(int statusCode) {
        Assertions.assertThat(actual.getStatusCode())
                .as("Status Code validation")
                .isEqualTo(statusCode);
        return this;
    }

    public ResponseAssert contentTypeIsApplicationJson() {
        softAssertions
                .assertThat(actual.getContentType())
                .as("Content Type validation")
                .containsIgnoringCase("application/json");
        return this;
    }

    public ResponseAssert andMatchingRule(Predicate<Response> condition, String errorMessage) {
        softAssertions.assertThat(condition).withFailMessage(errorMessage).accepts(actual);
        return this;
    }

    public ResponseAssert andMatchingRule(Predicate<Response> condition) {
        return andMatchingRule(condition, "Predicate validation failed");
    }

    public <T> ResponseAssert canBeDeserializedTo(Class<T> clazz) {
        softAssertions
                .assertThatCode(() -> actual.as(clazz))
                .withFailMessage("Response cannot be deserialized to " + clazz.getName())
                .doesNotThrowAnyException();
        return this;
    }

    public ResponseAssert hasKeyWithValue(String key, String expectedValue) {
        String actualValue = actual.jsonPath().getString(key);
        softAssertions
                .assertThat(actualValue)
                .as("body node validation in response")
                .contains(expectedValue);
        return this;
    }

    public ResponseAssert hasKeyWithValue(String key, boolean expectedValue) {
        boolean actualValue = actual.jsonPath().getBoolean(key);
        softAssertions
                .assertThat(actualValue)
                .as("body node validation in response")
                .isEqualTo(expectedValue);
        return this;
    }

    public ResponseAssert hasKeyWithValue(String key, int expectedValue) {
        int actualValue = actual.jsonPath().getInt(key);
        softAssertions
                .assertThat(actualValue)
                .as("body node validation in response")
                .isEqualTo(expectedValue);
        return this;
    }

    public ResponseAssert contains(String value) {
        softAssertions
                .assertThat(actual.body().asString())
                .as("Body node validation in response")
                .contains(value);
        return this;
    }

    @SneakyThrows
    public ResponseAssert matchesSchemaInFile(String schemaFileName) {
        getJsonSchemaCompareReport(schemaFileName);

        File file = new File(System.getProperty("user.dir") + SCHEMA_RESOURCES_PATH + schemaFileName);
        softAssertions
                .assertThat(JsonSchemaValidator.matchesJsonSchema(file).matches(actual.body().asString()))
                .as("Response schema validation: ")
                .isTrue();

        return this;
    }

    private void getJsonSchemaCompareReport(String schemaFileName)
            throws IOException, ProcessingException {
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonNode expectedJsonNode =
                JsonLoader.fromResource(RELATIVE_PATH_SCHEMAS_FROM_RESOURCES_DIR + schemaFileName);
        JsonSchema jsonSchema = factory.getJsonSchema(expectedJsonNode);

        JsonNode actualJsonNode = JsonLoader.fromString(actual.body().asString());
        ProcessingReport report = jsonSchema.validate(actualJsonNode, true);
        log.info("schema compare report: {}", report);
    }

    public ResponseAssert hasUnAuthenticatedErrorMessage() {
        softAssertions
                .assertThat(actual.jsonPath().getString("message"))
                .isEqualTo("Not authenticated");
        return this;
    }

    public ResponseAssert isEmpty() {
        softAssertions.assertThat(actual.body().asString()).as("Verify response has no body").isEmpty();
        return this;
    }

    public ResponseAssert hasUnAuthorizedUserPermissionErrorMessage() {
        softAssertions
                .assertThat(actual.jsonPath().getString("message"))
                .isEqualTo("The user has not enough right to access this resource");
        return this;
    }

    public ResponseAssert hasUnAuthorizedErrorText() {
        softAssertions.assertThat(actual.jsonPath().getString("error")).isEqualTo("Unauthorized");
        return this;
    }

    public void assertAll() {
        softAssertions.assertAll();
    }
}
