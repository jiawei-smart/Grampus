package org.grampus.core;

public enum GCellState {
    UNREGISTERED(0), REGISTERED(1), READY_TO_START(2),WAITING(4),RUNNING(5),STOPPED(6);
    private final int value;

    GCellState(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
