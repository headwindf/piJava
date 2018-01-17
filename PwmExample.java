
import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.util.Console;

public class PwmExample {
    String[] args = new String[]{};

    public GpioPinPwmOutput initPWMPin(Pin gpioPin,int pwmRange,int pwmClock) throws InterruptedException{
        GpioController gpio = GpioFactory.getInstance();

        Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class,    // pin provider class to obtain pin instance from
                gpioPin,  // default pin if no pin argument found
                args);             // argument array to search in

        GpioPinPwmOutput pwm = gpio.provisionPwmOutputPin(pin);
        com.pi4j.wiringpi.Gpio.pwmSetMode(com.pi4j.wiringpi.Gpio.PWM_MODE_MS);
        com.pi4j.wiringpi.Gpio.pwmSetRange(pwmRange);//1200
        com.pi4j.wiringpi.Gpio.pwmSetClock(pwmClock);//320
        return pwm;
    }

    public void setPwmPin(GpioPinPwmOutput pwm,int rate) throws InterruptedException{
        pwm.setPwm(rate);//100
    }
}
