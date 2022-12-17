package pl.autohouse.autohousebackend.device;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.autohouse.autohousebackend.gpio.DigitalOutputGPIO;
import pl.autohouse.autohousebackend.room.RoomRepository;

import javax.annotation.PreDestroy;
import javax.transaction.Transactional;
import java.util.ArrayList;
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

        //Set current status for each Device from the Device List
        List<Device> deviceList = deviceRepository.findAll();
        for (Device device : deviceList){
            DigitalOutputGPIO tempDGOut = getDGOutputByDeviceId(device.getDeviceId());
            device.setStatus(tempDGOut.getDigitalOutput().isOn());
        }

        return deviceList;
    }

    //Getting Device by ID
    public Object getDeviceById(Long deviceId) {

        //Set current status of the Device
        Optional<Device> device = deviceRepository.findById(deviceId);
        DigitalOutputGPIO tempDGOut = getDGOutputByDeviceId(deviceId);
        device.get().setStatus(tempDGOut.getDigitalOutput().isOn());

        return device;
    }

    //Adds new Device
    public void addNewDevice(Device device) {

        //Checking for illegal repeats in PinAddress
        checkIllegalRepeatsInPinAddress(device);

        //Checking for illegal repeats in Name and RoomId
        checkIllegalRepeatsInNameAndRoomId(device);

        //Check if Room exists by roomId
        checkIfRoomExist(device.getRoomId());

        //Add device to a outputGPIOList
        addDeviceToOutputGPIOList(device);

        //Adding Device to Database
        deviceRepository.save(device);
    }


    //Delete Devices by id

    public void deleteDevice(Long deviceId) {

        //Check if Device exists by deviceId
        checkIFDeviceExist(deviceId);

        //Delete device form outputGPIOList
        outputGPIOList.remove(getDGOutputByDeviceId(deviceId));

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
                !Objects.equals(getDGOutputByDeviceId(deviceId).getDigitalOutput().isOn(), updatedDevice.getStatus())){

            //Toggle device wit deviceId status
            toggleStateDevice(deviceId);
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

        // isFavourite
        if (updatedDevice.getIsFavourite() != null &&
                !Objects.equals(device.getIsFavourite(), updatedDevice.getIsFavourite())){

            device.setIsFavourite(updatedDevice.getIsFavourite());
        }
    }


    //PI4J
    Context pi4j = Pi4J.newAutoContext();

    //List of DigitalOutputDevices
    public List<DigitalOutputGPIO> outputGPIOList = new ArrayList<>();

    //Initializing devices form the Database and adding them to DigitalOutputDevices List
    @EventListener(ApplicationReadyEvent.class)
    public void createGPIO(){
        System.out.println("Device Start to initialize");
        List<Device> devices = deviceRepository.findAll();

        try {
            for (Device device : devices) {
                addDeviceToOutputGPIOList(device);
            }
        }
        catch (Exception e){
            System.out.println("Initialize devices error");
        }

    }

    //Toggling Device State
    public boolean toggleStateDevice(Long deviceId) {

        //Toggle DevicePin
        DigitalOutputGPIO temp = getDGOutputByDeviceId(deviceId);

        return temp.toggleState();


    }

    //Setting Device State High
    public boolean highStateDevice(Long deviceId) {

        //Turn DevicePin High
        DigitalOutputGPIO temp = getDGOutputByDeviceId(deviceId);

        return temp.stateHigh();
    }

    //Setting Device State Low
    public boolean lowStateDevice(Long deviceId) {

        //Turn DevicePin Low
        DigitalOutputGPIO temp = getDGOutputByDeviceId(deviceId);

        return temp.stateLow();
    }


    //Turning off PI4J before shutdown
    @PreDestroy
    private void gpioShutDown(){
        pi4j.shutdown();
        System.out.println("Turning off gpio");
    }


    //FUNCTIONS

    //Find DigitalOutput from the outputGPIOList (using pinAddress) by deviceId
    public DigitalOutputGPIO getDGOutputByDeviceId(Long deviceId){
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device with id "+ deviceId +" does not exist"));

             for(DigitalOutputGPIO digitalOutputGPIO : outputGPIOList){
                 if (digitalOutputGPIO.getPinAddress() == device.getPinAddress()){
                     return digitalOutputGPIO;
                 }
             }
             return null;
    }

    //Adds Device to OutputGPIOList and initialize Pin by its pinAddress
    public void  addDeviceToOutputGPIOList(Device device){
        int pin = device.getPinAddress();
        DigitalOutputGPIO temp = new DigitalOutputGPIO(pi4j, pin);
        outputGPIOList.add(temp);
        System.out.println("Device initialize pin: " + pin);
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
            throw new IllegalArgumentException("This Pin Address is already in use");
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
            throw new IllegalArgumentException("This Name is already in use in this Room");
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
            throw new IllegalArgumentException("There is a Device with the same Name in this Room");
        }
    }

    //Check if Room exists
    @SneakyThrows
    public void checkIfRoomExist(Long roomId){

        //Check if the Room exist and throws an exception if so
        if (!roomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("This Room does not exist");
        }
    }

    //Check if Device exists
    @SneakyThrows
    public void checkIFDeviceExist(Long deviceId){

        //Throws an exception if Device doesn't exist
        if (!deviceRepository.existsById(deviceId)) {
            throw new IllegalArgumentException("Device with id "+ deviceId +" does not exist");
        }
    }


}
