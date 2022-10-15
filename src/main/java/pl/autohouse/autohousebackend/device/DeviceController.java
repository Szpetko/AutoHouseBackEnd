package pl.autohouse.autohousebackend.device;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {


    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService){
        this.deviceService = deviceService;
    }

    @GetMapping
    public List<Device> getDevice(){
        return deviceService.getDevice();
    }

    @PostMapping
    public void addNewDevice(@RequestBody Device device){
        deviceService.addNewDevice(device);
    }

}
