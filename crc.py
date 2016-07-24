import crcmod

broadcast_message = "announce device 002fffff-ffff-ffff-4e45-3158200a0015"

#broadcast_message_bytes = b'\\x'.join(x.encode('hex') for x in 'broadcast_message')
broadcast_message_bytes = b''.join(x.encode('hex') for x in 'broadcast_message')

print broadcast_message_bytes

#crc16 = crcmod.mkCrcFun(0x18005, rev=False, initCrc=0xFFFF, xorOut=0x0000)
crc16 = crcmod.mkCrcFun(0x18005, rev=True, initCrc=0)

# print hex(crc16(broadcast_message_bytes.decode("hex")))
print crc16(broadcast_message_bytes)
