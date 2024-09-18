package etraveli.group.util;

import etraveli.group.dto.request.CardNumberDto;

import java.math.BigInteger;

public class CardNumberDtoFactory {

    public static CardNumberDto getCardNumberDtoWithIdCardNumber() {
        return CardNumberDto.builder()
                .cardNumber(new BigInteger("4218210123456789"))
                .build();
    }

    public static CardNumberDto getCardNumberDtoWithSevenDigitsCardNumber() {
        return CardNumberDto.builder()
                .cardNumber(new BigInteger("1234567"))
                .build();
    }

    public static CardNumberDto getCardNumberDtoWithTwentyDigitsCardNumber() {
        return CardNumberDto.builder()
                .cardNumber(new BigInteger("12345678912345678912"))
                .build();
    }
}
