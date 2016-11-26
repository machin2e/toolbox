package camp.computer;

public class Application {

    public static JavaScriptEngine javaScriptEngine;

    public static void main(String[] args) {
        System.out.println("Java JSON Server Demo");
        try {
            javaScriptEngine = new JavaScriptEngine();
            JavaHTTPServer httpServer = new JavaHTTPServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
