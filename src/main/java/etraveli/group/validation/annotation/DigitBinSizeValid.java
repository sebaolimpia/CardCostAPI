package etraveli.group.validation.annotation;

import static etraveli.group.common.Constants.VALIDATION_BIN_MUST_BE_EXACTLY_6_CHARACTERS_LONG;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import etraveli.group.validation.validator.DigitBinSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = DigitBinSizeValidator.class)
public @interface DigitBinSizeValid {

    String message() default VALIDATION_BIN_MUST_BE_EXACTLY_6_CHARACTERS_LONG;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
