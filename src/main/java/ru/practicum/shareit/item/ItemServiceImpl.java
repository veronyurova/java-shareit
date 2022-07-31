package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.exception.AccessDeniedException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserService userService,
                           BookingRepository bookingRepository,
                           CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<ItemDto> getOwnerItems(Long userId) {
        List<ItemDto> items = itemRepository.findByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        items.forEach(this::addLastAndNextBooking);
        items.forEach(this::addCommentsList);
        return items;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) return Collections.emptyList();
        return itemRepository.searchItems(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            String message = String.format("There is no item with id %d", itemId);
            log.warn("EntityNotFoundException at ItemServiceImpl.getItemById: {}", message);
            throw new EntityNotFoundException(message);
        }
        ItemDto itemDto = ItemMapper.toItemDto(item.get());
        if (userId.equals(itemDto.getOwner().getId())) addLastAndNextBooking(itemDto);
        addCommentsList(itemDto);
        return itemDto;
    }

    @Override
    public ItemDto addItem(Long userId, @Valid ItemDto itemDto) {
        Item item = ItemMapper.toItemAdd(itemDto);
        item.setOwner(UserMapper.toUser(userService.getUserById(userId)));
        Item addedItem = itemRepository.save(item);
        log.info("ItemServiceImpl.addItem: item {} successfully added", addedItem.getId());
        return ItemMapper.toItemDto(addedItem);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (itemOptional.isEmpty()) {
            String message = String.format("There is no item with id %d", itemId);
            log.warn("EntityNotFoundException at ItemServiceImpl.updateItem: {}", message);
            throw new EntityNotFoundException(message);
        }
        Item item = itemOptional.get();
        Item newItem = ItemMapper.toItemAdd(itemDto);
        if (!userId.equals(item.getOwner().getId())) {
            String message = String.format("User %d is not allowed to change item %d",
                                           userId, itemId);
            log.warn("AccessDeniedException at ItemServiceImpl.updateItem: {}", message);
            throw new AccessDeniedException(message);
        }
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) item.setAvailable(newItem.getAvailable());
        Item updatedItem = itemRepository.save(item);
        log.info("ItemServiceImpl.updateItem: item {} successfully updated", item.getId());
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public void deleteItemById(Long userId, Long itemId) {
        ItemDto itemDto = getItemById(userId, itemId);
        if (!userId.equals(itemDto.getOwner().getId())) {
            String message = String.format("User %d is not allowed to delete item %d",
                                           userId, itemId);
            log.warn("AccessDeniedException at ItemServiceImpl.deleteItemById: {}", message);
            throw new AccessDeniedException(message);
        }
        itemRepository.deleteById(itemId);
        log.info("ItemServiceImpl.deleteItemById: item {} successfully deleted", itemId);
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, @Valid CommentDto commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);
        Booking booking = bookingRepository.findCompletedBooking(userId, itemId,
                BookingStatus.APPROVED, LocalDateTime.now());
        if (booking == null) {
            String message = String.format("User %d haven't rented item %d", userId, itemId);
            log.warn("ValidationException at ItemServiceImpl.addComment: {}", message);
            throw new ValidationException(message);
        }
        comment.setItem(ItemMapper.toItem(getItemById(userId, itemId)));
        comment.setAuthor(UserMapper.toUser(userService.getUserById(userId)));
        Comment addedComment = commentRepository.save(comment);
        log.info("ItemServiceImpl.addComment: comment {} successfully added",
                 addedComment.getId());
        return CommentMapper.toCommentDto(addedComment);
    }

    @Override
    public void addLastAndNextBooking(ItemDto itemDto) {
        Optional<Booking> lastBooking = bookingRepository.findLastBookings(itemDto.getId(),
                LocalDateTime.now()).stream().findFirst();
        if (lastBooking.isPresent()) itemDto.setLastBooking(lastBooking.get());
        Optional<Booking> nextBooking = bookingRepository.findNextBookings(itemDto.getId(),
                LocalDateTime.now()).stream().findFirst();
        if (nextBooking.isPresent()) itemDto.setNextBooking(nextBooking.get());
    }

    @Override
    public void addCommentsList(ItemDto itemDto) {
        List<CommentDto> comments = commentRepository.findAllByItemId(itemDto.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemDto.setComments(comments);
    }
}
