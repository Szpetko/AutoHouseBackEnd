package pl.autohouse.autohousebackend.gpio;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;

import java.awt.*;

public class DigitalOutputGPIO extends Component {

    // !!!HIGH and LOW states are switched because LOW state was needed to turn on relay instead of the HIGH state!!!
    // If there was no switch, all devices would turn on when the power to the raspberry was cut off or raspberry would break down


    private final DigitalOutput digitalOutput;
    int pinAddress;

    public DigitalOutputGPIO(Context pi4j, int pinAddress){
        this.digitalOutput = pi4j.create(buildDigitalOutputConfig(pi4j, pinAddress));
        this.pinAddress = pinAddress;
    }


    public boolean stateHigh() {
        digitalOutput.state(DigitalState.LOW);
        return digitalOutput.isOn();
    }


    public boolean stateLow() {
        digitalOutput.state(DigitalState.HIGH);
        return digitalOutput.isOn();
    }


    public boolean toggleState() {
        digitalOutput.toggle();
        return digitalOutput.isOn();
    }

    public int getPinAddress(){
        return pinAddress;
    }


    public DigitalOutput getDigitalOutput() {
        return digitalOutput;
    }



    protected DigitalOutputConfig buildDigitalOutputConfig(Context pi4j, int pinAddress) {
        return DigitalOutput.newConfigBuilder(pi4j)
                .id("BCM" + pinAddress)
                .name("Digital Output with pinAddress" + pinAddress)
                .address(pinAddress)
                .provider("pigpio-digital-output")
                .onState(DigitalState.LOW)
                .initial(DigitalState.HIGH)
                .shutdown(DigitalState.HIGH)
                .build();
    }
}
