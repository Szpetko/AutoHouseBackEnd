package pl.autohouse.autohousebackend.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    //Search for device with specified PinAddress
    @Query("SELECT d FROM Device d WHERE d.pinAddress = ?1")
    Optional<Device> findDeviceByPinAddress(int pinAddress);

    //Search for device with specified device Name and roomId
    @Query("SELECT d FROM Device d WHERE d.name = ?1 and d.roomId =?2")
    Optional<Device> findDeviceByNameAndRoomId(String name, Long roomId);
}
