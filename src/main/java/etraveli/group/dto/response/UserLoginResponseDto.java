package etraveli.group.dto.response;

import etraveli.group.dto.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponseDto {

    private Integer id;
    private String username;
    private String token;
    private List<RoleDto> roles;
}