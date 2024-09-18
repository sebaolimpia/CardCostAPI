package etraveli.group.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import etraveli.group.dto.request.CardNumberDto;
import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.response.CardCostListByCostDto;
import etraveli.group.dto.response.CardCostListDto;

public interface ICardCostService {

    CardCostDto addCardCost(CardNumberDto cardNumberDto) throws JsonProcessingException;

    CardCostListDto listCardCost();

    CardCostDto findCardCostById(Integer idCardCost);

    CardCostDto findCardCostByCountry(String country);

    CardCostListByCostDto findCardCostByCost(Double cost);

    CardCostDto updateCardCost(Integer idCardCost, CardCostDto cardCost);

    void deleteCardCost(Integer idCardCost);
}
