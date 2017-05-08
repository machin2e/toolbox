import random
from socket import *

PORT = 4445

serverSocket = socket(AF_INET, SOCK_DGRAM)
serverSocket.bind(('', PORT))

while True:
	rand = random.randint(0, 10)
	message, address = serverSocket.recvfrom(1024)
	message = message.upper()
	print "Received:", message, "from", address
	if rand >= 4:
		serverSocket.sendto(message, address)
