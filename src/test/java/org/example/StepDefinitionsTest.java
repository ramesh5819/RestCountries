package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StepDefinitionsTest {
    private Response response;

    @Given("I send a GET request to {string}")
    public void i_send_a_get_request_to(String url) {
        response = RestAssured.get(url);

        System.out.println(response.asString());
    }

    @Then("the total number of countries should be {int}")
    public void the_total_number_of_countries_should_be(Integer expectedCount) {
        List<?> countries = response.jsonPath().getList("countries");
        Assertions.assertEquals(expectedCount, countries.size(), "Country count mismatch!");
    }

    @Then("the response should match the expected schema")
    public void the_response_should_match_the_expected_schema() {
        System.out.println("Schema Path: " + getClass().getClassLoader().getResource("schemas/restcountries_schema.json"));

        response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/restcountries_schema.json"));
    }

    @Then("South Africa should include {string} in its official languages")
    public void south_africa_should_include_in_its_official_languages(String language) {
        List<LinkedHashMap<String, Object>> countries = response.jsonPath().getList("$");

        LinkedHashMap<String, Object> southAfrica = countries.stream()
                .filter(country -> {
                    LinkedHashMap<String, String> name = (LinkedHashMap<String, String>) country.get("name");
                    return name != null && "South Africa".equalsIgnoreCase(name.get("common"));
                })
                .findFirst()
                .orElseThrow(() -> new AssertionError("South Africa not found in the response"));

        LinkedHashMap<String, String> officialLanguages = (LinkedHashMap<String, String>) southAfrica.get("languages");

        System.out.println("Languages for South Africa: " + officialLanguages);

        Assertions.assertTrue(officialLanguages != null && officialLanguages.containsValue(language),
                "Language '" + language + "' is not included in South Africa's official languages");
    }
}