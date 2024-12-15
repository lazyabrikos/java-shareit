package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(" SELECT i FROM Item i " +
            " WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            " OR UPPER(i.description) like UPPER(concat('%', ?1, '%'))")
    List<Item> getByContext(String text);

    @Query(" SELECT i " +
            "FROM Item i " +
            "WHERE i.itemRequest.id = ?1")
    List<Item> findAllByRequestId(Long userId);

    List<Item> findAllByOwnerId(Long userId);
}
