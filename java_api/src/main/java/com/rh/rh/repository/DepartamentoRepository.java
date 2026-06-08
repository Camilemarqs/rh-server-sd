package com.rh.rh.repository;

import com.rh.rh.model.Departamento;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DepartamentoRepository {

    private final Map<Integer, Departamento> store = new ConcurrentHashMap<>();

    public List<Departamento> findAll() {
        return new ArrayList<>(store.values()).stream()
                .sorted(Comparator.comparing(Departamento::getId)).toList();
    }

    public Optional<Departamento> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    public Departamento save(Departamento d) {
        store.put(d.getId(), d);
        return d;
    }

    public boolean deleteById(Integer id) {
        return store.remove(id) != null;
    }

    public boolean existsById(Integer id) {
        return store.containsKey(id);
    }
}
