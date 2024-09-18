package etraveli.group.dto;

import static etraveli.group.common.Constants.VALIDATION_THE_COUNTRY_CANNOT_BE_EMPTY;
import static etraveli.group.common.Constants.VALIDATION_COUNTRY_CODE_MUST_BE_EXACTLY_2_CHARACTERS_LONG;
import static etraveli.group.common.Constants.VALIDATION_THE_COST_MUST_BE_ZERO_OR_A_POSITIVE_NUMBER;
import static etraveli.group.common.Constants.VALIDATION_THE_COST_CANNOT_BE_EMPTY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class CardCostDto {

    private Integer id;

    @Size(min = 2, max = 2, message = VALIDATION_COUNTRY_CODE_MUST_BE_EXACTLY_2_CHARACTERS_LONG)
    @NotEmpty(message = VALIDATION_THE_COUNTRY_CANNOT_BE_EMPTY)
    private String country;

    @PositiveOrZero(message = VALIDATION_THE_COST_MUST_BE_ZERO_OR_A_POSITIVE_NUMBER)
    @NotNull(message = VALIDATION_THE_COST_CANNOT_BE_EMPTY)
    private Double cost;
}
