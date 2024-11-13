package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {
    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Long userId);

    void deleteUser(Long userId);

    Collection<User> getAll();

    Set<String> getEmails();
}
