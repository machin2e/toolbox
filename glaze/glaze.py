import requests
from time import sleep
import os

# TODO: Set up UDP server to listen for Clay.
# TODO: Show status messages from Clay as they are received (i.e., as units become available, unavailable, or change operational status).
# TODO: Add support for muliple people to use the terminal simultaneously (with add and kick commands).
# TODO: Add a command to communicate with the ESP8266 via AT commands (by passing it a command that Clay will pass the ESP8266 in turn, get the response, then relay it back in the HTTP response)
# TODO: Add "show" command to show local Clay
# TODO: Add "share" command to share local Clay (with everyone by default, or with a specific person)
# TODO: Add "timelines" (akin to "rooms" on IRC) that have their own histories and communities (of people) and represent interactions with Clay and other people.

os.system('clear') # Clear the console (OS X and Linux)
# os.system('cls') # Clear the command prompt (Windows)

protocol = "http://"
ip = "127.0.0.1";
baseUri = "/message?content="

print "clay (glaze)"
while True:
    message = raw_input("> ") # Note: raw_input() is used to read strings. input() is used to read integers.
    # print "%s" % message

    # Process the message.
    if message.startswith('send to '):
        # set ip to 192.168.0.1
        ip = message.split (' ')[2];
        # print 'Sending to IP address %s.' % ip
        print 'OK'
        continue

    # Check if everything is set up properly.
    if ip == "127.0.0.1":
        sleep(0.4)
        print "I can't."
        sleep(0.8)
        print "Tell me the recipient by typing \"send to <IP address>\"."
        continue

    # Process the message.
    message = protocol + ip + baseUri + message.replace(' ', '%20')
    #print '%s' % message

    # TODO: Create HTTP request
    response = requests.get(message, timeout=60)
    # TODO: Show result from HTTP request.
    if response.status_code == 200:
        print "OK"
    else:
        print "ERROR"
