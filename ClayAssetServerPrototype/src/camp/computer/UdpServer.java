package camp.computer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UdpServer {
//    public static void main(String[] args) {
//        //int port = args.length == 0 ? 57 : Integer.parseInt(args[0]);
//        new UdpServer().run(port);
//    }

    private DatagramSocket serverSocket;

    public static int UDP_RECEIVE_PORT = 4445;
    public static int UDP_SEND_PORT = 4446;

    public UdpServer(int listenPort, int sendPort) {
        try {
            serverSocket = new DatagramSocket(listenPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            byte[] receiveData = new byte[128];

            System.out.printf("Listening on udp:%s:%d%n", InetAddress.getLocalHost().getHostAddress(), serverSocket.getLocalPort());
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while (true) {
                serverSocket.receive(receivePacket);
                String incomingMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received UDP datagram: " + incomingMessage);
                // now send acknowledgement packet back to sender
                /*
                // Echo back to sender
                InetAddress IPAddress = receivePacket.getAddress();
                String sendString = "polo";
                byte[] sendData = sendString.getBytes("UTF-8");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, UDP_SEND_PORT);
                serverSocket.send(sendPacket);
                */
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            // should close serverSocket in finally block
            // serverSocket.close();
        }
    }

    public void send(InetAddress inetAddress, String message) {

        try {
            InetAddress IPAddress = inetAddress; // receivePacket.getAddress();
            String sendString = "polo";
            byte[] sendData = new byte[0];
            sendData = message.getBytes("UTF-8");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, UDP_SEND_PORT);
            serverSocket.send(sendPacket);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
