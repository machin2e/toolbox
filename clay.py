# Send UDP broadcast packets

#DISCOVERY_BROADCAST_ADDRESS = '<broadcast>'
DISCOVERY_BROADCAST_ADDRESS = '192.168.2.255'

DISCOVERY_BROADCAST_PORT = 4445
BROADCAST_PORT = 4446
MESSAGE_PORT = BROADCAST_PORT

import sys, time
import socket
import uuid
import random
import time

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

print get_broadcast_address()

class Behavior:
    uuid = None
    transform = None
    behaviors = []

class Unit:
    uuid = None
    behavior = []

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
    i = i + 1

print "DISCOVERY_BROADCAST_PORT", DISCOVERY_BROADCAST_PORT
print "BROADCAST_PORT", BROADCAST_PORT

# TODO: if command == "start simulator"
# TODO: if command == "monitor simulator"

if command == "start":

    #
    # Simulate units of Clay.
    #

    # define "millis()" function
    millis = lambda: int(round(time.time() * 1000))
    last_broadcast_time = 0
    broadcast_frequency = 1000

    # Generate UUID for Clay
    uuid = uuid.uuid4()

    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.bind(('', 0))
    s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

    s2 = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s2.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    s2.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    s2.bind(('', MESSAGE_PORT))
    s2.settimeout(1)

    while 1:
        # Send broadcast
        if (millis() > (last_broadcast_time + broadcast_frequency)):
            # print "sending broadcast"
            internetAddress = get_ip_address() # internetAddress = socket.gethostbyname(socket.gethostname())
            data = "set unit " + str(uuid) + " address to " + internetAddress
            print data
            s.sendto(data, (DISCOVERY_BROADCAST_ADDRESS, DISCOVERY_BROADCAST_PORT))
            last_broadcast_time = millis()

            #delay = random.randint(1, 3)
            #delay = 0.5 + random.random() * 3
            #time.sleep(delay)

        try:
            message, address = s2.recvfrom(DISCOVERY_BROADCAST_PORT) # Block until a packet is received on the port
            print "Received message from %s: %s" % (address[0], message) # Print the received message
            #s.sendto("Hello from server", address) # Send a response (optionally)
            data = "got " + message
            s.sendto(data, (address[0], DISCOVERY_BROADCAST_PORT))
            #print "Listening for broadcasts..."
        except (KeyboardInterrupt, SystemExit):
            raise
        except:
            print "Timeout (broadcast receive)"
            #traceback.print_exc()

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
