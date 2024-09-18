package etraveli.group.util;

import etraveli.group.dto.CardCostDto;
import etraveli.group.model.CardCost;
import net.bytebuddy.utility.RandomString;
import java.util.Locale;
import java.util.Random;

public class CardCostFactory {

    public static CardCost getCardCostWithIdCardCost() {
        Random random = new Random();
        return CardCost.builder()
                .id(random.nextInt(10) + 1)
                .country(RandomString.make().toUpperCase(Locale.ROOT))
                .cost(random.nextDouble(10.00) + 1)
                .build();
    }

    public static CardCost mapCardCostDtoToCardCost(CardCostDto cardCostDto) {
        return CardCost.builder()
                .id(cardCostDto.getId())
                .country(cardCostDto.getCountry())
                .cost(cardCostDto.getCost())
                .build();
    }
}
