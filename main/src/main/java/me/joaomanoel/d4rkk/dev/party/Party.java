package me.joaomanoel.d4rkk.dev.party;

import me.joaomanoel.d4rkk.dev.Manager;
import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;
import me.joaomanoel.d4rkk.dev.player.role.Role;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * VERSÃO BUNGEECORD - USA LanguageBungee DIRETAMENTE
 * NÃO USA NENHUMA CLASSE DO BUKKIT
 */
public abstract class Party {

  /**
   * O tempo em minutos que demora até deletar uma Party caso todos os jogadores dela estejam offline.
   */
  private static final long MINUTES_UNTIL_DELETE = 5L;

  private static final long MINUTES_UNTIL_EXPIRE_INVITE = 1L;
  protected PartyPlayer leader;
  protected List<PartyPlayer> members;
  protected Map<String, Long> invitesMap;
  private int slots;
  private boolean isOpen;
  private long lastOnlineTime;

  public Party(String leader, int slots) {
    this.slots = slots;
    this.leader = new PartyPlayer(leader, PartyRole.LEADER);
    this.members = new ArrayList<>();
    this.invitesMap = new ConcurrentHashMap<>();
    this.members.add(this.leader);
  }

  public void setIsOpen(boolean flag) {
    this.isOpen = flag;
  }

  public void invite(Object target) {
    String leader = Role.getPrefixed(this.getLeader());
    this.invitesMap.put(Manager.getName(target).toLowerCase(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_EXPIRE_INVITE));

    // ✅ USA LanguageBungee DIRETAMENTE (SEM LanguageUtils)
    String message = LanguageBungee.party$invite.replace("%leader%", leader);
    message = ChatColor.translateAlternateColorCodes('&', message);

    BaseComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText(message)) {
      component.addExtra(components);
    }

    // Botão de Aceitar
    String acceptMessage = LanguageBungee.party$invite_buttons$accept;
    acceptMessage = ChatColor.translateAlternateColorCodes('&', acceptMessage);
    BaseComponent accept = new TextComponent(acceptMessage);
    accept.setColor(ChatColor.GREEN);
    accept.setBold(true);
    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + this.getLeader()));
    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', LanguageBungee.party$hover_accept.replace("%leader%", leader)))));
    component.addExtra(accept);

    // Adicionando o "ou" entre os botões
    String orMessage = LanguageBungee.party$or;
    orMessage = ChatColor.translateAlternateColorCodes('&', orMessage);
    component.addExtra(new TextComponent(orMessage));

    // Botão de Negar
    String rejectMessage = LanguageBungee.party$invite_buttons$reject;
    rejectMessage = ChatColor.translateAlternateColorCodes('&', rejectMessage);
    BaseComponent reject = new TextComponent(rejectMessage);
    reject.setColor(ChatColor.RED);
    reject.setBold(true);
    reject.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + this.getLeader()));
    reject.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', LanguageBungee.party$hover_reject.replace("%leader%", leader)))));
    component.addExtra(reject);

    // Finaliza o convite
    Manager.sendMessage(target, component);
  }

  public List<String> getMembers() {
    List<String> memberNames = new ArrayList<>();
    for (PartyPlayer member : this.members) {
      memberNames.add(member.getName());
    }
    return memberNames;
  }

  public void reject(String member) {
    this.invitesMap.remove(member.toLowerCase());

    // ✅ Mensagem de negação usando LanguageBungee
    String message = LanguageBungee.party$reject.replace("%member%", Role.getPrefixed(member));
    message = ChatColor.translateAlternateColorCodes('&', message);
    this.leader.sendMessage(message);
  }

  public void join(String member) {
    // ✅ Usando LanguageBungee
    String message = LanguageBungee.party$join.replace("%member%", Role.getPrefixed(member));
    message = ChatColor.translateAlternateColorCodes('&', message);
    this.broadcast(message);

    this.members.add(new PartyPlayer(member, PartyRole.MEMBER));
    this.invitesMap.remove(member.toLowerCase());
  }

  public void leave(String member) {
    String leaderName = this.getLeader();
    this.members.removeIf(pp -> pp.getName().equalsIgnoreCase(member));

    if (this.members.isEmpty()) {
      this.delete();
      return;
    }

    String prefixed = Role.getPrefixed(member);
    if (leaderName.equals(member)) {
      this.leader = this.members.get(0);
      this.leader.setRole(PartyRole.LEADER);

      // ✅ Mensagem de mudança de líder usando LanguageBungee
      String message = LanguageBungee.party$transfer.replace("%member%", prefixed);
      message = ChatColor.translateAlternateColorCodes('&', message);
      this.broadcast(message);
    }

    // ✅ Mensagem de saída usando LanguageBungee
    String message = LanguageBungee.party$leave.replace("%member%", prefixed);
    message = ChatColor.translateAlternateColorCodes('&', message);
    this.broadcast(message);
  }

  public void kick(String member) {
    this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(member)).findFirst().ifPresent(pp -> {
      // ✅ Mensagem de expulsão usando LanguageBungee
      String message = LanguageBungee.party$kick.replace("%leader%", Role.getPrefixed(this.getLeader()));
      message = ChatColor.translateAlternateColorCodes('&', message);
      pp.sendMessage(message);

      this.members.removeIf(pap -> pap.equals(pp));
    });
  }

  public void transfer(String name) {
    PartyPlayer newLeader = this.getPlayer(name);
    if (newLeader == null) {
      return;
    }
    this.leader.setRole(newLeader.getRole());
    newLeader.setRole(PartyRole.LEADER);
    this.leader = newLeader;
  }

  public void broadcast(String message) {
    this.broadcast(message, false);
  }

  public void broadcast(String message, boolean ignoreLeader) {
    this.members.stream().filter(pp -> !ignoreLeader || !pp.equals(this.leader)).forEach(pp -> pp.sendMessage(message));
  }

  public void update() {
    if (onlineCount() == 0) {
      if (this.lastOnlineTime + (TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_DELETE)) < System.currentTimeMillis()) {
        this.delete();
      }

      return;
    }

    this.lastOnlineTime = System.currentTimeMillis();
    this.invitesMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
  }

  public abstract void delete();

  public void destroy() {
    this.slots = 0;
    this.leader = null;
    this.members.clear();
    this.members = null;
    this.invitesMap.clear();
    this.invitesMap = null;
    this.lastOnlineTime = 0L;
  }

  public int getSlots() {
    return this.slots;
  }

  public long onlineCount() {
    return this.members.stream().filter(PartyPlayer::isOnline).count();
  }

  public String getLeader() {
    return this.leader.getName();
  }

  public String getName(String name) {
    return this.members.stream().map(PartyPlayer::getName).filter(ppName -> ppName.equalsIgnoreCase(name)).findAny().orElse(name);
  }

  public PartyPlayer getPlayer(String name) {
    return this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(name)).findAny().orElse(null);
  }

  public boolean isOpen() {
    return this.isOpen;
  }

  public boolean canJoin() {
    return this.members.size() < this.slots;
  }

  public boolean isInvited(String name) {
    return this.invitesMap.containsKey(name.toLowerCase());
  }

  public boolean isMember(String name) {
    return this.members.stream().anyMatch(pp -> pp.getName().equalsIgnoreCase(name));
  }

  public boolean isLeader(String name) {
    return this.leader.getName().equalsIgnoreCase(name);
  }

  public List<PartyPlayer> listMembers() {
    return this.members;
  }
}