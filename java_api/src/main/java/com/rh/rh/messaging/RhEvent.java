package com.rh.rh.messaging;

import java.time.Instant;
import java.util.Map;

public class RhEvent {

    private String tipo;
    private String origem;
    private String timestamp;
    private Map<String, Object> dados;

    public RhEvent() {}

    public RhEvent(String tipo, String origem, Map<String, Object> dados) {
        this.tipo = tipo;
        this.origem = origem;
        this.timestamp = Instant.now().toString();
        this.dados = dados;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getDados() { return dados; }
    public void setDados(Map<String, Object> dados) { this.dados = dados; }
}
