package ru.practicum.shareit.item.repository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();
    private static Long ITEM_ID_COUNT = 0L;

    @Override
    public Item createItem(Item item) {
        item.setId(++ITEM_ID_COUNT);
        items.put(ITEM_ID_COUNT, item);
        return item;
    }

    @Override
    public Item updateItem(Item item, Long itemId) {
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getByContext(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
    }

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }
}
