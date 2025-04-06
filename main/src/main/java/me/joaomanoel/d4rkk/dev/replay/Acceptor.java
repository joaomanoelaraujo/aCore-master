/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.joaomanoel.d4rkk.dev.replay;

public abstract class Acceptor<T>
implements Runnable {
    private Consumer<T> consumer;

    public Acceptor(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    public abstract T getValue();

    @Override
    public void run() {
        this.consumer.accept(this.getValue());
    }
}

