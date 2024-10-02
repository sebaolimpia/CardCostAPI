package etraveli.group.dto.request;

import etraveli.group.validation.annotation.DigitCardNumberSizeValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class CardNumberDto {

    @DigitCardNumberSizeValid
    private BigInteger cardNumber;
}
