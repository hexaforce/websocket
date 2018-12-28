package io.hexaforce.websocket.model;

public enum DataChannel1 {
    BUY(1),
    SELL(2);

    private final int id;

    public int getId() {
        return id;
    }
    
    DataChannel1(int id) {
        this.id = id;
    }
}
