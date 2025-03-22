package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        if (model == null) {
            throw new IllegalArgumentException("Element must be not null.");
        }
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        if (model == null) {
            throw new IllegalArgumentException("Model cannot be null");
        }
        Base stored = memory.get(model.id());
        if (stored == null) {
            return false;
        }
        if (stored.version() != model.version()) {
            throw new OptimisticException("Versions are not equal");
        }
        return memory.replace(model.id(), stored, new Base(model.id(), model.name(), model.version() + 1));
    }

    public void delete(int id) {
        memory.remove(id);
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
