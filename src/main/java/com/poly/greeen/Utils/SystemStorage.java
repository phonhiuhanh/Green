package com.poly.greeen.Utils;

import com.poly.greeen.Entity.Product;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SystemStorage {
    private final Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public void remove(String key) {
        data.remove(key);
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public void bulkRemoveStartsWith(String prefix) {
        data.keySet().removeIf(key -> key.startsWith(prefix));
    }

    public Optional<Product> findProductById(Integer id) {
        List<Product> allProducts = (List<Product>) this.get("all-products");
        return allProducts.stream()
                .filter(p -> p.getProductID().equals(id))
                .findFirst();
    }
}
