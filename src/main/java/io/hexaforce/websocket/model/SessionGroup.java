package io.hexaforce.websocket.model;

public enum SessionGroup {
    BUY(1),
    SELL(2);

    private final int id;

    SessionGroup(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static SessionGroup valueOf(int id) {
        for (SessionGroup v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }
        throw new IllegalArgumentException("no such enum object for the id: " + id);
    }

    public SessionGroup getOther() {
        if (this.id == BUY.getId()) {
            return SELL;
        } else {
            return BUY;
        }
    }
}
