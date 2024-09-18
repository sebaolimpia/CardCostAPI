package etraveli.group.integration.controller;

import static etraveli.group.common.Constants.BASE_URL;
import static etraveli.group.util.CardCostDtoFactory.*;
import static etraveli.group.util.CardNumberDtoFactory.*;
import static etraveli.group.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.request.CardNumberDto;
import etraveli.group.dto.response.CardCostListByCostDto;
import etraveli.group.dto.response.CardCostListDto;
import etraveli.group.integration.IntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CardCostControllerTest extends IntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("List card cost with an card cost load by configuration.")
    public void listCardCostSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardCostListDto responseDTO = new CardCostListDto(List.of(getCardCostDtoIntegrationTest()));
        String jsonExpected = objectMapper.writeValueAsString(responseDTO);

        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(BASE_URL + "/all", GET, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @Order(2)
    @DisplayName("Find card cost by id doesn't found in DB.")
    public void findCardCostByIdNotFoundTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_DOES_NOT_EXIST, GET, requestEntity, String.class);

        // THEN
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_NOT_FOUND_BY_ID, responseEntity.getBody());
    }

    @Test
    @Order(3)
    @DisplayName("Find card cost by id non-positive.")
    public void findCardCostByIdNonPositiveTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_NON_POSITIVE, GET, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_ID, responseEntity.getBody());
    }

    @Test
    @Order(4)
    @DisplayName("Find card cost by id successful.")
    public void findCardCostByIdSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardCostDto responseDTO = getCardCostDtoIntegrationTest();
        String jsonExpected = objectMapper.writeValueAsString(responseDTO);

        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, GET, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @Order(5)
    @DisplayName("Find card cost by country doesn't found in DB.")
    public void findCardCostByCountryNotFoundTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + "/country?country=XX", GET, requestEntity, String.class);

        // THEN
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_NOT_FOUND_BY_COUNTRY, responseEntity.getBody());
    }

    @Test
    @Order(6)
    @DisplayName("Find card cost by country without letters.")
    public void findCardCostByCountryWithoutLettersTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + COUNTRY_QUERY_PARAM, GET, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_COUNTRY, responseEntity.getBody());
    }

    @Test
    @Order(7)
    @DisplayName("Find card cost by country with one letter.")
    public void findCardCostByCountryOneLetterTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + COUNTRY_QUERY_PARAM + "u", GET, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_COUNTRY, responseEntity.getBody());
    }

    @Test
    @Order(8)
    @DisplayName("Find card cost by country with more than two letter.")
    public void findCardCostByCountryMoreThanTwoLetterTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + COUNTRY_QUERY_PARAM + "uss", GET, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_COUNTRY, responseEntity.getBody());
    }

    @Test
    @Order(9)
    @DisplayName("Find card cost by country successful.")
    public void findCardCostByCountrySuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardCostDto responseDTO = getCardCostDtoIntegrationTest();
        String jsonExpected = objectMapper.writeValueAsString(responseDTO);

        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + COUNTRY_QUERY_PARAM + "US", GET, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @Order(10)
    @DisplayName("Find card cost non-positive cost.")
    public void findCardCostByCostNonPositiveTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + COST_QUERY_PARAM + "-1", GET, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_COST, responseEntity.getBody());
    }

    @Test
    @Order(11)
    @DisplayName("Find card cost by cost successful.")
    public void findCardCostByCostSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardCostListByCostDto responseDTO = getCardCostListDtoIntegrationTest();
        String jsonExpected = objectMapper.writeValueAsString(responseDTO);

        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + COST_QUERY_PARAM + "5.0", GET, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @Order(12)
    @DisplayName("Update card cost non-positive id.")
    public void updateCardCostNonPositiveIdTest() {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_NON_POSITIVE, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_ID, responseEntity.getBody());
    }

    @Test
    @Order(13)
    @DisplayName("Update card cost with null country.")
    public void updateCardCostWithNullCountryTest() {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();
        requestDto.setCountry(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_NULL_COUNTRY, responseEntity.getBody());
    }

    @Test
    @Order(14)
    @DisplayName("Update card cost with null cost.")
    public void updateCardCostWithNullCostTest() {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();
        requestDto.setCost(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_NULL_COST, responseEntity.getBody());
    }

    @Test
    @Order(15)
    @DisplayName("Update card cost with null country and cost.")
    public void updateCardCostWithNullCountryAndCostTest() throws JsonProcessingException {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();
        requestDto.setCountry(null);
        requestDto.setCost(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        var body = responseEntity.getBody();
        assertNotNull(body);
        var list = objectMapper.readValue(body, List.class);
        var messagesExpectedPresents = body.matches(REGEX_CARD_COST_NULL_COUNTRY)
                && body.matches(REGEX_CARD_COST_NULL_COST);
        assertTrue(messagesExpectedPresents);
        assertEquals(2, list.size());
    }

    @Test
    @Order(16)
    @DisplayName("Update card cost with one letter country.")
    public void updateCardCostWithOneLetterCountryTest() {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();
        requestDto.setCountry("a");

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_VIOLATION_FIELD_COUNTRY, responseEntity.getBody());
    }

    @Test
    @Order(17)
    @DisplayName("Update card cost with more than two letter country.")
    public void updateCardCostWithMoreThanTwoLetterCountryTest() {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();
        requestDto.setCountry("arr");

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_VIOLATION_FIELD_COUNTRY, responseEntity.getBody());
    }

    @Test
    @Order(18)
    @DisplayName("Update card cost non-positive cost.")
    public void updateCardCostNonPositiveCostTest() {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();
        requestDto.setCost(-1.0);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_VIOLATION_FIELD_COST, responseEntity.getBody());
    }

    @Test
    @Order(19)
    @DisplayName("Update card cost with one letter country and non-positive cost.")
    public void updateCardCostOneLetterCountryAndNonPositiveCostTest() throws JsonProcessingException {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoIntegrationTest();
        requestDto.setCountry("b");
        requestDto.setCost(-1.0);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        var body = responseEntity.getBody();
        assertNotNull(body);
        var list = objectMapper.readValue(body, List.class);
        var messagesExpectedPresents = body.matches(REGEX_CARD_COST_LENGTH_COUNTRY)
                && body.matches(REGEX_CARD_COST_POSITIVE_COST);
        assertTrue(messagesExpectedPresents);
        assertEquals(2, list.size());
    }

    @Test
    @Order(20)
    @DisplayName("Update card cost successful.")
    public void updateCardCostSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardCostDto requestDto = getCardCostDtoToUpdateIntegrationTest();
        String jsonExpected = objectMapper.writeValueAsString(requestDto);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @Order(21)
    @DisplayName("Delete card cost doesn't found in DB.")
    public void deleteCardCostNotFoundTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, null, DELETE);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_DOES_NOT_EXIST, DELETE, requestEntity, String.class);

        // THEN
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_NOT_FOUND_BY_ID, responseEntity.getBody());
    }

    @Test
    @Order(22)
    @DisplayName("Delete card cost non-positive id.")
    public void deleteCardCostNonPositiveTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, null, DELETE);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_NON_POSITIVE, DELETE, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_CONSTRAINT_VIOLATION_BY_ID, responseEntity.getBody());
    }

    @Test
    @Order(23)
    @DisplayName("Delete card cost successful.")
    public void deleteCardCostSuccessfulTest() {
        // GIVEN
        String jsonExpected = "{\"deleted\":true}";

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, null, DELETE);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL + URL_ID_CARD_COST_1, DELETE, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @Order(24)
    @DisplayName("Add card cost with seven digits of card number.")
    public void addCardCostSevenDigitsCardNumberTest() {
        // GIVEN
        CardNumberDto requestDto = getCardNumberDtoWithSevenDigitsCardNumber();

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_NUMBER_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @Order(25)
    @DisplayName("Add card cost with twenty digits of card number.")
    public void addCardCostTwentyDigitsCardNumberTest() {
        // GIVEN
        CardNumberDto requestDto = getCardNumberDtoWithTwentyDigitsCardNumber();

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_NUMBER_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @Order(26)
    @DisplayName("Add card cost with null card number.")
    public void addCardCostNullCardNumberTest() {
        // GIVEN
        CardNumberDto requestDto = new CardNumberDto();
        requestDto.setCardNumber(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_NUMBER_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @Order(27)
    @DisplayName("Add card cost successful.")
    public void addCardCostSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardNumberDto requestDto = getCardNumberDtoWithIdCardNumber();

        CardCostDto expectedDto = getCardCostDtoWithoutIdToAdd();
        String jsonExpected = objectMapper.writeValueAsString(expectedDto);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }
}
