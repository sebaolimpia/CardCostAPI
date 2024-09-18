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
public class UserSignUpResponseDto {

    private String username;

    private String password;

    private List<RoleDto> userRoles;
}