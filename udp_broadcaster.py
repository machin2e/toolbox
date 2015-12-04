# Send UDP broadcast packets

MYPORT = 4446

import sys, time
from socket import *

s = socket(AF_INET, SOCK_DGRAM)
s.bind(('', 0))
s.setsockopt(SOL_SOCKET, SO_BROADCAST, 1)

while 1:
    # data = repr(time.time()) + '\n'
    # address = 'BROADCASTER'
    # message = 'pulse'
    # data = { 'client': address, 'text': message }
    data = "turn light 2 on"
    s.sendto(data, ('<broadcast>', MYPORT))
    time.sleep(2)
