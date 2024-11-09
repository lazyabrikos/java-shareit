package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Got POST request with body {}", userDto);
        UserDto response = userService.createUser(userDto);
        log.info("Send response for POST request with body {}", response);
        return response;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Get GET request /users/{}", userId);
        UserDto response = userService.getUserById(userId);
        log.info("Send response for GET request with body {}", response);
        return response;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Get PATCH for user with id = {} with body {}", userId, userDto);
        UserDto response = userService.updateUser(userDto, userId);
        log.info("Send response for PATCH request with body {}", response);
        return response;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Get DELETE request /users/{}", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Get GET request /users");
        List<UserDto> response = userService.getAll();
        log.info("Send response fot GET request with body {}", response);
        return response;
    }
}
