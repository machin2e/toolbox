from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
#from urlparse import parse_qs
import urlparse
import os

class RequestHandler(BaseHTTPRequestHandler):
    def _writeheaders(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

    def _writeheaders2(self, contentType, contentLength):
        self.send_response(200)
        self.send_header('Content-type', contentType)
        # self.send_header('Content-length', str(contentLength))
        self.end_headers()

    def _writeapplicationheaders(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/application')
        self.end_headers()

    def _writetextheaders(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

    def do_HEAD(self):
        self._writeheaders()

    def do_GET(self):

        # print self

        # Parse GET parameters
        # print self.path
        request = urlparse.urlparse(self.path)
        # print "path: "
        # print request.path
        # params = urlparse.parse_qs(request.query)
        # print "params: "
        # print params

        if request.path == '/firmware/size' or request.path == '/firmware/size/':

            self._writetextheaders()

            # Get latest firmware
            latestFirmware = './firmware/Clay_C5_Firmware_2015_12_20_20_29_00.hex'

            self.wfile.write(str (os.path.getsize (latestFirmware)))

        elif request.path == '/firmware' or request.path == '/firmware/':
            #

            # print "path: "
            # print request.path
            params = urlparse.parse_qs(request.query)
            # print "params:", params

            start = int(params['start'][0])
            size = int(params['size'][0])

            print "first byte:", str(start)
            print "block size:", str(size)

            # Write binary response, byte by byte.
            # firmwareFile = './firmware/Clay_C5_Firmware.hex'
            startByteIndex = start
            byteIndex = 0
            byteCount = 0
            latestFirmware = './firmware/Clay_C5_Firmware_2015_12_20_20_29_00.hex'
            latestFirmwareSize = os.path.getsize (latestFirmware)
            print "latest firmware size:", latestFirmwareSize

            # Calculate the size, in bytes, of the block being sent (to be stored in the "Content-size" header in the response)
            remainingSize = latestFirmwareSize - startByteIndex # Remaining size to send (sequentially, until end of file)
            contentLength = size # The default size, in bytes, of the binary firmware block being send in the response.
            print "remaining size:", remainingSize
            if (remainingSize < size):
                contentLength = remainingSize # Update the size if at the end of the file
                print "new Content-Length:", contentLength

            # self._writeheaders2('text/application', contentLength)
            # self._writeapplicationheaders()


            # print "Content-Length:", str(size)

            self.send_response(200)
            self.send_header('Content-Length', int(contentLength))
            self.send_header('Content-Type', "application/octet-stream")
            # self.send_header('Content-Type', "text/application")
            self.end_headers()

            with open(latestFirmware, "rb") as f:
                byte = f.read(1)
                while byte != "":
                    # Do stuff with byte.
                    if (byteIndex >= startByteIndex and byteIndex < (startByteIndex + size)):
                        self.wfile.write(byte)
                        byteCount = byteCount + 1

                    # Increase the byte index
                    byteIndex = byteIndex + 1

                    # Get the next byte or break
                    if (byteCount < size):
                        byte = f.read(1)
                    else:
                        break
        else:
            self._writeheaders()
            self.wfile.write("""
            <html><head><title>Clay</title></head>
            <body>Hey.</body></html>
            """)

    def do_POST(self):

        if self.path == '/experiment' or self.path == '/experiment/':

            # Get the request body
            content_len = int(self.headers.getheader('content-length', 0))
            post_body = self.rfile.read(content_len)

            # Append the received data to the file
            with open('data.txt', 'a') as file:
                file.write(post_body + '\n')

            self._writeheaders()
            self.wfile.write("done")

        else:

            # 404
            self._writeheaders()
            self.wfile.write("""
            <HTML><HEAD><TITLE>Sample Page</TITLE></HEAD>
            <BODY>This is a sample HTML page.</BODY></HTML>
            """)


serveraddr = ('', 8080)
srvr = HTTPServer(serveraddr, RequestHandler)
srvr.serve_forever()
