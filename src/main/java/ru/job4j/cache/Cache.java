package ru.job4j.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        if (model == null) {
            throw new IllegalArgumentException("Element must be not null.");
        }
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }
        return memory.computeIfPresent(model.id(), (id, stored) -> {
            if (stored.version() != model.version()) {
                throw new OptimisticException("Versions are not equal");
            }
            return new Base(model.id(), model.name(), model.version() + 1);
        }) != null;
    }

    public void delete(int id) {
        memory.remove(id);
    }

    public Optional<Base> findById(int id) {
        return Optional.ofNullable(memory.get(id));
    }
}
