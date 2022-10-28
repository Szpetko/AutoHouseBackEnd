package pl.autohouse.autohousebackend.device;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {


    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }



    @GetMapping
    public List<Device> getDevice() {
        return deviceService.getDevice();
    }

    @GetMapping("/{deviceId}")
    public Object getDeviceById(@PathVariable Long deviceId){
        return deviceService.getDeviceById(deviceId);
    }

    @PostMapping
    public void addNewDevice(@RequestBody Device device) {
        deviceService.addNewDevice(device);
    }

    @PatchMapping("/{deviceId}")
    public void updateDevice(@PathVariable Long deviceId, @RequestBody Device updatedDevice){
        deviceService.updateDevice(deviceId, updatedDevice);
    }
    @DeleteMapping("/{deviceId}")
    public void deleteDevice(@PathVariable Long deviceId){
        deviceService.deleteDevice(deviceId);
    }

    @PutMapping ("/{deviceId}/toggle")
    public boolean toggleStateDevice(@PathVariable Long deviceId){
        return deviceService.toggleStateDevice(deviceId);
    }

    @PutMapping ("/{deviceId}/stateHigh")
    public boolean highStateDevice(@PathVariable Long deviceId){
        return deviceService.highStateDevice(deviceId);
    }

    @PutMapping ("/{deviceId}/stateLow")
    public boolean lowStateDevice(@PathVariable Long deviceId){
        return deviceService.lowStateDevice(deviceId);
    }

}
