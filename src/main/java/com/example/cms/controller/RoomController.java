package com.example.cms.controller;

import com.example.cms.controller.dto.RoomDto;
import com.example.cms.controller.exceptions.ResidenceNotFoundException;
import com.example.cms.controller.exceptions.RoomNotFoundException;
import com.example.cms.model.entity.Residence;
import com.example.cms.model.entity.Room;
import com.example.cms.model.entity.RoomKey;
import com.example.cms.model.repository.ResidenceRepository;
import com.example.cms.model.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {
    @Autowired
    private final RoomRepository repository;

    @Autowired
    private ResidenceRepository residenceRepository;

    public RoomController(RoomRepository repository) {
        this.repository = repository;
    }


    @DeleteMapping("/rooms/{id}")
    void deleteRoom(@PathVariable("id") Long roomNum) {
        repository.deleteById(roomNum);
    }

    @GetMapping("/rooms")
    List<Room> retrieveAllRooms() {
        return repository.findAll();
    }

    /**
    @PostMapping("/rooms")
    Room createRoom(@RequestBody Room newRoom) {
        return repository.save(newRoom);
    } **/

    @PostMapping("/rooms")
    Room createRoom(@RequestBody RoomDto roomDto){
        Room newRoom = new Room();
        newRoom.setRoomKey(new RoomKey(roomDto.getRoomNum(), roomDto.getResidenceId()));
        newRoom.setRoomType(roomDto.getRoomType());
        newRoom.setCapacity(roomDto.getRoomCap());
        newRoom.setResidence(residenceRepository.findById(roomDto.getResidenceId()).orElseThrow(() -> new ResidenceNotFoundException(roomDto.getResidenceId())));
        newRoom.setRoomN(roomDto.getRoomNum());
        return repository.save(newRoom);
    }

    @GetMapping("/rooms/{id}")
    Room retrieveRoom(@PathVariable("id") Long roomID) {
        return repository.findById(roomID)
                .orElseThrow(() -> new RoomNotFoundException(roomID));
    }

    @PutMapping("/rooms/{id}")
    Room updateRooms(@RequestBody Room newRoom, @PathVariable("id") Long roomId) {
        return repository.findById(roomId)
                .map(room -> {
                    room.setRoomType(newRoom.getRoomType());
                    return repository.save(room);
                })
                .orElseGet(() -> {
                    newRoom.setRoomN(roomId);
                    return repository.save(newRoom);
                });
    }
}
