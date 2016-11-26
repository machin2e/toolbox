package camp.computer.clay;

// This is a stand-in for the C API that will be exposed to JavaScript/JerryScript.
public class VirtualPlatformInterface {

    // Restrictions:
    // - Call constraints... can't call one action from another? Just operate on flow of data?

    // <USER_API>
    // Core:
    // TODO: Publish and subscribe to hardware events (e.g., port change events, message events, motion events, LED events, power events)
    // TODO: Hardware state/status API
    // TODO: IASM API?
    // TODO: IMU motion/orientation API
    // TODO: Portable API (i.e., FPGA interface control)
    // TODO: Wired Communication Protocol APIs (e.g., I2C, SPI, UART, MIDI, touch?)
    // TODO: Current Sense API (part of Portable API?)
    // TODO: LED API (part of Portable?)
    // TODO: Touch API
    // TODO: Clock/Time API
    // TODO: Internet API (i.e., HTTP, TCP, UDP)
    // TODO: Thread/Mesh API: getPeers()
    // TODO: Bluetooth API
    // TODO: Flash storage API
    // TODO: Internet storage API
    // TODO: Utility functions (i.e., math?, data format adapters, data transforms, data filters, etc.)

    // Extra:
    // TODO: Action/Script management/metaprogramming API (e.g., getActions(), addAction(), removeAction(), etc.)
    // TODO: API for accessing Internet-based 3rd party services (e.g., REST HTTP API registered with Clay)
    // TODO: Smartphone app API (i.e., for communicating to app, drawing on screen, etc.)
    // TODO: "Screen" API (i.e., for drawing to Chromecast, for example)
    // </USER_API>

    // <DEBUG_API>
    // TODO: Bootloader API
    // TODO: Status LED API
    // </DEBUG_API>

    public void getPorts() {
        System.out.println("Platform.getPorts()");
    }

    public void getPort() {
        System.out.println("Platform.getPort()");
    }

    public boolean setPort(int portIndex, String configuration) {
        System.out.println("Setting port " + portIndex + ": " + configuration);
        return true;
    }

    public void getPortStates() {
        System.out.println("Platform.getPortStates()");
    }

    public void getPortPowerLevel() {
        System.out.println("getPortPowerLevel()");
    }

    public void setPortPowerLevel() {
        System.out.println("Platform.setPortPowerLevel()");
    }

    /*
    public void getAvailablePorts() {

    }
    */

    public void getButtonState() {
        System.out.println("Platform.getButtonState()");
    }

    public void getLightStates() {
        System.out.println("Platform.getLightStates()");
    }

    public void setLightState() {
        System.out.println("Platform.setLightState()");
    }

    public void getOrientation() {
        System.out.println("Platform.getOrientation()");
    }

    public void getAcceleration() {
        System.out.println("Platform.getAcceleration()");
    }

    public void getAngularVelocity() {
        System.out.println("Platform.getAngularVelocity()");
    }

    public void getDirection() {
        System.out.println("Platform.getDirection()");
    }

    public void getAltitude() {
        System.out.println("Platform.getAltitude()");
    }

    public void getPowerLevel() {
        System.out.println("Platform.getPowerLevel()");
    }

    public void getCurrentSense() {
        System.out.println("Platform.getCurrentSense()");
    }

    public void sendHttpRequest() {
        System.out.println("Platform.sendHttpRequest()");
    }

    public void sendThreadMessage() {
        System.out.println("Platform.sendThreadMessage()");
    }

    // <DANGER_ZONE>
    public void shutdown() {
        System.out.println("Platform.shutdown()");
    }

    public void restart() {
        System.out.println("Platform.restart()");
    }

    public void updateFirmware() {
        System.out.println("Platform.updateFirmware()");
    }
    // </DANGER_ZONE>
}
