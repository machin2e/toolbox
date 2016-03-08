import socket, traceback

listen_port = 4445

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
s.bind(('', listen_port))
# s.settimeout(1)

print "Clay"
print "Listening for UDP broadcasts on port %s" % (listen_port)

while 1:
    try:
        message, address = s.recvfrom(listen_port) # Block until a packet is received on the port
        print "%s: %s" % (address, message) # Print the received message
        #s.sendto("Hello from server", address) # Send a response (optionally)
        #print "Listening for broadcasts..."
    except (KeyboardInterrupt, SystemExit):
        raise
    except:
        print "Timeout (broadcast receive)"
        #traceback.print_exc()
