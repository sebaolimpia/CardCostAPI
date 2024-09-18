package etraveli.group.dto.request;

import static etraveli.group.common.Constants.VALIDATION_THE_PASSWORD_CANNOT_BE_EMPTY;
import static etraveli.group.common.Constants.VALIDATION_THE_USERNAME_CANNOT_BE_EMPTY;

import jakarta.validation.constraints.NotEmpty;
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
public class UserRequestDto {

    @NotEmpty(message = VALIDATION_THE_USERNAME_CANNOT_BE_EMPTY)
    private String username;

    @NotEmpty(message = VALIDATION_THE_PASSWORD_CANNOT_BE_EMPTY)
    private String password;
}
