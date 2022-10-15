package pl.autohouse.autohousebackend.room;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.autohouse.autohousebackend.room.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


}
