package etraveli.group.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import etraveli.group.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Builder
public class RoleDto {

    @JsonIgnore
    private Integer id;
    private RoleEnum name;
    private String description;
}
