package pl.autohouse.autohousebackend.room;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getRoom() {
        return roomService.getRoom();
    }

    @GetMapping("/{roomId}")
    public Object getRoomById(@PathVariable Long roomId){
        return roomService.getRoomById(roomId);
    }

    @PostMapping
    public void addNewDevice(@RequestBody Room room) {
        roomService.addNewDevice(room);
    }

    @PatchMapping("/{roomId}")
    public void updateRoom(@PathVariable Long roomId, @RequestBody Room updatedRoom){
        roomService.updateRoom(roomId, updatedRoom);
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoom(roomId);
    }
}
