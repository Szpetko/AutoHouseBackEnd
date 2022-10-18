package pl.autohouse.autohousebackend.device;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.autohouse.autohousebackend.room.RoomRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final RoomRepository roomRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, RoomRepository roomRepository) {
        this.deviceRepository = deviceRepository;
        this.roomRepository = roomRepository;
    }

    //Getting all Devices and returns List
    public List<Device> getDevice() {
        return deviceRepository.findAll();
    }

    //Getting Device by ID
    public Object getDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId);
    }

    //Adds new Device
    public void addNewDevice(Device device) {

        //Checking for illegal repeats in PinAddress
        checkIllegalRepeatsInPinAddress(device);

        //Checking for illegal repeats in Name and RoomId
        checkIllegalRepeatsInNameAndRoomId(device);

        //Check if Room exists by roomId
        checkIfRoomExist(device.getRoomId());

        //Adding Device to Database
        deviceRepository.save(device);
    }


    //Delete Devices by id

    public void deleteDevice(Long deviceId) {

        checkIFDeviceExist(deviceId);

        //Deleting Device from Database
        deviceRepository.deleteById(deviceId);
    }

    //Updating Devices by id
    @Transactional
    public void updateDevice(Long deviceId, Device updatedDevice) {


        //Check if Device exists

        //Finds device to update and throws and exception if it does not exist
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device with id "+ deviceId +" does not exist"));


        // name
        if (updatedDevice.getName() != null &&
                updatedDevice.getName().length() > 0 &&
                !Objects.equals(device.getName(), updatedDevice.getName())){

            //Checks for illegal repeats in Name and RoomId
            checkIllegalRepeatsInNameAndRoomId(updatedDevice.getName(), device.getRoomId());

            device.setName(updatedDevice.getName());
        }


        // iconID
        if (updatedDevice.getIconId() != null &&
                !Objects.equals(device.getIconId(), updatedDevice.getIconId())){

            device.setIconId(updatedDevice.getIconId());
        }

        // status
        if (updatedDevice.getStatus() != null &&
                !Objects.equals(device.getStatus(), updatedDevice.getStatus())){

            //TODO toggle device wit deviceId status
            device.setStatus(updatedDevice.getStatus());
        }

        // pinAddress
        if (updatedDevice.getPinAddress() != null &&
                !Objects.equals(device.getPinAddress(), updatedDevice.getPinAddress())){

            checkIllegalRepeatsInPinAddress(updatedDevice);
            device.setPinAddress(updatedDevice.getPinAddress());
        }

        // roomId
        if (updatedDevice.getRoomId() != null &&
                !Objects.equals(device.getRoomId(), updatedDevice.getRoomId())){

            //Checks for illegal repeats in Name and RoomId
            checkIllegalRepeatsInNameAndRoomId(device.getName(), updatedDevice.getRoomId());

            //Check if Room exists by roomId
            checkIfRoomExist(updatedDevice.getRoomId());

            device.setRoomId(updatedDevice.getRoomId());
        }
    }

    //Checks for illegal repeats in PinAddress
    @SneakyThrows
    public void checkIllegalRepeatsInPinAddress(Device device){

        //Don't allow to use taken PinAddress
        //Finding a device in database with the same PinAddress as newly added device
        Optional<Device> deviceByPinAddress = deviceRepository
                .findDeviceByPinAddress(device.getPinAddress());

        //Check if the Devise exist and throws an exception if so
        if (deviceByPinAddress.isPresent()) {
            throw new IllegalAccessException("This Pin Address is already in use");
        }
    }

    //Checks for illegal repeats in Name and RoomId
    @SneakyThrows
    public void checkIllegalRepeatsInNameAndRoomId(Device device){

        //Don't allow the same Name in one Room
        //Finding a device in database with the same Name and roomId as newly added device
        Optional<Device> deviceByNameAndRoomId = deviceRepository
                .findDeviceByNameAndRoomId(device.getName(), device.getRoomId());

        //Check if the Devise exist and throws an exception if so
        if (deviceByNameAndRoomId.isPresent()) {
            throw new IllegalAccessException("This Name is already in use in this Room");
        }
    }

    //Checks for illegal repeats in Name and RoomId
    @SneakyThrows
    public void checkIllegalRepeatsInNameAndRoomId(String name, Long roomId){

        //Don't allow the same Name in one Room
        //Finding a device in database with the same Name and roomId as newly added device
        Optional<Device> deviceByNameAndRoomId = deviceRepository
                .findDeviceByNameAndRoomId(name, roomId);

        //Check if the Devise exist and throws an exception if so
        if (deviceByNameAndRoomId.isPresent()) {
            throw new IllegalAccessException("There is a Device with the same Name in this Room");
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

    //Check if Device exists
    @SneakyThrows
    public void checkIFDeviceExist(Long deviceId){

        //Throws an exception if Device doesn't exist
        if (!deviceRepository.existsById(deviceId)) {
            throw new IllegalAccessException("Device with id "+ deviceId +" does not exist");
        }
    }



}
