package pl.autohouse.autohousebackend.room;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.autohouse.autohousebackend.device.Device;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    //Search for room with specified room Name
    @Query("SELECT r FROM Room r WHERE r.name = ?1")
    Optional<Room> findDeviceByName(String name);
}
