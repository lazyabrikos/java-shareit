package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item createItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItemById(Long itemId);

    List<Item> getByContext(String text);

    Collection<Item> getAll();
}
