package camp.computer;

import redis.clients.jedis.Jedis;

public class Redis {

    public static String REDIS_SERVER_URI = "localhost";
    public static int REDIS_SERVER_PORT = 6379;
    public static String REDIS_SERVER_PASSWORD = ""; // "redisadmin";

    public Redis() {
        Jedis jedis = new Jedis(REDIS_SERVER_URI, REDIS_SERVER_PORT);
        System.out.println("Connection to server sucessfully");
        //check whether server is running or not
        System.out.println("Server is running: " + jedis.ping());

        // Authenticate
        if (REDIS_SERVER_PASSWORD != null && REDIS_SERVER_PASSWORD.length() > 0) {
            jedis.auth(REDIS_SERVER_PASSWORD);
        }

        // Test
        //jedis.set("foo", "bar");
        long nextScriptId = jedis.incr("next_script_id");
        String value = jedis.get("foo");
        System.out.println("Value for key \"foo\": " + value);
    }

//    public long getNextId() {
//        // INCR next_user_id
//    }
}
