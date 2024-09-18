package etraveli.group.dto.response;

import etraveli.group.dto.UsersDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserListDto {
    private List<UsersDto> users;
}
