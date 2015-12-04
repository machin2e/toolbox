from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler

class RequestHandler(BaseHTTPRequestHandler):
    def _writeheaders(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

    def _writetextheaders(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()

    def do_HEAD(self):
        self._writeheaders()

    def do_GET(self):

        if self.path == '/experiment' or self.path == '/experiment/':
            self._writetextheaders()

            # Write data to response, line by line.
            with open('data.txt') as file:
                for line in file:
                    self.wfile.write(line)
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
