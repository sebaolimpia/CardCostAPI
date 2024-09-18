package etraveli.group.controller;

import etraveli.group.dto.UsersDto;
import etraveli.group.dto.request.UserRequestDto;
import etraveli.group.dto.response.UserListDto;
import etraveli.group.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static etraveli.group.common.Constants.LOCALHOST_URL;
import static etraveli.group.common.Constants.USERS_BASE_URL;

@RestController
@CrossOrigin(LOCALHOST_URL)
@RequestMapping(USERS_BASE_URL)
public class UsersController {

    private final IUsersService usersService;

    @Autowired
    public UsersController(IUsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/admin")
    public ResponseEntity<UsersDto> createAdministrator(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(usersService.createAdministrator(userRequestDto));
    }

    @GetMapping("/all")
    public ResponseEntity<UserListDto> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }
}