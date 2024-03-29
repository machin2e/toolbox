# Send UDP broadcast packets

#DISCOVERY_BROADCAST_ADDRESS = '<broadcast>'
DISCOVERY_BROADCAST_ADDRESS = '192.168.255.255'

DISCOVERY_BROADCAST_PORT = 4445
BROADCAST_PORT = 4446
MESSAGE_PORT = BROADCAST_PORT

import sys, time
import socket
import uuid
import random
import time
import traceback

if len (sys.argv) == 1:
    print "Usage: clay [OPTIONS] COMMAND [arg...]"
    print "       clay [ --help | -v | --version ]"
    print ""
    print "A command prompt for Clay."
    print ""
    print "Commands:"
    print "    start    Start a simulated unit of Clay"
    print "    stop     Stop a simulated unit of Clay"
    print "    connect  Connect to a physical unit of Clay."
    print "    monitor  Listen for communicaitons from Clay."
    print "    update   Update a feature of Clay."
    print ""
    print "Run 'clay COMMAND --help' for more information on a command."
    exit ()

def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.connect(("8.8.8.8", 80))
    return s.getsockname()[0]

def get_broadcast_address():
    address = get_ip_address()
    print 'address:', address
    octets = address.split('.')
    print 'octets:', octets
    octets[3] = '255'
    print 'octets:', octets
    broadcast_address = '.'.join(octets)
    print broadcast_address
    return broadcast_address

print "Computed boradcast address:", get_broadcast_address()

class Behavior:
    uuid = None
    transform = None
    behaviors = []

class Unit:
    uuid = None
    behavior = []

# Initialize defaults
UNIT_UUID = uuid.uuid4()

# Parse command-line arguments.
command = None
if len (sys.argv) > 1:
    command = sys.argv[1] # Get the command.

# Parse command line options.
i = 0
for option in sys.argv:
    if option == "-h": # i.e., discovery broadcast address (used to infer broadcast address)
        DISCOVERY_BROADCAST_ADDRESS = sys.argv[i+1]
    elif option == "-p": # i.e., ports for incoming and outgoing UDP traffic
        udp_ports = sys.argv[i + 1]
        print "udp_ports:", udp_ports
        DISCOVERY_BROADCAST_PORT = int(udp_ports.split(',')[0])
        BROADCAST_PORT = int(udp_ports.split(',')[1])
    elif option == "-uuid": # i.e., the uuid to use for the simulated unit
        UNIT_UUID = uuid.UUID("{" + sys.argv[i + 1] + "}");
        print "-uuid: ", UNIT_UUID
    i = i + 1

print "DISCOVERY_BROADCAST_PORT", DISCOVERY_BROADCAST_PORT
print "BROADCAST_PORT", BROADCAST_PORT

# Starts a simulation of multiple units of Clay.
# This simulation can be monitored with the "monitor" command.
if command == "start":

    print "Starting Clay"

    # "millis()" function
    millis = lambda: int(round(time.time() * 1000))
    last_broadcast_time = 0
    broadcast_frequency = 1000

    # Generate UUID for Clay
    # uuid = uuid.uuid4()
    uuid = UNIT_UUID

    outgoingSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    outgoingSocket.bind(('', 0))
    outgoingSocket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

    incomingSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    incomingSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    incomingSocket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    incomingSocket.bind(('', MESSAGE_PORT))
    incomingSocket.settimeout(1)

    while 1:

        # Send broadcast
        if (millis() > (last_broadcast_time + broadcast_frequency)):
            # print "sending broadcast"
            internetAddress = get_ip_address() # internetAddress = socket.gethostbyname(socket.gethostname())
            data = "set unit " + str(uuid) + " address to " + internetAddress
            print data
            outgoingSocket.sendto(data, (DISCOVERY_BROADCAST_ADDRESS, DISCOVERY_BROADCAST_PORT))
            last_broadcast_time = millis()

        try:
            message, address = incomingSocket.recvfrom (MESSAGE_PORT) # Block until a packet is received on the port
            print "Received from %s: %s" % (address[0], message) # Print the received message
            data = "got " + message
            outgoingSocket.sendto(data, (address[0], DISCOVERY_BROADCAST_PORT))
        except (KeyboardInterrupt, SystemExit):
            raise
        except:
            print "Timeout (broadcast receive)"
            #traceback.print_exc()

# Monitor the simulation started with the "start" command.
elif command == "monitor":

    print "Monitoring Clay"

    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    s.bind(('', DISCOVERY_BROADCAST_PORT))

    while 1:
        try:
            message, address = s.recvfrom(DISCOVERY_BROADCAST_PORT) # 8192
            print "from %s: %s" % (address, message)
        except (KeyboardInterrupt, SystemExit):
            raise
        except:
            traceback.print_exc()

elif command == "update":

    # Get command arguments.
    target = sys.argv[2] # Get the target of the update.

    if target == "firmware":

        # TODO: address = sys.argv[3]

        # s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        # s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        # s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        # s.bind(('', BROADCAST_PORT))

        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.bind(('', 0))
        s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

        message = "update"
        s.sendto(message, ('<broadcast>', 4446))

elif command == "connect":

    # Get command arguments.
    command = sys.argv[2] # Get the IP address.

    # define "millis()" function
    # millis = lambda: int(round(time.time() * 1000))
    # last_broadcast_time = 0
    # broadcast_frequency = 1000

    # uuid = uuid.uuid4()

    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind(('', 0))
    s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

    s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s2.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s2.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    s2.bind(('', DISCOVERY_BROADCAST_PORT))
    s2.settimeout(1)

    while True:
        message = raw_input ("> ")
        print message

        if message == "exit":
            print "Goodbye"

        # Send message to Clay.
        s.sendto (message, ('<broadcast>', MESSAGE_PORT))

        # Listen for response.
        try:
            message, address = s2.recvfrom (DISCOVERY_BROADCAST_PORT) # Block until a packet is received on the port
            print "Received message from %s: %s" % (address[0], message) # Print the received message
            #s.sendto("Hello from server", address) # Send a response (optionally)
            data = "got " + message
            # s.sendto (data, (address[0], DISCOVERY_BROADCAST_PORT))
            #print "Listening for broadcasts..."
        except (KeyboardInterrupt, SystemExit):
            raise
        except:
            print "There was no response :("
            #traceback.print_exc()
