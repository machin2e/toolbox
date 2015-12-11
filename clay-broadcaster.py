# Send UDP broadcast packets

DISCOVERY_BROADCAST_PORT = 4445
BROADCAST_PORT = 4446
MESSAGE_PORT = BROADCAST_PORT

import sys, time
#from socket import *
import socket
import uuid
import random
import time

# define "millis()" function
millis = lambda: int(round(time.time() * 1000))
last_broadcast_time = 0
broadcast_frequency = 1000

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
        #print "sending broadcast"
        internetAddress = "192.168.1.105" # internetAddress = socket.gethostbyname(socket.gethostname())
        data = "connect to " + internetAddress
        print data
        s.sendto(data, ('<broadcast>', DISCOVERY_BROADCAST_PORT))
    #    s.sendto(data, ('192.168.1.104', PORT))
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
