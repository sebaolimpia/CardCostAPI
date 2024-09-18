package etraveli.group.dto.response;

import etraveli.group.dto.CardCostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardCostListDto {

    private List<CardCostDto> cardCost;
}
