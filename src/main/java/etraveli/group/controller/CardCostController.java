package etraveli.group.controller;

import static etraveli.group.common.Constants.LOCALHOST_URL;
import static etraveli.group.common.Constants.BASE_URL;
import static etraveli.group.common.Constants.VALIDATION_COUNTRY_CODE_MUST_BE_EXACTLY_2_CHARACTERS_LONG;
import static etraveli.group.common.Constants.VALIDATION_THE_CARD_COST_ID_MUST_BE_A_POSITIVE_NUMBER;
import static etraveli.group.common.Constants.VALIDATION_THE_COST_MUST_BE_ZERO_OR_A_POSITIVE_NUMBER;

import com.fasterxml.jackson.core.JsonProcessingException;
import etraveli.group.dto.request.CardNumberDto;
import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.response.CardCostListByCostDto;
import etraveli.group.dto.response.CardCostListDto;
import etraveli.group.service.ICardCostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.Map;

@RestController
@CrossOrigin(LOCALHOST_URL)
@RequestMapping(BASE_URL)
@Validated
public class CardCostController {

    private final ICardCostService cardCostService;

    @Autowired
    public CardCostController(ICardCostService cardCostService) {
        this.cardCostService = cardCostService;
    }

    @PostMapping("")
    public ResponseEntity<CardCostDto> addCardCost(@Valid @RequestBody CardNumberDto cardNumberDto)
            throws JsonProcessingException {
        return ResponseEntity.ok(cardCostService.addCardCost(cardNumberDto));
    }

    @GetMapping("/all")
    public ResponseEntity<CardCostListDto> listCardCost() {
        return ResponseEntity.ok(cardCostService.listCardCost());
    }

    @GetMapping("/{idCardCost}")
    public ResponseEntity<CardCostDto> findCardCostById(
            @Positive(message = VALIDATION_THE_CARD_COST_ID_MUST_BE_A_POSITIVE_NUMBER)
            @PathVariable Integer idCardCost) {
        return ResponseEntity.ok(cardCostService.findCardCostById(idCardCost));
    }

    @GetMapping("/country")
    public ResponseEntity<CardCostDto> findCardCostByCountry(
            @Size(min = 2, max = 2, message = VALIDATION_COUNTRY_CODE_MUST_BE_EXACTLY_2_CHARACTERS_LONG)
            @RequestParam String country) {
        return ResponseEntity.ok(cardCostService.findCardCostByCountry(country));
    }

    @GetMapping("/cost")
    public ResponseEntity<CardCostListByCostDto> findCardCostByCost(
            @PositiveOrZero(message = VALIDATION_THE_COST_MUST_BE_ZERO_OR_A_POSITIVE_NUMBER)
            @RequestParam Double cost) {
        return ResponseEntity.ok(cardCostService.findCardCostByCost(cost));
    }

    @PutMapping("/{idCardCost}")
    public ResponseEntity<CardCostDto> updateCardCost(
            @Positive(message = VALIDATION_THE_CARD_COST_ID_MUST_BE_A_POSITIVE_NUMBER)
            @PathVariable Integer idCardCost,
            @Valid @RequestBody CardCostDto cardCost) {
        return ResponseEntity.ok(cardCostService.updateCardCost(idCardCost, cardCost));
    }

    @DeleteMapping("/{idCardCost}")
    public ResponseEntity<Map<String, Boolean>> deleteCardCost(
            @Positive(message = VALIDATION_THE_CARD_COST_ID_MUST_BE_A_POSITIVE_NUMBER)
            @PathVariable Integer idCardCost) {
        cardCostService.deleteCardCost(idCardCost);
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}
