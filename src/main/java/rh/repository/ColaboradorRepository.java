package rh.repository;

import rh.model.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ColaboradorRepository {

    private final Map<Integer, Colaborador> store = new ConcurrentHashMap<>();

    public ColaboradorRepository() {
        store.put(1, new Funcionario(1, "Ana Lima",    4500.0, "Analista",  "01/03/2022"));
        store.put(2, new Estagiario(2, "Carlos Melo",  1200.0, "Ciência da Computação", 30));
        store.put(3, new Autonomo  (3, "Diana Souza",  8000.0, "UX Designer", "12.345.678/0001-99"));
        store.put(4, new Efetivo   (4, "Eduardo Neto", 7200.0, "Tech Lead",
                "15/06/2018", 12000.0, 7));
    }

    public List<Colaborador> findAll() {
        return new ArrayList<>(store.values()).stream()
                .sorted(Comparator.comparing(Colaborador::getId)).toList();
    }

    public Optional<Colaborador> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    public Colaborador save(Colaborador c) {
        store.put(c.getId(), c);
        return c;
    }

    public boolean deleteById(Integer id) {
        return store.remove(id) != null;
    }

    public boolean existsById(Integer id) {
        return store.containsKey(id);
    }
}
