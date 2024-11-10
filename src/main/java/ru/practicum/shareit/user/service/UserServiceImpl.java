package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        Optional<User> savedUser = userRepository.getByEmail(user.getEmail());
        if (savedUser.isPresent()) {
            throw new EmailException("Email is already exists");
        }
        return UserMapper.mapToUserDto(userRepository.createUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User user = UserMapper.mapToUser(userDto);


        Optional<User> savedWithEmail = userRepository.getByEmail(user.getEmail());
        if (savedWithEmail.isPresent()) {
            throw new EmailException("Email is already exists");
        }

        User savedUser = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id= " + userId));

        if (user.getEmail() == null) {
            user.setEmail(savedUser.getEmail());
        }

        if (user.getName() == null) {
            user.setName(savedUser.getName());
        }

        return UserMapper.mapToUserDto(userRepository.updateUser(user, userId));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.mapToUserDto(userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id= " + userId)));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }

    @Override
    public List<UserDto> getAll() {

        Collection<User> users = userRepository.getAll();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }
}
