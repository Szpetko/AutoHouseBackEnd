package pl.autohouse.autohousebackend.room;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;
import pl.autohouse.autohousebackend.device.DeviceRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    //Getting all Rooms and returns a List
    public List<Room> getDevice() {
        return roomRepository.findAll();
    }

    //Adds new Room
    public void addNewDevice(Room room) {

        //Check for repeats in Name
        checkIllegalRepeatsInName(room.getName());

        //Adding Room to Database
        roomRepository.save(room);
    }


    //Delete Room by id
    public void deleteRoom(Long roomId) {

        //Check if Room exists
        checkIfRoomExist(roomId);

        roomRepository.deleteById(roomId);
    }


    //Updating Room by id
    @Transactional
    public void updateRoom(Long roomId, Room updatedRoom) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room with id "+ roomId +" does not exist"));
        // name
        if (updatedRoom.getName() != null &&
                !Objects.equals(updatedRoom.getName(), room.getName())){

            //Check for repeats in Name
            checkIllegalRepeatsInName(updatedRoom.getName());

            room.setName(updatedRoom.getName());
        }

        // iconId
        if (updatedRoom.getIconId() != null && !Objects.equals(room.getIconId(), updatedRoom.getIconId())){

            room.setIconId(updatedRoom.getIconId());
        }

    }


    //Check if Room exists
    @SneakyThrows
    public void checkIfRoomExist(Long roomId){

        //Check if the Room exist and throws an exception if so
        if (!roomRepository.existsById(roomId)) {
            throw new IllegalAccessException("This Room does not exist");
        }
    }

    //Check for repeats in Name
    @SneakyThrows
    public void checkIllegalRepeatsInName(String name){

        //Don't allow to use taken Room Name
        //Finding a room in database with the same Name as newly added room
        Optional<Room> roomByName = roomRepository
                .findDeviceByName(name);

        //Check if the Room exist and throws an exception if so
        if (roomByName.isPresent()) {
            throw new IllegalAccessException("This Room Name is already in use");
        }
    }


}
