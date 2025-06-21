package me.joaomanoel.d4rkk.dev.nms;

import com.mojang.authlib.GameProfile;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

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
