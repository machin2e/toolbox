# Send UDP broadcast packets

PORT = 4446

import sys, time
#from socket import *
import socket
import uuid

uuid = uuid.uuid4()

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind(('', 0))
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

i = 0
while 1:
    internetAddress = socket.gethostbyname(socket.gethostname())
    #data = "hand grenade\n"
    #data = "jizz\n"
    #data = "documents\n"
    data = "hundred million\n"
    s.sendto(data, ('<broadcast>', PORT))
    i = i+1
    time.sleep(0.1)
