package me.joaomanoel.d4rkk.dev.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.mojang.authlib.GameProfile;
import me.joaomanoel.d4rkk.dev.nms.particle.ParticleOptions;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

/**
 * Classe com utilitários relacionado a {@link org.bukkit}.
 */
public class BukkitUtils {

  private static BukkitUtilsItf instance;


  public static void setInstance(BukkitUtilsItf instance) {
    BukkitUtils.instance = instance;
  }

  private static final boolean USE_API;
  private static final Method M_SPAWN_PARTICLE;
  private static final Class<?> BUKKIT_PARTICLE_ENUM;

  static {
    boolean api = false;
    Method spawn = null;
    Class<?> partEnum = null;
    try {
      partEnum = Class.forName("org.bukkit.Particle");
      spawn = World.class.getMethod("spawnParticle",
              partEnum,
              Location.class,
              int.class,
              double.class, double.class, double.class,
              double.class);
      api = true;
      Bukkit.getLogger().info("[Core] usando API nativa de partículas (1.9+)");
    } catch (Exception ex) {
      api = false;
      Bukkit.getLogger().info("[Core] usando fallback NMS para partículas (1.8.8)");
    }
    USE_API = api;
    M_SPAWN_PARTICLE = spawn;
    BUKKIT_PARTICLE_ENUM = partEnum;
  }

  /**
   * Spawna a partícula tanto em 1.9+ (via World#spawnParticle) quanto em 1.8.8 (via PacketPlayOutWorldParticles).
   */
  public static void displayParticle(Player viewer,
                                     String particleName,
                                     boolean isFar,
                                     float x, float y, float z,
                                     float offSetX, float offSetY, float offSetZ,
                                     float speed,
                                     int count) {
    if (USE_API) {
      try {
        // Particle.valueOf( “HAPPY_VILLAGER” ) espera undercores e uppercase
        Object enumConst = Enum.valueOf(
                (Class<Enum>)BUKKIT_PARTICLE_ENUM,
                particleName.toUpperCase(Locale.ROOT)
                        .replace("HAPPYVILLAGER", "VILLAGER_HAPPY")
                        .replace("ANGRYVILLAGER", "VILLAGER_ANGRY")
        );
        // world.spawnParticle(enumConst, loc, count, offset..., speed)
        M_SPAWN_PARTICLE.invoke(
                viewer.getWorld(),
                enumConst,
                new Location(viewer.getWorld(), x, y, z),
                count,
                offSetX, offSetY, offSetZ,
                speed
        );
        return;
      } catch (Throwable t) {
        t.printStackTrace();
        // cai no fallback NMS
      }
    }

    // ===== fallback NMS 1.8.8 =====
    try {
      String version = Bukkit.getServer()
              .getClass()
              .getPackage()
              .getName()
              .split("\\.")[3];

      Class<?> packetClass = Class.forName(
              "net.minecraft.server." + version + ".PacketPlayOutWorldParticles"
      );
      Class<?> enumParticleClass = Class.forName(
              "net.minecraft.server." + version + ".EnumParticle"
      );

      // normaliza o nome: HAPPYVILLAGER → VILLAGER_HAPPY, remove "_" p/ comparação genérica
      String name = particleName.toUpperCase(Locale.ROOT).replace("_","");
      if (name.equals("HAPPYVILLAGER"))      name = "VILLAGER_HAPPY";
      else if (name.equals("ANGRYVILLAGER")) name = "VILLAGER_ANGRY";
      else {
        for (Object c : enumParticleClass.getEnumConstants()) {
          String cst = ((Enum<?>)c).name().replace("_","");
          if (cst.equalsIgnoreCase(name)) {
            name = ((Enum<?>)c).name();
            break;
          }
        }
      }

      Object enumParticle = Enum.valueOf(
              (Class<Enum>)enumParticleClass,
              name
      );

      Constructor<?> cons = packetClass.getConstructor(
              enumParticleClass, boolean.class,
              float.class, float.class, float.class,
              float.class, float.class, float.class,
              float.class,    // speed
              int.class,      // count
              int[].class     // varargs data
      );

      Object packet = cons.newInstance(
              enumParticle,
              isFar,            // longDistance
              x, y, z,
              offSetX, offSetY, offSetZ,
              speed,
              count,
              new int[0]
      );

      // envia só para este player
      Method  mGetHandle = viewer.getClass().getMethod("getHandle");
      Object  nmsPlayer  = mGetHandle.invoke(viewer);
      Field fConn      = nmsPlayer.getClass().getField("playerConnection");
      Object  conn       = fConn.get(nmsPlayer);

      Class<?> packetIntf = Class.forName(
              "net.minecraft.server." + version + ".Packet"
      );
      Method mSend = conn.getClass().getMethod("sendPacket", packetIntf);
      mSend.invoke(conn, packet);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private BufferedImage glyphImage;


  public static void showIn(Player origin, Location location) {
    instance.showIn(origin, location);
  }


  /** Sobrecarga que recebe ParticleOptions */
  public static void displayParticle(Player viewer,
                                     ParticleOptions option,
                                     boolean isFar,
                                     float x, float y, float z,
                                     float offSetX, float offSetY, float offSetZ,
                                     float speed,
                                     int count) {
    displayParticle(viewer,
            option,
            isFar, x, y, z,
            offSetX, offSetY, offSetZ,
            speed, count);
  }

  public static void openBook(Player player, ItemStack book) {
    instance.openBook(player, book);
  }

  /**
   * Cria um {@link ItemStack} a partir de uma {@code String}.<br/>
   * Formato: {@code MATERIAL:DURABILIDADE : QUANTIDADE : tag>valor}<br/>
   * Referência de Material: {@link Material}<br/>
   * <br/>
   * Propriedades (TAGS) disponíveis:
   * <ul>
   * <li>name>&aSei lá - Seta o nome do Item.</li>
   * <li>desc>&7Linha 1\n&7Linha2 - Seta a descrição do Item.</li>
   * <li>encantar>DAMAGE_ALL:1\nFIRE_ASPECT:1 - Encanta o Item.</li>
   * <li>pintar>{@link Color} ou pintar>r:g:b - Pinta os Itens: Armadura de Couro e Fogos de
   * Artifício.</li>
   * <li>dono>Notch - Seta o dono de uma cabeça (Recomendado skin>skinvalue}.</li>
   * <li>skin>skinvalue - Seta o valor da Skin através do {@link GameProfile} para cabeças
   * customizadas.</li>
   * <li>paginas>Linha1 pagina1\nLinha2 pagina1{pular}Linha1 pagina2\nLinha2 pagina2 - Seta as Pages
   * do livro. (Utilize {pular} para pular para outra Page)</li>
   * <li>autor>&6d4rkk - Seta o autor do livro.</li>
   * <li>titulo>&6aCore - Seta o título do livro.</li>
   * <li>efeito>{@link PotionEffectType}:nivel(começa do 0):ticks(20ticks =
   * 1segundo)\nINVISIBILITY:0:600 - Adiciona efeitos em poções.</li>
   * <li>hide>{@link ItemFlag}\n{@link ItemFlag} ou hide>all - Aplica ItemFlags (Adicionado
   * na 1.8)</li>
   * </ul>
   *
   * @param item O {@link ItemStack} em uma String.
   * @return O {@link ItemStack} criado.
   */
  public static ItemStack deserializeItemStack(String item) {
    return instance.deserializeItemStack(item);
  }

  /**
   * Transforma um {@link ItemStack} em uma {@code String}.<br/>
   *
   * @param item O ItemStack para transforma em String.
   * @return Um ItemStack transformado em {@code String} para ser utilizado no método
   * {@link BukkitUtils#deserializeItemStack(String)}.
   */
  public static String serializeItemStack(ItemStack item) {
    return instance.serializeItemStack(item);
  }

  /**
   * Seta a partir da {@code Reflection} o perfil de um {@link ItemStack} do tipo Cabeça.
   *
   * @param player O jogador para requisitar o {@link GameProfile}.
   * @param head   O {@link ItemStack} do tipo Cabeça.
   * @return O {@link ItemStack} modificado com o Perfil do jogador.
   */
  public static ItemStack putProfileOnSkull(Player player, ItemStack head) {
    return instance.putProfileOnSkull(player, head);
  }

  /**
   * Seta a partir da {@code Reflection} o perfil de um {@link ItemStack} do tipo Cabeça.
   *
   * @param profile O {@link GameProfile} para modificar.
   * @param head    O {@link ItemStack} do tipo Cabeça.
   * @return O {@link ItemStack} modificado com o Perfil.
   */
  public static ItemStack putProfileOnSkull(Object profile, ItemStack head) {
    return instance.putProfileOnSkull(profile, head);
  }

  /**
   * Faz o item "Brilhar" (Encantamento) sem mostrar os encantamentos do item.
   *
   * @param item O {@link ItemStack}
   */
  public static void putGlowEnchantment(ItemStack item) {
    instance.putGlowEnchantment(item);
  }

  public static ItemStack applyNTBTag(ItemStack item, List<Object> lines){
    return instance.applyNTBTag(item, lines);
  }

  /**
   * Transforma uma {@link Location} em uma {@code String} utilizando o seguinte formato:<br/>
   * {@code "mundo; x; y; z; yaw; pitch"}
   *
   * @param unserialized A {@link Location} para transformar em {@code String}.
   * @return A {@link Location} transformada em uma {@code String}.
   */
  public static String serializeLocation(Location unserialized) {
    return unserialized.getWorld().getName() + "; " + unserialized.getX() + "; " + unserialized.getY() + "; " + unserialized.getZ() + "; " + unserialized
        .getYaw() + "; " + unserialized.getPitch();
  }
  
  /**
   * Transforma uma {@code String} em uma {@link Location} utilizando o seguinte formato:<br/>
   * {@code "mundo; x; y; z; yaw; pitch"}
   *
   * @param serialized A {@code String} para transformar em {@link Location}.
   * @return A {@code String} transformada em uma {@link Location}.
   */
  public static Location deserializeLocation(String serialized) {
    String[] divPoints = serialized.split("; ");
    Location deserialized = new Location(Bukkit.getWorld(divPoints[0]), parseDouble(divPoints[1]), parseDouble(divPoints[2]), parseDouble(divPoints[3]));
    deserialized.setYaw(parseFloat(divPoints[4]));
    deserialized.setPitch(parseFloat(divPoints[5]));
    return deserialized;
  }
}
