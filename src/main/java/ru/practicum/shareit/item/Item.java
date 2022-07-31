package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;

@Entity
@Table(name = "items")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}
