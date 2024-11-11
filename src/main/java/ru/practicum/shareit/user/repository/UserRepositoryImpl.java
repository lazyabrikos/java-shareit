package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emailUniqueSet = new HashSet<>();
    private Long userIdCount = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++userIdCount);
        users.put(user.getId(), user);
        emailUniqueSet.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Set<String> getEmails() {
        return emailUniqueSet;
    }


}
