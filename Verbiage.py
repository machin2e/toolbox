import requests
from time import sleep
import os
import random

os.system('clear') # Clear the console (OS X and Linux)
# os.system('cls') # Clear the command prompt (Windows)

text = "The birch canoe slid on the smooth planks. Glue the sheet to the dark blue background. It's easy to tell the depth of a well. These days a chicken leg is a rare dish. Rice is often served in round bowls. The juice of lemons makes fine punch. The box was thrown beside the parked truck. The hogs were fed chopped corn and garbage. Four hours of steady work faced us. A large size in stockings is hard to sell."
sentences = text.split('. ')
# print sentences

i = 0
j = 0

protocol = "https://"
address = "www.metamind.io";

print "Verbiage (for Verbalizer)"
while True:
    # message = raw_input("> ") # Note: raw_input() is used to read strings. input() is used to read integers.
    # print "%s" % message

    # Process the message.
    # if message.startswith('send to '):
    #     # set ip to 192.168.0.1
    #     address = message.split (' ')[2];
    #     # print 'Sending to IP address %s.' % address
    #     print 'OK'
    #     continue

    # Check if everything is set up properly.
    # if address == "127.0.0.1":
    #     sleep(0.4)
    #     print "I can't."
    #     sleep(0.8)
    #     print "Tell me the recipient by typing \"send to <IP address>\"."
    #     continue

    # Select the sentences
    i = random.randint(0, len(sentences) - 1)
    j = random.randint(0, len(sentences) - 1)

    # Create the request
    # sentenceOne = "Kids+in+red+shirts+are+playing+in+the+leaves"
    # sentenceTwo = "Three+kids+are+jumping+in+the+leaves"
    # baseUri = "/language/relatedness/test?text_1=" + sentenceOne + "&text_2=" + sentenceTwo
    sentenceOne = sentences[i] # .replace(' ', '+') # "Kids+in+red+shirts+are+playing+in+the+leaves"
    sentenceTwo = sentences[j] # .replace(' ', '+') # "Three+kids+are+jumping+in+the+leaves"
    print sentenceOne
    print sentenceTwo
    sentenceOne = sentences[i].replace(' ', '+') # "Kids+in+red+shirts+are+playing+in+the+leaves"
    sentenceTwo = sentences[j].replace(' ', '+') # "Three+kids+are+jumping+in+the+leaves"
    baseUri = "/language/relatedness/test?text_1=" + sentenceOne + "&text_2=" + sentenceTwo

    # Process the message.
    message = protocol + address + baseUri # + message.replace(' ', '%20')
    #print '%s' % message

    # TODO: Create HTTP request
    response = requests.get(message, timeout=60)
    # TODO: Show result from HTTP request.
    if response.status_code == 200:
        # print "OK"
        # print response.text
        print response.json()['score']
        print ""
    else:
        print "ERROR"
