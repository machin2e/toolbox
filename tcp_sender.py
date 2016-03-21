# Send UDP broadcast packets

import sys, time
import socket
import uuid

ADDRESS = '10.0.0.8'
PORT = 1002

uuid = uuid.uuid4()
message = "hello there"

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((ADDRESS, PORT))

while 1:
    s.send(message)
    time.sleep(1)
