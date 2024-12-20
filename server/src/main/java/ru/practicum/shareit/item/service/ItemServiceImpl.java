package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotAvailableForBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;


    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id =" + userId));
        Item item = ItemMapper.mapToItem(itemDto);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Item request not found with id = " +
                            itemDto.getRequestId()));
            item.setItemRequest(itemRequest);
        }
        item.setOwner(user);
        return ItemMapper.mapToItemDto(itemRepository.save(item), null, null, null);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id=" + itemId));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Item has another user");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        item.setId(itemId);
        LocalDateTime lastBooking = bookingRepository.findLastBookingForItem(userId, itemId, LocalDateTime.now())
                .map(Booking::getEnd).orElse(null);
        LocalDateTime nextBooking = bookingRepository.findNextBookingForItem(userId, itemId, LocalDateTime.now())
                .map(Booking::getStart).orElse(null);
        List<CommentDto> comments = findComments(itemId);
        return ItemMapper.mapToItemDto(itemRepository.save(item), lastBooking, nextBooking, comments);

    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id=" + itemId));
        LocalDateTime lastBooking = null;
        LocalDateTime nextBooking = null;
        List<CommentDto> comments = findComments(itemId);
        if (item.getOwner().getId().equals(userId)) {
            lastBooking = bookingRepository.findLastBookingForItem(userId, itemId, LocalDateTime.now())
                    .map(Booking::getEnd).orElse(null);
            nextBooking = bookingRepository.findNextBookingForItem(userId, itemId, LocalDateTime.now())
                    .map(Booking::getStart).orElse(null);
        }

        return ItemMapper.mapToItemDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> getByContext(String text, Long userId) {
        if (text.isBlank()) return List.of();
        List<Item> items = itemRepository.getByContext(text);
        return items.stream()
                .filter(Item::getAvailable)
                .map(item -> ItemMapper.mapToItemDto(item, null, null,
                        findComments(item.getId()))
                )
                .toList();
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        Collection<Item> items = itemRepository.findAll();
        return items.stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(item -> ItemMapper.mapToItemDto(item,
                                bookingRepository.findLastBookingForItem(userId, item.getId(), LocalDateTime.now())
                                        .map(Booking::getEnd).orElse(null),
                                bookingRepository.findNextBookingForItem(userId, item.getId(), LocalDateTime.now())
                                        .map(Booking::getStart).orElse(null),
                                findComments(item.getId())
                        )
                )
                .toList();
    }

    @Override
    @Transactional
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id = " + itemId));
        LocalDateTime created = LocalDateTime.now();
        boolean hasBooking = bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(itemId, userId, created);
        if (!hasBooking) {
            throw new NotAvailableForBooking("No approve bookings");
        }

        commentDto.setAuthorName(user.getName());
        commentDto.setCreated(created);
        Comment comment = CommentMapper.mapCommentDtoToComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        return CommentMapper.mapCommentToCommentDto(commentRepository.save(comment));
    }

    List<CommentDto> findComments(Long itemId) {
        return commentRepository.findAllByItemId(itemId)
                .stream()
                .map(CommentMapper::mapCommentToCommentDto)
                .toList();
    }
}
