package camp.computer;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import camp.computer.clay.VirtualHost;

public class JavaScriptEngine {

    public static String JAVASCRIPT_ENGINE_NAME = "nashorn";

    // JavaScript Nashorn Engine
    ScriptEngineManager engineManager;
    ScriptEngine engine;

    public JavaScriptEngine() throws ScriptException {

        // Create JavaScript Engine
        engineManager = new ScriptEngineManager();
        engine = engineManager.getEngineByName(JAVASCRIPT_ENGINE_NAME);

        // Load Platform API
        // i.e., a custom Java API for composing scripts into actions (a la Clay) and JS methods from JavaScript
        engine.eval("var Clay = Java.type('camp.computer.clay.VirtualPlatformInterface');");
        engine.eval("var clay = new Clay;"); // Create object
    }

    public void eval(String script) {
        try {

            // Clean JSON string
            script = strip(script);

            // Evaluate JavaScript
            engine.eval(script);

            // Get data namespace
            VirtualHost virtualHost = new VirtualHost();
            String dataNamespace = virtualHost.getDataNamespace();

            Invocable invocable = (Invocable) engine;
            Object result = invocable.invokeFunction("action", dataNamespace);

            String resultString = (String) result;

            // System.out.println("result: " + resultString);

            /*
            engine.eval("var data = '{ \"color\": \"blue\", \"brightness\": 0.8 }';");
            Object jsonStringifyResult = engine.eval("var deserializedData = JSON.parse(data);");
            jsonStringifyResult = engine.eval("print(deserializedData['color']);");
            jsonStringifyResult = engine.eval("print(deserializedData['brightness']);");
            jsonStringifyResult = engine.eval("deserializedData['color'] = 'red';");
            jsonStringifyResult = engine.eval("var serializedData = JSON.stringify(deserializedData);");
            jsonStringifyResult = engine.eval("serializedData;");

            System.out.println("JSON.stringify: " + (String) jsonStringifyResult);

            // Deserialize result data from script execution
            String value = ClayAssetServerPrototype.parseValue((String) jsonStringifyResult, "color");
            System.out.println("new color value: " + value);
            */

        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static String strip(String string) {
        return string.replaceAll("^\"|\"$", "");
    }
}
