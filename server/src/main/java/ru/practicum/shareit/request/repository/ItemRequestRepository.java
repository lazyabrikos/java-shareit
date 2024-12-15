package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT ir " +
            "FROM ItemRequest ir " +
            "WHERE ir.requester.id = ?1 " +
            "ORDER BY ir.created DESC")
    List<ItemRequest> findAllByRequester(Long userId);


    @Query("SELECT ir " +
            "FROM ItemRequest ir " +
            "WHERE ir.requester.id <> ?1 " +
            "ORDER BY ir.created DESC")
    List<ItemRequest> findOtherUsersRequests(Long userId);
}
