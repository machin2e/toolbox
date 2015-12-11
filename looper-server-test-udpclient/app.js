var dgram = require('dgram');

var address = '192.168.43.235'; 
//var address = '255.255.255.255';
//var address = '192.168.0.255';
//var address =  'localhost';
var port = 4445;
var message = new Buffer ('hello clay');

var client = dgram.createSocket('udp4');
client.bind(4445);
client.on("listening", function () {
    client.setBroadcast(true);
    client.send(message, 0, message.length, port, address, function(err, bytes) {
        client.close();
    });
});

/*
//client.bind(port);
//client.send(message, 0, message.length, port, address);

var message = new Buffer ('my datagram');
client.setBroadcast(true);
client.send(message, 0, message.length, port, address, 
    function (err, bytes) {
        if (err) {
            throw err;
        }
        // Reuse the message buffer,
        // or close client
        client.close();
    }
);
*/
