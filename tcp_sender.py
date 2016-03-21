import sys, time
import socket
import uuid

ADDRESS = '10.0.0.6'
PORT = 1002

uuid = uuid.uuid4()
#message = "laser beaming\n"
message = "pew\n"

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((ADDRESS, PORT))
s.send(message)

while 1:
    s.send(message)
    time.sleep(0.1)
