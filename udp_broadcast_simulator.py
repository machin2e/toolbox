import sys, time
import socket
import uuid

DATAGRAM_BROADCAST_PORT = 4445

config_frame_count = 5

def generate_broadcast_messages(count):

    broadast_messages = [
        "\f52\t33439\ttext\tannounce device f1aceb8b-e8e9-4cda-b29c-de7bc7cc390f",
        "\f52\t21266\ttext\tannounce device 853b8a53-a99a-4a89-a724-2b845ffbb6f5",
        "\f52\t6809\ttext\tannounce device db70d05d-c108-4703-82c2-615a33502938",
        "\f52\t58392\ttext\tannounce device 4d3d999d-1954-4187-ad2f-7e8139e02a2e",
        "\f52\t11780\ttext\tannounce device de66a614-cd21-4ec4-9648-ffca4edac1b2",
        "\f52\t1984\ttext\tannounce device a072eb01-d16b-4d48-bdd2-d0cebf77fce6",
        "\f52\t65066\ttext\tannounce device 7535aa97-03bd-4669-8c2a-7c321812ebfc",
        "\f52\t63889\ttext\tannounce device 13033fad-cc90-466e-ab24-e98205d54690",
        "\f52\t41359\ttext\tannounce device 866cc397-fa03-419d-a9ee-06eb663f96ed",
        "\f52\t48439\ttext\tannounce device d6ce1b35-530b-4a6a-ad89-11acbe991fb5",
        "\f52\t46564\ttext\tannounce device 115f4634-55eb-4484-a432-86545489ac26",
        "\f52\t57610\ttext\tannounce device 93d89f0b-533c-4b64-9094-76015d5ed8f7",
        "\f52\t54594\ttext\tannounce device f4c6df90-faed-4f84-a163-b1069c2a1c3c",
        "\f52\t22\ttext\tannounce device 7bbdab2f-b234-4153-964c-caa7498d98b1",
        "\f52\t62011\ttext\tannounce device 7dd66668-bd84-4725-8164-ac1492e96ea0",
        "\f52\t5767\ttext\tannounce device 4059a2d6-b1c1-4326-88ec-53c0cb890a74",
        "\f52\t17840\ttext\tannounce device 3a4a7fec-3e6c-4a04-9cb4-471f6880db62",
        "\f52\t960\ttext\tannounce device 173f8b1e-3af5-48fe-803b-4d003330e67c",
        "\f52\t35370\ttext\tannounce device 20c41326-1103-46e1-9970-866745ed97d0",
        "\f52\t47970\ttext\tannounce device f23fd817-1eb7-4bff-b4ba-bf677cda0b00"
    ]

    return broadast_messages[:count]

def generate_display_message(message, format):

    display_message = None

    if format == 'raw':
        display_message = frame_broadcast_messages[i]

    if format == 'clean':
        display_message = frame_broadcast_messages[i]
        # display_message = display_message.replace('\f','')
        for j in range(3):
            tab_skip_index = display_message.find('\t') + 1
            display_message = display_message[tab_skip_index:]

    return display_message

print "Clay"

# Generate messages
frame_broadcast_messages = generate_broadcast_messages(config_frame_count)

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind(('', 0))
s.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)

i = 0
while 1:
    internetAddress = socket.gethostbyname(socket.gethostname())

    # "\f<content_length>\t<content_checksum>\t<content_type>\t<content>"
    # e.g., "\f52\t16561\ttext\tannounce device 002fffff-ffff-ffff-4e45-3158200a0015"
    for i in range(config_frame_count):

        # Generate the message to display
        display_message = generate_display_message(frame_broadcast_messages[i], 'clean')

        # Display the message
        print display_message

        # Send the message
        s.sendto(frame_broadcast_messages[i], ('<broadcast>', DATAGRAM_BROADCAST_PORT))
        time.sleep(0.2)

    time.sleep(2.0)
