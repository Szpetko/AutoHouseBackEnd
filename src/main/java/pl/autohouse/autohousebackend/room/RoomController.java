package pl.autohouse.autohousebackend.room;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getDevice(){
        return roomService.getDevice();
    }

    @PostMapping
    public void addNewDevice(@RequestBody Room room){
        roomService.addNewDevice(room);
    }
}
