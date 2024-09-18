package etraveli.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class containing relevant information from an API call error.
 * error -> Error short description.
 * message -> Full error message.
 * status -> HTTP Status.
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorDto {
    private String error;
    private String message;
    private Integer status;
}
