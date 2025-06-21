// src/me/joaomanoel/d4rkk/dev/utils/PacketAction.java
package me.joaomanoel.d4rkk.dev.utils;

/**
 * Ações permitidas para PacketPlayOutScoreboardTeam.
 * Mapeia para os códigos esperados pelo Wrapper/NMS.
 */
public enum PacketAction {
    CREATE(0),        // criar time
    REMOVE(1),        // remover time
    UPDATE(2),        // atualizar prefix/suffix
    ADD_PLAYER(3),    // adicionar jogador
    REMOVE_PLAYER(4); // remover jogador

    private final int id;
    PacketAction(int id) {
        this.id = id;
    }
    /** Retorna o código numérico usado internamente no packet */
    public int getId() {
        return id;
    }
}
