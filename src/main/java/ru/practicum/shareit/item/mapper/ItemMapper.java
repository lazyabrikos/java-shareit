package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

public class ItemMapper {
    public static Item mapToItem(ItemDto itemDto) {
        return new Item(itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    public static ItemDto mapToItemDto(Item item, LocalDateTime lastBooking,
                                       LocalDateTime nextBooking, List<CommentDto> comments) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), lastBooking, nextBooking, comments);
    }
}
