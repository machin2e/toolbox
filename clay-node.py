import socket, sys
import time

dest = ('<broadcast>', 4446) # 5000

ip_address = socket.gethostbyname(socket.gethostname())
discovery_message = "connect to " + ip_address

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)


while 1:
    s.sendto(discovery_message, dest)
    print "Listening for replies; press Ctrl-C to stop."

#    s.settimeout(1)
    (buf, address) = s.recvfrom(4447) # 2048
    if not len(buf):
        print "Received nothing!"
    print "Received from %s: %s" % (address, buf)
    # s.settimeout(None)

    time.sleep(2)
