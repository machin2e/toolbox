# Send UDP broadcast packets

MYPORT = 4446

import sys, time
#from socket import *
import socket
import uuid

uuid = uuid.uuid4()

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind(('', 0))
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

while 1:
    # data = repr(time.time()) + '\n'
    # address = 'BROADCASTER'
    # message = 'pulse'
    # data = { 'client': address, 'text': message }
    # data = "turn light 2 on"
    internetAddress = socket.gethostbyname(socket.gethostname())
    # data = "connect to " + internetAddress
    # s.sendto(data, ('<broadcast>', MYPORT))
    data = "update"
    s.sendto(data, ('<broadcast>', MYPORT))
    time.sleep(2)
