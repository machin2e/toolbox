# Send UDP broadcast packets

PORT = 4445

import sys, time
#from socket import *
import socket
import uuid

uuid = uuid.uuid4()
print "uuid: %s" % uuid

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind(('', 0))
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

i = 0
while 1:
    internetAddress = socket.gethostbyname(socket.gethostname())

    # "\f<content_length>\t<content_checksum>\t<content_type>\t<content>"
    # e.g., "\f52	16561	text	announce device 002fffff-ffff-ffff-4e45-3158200a0015"
    # data = "\f52\t16561\ttext\tannounce device 002fffff-ffff-ffff-4e45-3158200a0015";
    data = "\f52\t33439\ttext\tannounce device f1aceb8b-e8e9-4cda-b29c-de7bc7cc390f"

    print data

    s.sendto(data, ('<broadcast>', PORT))
    i = i+1
    time.sleep(1)

    data = "\f52\t58392\ttext\tannounce device 4d3d999d-1954-4187-ad2f-7e8139e02a2e"
    print data
    s.sendto(data, ('<broadcast>', PORT))
