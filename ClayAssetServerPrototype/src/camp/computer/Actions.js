var action = function(data) {
    print("action(data) called with data: " + data);

    var result = clay.setPort(1, data);
    print(result);

    data = "changed in Action_001";

    return data;
}

//virtualRedisStore.put("get ports", "var action = function(data) { clay.getPorts(); return data; }");
//virtualRedisStore.put("set brightness", "var action = function(data) { print('setBrightness'); print(data); return data; }");
//virtualRedisStore.put("set color", "var action = function(data) { print('setColor');  print(data); return data; }");
//virtualRedisStore.put("set rotation direction", "var action = function(data) { print('setRotationDirection'); print(data); return data; }");
//virtualRedisStore.put("set rotation speed", "var action = function(data) { print('setRotationSpeed'); print(data); return data; }");
//virtualRedisStore.put("send message", "var action = function(data) { print('sendMessage'); print(data); return data; }");
//virtualRedisStore.put("get sensor data", "var action = function(data) { print('getSernsorData'); print(data); return data; }");