package etraveli.group.validation.validator;

import etraveli.group.validation.annotation.DigitBinSizeValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DigitBinSizeValidator implements ConstraintValidator<DigitBinSizeValid, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        int length = value.toString().length();
        return length == 6;
    }
}
