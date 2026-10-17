package ru.student.hotel.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.student.hotel.dto.RoomRequest;
import ru.student.hotel.dto.RoomResponse;
import ru.student.hotel.exception.BusinessRuleException;
import ru.student.hotel.exception.ResourceNotFoundException;
import ru.student.hotel.model.Room;
import ru.student.hotel.model.RoomStatus;
import ru.student.hotel.repository.RoomRepository;

import java.util.List;

@Service
@Transactional
public class RoomService {
    private final RoomRepository rooms;

    public RoomService(RoomRepository rooms) { this.rooms = rooms; }

    @Transactional(readOnly = true)
    public List<RoomResponse> findAll() { return rooms.findAll().stream().map(this::toResponse).toList(); }

    @Transactional(readOnly = true)
    public RoomResponse findById(Long id) { return toResponse(getEntity(id)); }

    public RoomResponse create(RoomRequest request) {
        String number = request.number().trim().toUpperCase();
        if (rooms.existsByNumberIgnoreCase(number)) throw new BusinessRuleException("Номер комнаты " + number + " уже существует.");
        Room room = new Room();
        apply(room, request, number);
        if (room.getStatus() == null) room.setStatus(RoomStatus.AVAILABLE);
        return toResponse(rooms.save(room));
    }

    public RoomResponse update(Long id, RoomRequest request) {
        Room room = getEntity(id);
        String number = request.number().trim().toUpperCase();
        if (!number.equalsIgnoreCase(room.getNumber()) && rooms.existsByNumberIgnoreCase(number)) {
            throw new BusinessRuleException("Другой номер комнаты уже использует номер " + number + ".");
        }
        apply(room, request, number);
        if (request.status() != null) room.setStatus(request.status());
        return toResponse(rooms.save(room));
    }

    public void delete(Long id) {
        Room room = getEntity(id);
        try {
            rooms.delete(room);
            rooms.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessRuleException("Нельзя удалить номер, на который ссылаются бронирования.");
        }
    }

    public Room getEntity(Long id) {
        return rooms.findById(id).orElseThrow(() -> new ResourceNotFoundException("Номер с ID " + id + " не найден."));
    }

    public Room getEntityForUpdate(Long id) {
        return rooms.findByIdForUpdate(id).orElseThrow(() -> new ResourceNotFoundException("Номер с ID " + id + " не найден."));
    }

    private void apply(Room room, RoomRequest request, String normalizedNumber) {
        room.setNumber(normalizedNumber);
        room.setType(request.type());
        room.setCapacity(request.capacity());
        room.setPricePerNight(request.pricePerNight());
        if (request.status() != null) room.setStatus(request.status());
    }

    public RoomResponse toResponse(Room r) {
        return new RoomResponse(r.getId(), r.getNumber(), r.getType(), r.getCapacity(), r.getPricePerNight(), r.getStatus());
    }
}
