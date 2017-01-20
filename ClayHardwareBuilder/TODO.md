# TODO: Remove everything following # to allow end-of-line comments.

# TODO: Support "statement compression" for some constructs. The goal is to minimize the number of expressions required to fully add and configure constructs to the workspace and retain "operational transformability" or "undoability" and "uncompressed expression order". Add:
# TODO: add devices (2)
# TODO: add device (12 ports)
# TODO: add ports (12)

# TODO: - Rename "configuration" (as used here) to "option"...
# TODO:   ...then and make "configuration" a sequence of expressions usable as a procedure...
# TODO:   ...usable in scripts (equivalent to atomically adding).
# TODO: - ALTERNATIVELY, create a more generic notation to define "procedure" instruction sequence that can be reused.
# TODO: - Also, consider renaming "option" to "constraint" or "rule" or "relation".

# open configuration "clay-port-configuration"
# edit port 3
# add configuration <attribute_title>:<attribute_value>;<attribute_title>:<input,output,bidirectional>;<voltage>:<ttl,cmos>
# add configuration mode:none;direction:null;voltage:null
# add configuration mode:digital;direction:input,output,bidirectional;voltage:ttl,cmos
# add option mode:none;direction:null;voltage:null
# add option mode:digital;direction:input,output,bidirectional;voltage:ttl,cmos

# NOTE: _configuration_ consists of _attributes_ for which _constraints_ can be applied to restrict the configuration to certain attribute-value combinations
# ...
# close configuration



# OPTION 1:
# edit port 3
# set mode digital
# set direction output
# set voltage ttl
#
# OPTION 2: set configuration digital;output;ttl
# OPTION 3: set configuration mode:digital;direction:output;voltage:ttl

# TODO: configuration --> specification/constraint :::: AVAILABLE CONFIGURATIONS (of construct)
# TODO: configuration is STATE (of construct)



# add port (power;input;ttl)
# add port (power;output;common)
# add port (analog;output;ttl)



# OPTION 1:
# TODO: add devices (2)
# TODO: add ports (12) # NOTE: after "edit device <UID>"

# OPTION 2:
# add device (12 ports) --> unpacks to add device; add port; ... (12 total)
# add device (2 ports)

# TODO: "edit port" to select the last-created construct
#           --OR-- "edit device <uid>"
#           --OR-- "edit device <uuid>"
#           --OR-- "edit device <index>"
#           --OR-- "edit port <uid>"
#           --OR-- "edit device <uid> port <uid>"
#           --OR-- "edit device <uid> port <index>"
#           --OR-- "edit device <index> port <index>"
# TODO: store index (relative to container); per-session workspace UID (ephemeral); UUID (persistent, for persisting configurations, constructs, etc.)
# TODO: test "import file <filename>" from within a file, and try nesting file hierarchies...
# TODO: save/load workspace (.build file)
# TODO: update "edit" to refer to just one construct, not one of each title of construct
# TODO: represent/infer "dependencies" based on configuration options---for use in IASM


# BEGIN BUILD Clay_7_Port
# add configuration none;null;null
# add configuration digital;input,output,bidirectional;ttl,cmos
# add configuration analog;input,output;ttl,cmos
# add configuration pwm;input,output;ttl,cmos
# add configuration resistive_touch;input;null
# add configuration power;output;ttl,cmos
# add configuration power;input;common
# add configuration i2c(scl);bidirectional;cmos
# add configuration i2c(sda);bidirectional;cmos
# add configuration spi(sclk);output;ttl,cmos
# add configuration spi(mosi);output;ttl,cmos
# add configuration spi(miso);input;ttl,cmos
# add configuration spi(ss);output;ttl,cmos
# add configuration uart(rx);input;ttl,cmos
# add configuration uart(tx);output;ttl,cmos
# END BUILD



# TODO: Update path construction statements and compressed statements.
# add path
# edit path
# set source device 1 port 3
# set target device 14 port 15
add path device 1 port 3 device 14 port 15



#set configuration mode:digital;direction:output;voltage:ttl
# set configuration digital;output;ttl
# set attribute mode digital
# set attributes mode:digital;direction:output;voltage:ttl

# TODO: add path device 1 port 3 device 2 port 15
# TODO: >>> resolve the configuration of ^ port 3 and ^ port 15 (i.e., in this case, configure port 3 based on port 15 since port 15 has just one configuration; when there's multiple configurations available, as with connecting Clay to Arduino, show the interaction/common configurations and interactively assign it)



# workspace
# └ project
#   └ device
#     └ port (<mode>)
#     └ port (...)
#     └ port
#     └ port
#     └ port
#     └ port
#   └ device
#     └ port
#     └ port
#     └ port
#   └ device
#     └ port
#     └ port
#   └ path (device X port Y » device A port B)
#     └ source: port
#     └ destination: port
# └ project



// TODO: label/title (i.e., "VCC", "AREF", "GND", "A0", "Port 3", "3", etc.

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