package camp.computer.construct;

import camp.computer.data.format.configuration.PortConfiguration;
import camp.computer.data.format.configuration.ValueSet;

import java.util.ArrayList;
import java.util.List;

public class PortConstruct {

    // TODO: label/title (i.e., "VCC", "AREF", "GND", "A0", "Port 3", "3", etc.)

    public long uid = DeviceConstruct.constructCounter++;

    // TODO: List of (portConfiguration string, INPUT | OUTPUT | IO, 0V | 3.3V | 5V – or range) + path constraint/relation resolver/arithmetic

    // <configuration_label>; [<direction_scope>]; [<voltage_scope>];
    // Power, (Output), (0V, 3.3V, 5V), (Off, On)
    // Power, (Input), (0V), (Common)
    // Discrete, (Input, Output), (3.3V, 5V), (True, False)
    // Continuous, (Input, Output), (3.3V, 5V), (<function>)
    // PWM, (Input, Output), (3.3V, 5V), (Period; Duty Cycle)

    // TODO: Add supported portConfigurations for Clay 7 device (see above)
    // TODO: Add supported portConfigurations for IR rangefinder device
    // TODO: Add supported portConfigurations for Ultrasonic Rangefinder device

    public List<PortConfiguration> portConfigurations = new ArrayList<>();

    public PortConfiguration portConfiguration = null;

    public PortConstruct() {

        /* Clay 7 */

//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.NONE, null, null));
//
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.DIGITAL, new ValueSet(PortConfiguration.Direction.INPUT, PortConfiguration.Direction.OUTPUT, PortConfiguration.Direction.BIDIRECTIONAL), new ValueSet(PortConfiguration.Voltage.CMOS, PortConfiguration.Voltage.TTL)));
//
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.ANALOG, new ValueSet(PortConfiguration.Direction.INPUT, PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.PWM, new ValueSet(PortConfiguration.Direction.INPUT, PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.RESISTIVE_TOUCH, new ValueSet(PortConfiguration.Direction.INPUT), null));
//
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.POWER, new ValueSet(PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.POWER, new ValueSet(PortConfiguration.Direction.INPUT), new ValueSet(PortConfiguration.Voltage.COMMON)));
//
//        // I2C Master-Slave Role
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.I2C_SCL, new ValueSet(PortConfiguration.Direction.BIDIRECTIONAL), new ValueSet(PortConfiguration.Voltage.CMOS)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.I2C_SDA, new ValueSet(PortConfiguration.Direction.BIDIRECTIONAL), new ValueSet(PortConfiguration.Voltage.CMOS)));
//
//        // SPI Master Role
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.SPI_SCLK, new ValueSet(PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.SPI_MOSI, new ValueSet(PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.SPI_MISO, new ValueSet(PortConfiguration.Direction.INPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.SPI_SS, new ValueSet(PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//
//        // UART
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.UART_RX, new ValueSet(PortConfiguration.Direction.INPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.UART_TX, new ValueSet(PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));

//        /* IR Rangefinder */
//
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.POWER, new ValueSet(PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.POWER, new ValueSet(PortConfiguration.Direction.INPUT), new ValueSet(PortConfiguration.Voltage.COMMON)));
//        portConfigurations.add(new PortConfiguration(PortConfiguration.Mode.ANALOG, new ValueSet(PortConfiguration.Direction.OUTPUT), new ValueSet(PortConfiguration.Voltage.TTL, PortConfiguration.Voltage.CMOS)));

    }

}
