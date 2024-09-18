package etraveli.group.util;

import etraveli.group.dto.CardCostDto;
import etraveli.group.dto.response.CardCostListByCostDto;
import etraveli.group.model.CardCost;
import net.bytebuddy.utility.RandomString;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class CardCostDtoFactory {

    public static CardCostDto getCardCostDtoWithIdCardCost() {
        Random random = new Random();
        return CardCostDto.builder()
                .id(random.nextInt(10) + 1)
                .country(RandomString.make().toUpperCase(Locale.ROOT))
                .cost(random.nextDouble(10.00) + 1)
                .build();
    }

    public static CardCostDto getCardCostDtoWithoutIdToAdd() {
        return CardCostDto.builder()
                .country("UY")
                .cost(10.0)
                .build();
    }

    public static CardCostDto getCardCostDtoIntegrationTest() {
        return CardCostDto.builder()
                .id(1)
                .country("US")
                .cost(5.0)
                .build();
    }

    public static CardCostDto getCardCostDtoToUpdateIntegrationTest() {
        return CardCostDto.builder()
                .country("US")
                .cost(10.25)
                .build();
    }

    public static CardCostListByCostDto getCardCostListDtoIntegrationTest() {
        return CardCostListByCostDto.builder()
                .cost(5.0)
                .countries(List.of("US"))
                .build();
    }

    public static CardCostDto mapCardCostToCardCostDto(CardCost cardCost) {
        return CardCostDto.builder()
                .id(cardCost.getId())
                .country(cardCost.getCountry())
                .cost(cardCost.getCost())
                .build();
    }

    public static CardCostListByCostDto mapCardCostListToCardCostListByCostDto(List<CardCost> cardCostList) {
        return CardCostListByCostDto.builder()
                .cost(cardCostList.getFirst().getCost())
                .countries(List.of(cardCostList.getFirst().getCountry()))
                .build();
    }
}
