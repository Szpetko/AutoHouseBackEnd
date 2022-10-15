package pl.autohouse.autohousebackend.device;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.autohouse.autohousebackend.device.Device;
import pl.autohouse.autohousebackend.device.DeviceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService{

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getDevice(){
        return deviceRepository.findAll();
    }

    @SneakyThrows
    public void addNewDevice(Device device) {
        Optional<Device> deviceByPinAddress = deviceRepository
                .findDeviceByPinAddress(device.getPinAddress());

        if(deviceByPinAddress.isPresent()) {
            throw new IllegalAccessException("This Pin Address is already in use");
        }
        deviceRepository.save(device);
    }
}
