import socket, sys
import time

dest = ('<broadcast>', 4445) # 5000

ip_address = socket.gethostbyname(socket.gethostname())
discovery_message = "ping " + ip_address

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

while 1:
    s.sendto(discovery_message, dest)
    print "Listening for replies; press Ctrl-C to stop."
    while 1:
        (buf, address) = s.recvfrom(4446) # 2048
        if not len(buf):
            break
        print "Received from %s: %s" % (address, buf)
        time.sleep(2)
        break
