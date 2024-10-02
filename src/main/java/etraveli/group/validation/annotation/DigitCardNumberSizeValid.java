package etraveli.group.validation.annotation;

import static etraveli.group.common.Constants.VALIDATION_THE_NUMBER_MUST_BE_BETWEEN_8_AND_19_DIGITS;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import etraveli.group.validation.validator.DigitCardNumberSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = DigitCardNumberSizeValidator.class)
public @interface DigitCardNumberSizeValid {

    String message() default VALIDATION_THE_NUMBER_MUST_BE_BETWEEN_8_AND_19_DIGITS;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
