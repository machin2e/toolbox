package camp.computer;

public class Application {

    public static JavaScriptEngine javaScriptEngine;

    public static void main(String[] args) {
        System.out.println("Clay Demo Server");
        try {

            System.out.print("Creating JavaScript Engine... ");
            javaScriptEngine = new JavaScriptEngine();
            System.out.println("Success.");

//            Redis redis = new Redis();

            System.out.print("Creating HTTP server... ");
            ClayAssetServerPrototype httpServer = new ClayAssetServerPrototype();
            System.out.println("Success.");

            System.out.println("Starting HTTP server... ");
            httpServer.start();
            System.out.println("Success.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        UdpServer udpServer = new UdpServer(UdpServer.UDP_RECEIVE_PORT, UdpServer.UDP_SEND_PORT);
        udpServer.run();
    }
}
