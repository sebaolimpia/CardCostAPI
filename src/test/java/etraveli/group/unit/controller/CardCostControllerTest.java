package etraveli.group.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import com.fasterxml.jackson.core.JsonProcessingException;
import etraveli.group.controller.CardCostController;
import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.request.CardNumberDto;
import etraveli.group.dto.response.CardCostListByCostDto;
import etraveli.group.dto.response.CardCostListDto;
import etraveli.group.service.ICardCostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CardCostControllerTest {

    @Mock
    ICardCostService cardCostService;

    @InjectMocks
    CardCostController cardCostController;

    @Test
    @DisplayName("Add card cost successful test.")
    public void addCardCostSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardNumberDto cardNumberDto = new CardNumberDto();
        CardCostDto expected = new CardCostDto();
        when(cardCostService.addCardCost(cardNumberDto)).thenReturn(expected);

        // WHEN
        ResponseEntity<CardCostDto> current = cardCostController.addCardCost(cardNumberDto);

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("List card cost successful test.")
    public void listCardCostSuccessfulTest() {
        // GIVEN
        CardCostListDto expected = new CardCostListDto();
        when(cardCostService.listCardCost()).thenReturn(expected);

        // WHEN
        ResponseEntity<CardCostListDto> current = cardCostController.listCardCost();

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Find card cost by id successful test.")
    public void findCardCostByIdSuccessfulTest() {
        // GIVEN
        CardCostDto expected = new CardCostDto();
        when(cardCostService.findCardCostById(anyInt())).thenReturn(expected);

        // WHEN
        ResponseEntity<CardCostDto> current = cardCostController.findCardCostById(anyInt());

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Find card cost by country successful test.")
    public void findCardCostByCountrySuccessfulTest() {
        // GIVEN
        CardCostDto expected = new CardCostDto();
        when(cardCostService.findCardCostByCountry(anyString())).thenReturn(expected);

        // WHEN
        ResponseEntity<CardCostDto> current = cardCostController.findCardCostByCountry(anyString());

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Find card cost by cost successful test.")
    public void findCardCostByCostSuccessfulTest() {
        // GIVEN
        CardCostListByCostDto expected = new CardCostListByCostDto();
        when(cardCostService.findCardCostByCost(anyDouble())).thenReturn(expected);

        // WHEN
        ResponseEntity<CardCostListByCostDto> current = cardCostController.findCardCostByCost(anyDouble());

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Update card cost successful test.")
    public void updateCardCostSuccessfulTest() {
        // GIVEN
        CardCostDto expected = new CardCostDto();
        when(cardCostService.updateCardCost(1, expected)).thenReturn(expected);

        // WHEN
        ResponseEntity<CardCostDto> current = cardCostController.updateCardCost(1, expected);

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }

    @Test
    @DisplayName("Delete card cost successful test.")
    public void deleteCardCostSuccessfulTest() {
        // GIVEN
        Map<String, Boolean> expected = Map.of("deleted", true);
        doNothing().when(cardCostService).deleteCardCost(anyInt());

        // WHEN
        ResponseEntity<Map<String, Boolean>> current = cardCostController.deleteCardCost(anyInt());

        // THEN
        assertEquals(expected, current.getBody());
        assertEquals(OK, current.getStatusCode());
    }
}
