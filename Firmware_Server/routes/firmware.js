var express = require('express');
var fs = require('fs');
var crc = require('crc');
var router = express.Router();

/* GET firmware listing. */
router.get('/', function(req, res, next) {

  // Parse query
  // Reference: http://expressjs.com/en/4x/api.html#req.query
  startByte = parseInt (req.query.startByte);
  byteCount = parseInt (req.query.byteCount);

  var firmwareFilePath = './public/firmware/Clay_C5_Firmware_2015_12_20_20_29_00.hex';
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
            buffer.copy (resultBuffer, 0, startByte, startByte + byteCount);

            // Calculate the CRC16 of the buffer.
            // Reference: https://github.com/alexgorbatchev/node-crc
            var resultCrc = crc.crc16(resultBuffer).toString(16);
            console.log ("crc16: " + resultCrc);

            // TODO: Add the resultCrc bytes to the resultString.

            // Generate a string of UTF-8 bytes to send in the response.
            resultString = resultBuffer.toString ('utf-8', 0, byteCount);

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
