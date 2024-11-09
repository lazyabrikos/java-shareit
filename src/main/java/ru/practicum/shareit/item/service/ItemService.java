package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto ItemDto, Long userId);

    ItemDto updateItem(ItemDto ItemDto, Long itemId, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getByContext(String text, Long userId);

    List<ItemDto> getAll(Long userId);
}
