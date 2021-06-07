package ge.bog.conversion.controller;

import ge.bog.conversion.model.UserDto;
import ge.bog.conversion.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserService userService;

    @PostMapping
    ResponseEntity<String> createUser(@RequestBody UserDto userDto){
        String response = userService.createUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
