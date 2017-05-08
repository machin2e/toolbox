package camp.computer.util;

import java.util.HashMap;

public class Cache {

    private long cacheUid = 0L;

    private HashMap<Long, Object> elements;

    public Cache() {
        elements = new HashMap<>();
    }

    public long add(Object object) {
        long objectUid = cacheUid++;
        elements.put(objectUid, object);
        return objectUid;
    }

    public Object get(long uid) {
        return elements.get(uid);
    }

    public List<Long> getUids() {
        return new List<>(elements.keySet());
    }

    public List<Object> getObjects() {
        return new List<>(elements.values());
    }

    public <T> List<T> getObjects(Class<T> objectType) {
        List<T> objects = new List<>();
        return objects;
    }

    public int size() {
        return elements.size();
    }
}
