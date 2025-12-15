package me.joaomanoel.d4rkk.dev.replay;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.game.Game;
import me.joaomanoel.d4rkk.dev.nms.BukkitUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class Replays {

    private final Game<?> game;
    private final String name;
    private String gameName;
    private String mode;
    private String map;
    private String server;
    private int players;

    public Replays(Game<?> game, String name) {
        this.game = game;
        this.name = name + "-id-" + System.nanoTime();
    }

    public Game<?> getGame() {
        return this.game;
    }

    public String getName() {
        return this.name;
    }

    /** Inicia a gravação do replay com informações padrão. */
    public void createReplay() {
        ReplayAPI.getInstance().recordReplay(this.name, this.game.listPlayers(), this.gameName, this.mode, this.map, this.server, this.players);
    }

    /** Inicia a gravação do replay com informações detalhadas. */
    public void createReplay(String gameName, String mode, String map, String server, int players) {
        this.gameName = gameName;
        this.mode = mode;
        this.map = map;
        this.server = server;
        this.players = players;

        // Nova versão que passa os dados do jogo
        ReplayAPI.getInstance().recordReplay(this.name, this.game.listPlayers(), gameName, mode, map, server, players);
    }

    /** Para e salva o replay atual. */
    public void stopReplay() {
        Replay replay = ReplayManager.activeReplays.get(this.name);
        if (replay != null) {
            ReplayInfo info = replay.getReplayInfo() != null
                    ? replay.getReplayInfo()
                    : new ReplayInfo(
                    replay.getId(),
                    replay.getData().getCreator(),
                    System.currentTimeMillis(),
                    replay.getData().getDuration(),
                    replay.getGameName(),
                    replay.getMode(),
                    replay.getMap(),
                    replay.getServer(),
                    replay.getPlayers()
            );

            if (info == null) {
                info = new ReplayInfo(
                        replay.getId(),
                        replay.getData().getCreator(),
                        System.currentTimeMillis(),
                        replay.getData().getDuration(),
                        replay.getGameName(),
                        replay.getMode(),
                        replay.getMap(),
                        replay.getServer(),
                        replay.getPlayers()
                );
                replay.setReplayInfo(info);
            } else {
                info.setGameName(this.gameName);
                info.setMode(this.mode);
                info.setMap(this.map);
                info.setServer(this.server);
                info.setPlayers(this.players);
            }
        }

        ReplayAPI.getInstance().stopReplay(this.name, true);
    }


    public static List<ReplayInfo> getReplays() {
        List<ReplayInfo> replays = new ArrayList<>();
        try {
            MySQLDatabase database = ConfigManager.getMySQLDatabase();
            if (database == null || database.getConnection() == null) {
                Core.getInstance().getLogger().warning("Banco de dados não inicializado!");
                return replays;
            }

            PreparedStatement pst = database.getConnection().prepareStatement(
                    "SELECT id, creator, duration, time, game_name, mode, map, server, players FROM replays"
            );

            ResultSet rs = database.query(pst);
            while (rs.next()) {
                String id = rs.getString("id");
                List<String> creator = Collections.singletonList(rs.getString("creator"));
                int duration = rs.getInt("duration");
                long time = rs.getLong("time");
                String gameName = rs.getString("game_name");
                String mode = rs.getString("mode");
                String map = rs.getString("map");
                String server = rs.getString("server");
                int players = rs.getInt("players");

                replays.add(new ReplayInfo(id, creator, time, duration, gameName, mode, map, server, players));
            }
            pst.close();
        } catch (Exception e) {
            Core.getInstance().getLogger().warning("Erro ao buscar replays: " + e.getMessage());
            e.printStackTrace();
        }
        return replays;
    }

    /**
     * Retorna os replays do jogador como ícones no inventário.
     * Estilo de descrição e formatação inspirado na Hypixel.
     */
    public static List<ItemStack> getItem(Player player) {
        List<ItemStack> items = new ArrayList<>();
        try {
            MySQLDatabase database = ConfigManager.getMySQLDatabase();
            if (database == null || database.getConnection() == null) {
                Core.getInstance().getLogger().warning("Banco de dados não inicializado!");
                return items;
            }

            PreparedStatement pst = database.getConnection().prepareStatement(
                    "SELECT id, creator, duration, time, game_name, mode, map, server, players FROM replays"
            );

            ResultSet rs = database.query(pst);
            while (rs.next()) {
                String id = rs.getString("id");
                String creator = rs.getString("creator");
                int duration = rs.getInt("duration");
                long time = rs.getLong("time");
                String gameName = rs.getString("game_name");
                String mode = rs.getString("mode");
                String map = rs.getString("map");
                String server = rs.getString("server");
                int players = rs.getInt("players");

                if (!creator.contains(player.getName())) continue;

                // Ícone dinâmico de acordo com o jogo
                Material icon = getIconForGame(gameName);

                // Data formatada
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(time));

                // Descrição estilo Hypixel
                String desc = "&7Game: &e" + (gameName != null ? gameName : "Desconhecido")
                        + (mode != null ? "\n&7Mode: &e" + mode : "")
                        + (map != null ? "\n&7Map: &e" + map : "")
                        + (server != null ? "\n&7Server: &e" + server : "")
                        + "\n&7Players: &e" + players
                        + "\n&7Duration: &e" + (duration / 20) + "s"
                        + "\n&7Date: &e" + formattedDate
                        + "\n"
                        + "\n&eClick to watch!";

                // Criação do item
                ItemStack item = BukkitUtils.deserializeItemStack(
                        icon.name() + " : 1 : name>&aReplay #" + id + " : desc>" + desc
                );

                items.add(item);
            }

            pst.close();
        } catch (Exception e) {
            Core.getInstance().getLogger().warning("Erro ao carregar replays: " + e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    /** Define o ícone do replay conforme o minigame. */
    private static Material getIconForGame(String gameName) {
        if (gameName == null) return Material.BOOK;
        String name = gameName.toLowerCase();

        if (name.contains("bedwars")) return Material.BED;
        if (name.contains("skywars")) return Material.IRON_SWORD;
        if (name.contains("bridge")) return Material.BRICK;
        if (name.contains("uhc")) return Material.GOLDEN_APPLE;
        if (name.contains("build")) return Material.WORKBENCH;
        if (name.contains("murder")) return Material.IRON_AXE;
        if (name.contains("duel")) return Material.DIAMOND_SWORD;
        if (name.contains("sg")) return Material.BOW;

        return Material.BOOK;
    }
}
