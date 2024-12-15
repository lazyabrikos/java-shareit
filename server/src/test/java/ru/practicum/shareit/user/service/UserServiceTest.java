package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        UserDto userDto = new UserDto(null, "tmp", "tmp@mail.ru");
        User user = new User(1L, "tmp", "tmp@mail.ru");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDto result = userService.createUser(userDto);
        assertNotNull(result);

    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).deleteById(Mockito.anyLong());
        userService.deleteUser(1L);
        verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void updateUser() {
        Long userId = 1L;
        UserDto userDto = new UserDto(null, "tmp", "tmp@mail.ru");
        User user = new User(1L, "tmp", "tmp@mail.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        UserDto result = userService.updateUser(userDto, userId);
        assertNotNull(result);
    }

    @Test
    void getUser() {
        Long userId = 1L;
        User user = new User(1L, "tmp", "tmp@mail.ru");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        UserDto result = userService.getUserById(userId);
        assertNotNull(result);


    }
}
