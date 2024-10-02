package etraveli.group.service.impl;

import static etraveli.group.common.Constants.*;
import static org.springframework.http.HttpMethod.GET;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import etraveli.group.dto.BinInfoDto;
import etraveli.group.dto.request.CardNumberDto;
import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.response.CardCostListByCostDto;
import etraveli.group.dto.response.CardCostListDto;
import etraveli.group.exception.BadRequestException;
import etraveli.group.exception.NotFoundException;
import etraveli.group.model.CardCost;
import etraveli.group.repository.ICardCostRepository;
import etraveli.group.service.IBinInfoService;
import etraveli.group.service.ICardCostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class CardCostService implements ICardCostService {

    private final ICardCostRepository cardCostRepository;

    private final IBinInfoService binInfoService;

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Autowired
    public CardCostService(ICardCostRepository cardCostRepository,
                           IBinInfoService binInfoService,
                           ModelMapper modelMapper,
                           ObjectMapper objectMapper,
                           RestTemplate restTemplate) {
        this.cardCostRepository = cardCostRepository;
        this.binInfoService = binInfoService;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    /**
     * Add new card cost.
     * @param cardNumberDto Card number.
     * @return Card cost added.
     */
    @Override
    @CacheEvict(value = "card_cost", allEntries = true)
    public CardCostDto addCardCost(CardNumberDto cardNumberDto) throws JsonProcessingException {
        String cardNumber = String.valueOf(cardNumberDto.getCardNumber());
        Integer bin = Integer.valueOf(cardNumber.substring(0, 6));
        // Check if the BIN-country relation already exists.
        BinInfoDto binInfoDto = binInfoService.findBinInfoByBin(bin);
        if (binInfoDto != null) {
            return CardCostDto.builder()
                .country(binInfoDto.getCountry())
                .cost(binInfoDto.getCost())
                .build();
        }
        String countryInUpperCase = getCountryFromResponseEntity(bin);

        // The country already has a card cost, but the BIN info isn't present.
        CardCost cardCost = cardCostRepository.findByCountry(countryInUpperCase).orElse(null);
        if (cardCost != null) {
            // Before to return the card cost, we add the BIN info to avoid future calls to the external API.
            binInfoService.addBinInfo(BinInfoDto.builder()
                    .bin(bin)
                    .country(countryInUpperCase)
                    .cost(cardCost.getCost())
                    .build());
            return CardCostDto.builder()
                .country(cardCost.getCountry())
                .cost(cardCost.getCost())
                .build();
        }

        CardCostDto cardCostDto = CardCostDto.builder()
            .country(countryInUpperCase)
            .cost(10.0)
            .build();
        cardCostRepository.save(modelMapper.map(cardCostDto, CardCost.class));

        // After add the card cost, we add the BIN info to avoid future calls to the external API.
        binInfoService.addBinInfo(BinInfoDto.builder()
            .bin(bin)
            .country(cardCostDto.getCountry())
            .cost(cardCostDto.getCost())
            .build());
        return cardCostDto;
    }

    /**
     * Return all card costs.
     * @return List of card costs.
     */
    @Override
    @Cacheable("card_cost")
    public CardCostListDto listCardCost() {
        List<CardCost> cardCosts = cardCostRepository.findAll();
        CardCostListDto cardCostListDto = CardCostListDto.builder().build();
        if (cardCosts.isEmpty()) {
            cardCostListDto.setCardCost(List.of());
            return cardCostListDto;
        }
        cardCostListDto.setCardCost(cardCosts.stream().map(cardCost ->
                modelMapper.map(cardCost, CardCostDto.class)).toList());
        return cardCostListDto;
    }

    /**
     * Return card cost by id.
     * @param idCardCost Card cost id.
     * @return Card cost.
     */
    @Override
    @Cacheable("card_cost")
    public CardCostDto findCardCostById(Integer idCardCost) {
        CardCost cardCost = findCardCost(idCardCost);
        return modelMapper.map(cardCost, CardCostDto.class);
    }

    /**
     * Return card cost by country.
     * @param country Country code.
     * @return Card cost.
     */
    @Override
    @Cacheable("card_cost")
    public CardCostDto findCardCostByCountry(String country) {
        CardCost cardCost = cardCostRepository.findByCountry(country).orElse(null);
        if (cardCost == null) {
            throw new NotFoundException(String.format(EXCEPTION_NOT_FOUND_CARD_COST_WITH_COUNTRY, country));
        }
        return modelMapper.map(cardCost, CardCostDto.class);
    }

    /**
     * Return all the card cost with a specific cost.
     * @param cost Cost.
     * @return List of card costs.
     */
    @Override
    @Cacheable("card_cost")
    public CardCostListByCostDto findCardCostByCost(Double cost) {
        List<CardCost> cardCostList = cardCostRepository.findByCost(cost).orElse(null);
        if (cardCostList == null) {
            throw new NotFoundException(String.format(EXCEPTION_NOT_FOUND_CARD_COST_WITH_COST, cost));
        }
        return CardCostListByCostDto.builder()
            .cost(cost)
            .countries(cardCostList.stream().map(CardCost::getCountry).toList())
            .build();
    }

    /**
     * Update card cost.
     * @param idCardCost Card cost id.
     * @param cardCostDto Card cost update.
     * @return Card cost updated.
     */
    @Override
    @CacheEvict(value = "card_cost", allEntries = true)
    public CardCostDto updateCardCost(Integer idCardCost, CardCostDto cardCostDto) {
        CardCost cardCost = findCardCost(idCardCost);
        cardCostDto.setId(cardCost.getId());
        // The country code must be in uppercase.
        cardCostDto.setCountry(cardCostDto.getCountry().toUpperCase(Locale.ROOT));
        cardCostRepository.save(modelMapper.map(cardCostDto, CardCost.class));
        // After update the card cost, return it without id
        cardCostDto.setId(null);
        return cardCostDto;
    }

    /**
     * Delete card cost by id.
     * @param idCardCost Card cost id.
     */
    @Override
    @CacheEvict(value = "card_cost", allEntries = true)
    public void deleteCardCost(Integer idCardCost) {
        CardCost cardCost = findCardCost(idCardCost);
        cardCostRepository.delete(cardCost);
    }

    private CardCost findCardCost(Integer idCardCost) {
        CardCost cardCost = cardCostRepository.findById(idCardCost).orElse(null);
        if (cardCost == null) {
            throw new NotFoundException(String.format(EXCEPTION_NOT_FOUND_CARD_COST_WITH_ID, idCardCost));
        }
        return cardCost;
    }

    private String getCountryFromResponseEntity(Integer bin) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Version", "3");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        String url = String.format(BIN_LIST_URL, bin);

        ResponseEntity<String> response = restTemplate.exchange(url, GET, requestEntity, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new NotFoundException(String.format(EXCEPTION_NOT_FOUND_COUNTRY_WITH_BIN, bin));
        }

        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode countryJsonNode = rootNode.get("country");
        if (countryJsonNode == null) {
            throw new NotFoundException(EXCEPTION_NOT_FOUND_COUNTRY_FROM_EXTERNAL_API_CALL);
        }
        JsonNode alpha2JsonNode = countryJsonNode.get("alpha2");
        if (alpha2JsonNode == null) {
            throw new NotFoundException(EXCEPTION_NOT_FOUND_COUNTRY_FROM_EXTERNAL_API_CALL);
        }
        String country = alpha2JsonNode.asText();
        if (country.length() != 2) {
            throw new BadRequestException(VALIDATION_COUNTRY_CODE_API_CALL_MUST_BE_EXACTLY_2_CHARACTERS_LONG);
        }
        return country.toUpperCase(Locale.ROOT);
    }
}
