package me.joaomanoel.d4rkk.dev.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;
import me.joaomanoel.d4rkk.dev.database.Database;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import me.joaomanoel.d4rkk.dev.velocity.channels.VelocityChannels;
import me.joaomanoel.d4rkk.dev.velocity.cmd.Commands;
import me.joaomanoel.d4rkk.dev.velocity.listeners.VelocityListeners;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

@Plugin(
        id = "acore",
        name = "aCore",
        version = "2.9.0",
        authors = {"D4rkk"}
)
public class VelocityPlugin {

    public static final String STEVE =
            "eyJ0aW1lc3RhbXAiOjE1ODcxNTAzMTc3MjAsInByb2ZpbGVJZCI6IjRkNzA0ODZmNTA5MjRkMzM4NmJiZmM5YzEyYmFiNGFlIiwicHJvZmlsZU5hbWUiOiJzaXJGYWJpb3pzY2hlIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYTRhZjcxODQ1NWQ0YWFiNTI4ZTdhNjFmODZmYTI1ZTZhMzY5ZDE3NjhkY2IxM2Y3ZGYzMTlhNzEzZWI4MTBiIn19fQ==:syZ2Mt1vQeEjh/t8RGbv810mcfTrhQvnwEV7iLCd+5udVeroTa5NjoUehgswacTML3k/KxHZHaq4o6LmACHwsj/ivstW4PWc2RmVn+CcOoDKI3ytEm70LvGz0wAaTVKkrXHSw/RbEX/b7g7oQ8F67rzpiZ1+Z3TKaxbgZ9vgBQZQdwRJjVML2keI0669a9a1lWq3V/VIKFZc1rMJGzETMB2QL7JVTpQFOH/zXJGA+hJS5bRol+JG3LZTX93+DililM1e8KEjKDS496DYhMAr6AfTUfirLAN1Jv+WW70DzIpeKKXWR5ZeI+9qf48+IvjG8DhRBVFwwKP34DADbLhuebrolF/UyBIB9sABmozYdfit9uIywWW9+KYgpl2EtFXHG7CltIcNkbBbOdZy0Qzq62Tx6z/EK2acKn4oscFMqrobtioh5cA/BCRb9V4wh0fy5qx6DYHyRBdzLcQUfb6DkDx1uyNJ7R5mO44b79pSo8gdd9VvMryn/+KaJu2UvyCrMVUtOOzoIh4nCMc9wXOFW3jZ7ZTo4J6c28ouL98rVQSAImEd/P017uGvWIT+hgkdXnacVG895Y6ilXqJToyvf1JUQb4dgry0WTv6UTAjNgrm5a8mZx9OryLuI2obas97LCon1rydcNXnBtjUk0TUzdrvIa5zNstYZPchUb+FSnU=";
    public static final String ALEX =
            "eyJ0aW1lc3RhbXAiOjE1ODcxMzkyMDU4MzUsInByb2ZpbGVJZCI6Ijc1MTQ0NDgxOTFlNjQ1NDY4Yzk3MzlhNmUzOTU3YmViIiwicHJvZmlsZU5hbWUiOiJUaGFua3NNb2phbmciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNiNjBhMWY2ZDU2MmY1MmFhZWJiZjE0MzRmMWRlMTQ3OTMzYTNhZmZlMGU3NjRmYTQ5ZWEwNTc1MzY2MjNjZDMiLCJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifX19fQ==:W60UUuAYlWfLFt5Ay3Lvd/CGUbKuuU8+HTtN/cZLhc0BC22XNgbY1btTite7ZtBUGiZyFOhYqQi+LxVWrdjKEAdHCSYWpCRMFhB1m0zEfu78yg4XMcFmd1v7y9ZfS45b3pLAJ463YyjDaT64kkeUkP6BUmgsTA2iIWvM33k6Tj3OAM39kypFSuH+UEpkx603XtxratD+pBjUCUvWyj2DMxwnwclP/uACyh0ZVrI7rC5xJn4jSura+5J2/j6Z/I7lMBBGLESt7+pGn/3/kArDE/1RShOvm5eYKqrTMRfK4n3yd1U1DRsMzxkU2AdlCrv1swT4o+Cq8zMI97CF/xyqk8z2L98HKlzLjtvXIE6ogljyHc9YsfU9XhHwZ7SKXRNkmHswOgYIQCSa1RdLHtlVjN9UdUyUoQIIO2AWPzdKseKJJhXwqKJ7lzfAtStErRzDjmjr7ld/5tFd3TTQZ8yiq3D6aRLRUnOMTr7kFOycPOPhOeZQlTjJ6SH3PWFsdtMMQsGzb2vSukkXvJXFVUM0TcwRZlqT5MFHyKBBPprIt0wVN6MmSKc8m5kdk7ZBU2ICDs/9Cd/fyzAIRDu3Kzm7egbAVK9zc1kXwGzowUkGGy1XvZxyRS5jF1zu6KzVgaXOGcrOLH4z/OHzxvbyW22/UwahWGN7MD4j37iJ7gjZDrk=";

    private static VelocityPlugin instance;
    private static final Map<String, String> fakeNames = new HashMap<>();
    private static final Map<String, Role> fakeRoles = new HashMap<>();
    private static final Map<String, String> fakeSkins = new HashMap<>();
    private static List<String> randoms;

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private CommentedConfigurationNode config;
    private CommentedConfigurationNode utils;
    private CommentedConfigurationNode roles;

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        instance = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        loadConfigs();

        // Configuração do database usando velocity.yml
        File sqliteFile = new File(dataDirectory.toFile(),
                config.node("database", "sqlite", "file").getString("database.db"));

        Database.setupDatabase(
                config.node("database", "type").getString("sqlite"),
                config.node("database", "mysql", "host").getString("localhost"),
                config.node("database", "mysql", "port").getString("3306"),
                config.node("database", "mysql", "name").getString("database"),
                config.node("database", "mysql", "user").getString("root"),
                config.node("database", "mysql", "pass").getString(""),
                config.node("database", "mysql", "hikari").getBoolean(true),
                config.node("database", "mysql", "mariadb").getBoolean(false),
                config.node("database", "mongodb", "url").getString(""),
                sqliteFile
        );

        setupRoles();
        LanguageVelocity.setupLanguage();
        Commands.setupCommands(this);

        server.getEventManager().register(this, new VelocityListeners(this));
        server.getChannelRegistrar().register(VelocityChannels.MAIN_CHANNEL);

        logger.info("Plugin aCore ativado com sucesso!");
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("Plugin aCore desativado.");
    }

    private void loadConfigs() {
        try {
            dataDirectory.toFile().mkdirs();

            // Carrega APENAS velocity.yml (não gera config.yml)
            config = loadConfig("velocity.yml", true);

            // Carrega configs compartilhados opcionais
            utils = loadConfig("utils.yml", false);
            roles = loadConfig("roles.yml", true);

        } catch (Exception e) {
            logger.error("Erro ao carregar configurações", e);
        }
    }

    private CommentedConfigurationNode loadConfig(String fileName, boolean createDefault) throws IOException {
        File file = new File(dataDirectory.toFile(), fileName);

        // Cria o arquivo padrão se não existir
        if (!file.exists() && createDefault) {
            // Tenta copiar do resource, se existir
            try (InputStream in = getClass().getResourceAsStream("/" + fileName)) {
                if (in != null) {
                    try (OutputStream out = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                    logger.info("Arquivo " + fileName + " criado com valores padrão");
                } else {
                    // Cria arquivo com valores padrão para velocity.yml
                    if (fileName.equals("velocity.yml")) {
                        createDefaultVelocityConfig(file);
                    } else if (fileName.equals("roles.yml")) {
                        createDefaultRolesConfig(file);
                    }
                }
            }
        }

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build();

        return file.exists() ? loader.load() : loader.createNode();
    }

    private void createDefaultVelocityConfig(File file) throws IOException {
        file.createNewFile();
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build();

        CommentedConfigurationNode node = loader.createNode();

        // Database
        node.node("database", "type").set("sqlite");
        node.node("database", "sqlite", "file").set("database.db");
        node.node("database", "mysql", "host").set("localhost");
        node.node("database", "mysql", "port").set("3306");
        node.node("database", "mysql", "name").set("database");
        node.node("database", "mysql", "user").set("root");
        node.node("database", "mysql", "pass").set("");
        node.node("database", "mysql", "hikari").set(true);
        node.node("database", "mysql", "mariadb").set(false);
        node.node("database", "mongodb", "url").set("mongodb://localhost:27017");

        // Fake
        node.node("fake", "enabled").set(true);
        node.node("fake", "kick-apply").set("§aAplicando fake...");
        node.node("fake", "kick-remove").set("§cRemovendo fake...");
        node.node("fake", "role").set(Arrays.asList("Member", "VIP"));
        node.node("fake", "randoms").set(Arrays.asList("Fake1", "Fake2", "Fake3"));

        // Party
        node.node("party", "size", "role.master").set(20);
        node.node("party", "size", "role.youtuber").set(15);
        node.node("party", "size", "role.mvpplus").set(10);
        node.node("party", "size", "role.mvp").set(5);

        loader.save(node);
        logger.info("Arquivo velocity.yml criado com configurações padrão");
    }

    private void createDefaultRolesConfig(File file) throws IOException {
        file.createNewFile();
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build();

        CommentedConfigurationNode node = loader.createNode();

        node.node("roles", "member", "name").set("Member");
        node.node("roles", "member", "prefix").set("§7");
        node.node("roles", "member", "permission").set("");
        node.node("roles", "member", "broadcast").set(false);
        node.node("roles", "member", "alwaysvisible").set(false);
        node.node("roles", "member", "fly").set(false);

        loader.save(node);
        logger.info("Arquivo roles.yml criado com configurações padrão");
    }

    private void setupRoles() {
        try {
            CommentedConfigurationNode rolesSection = roles.node("roles");

            if (rolesSection.virtual() || rolesSection.childrenMap().isEmpty()) {
                // Cria role padrão se não existir nenhuma
                Role.listRoles().add(new Role("§7Member", "§7", "", false, false, false));
                logger.warn("Nenhuma role configurada em roles.yml. Usando role padrão.");
                return;
            }

            for (Object key : rolesSection.childrenMap().keySet()) {
                String keyStr = key.toString();
                CommentedConfigurationNode roleNode = rolesSection.node(keyStr);

                String name = roleNode.node("name").getString("");
                String prefix = roleNode.node("prefix").getString("");
                String permission = roleNode.node("permission").getString("");
                boolean broadcast = roleNode.node("broadcast").getBoolean(true);
                boolean alwaysVisible = roleNode.node("alwaysvisible").getBoolean(false);
                boolean fly = roleNode.node("fly").getBoolean(false);

                Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast, fly));
            }

            logger.info("Carregadas " + Role.listRoles().size() + " roles");
        } catch (Exception e) {
            logger.error("Erro ao carregar roles", e);
        }
    }

    // Métodos estáticos
    public static VelocityPlugin getInstance() {
        return instance;
    }

    public static void applyFake(Player player, String fakeName, String role, String skin) {
        Component message = LegacyComponentSerializer.legacySection()
                .deserialize(getInstance().getConfig().node("fake", "kick-apply").getString("§aAplicando fake..."));
        player.disconnect(message);

        fakeNames.put(player.getUsername(), fakeName);
        fakeRoles.put(player.getUsername(), Role.getRoleByName(role));
        fakeSkins.put(player.getUsername(), skin);
    }

    public static void removeFake(Player player) {
        Component message = LegacyComponentSerializer.legacySection()
                .deserialize(getInstance().getConfig().node("fake", "kick-remove").getString("§cRemovendo fake..."));
        player.disconnect(message);

        fakeNames.remove(player.getUsername());
        fakeRoles.remove(player.getUsername());
        fakeSkins.remove(player.getUsername());
    }

    public static Player getPlayerByName(String playerName) {
        return getInstance().getServer().getPlayer(playerName).orElse(null);
    }

    public static boolean isVelocityActive() {
        return getInstance().getServer() != null && !getInstance().getServer().getAllPlayers().isEmpty();
    }

    public static String getCurrent(String playerName) {
        return isFake(playerName) ? getFake(playerName) : playerName;
    }

    public static String getFake(String playerName) {
        return fakeNames.get(playerName);
    }

    public static Role getRole(String playerName) {
        return fakeRoles.getOrDefault(playerName, Role.getLastRole());
    }

    public static String getSkin(String playerName) {
        return fakeSkins.getOrDefault(playerName, STEVE);
    }

    public static String getServerName(Player player) {
        return player.getCurrentServer().map(s -> s.getServerInfo().getName()).orElse(null);
    }

    public static boolean isFake(String playerName) {
        return fakeNames.containsKey(playerName);
    }

    public static boolean isUsable(String name) {
        return !fakeNames.containsKey(name)
                && !fakeNames.containsValue(name)
                && !getInstance().getServer().getPlayer(name).isPresent();
    }

    public static List<String> listNicked() {
        return new ArrayList<>(fakeNames.keySet());
    }

    public static List<String> getRandomNicks() throws SerializationException {
        if (randoms == null) {
            randoms = getInstance().getConfig().node("fake", "randoms").getList(String.class, new ArrayList<>());
        }
        return randoms;
    }

    public static boolean isFakeRole(String roleName) throws SerializationException {
        return getInstance().getConfig().node("fake", "role").getList(String.class, new ArrayList<>())
                .stream().anyMatch(role -> role.equalsIgnoreCase(roleName));
    }

    // Getters
    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public CommentedConfigurationNode getConfig() {
        return config;
    }

    public CommentedConfigurationNode getUtils() {
        return utils;
    }

    public CommentedConfigurationNode getRoles() {
        return roles;
    }
}