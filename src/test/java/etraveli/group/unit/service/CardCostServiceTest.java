package etraveli.group.unit.service;

import static etraveli.group.util.CardCostDtoFactory.*;
import static etraveli.group.util.CardCostFactory.getCardCostWithIdCardCost;
import static etraveli.group.util.CardCostFactory.mapCardCostDtoToCardCost;
import static etraveli.group.util.CardNumberDtoFactory.getCardNumberDtoWithIdCardNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.request.CardNumberDto;
import etraveli.group.dto.response.CardCostListByCostDto;
import etraveli.group.dto.response.CardCostListDto;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.CardCost;
import etraveli.group.repository.ICardCostRepository;
import etraveli.group.service.impl.CardCostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class CardCostServiceTest {

    // Necessary for modelMapper to call the real methods of the ModelMapper class
    @Spy
    private ModelMapper modelMapper;

    // Necessary for modelMapper to call the real methods of the ObjectMapper class
    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private ICardCostRepository cardCostRepository;

    @InjectMocks
    private CardCostService cardCostService;

    @Test
    @DisplayName("Add card cost successful.")
    void addCardCostSuccessfulTest() throws JsonProcessingException {
        // GIVEN
        CardNumberDto cardNumberDto = getCardNumberDtoWithIdCardNumber();

        CardCostDto expectedCardCostDto = getCardCostDtoWithoutIdToAdd();
        CardCost cardCost = mapCardCostDtoToCardCost(expectedCardCostDto);

        when(cardCostRepository.findByCountry(anyString())).thenReturn(Optional.empty());
        when(cardCostRepository.save(any(CardCost.class))).thenReturn(cardCost);

        // WHEN
        CardCostDto currentCardCostDto = cardCostService.addCardCost(cardNumberDto);

        // THEN
        InOrder inOrder = inOrder(cardCostRepository, cardCostRepository);
        inOrder.verify(cardCostRepository).findByCountry(anyString());
        inOrder.verify(cardCostRepository).save(cardCost);
        assertEquals(expectedCardCostDto, currentCardCostDto);
    }

    private static Stream<Arguments> getCardCostListParameters() {
        CardCost cardCost = getCardCostWithIdCardCost();
        CardCostDto cardCostDto = mapCardCostToCardCostDto(cardCost);
        return Stream.of(
                Arguments.of(List.of(cardCost), List.of(cardCostDto)),
                Arguments.of(List.of(), List.of()));
    }

    @ParameterizedTest
    @MethodSource("getCardCostListParameters")
    @DisplayName("List card cost with exist and not exist card code.")
    public void listCardCostTest(List<CardCost> cardCostList, List<CardCostDto> cardCostListDto) {
        // GIVEN
        CardCostListDto expectedCardCostList = new CardCostListDto(cardCostListDto);

        when(cardCostRepository.findAll()).thenReturn(cardCostList);

        // WHEN
        CardCostListDto currentCardCodeList = cardCostService.listCardCost();

        // THEN
        verify(cardCostRepository).findAll();
        assertEquals(expectedCardCostList, currentCardCodeList);
    }

    @Test
    @DisplayName("Find card cost by id successful.")
    void findCardCostByIdSuccessfulTest() {
        // GIVEN
        CardCost cardCost = getCardCostWithIdCardCost();
        CardCostDto expectedCardCostDto = mapCardCostToCardCostDto(cardCost);

        when(cardCostRepository.findById(anyInt())).thenReturn(Optional.of(cardCost));

        // WHEN
        CardCostDto currentCardCostDto = cardCostService.findCardCostById(anyInt());

        // THEN
        verify(cardCostRepository).findById(anyInt());
        assertEquals(expectedCardCostDto, currentCardCostDto);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Find card cost by id doesn't found in DB.")
    void findCardCostByIdUnSuccessfulTest(CardCost nullCardCost) {
        // GIVEN
        when(cardCostRepository.findById(anyInt())).thenReturn(Optional.ofNullable(nullCardCost));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () -> cardCostService.findCardCostById(anyInt()));
        verify(cardCostRepository).findById(anyInt());
    }

    @Test
    @DisplayName("Find card cost by country successful.")
    void findCardCostByCountrySuccessfulTest() {
        // GIVEN
        CardCost cardCost = getCardCostWithIdCardCost();
        CardCostDto expectedCardCostDto = mapCardCostToCardCostDto(cardCost);

        when(cardCostRepository.findByCountry(anyString())).thenReturn(Optional.of(cardCost));

        // WHEN
        CardCostDto currentCardCostDto = cardCostService.findCardCostByCountry(anyString());

        // THEN
        verify(cardCostRepository).findByCountry(anyString());
        assertEquals(expectedCardCostDto, currentCardCostDto);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Find card cost by country doesn't found in DB.")
    void findCardCostByCountryUnSuccessfulTest(CardCost nullCardCost) {
        // GIVEN
        when(cardCostRepository.findByCountry(anyString())).thenReturn(Optional.ofNullable(nullCardCost));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () -> cardCostService.findCardCostByCountry(anyString()));
        verify(cardCostRepository).findByCountry(anyString());
    }

    @Test
    @DisplayName("Find card cost by cost successful.")
    void findCardCostByCostSuccessfulTest() {
        // GIVEN
        List<CardCost> cardCostList = List.of(getCardCostWithIdCardCost());
        CardCostListByCostDto expectedCardCostListDto = mapCardCostListToCardCostListByCostDto(cardCostList);

        when(cardCostRepository.findByCost(eq(cardCostList.getFirst().getCost()))).thenReturn(Optional.of(cardCostList));

        // WHEN
        CardCostListByCostDto currentCardCostListDto =
                cardCostService.findCardCostByCost(cardCostList.getFirst().getCost());

        // THEN
        verify(cardCostRepository).findByCost(eq(cardCostList.getFirst().getCost()));
        assertEquals(expectedCardCostListDto, currentCardCostListDto);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Find card cost by cost doesn't found in DB.")
    void findCardCostByCostUnSuccessfulTest(List<CardCost> nullCardCost) {
        // GIVEN
        when(cardCostRepository.findByCost(anyDouble())).thenReturn(Optional.ofNullable(nullCardCost));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () -> cardCostService.findCardCostByCost(anyDouble()));
        verify(cardCostRepository).findByCost(anyDouble());
    }

    @Test
    @DisplayName("Update card cost successful.")
    void updateCardCostSuccessfulTest() {
        // GIVEN
        CardCostDto expectedCardCostDto = getCardCostDtoWithIdCardCost();
        CardCost cardCost = mapCardCostDtoToCardCost(expectedCardCostDto);
        when(cardCostRepository.findById(eq(1))).thenReturn(Optional.of(cardCost));
        when(cardCostRepository.save(any(CardCost.class))).thenReturn(cardCost);

        // WHEN
        CardCostDto currentCardCostDto = cardCostService.updateCardCost(1, expectedCardCostDto);

        // THEN
        InOrder inOrder = inOrder(cardCostRepository, cardCostRepository);
        inOrder.verify(cardCostRepository).findById(eq(1));
        inOrder.verify(cardCostRepository).save(cardCost);
        assertEquals(expectedCardCostDto, currentCardCostDto);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Update card cost doesn't found in DB.")
    void updateCardCostUnSuccessfulTest(CardCost nullCardCost) {
        // GIVEN
        when(cardCostRepository.findById(anyInt())).thenReturn(Optional.ofNullable(nullCardCost));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () ->
                cardCostService.updateCardCost(1, any(CardCostDto.class)));
        verify(cardCostRepository).findById(anyInt());
        verifyNoMoreInteractions(cardCostRepository);
    }

    @Test
    @DisplayName("Delete card cost successful.")
    void deleteCardCostSuccessfulTest() {
        // GIVEN
        CardCost cardCost = getCardCostWithIdCardCost();
        when(cardCostRepository.findById(eq(1))).thenReturn(Optional.of(cardCost));

        doNothing().when(cardCostRepository).delete(cardCost);

        // WHEN

        // THEN
        assertDoesNotThrow(() -> cardCostService.deleteCardCost(1));

        InOrder inOrder = inOrder(cardCostRepository, cardCostRepository);
        inOrder.verify(cardCostRepository).findById(eq(1));
        inOrder.verify(cardCostRepository).delete(cardCost);
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Delete card cost doesn't found in DB.")
    void deleteCardCostUnSuccessfulTest(CardCost nullCardCost) {
        // GIVEN
        when(cardCostRepository.findById(anyInt())).thenReturn(Optional.ofNullable(nullCardCost));

        // WHEN

        // THEN
        assertThrows(NotFoundException.class, () -> cardCostService.deleteCardCost(anyInt()));
        verify(cardCostRepository).findById(anyInt());
        verifyNoMoreInteractions(cardCostRepository);
    }
}
