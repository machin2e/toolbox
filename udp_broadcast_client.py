import socket, sys

broadcast_port = 4446

dest = ('<broadcast>', broadcast_port) # 5000

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

s.sendto("ping", dest)

print "Listening for replies; press Ctrl-C to stop."
while 1:
    (buf, address) = s.recvfrom(4446) # 2048
    if not len(buf):
        break
    print "Received from %s: %s" % (address, buf)
    break

exit
