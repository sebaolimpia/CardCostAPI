package etraveli.group.integration;

import static etraveli.group.common.Constants.AUTHORIZATION_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import etraveli.group.CardCostApiApplication;
import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.service.impl.AuthenticationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CardCostApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"SCOPE_SUFFIX = integration_test"})
public class IntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    protected <T> RequestEntity<T> createRequestEntityWithAuthorizationToken(
            String username, String password, T body, HttpMethod method) {
        UserRequestDto userRequestDto = UserRequestDto.builder()
                .username(username)
                .password(password)
                .build();
        String token = authenticationService.login(userRequestDto).getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, token);
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return new RequestEntity<>(body, headers, method, null);
    }
}
