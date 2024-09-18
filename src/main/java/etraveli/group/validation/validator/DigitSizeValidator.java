package etraveli.group.validation.validator;

import etraveli.group.validation.annotation.DigitSizeValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class DigitSizeValidator implements ConstraintValidator<DigitSizeValid, BigInteger> {

    @Override
    public boolean isValid(BigInteger value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        int length = value.toString().length();
        return length >= 8 && length <= 19;
    }
}
