package pl.autohouse.autohousebackend.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.autohouse.autohousebackend.device.Device;

import java.util.Optional;


@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {


    @Query("SELECT d FROM Device d WHERE d.pinAddress = ?1")
    Optional<Device> findDeviceByPinAddress(int pinAddress);
}
