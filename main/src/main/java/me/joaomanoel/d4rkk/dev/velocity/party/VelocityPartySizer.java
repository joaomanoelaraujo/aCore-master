package me.joaomanoel.d4rkk.dev.velocity.party;

import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.velocity.VelocityPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class VelocityPartySizer {

    private static final CommentedConfigurationNode UTILS;
    private static final Map<String, Integer> SIZES;

    static {
        UTILS = VelocityPlugin.getInstance().getUtils();
        SIZES = new LinkedHashMap<>();

        try {
            CommentedConfigurationNode partyNode = UTILS.node("party", "size");

            // Se não existir configuração, usa valores padrão
            if (partyNode.virtual() || partyNode.childrenMap().isEmpty()) {
                SIZES.put("role.master", 20);
                SIZES.put("role.youtuber", 15);
                SIZES.put("role.mvpplus", 10);
                SIZES.put("role.mvp", 5);
                VelocityPlugin.getInstance().getLogger().info("Usando tamanhos padrão de party (nenhuma configuração em utils.yml)");
            } else {
                // Carregar tamanhos do utils.yml
                for (Object key : partyNode.childrenMap().keySet()) {
                    String keyStr = key.toString();
                    int size = partyNode.node(keyStr).getInt(3);
                    // Converte role_master para role.master
                    SIZES.put(keyStr.replace("_", "."), size);
                }
                VelocityPlugin.getInstance().getLogger().info("Carregados " + SIZES.size() + " tamanhos de party do utils.yml");
            }
        } catch (Exception e) {
            VelocityPlugin.getInstance().getLogger().error("Erro ao carregar party sizes", e);
            // Valores padrão em caso de erro
            SIZES.put("role.master", 20);
            SIZES.put("role.youtuber", 15);
            SIZES.put("role.mvpplus", 10);
            SIZES.put("role.mvp", 5);
        }
    }

    public static int getPartySize(Player player) {
        // Verifica permissões em ordem de prioridade (maior para menor)
        for (Map.Entry<String, Integer> entry : SIZES.entrySet()) {
            if (player.hasPermission(entry.getKey())) {
                return entry.getValue();
            }
        }
        // Tamanho padrão se não tiver nenhuma permissão
        return 3;
    }
}