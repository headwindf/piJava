
import com.pi4j.wiringpi.Gpio;

public class WiringPiGpioInterrupt {

    /**
     * Example program to demonstrate the usage of WiringPiISR()
     *
     * @param args
     * @throws InterruptedException
     */
    public void initGpioInterrupt() throws InterruptedException {

        //System.out.println("<--Pi4J--> GPIO interrupt test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1) {
            //System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // configure pins as input pins
        Gpio.pinMode(0, Gpio.INPUT) ;

        // configure pins with pull down resistance
        Gpio.pullUpDnControl(0, Gpio.PUD_UP);

        // here is another approach using a custom callback class/instance instead of an anonymous method
        InterruptCallback risingCallbackInstance = new InterruptCallback("RISING");

        // setup a pin interrupt callbacks for pin 2
        Gpio.wiringPiISR(0, Gpio.INT_EDGE_FALLING, risingCallbackInstance);
        //Gpio.wiringPiISR(0, Gpio.INT_EDGE_BOTH, risingCallbackInstance);

        // wait for user to exit program
        //System.console().readLine("Press <ENTER> to exit program.\r\n");
    }
}
