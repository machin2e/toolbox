var express = require('express');
var fs = require('fs');
var crc = require('crc');
var router = express.Router();

/* GET firmware listing. */
router.get('/', function(req, res, next) {

  // To download firmware, send HTTP GET requests of the form (<em>e.g.,</em>) <a href="http://clay.computer/firmware?startByte=0&byteCount=500">http://clay.computer/firmware?startByte=0&byteCount=500</a>. Change the parameters <strong>startByte</strong> and <strong>byteCount</strong> to change the first byte received and the number of bytes received of the firmware from the bootloader server.

  // To test this request handler, you may use the cURL command "curl --request GET 'http://localhost:3000/firmware/?startByte=0&byteCount=500'"

  // Parse query
  // Reference: http://expressjs.com/en/4x/api.html#req.query
  startByte = parseInt (req.query.startByte);
  byteCount = parseInt (req.query.byteCount);

  var firmwareFilePath = './public/firmware/firmware.hex';
  var firmwareFileSize = 0;

  var resultString = '';
  fs.exists(firmwareFilePath, function(exists) {
    if (exists) {
      fs.stat(firmwareFilePath, function(error, stats) {
        fs.open(firmwareFilePath, 'r', function (status, fd) {
          if (status) {
            console.log(status.message);
            return;
          }

          // Create buffer to store the firmware file then load the firmware file into the buffer.
          var buffer = new Buffer (stats.size);

          // Update the byte count if it exceeds the number of remaining bytes given the start byte.
          if ((startByte + byteCount) > stats.size) {
            byteCount = stats.size - startByte;
          }

          console.log ('firmwareFilePath: ' + firmwareFilePath);
          console.log ("firmwareSize: " + stats.size);
          console.log ('startByte: ' + startByte);
          console.log ('byteCount: ' + byteCount);

          // Read the firmware file from disk.
          fs.read (fd, buffer, 0, buffer.length, 0, function (err, num) { // fs.read (fd, buffer, 0, 100, 0, function (err, num) {

            // Copy the bytes in the specified range to a second buffer, which contains the bytes to send in the response.
            // Reference: https://nodejs.org/api/buffer.html#buffer_buf_copy_targetbuffer_targetstart_sourcestart_sourceend
            var resultBuffer = new Buffer (byteCount);
            // buffer.copy (resultBuffer, 0, startByte, startByte + byteCount);
            buffer.copy (resultBuffer, 0, startByte, startByte + byteCount);

            // Calculate the CRC16 of the buffer.
            // Reference: https://github.com/alexgorbatchev/node-crc
            var resultCrc = crc.crc16(resultBuffer).toString(16);
            console.log ("crc16: " + resultCrc);

            // TODO: Add the resultCrc bytes to the resultString.

            // Generate a string of UTF-8 bytes to send in the response.
            // resultString = resultBuffer.toString ('utf-8', 0, byteCount);
            resultString = resultBuffer.toString ();

            console.log ("hex:");
            console.log (resultBuffer.toString ('hex', 0, byteCount));
            console.log ("\n");

            console.log ("binary:");
            console.log (resultBuffer.toString ('binary', 0, byteCount));
            console.log ("\n");

            console.log ("ascii:");
            console.log (resultBuffer.toString ('ascii', 0, byteCount));
            console.log ("\n");

            console.log ("utf-8:");
            console.log (resultBuffer.toString ('utf-8', 0, byteCount));
            console.log ("\n");

            // Send the response
            res.append ('Content-Type', 'application/octet-stream');
            res.append ('Content-Length', byteCount);
            res.send (resultString);

          });
        });
      });
    }
  });

});

module.exports = router;
