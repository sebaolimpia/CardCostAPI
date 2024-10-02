package etraveli.group.integration.controller;

import static etraveli.group.common.Constants.INFO_BIN_BASE_URL;
import static etraveli.group.util.BinInfoDtoFactory.*;
import static etraveli.group.util.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.response.BinInfoListDto;
import etraveli.group.integration.IntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BinInfoControllerTest extends IntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Add BIN info already exist.")
    public void addBinInfoAlreadyExistTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_INFO_ALREADY_EXIST, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with five digits BIN.")
    public void addBinInfoFiveDigitsBinTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setBin(12345);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with seven digits BIN.")
    public void addBinInfoSevenDigitsBinTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setBin(1234567);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with one letter country.")
    public void addBinInfoWithOneLetterCountryTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCountry("a");

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_VIOLATION_FIELD_COUNTRY, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with more than two letter country.")
    public void addBinInfoWithMoreThanTwoLetterCountryTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCountry("abc");

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_VIOLATION_FIELD_COUNTRY, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with null country.")
    public void addBinInfoWithNullCountryTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCountry(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_NULL_COUNTRY, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with non-positive cost.")
    public void addBinInfoWithNonPositiveCostTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCost(-1.0);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_VIOLATION_FIELD_COST, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with null cost.")
    public void addBinInfoWithNullCostTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCost(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_NULL_COST, responseEntity.getBody());
    }

    @Test
    @DisplayName("Add BIN info with five digits BIN and one letter country and non-positive cost.")
    public void addBinInfoFiveDigitsBinAndOneLetterCountryAndNonPositiveCostTest() throws JsonProcessingException {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setBin(12345);
        requestDto.setCountry("b");
        requestDto.setCost(-1.0);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        var body = responseEntity.getBody();
        assertNotNull(body);
        var list = objectMapper.readValue(body, List.class);
        var messagesExpectedPresents = body.matches(REGEX_LENGTH_BIN) && body.matches(REGEX_LENGTH_COUNTRY)
                && body.matches(REGEX_POSITIVE_COST);
        assertTrue(messagesExpectedPresents);
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Add BIN info successful.")
    public void addBinInfoSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoWithoutId();
        String jsonExpected = objectMapper.writeValueAsString(requestDto);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL, POST, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @Order(1)
    @DisplayName("List BIN info with a BIN info load by configuration.")
    public void listBinInfoSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        BinInfoListDto responseDTO = new BinInfoListDto(List.of(getBinInfoDtoIntegrationTest()));
        String jsonExpected = objectMapper.writeValueAsString(responseDTO);

        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity =
                testRestTemplate.exchange(INFO_BIN_BASE_URL + "/all", GET, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @DisplayName("Find BIN info by BIN not found in DB.")
    public void findBinInfoByBinNotFoundTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + "/111111", GET, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Find BIN info with five digits of BIN.")
    public void findBinInfoFiveDigitsBinTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + "/12345", GET, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_CONSTRAINT_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @DisplayName("Find BIN info with seven digits of BIN.")
    public void findBinInfoSevenDigitsBinTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + "/1234567", GET, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_CONSTRAINT_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @DisplayName("Find BIN info successful.")
    public void findBinInfoSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        BinInfoDto responseDTO = getBinInfoDtoIntegrationTest();
        String jsonExpected = objectMapper.writeValueAsString(responseDTO);

        var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, POST);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + "/421821", GET, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info non-positive id.")
    public void updateBinInfoNonPositiveIdTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_NON_POSITIVE, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_INFO_CONSTRAINT_VIOLATION_BY_ID, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with five digits BIN.")
    public void updateBinInfoFiveDigitsBinTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoWithoutId();
        requestDto.setBin(12345);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with seven digits BIN.")
    public void updateBinInfoSevenDigitsBinTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoWithoutId();
        requestDto.setBin(1234567);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_VIOLATION_FIELD_LENGTH_DIGITS, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with one letter country.")
    public void updateBinInfoWithOneLetterCountryTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCountry("a");

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_VIOLATION_FIELD_COUNTRY, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with more than two letter country.")
    public void updateBinInfoWithMoreThanTwoLetterCountryTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCountry("abc");

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_CARD_COST_VIOLATION_FIELD_COUNTRY, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with null country.")
    public void updateBinInfoWithNullCountryTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCountry(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_NULL_COUNTRY, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with non-positive cost.")
    public void updateBinInfoWithNonPositiveCostTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCost(-1.0);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_VIOLATION_FIELD_COST, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with null cost.")
    public void updateBinInfoWithNullCostTest() {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setCost(null);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_NULL_COST, responseEntity.getBody());
    }

    @Test
    @DisplayName("Update BIN info with five digits BIN and one letter country and non-positive cost.")
    public void updateBinInfoFiveDigitsBinAndOneLetterCountryAndNonPositiveCostTest() throws JsonProcessingException {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoIntegrationTest();
        requestDto.setBin(12345);
        requestDto.setCountry("b");
        requestDto.setCost(-1.0);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        var body = responseEntity.getBody();
        assertNotNull(body);
        var list = objectMapper.readValue(body, List.class);
        var messagesExpectedPresents = body.matches(REGEX_LENGTH_BIN) && body.matches(REGEX_LENGTH_COUNTRY)
                && body.matches(REGEX_POSITIVE_COST);
        assertTrue(messagesExpectedPresents);
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("Update BIN info successful.")
    public void updateBinInfoSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        BinInfoDto requestDto = getBinInfoDtoToUpdateIntegrationTest();
        String jsonExpected = objectMapper.writeValueAsString(requestDto);

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, requestDto, PUT);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, PUT, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }

    @Test
    @DisplayName("Delete BIN info not found in DB.")
    public void deleteBinInfoNotFoundTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, null, DELETE);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_NOT_FOUND, DELETE, requestEntity, String.class);

        // THEN
        assertEquals(NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_INFO_NOT_FOUND_BY_ID, responseEntity.getBody());
    }

    @Test
    @DisplayName("Delete BIN info non-positive id.")
    public void deleteBinInfoNonPositiveIdTest() {
        // GIVEN
        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, null, DELETE);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_NON_POSITIVE, DELETE, requestEntity, String.class);

        // THEN
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(ERROR_BIN_INFO_CONSTRAINT_VIOLATION_BY_ID, responseEntity.getBody());
    }

    @Test
    @DisplayName("Delete BIN info successful.")
    public void deleteBinInfoSuccessfulTest() {
        // GIVEN
        String jsonExpected = "{\"deleted\":true}";

        var requestEntity = createRequestEntityWithAuthorizationToken(ADMIN, PASSWORD, null, DELETE);

        // WHEN
        ResponseEntity<String> responseEntity = testRestTemplate
                .exchange(INFO_BIN_BASE_URL + URL_ID_1, DELETE, requestEntity, String.class);

        // THEN
        assertEquals(OK, responseEntity.getStatusCode());
        assertEquals(APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(jsonExpected, responseEntity.getBody());
    }
}
