package me.joaomanoel.d4rkk.dev.database.data.container;

import me.joaomanoel.d4rkk.dev.database.data.DataContainer;
import me.joaomanoel.d4rkk.dev.database.data.interfaces.AbstractContainer;
import me.joaomanoel.d4rkk.dev.player.enums.*;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class PreferencesContainer extends AbstractContainer {
  
  public PreferencesContainer(DataContainer dataContainer) {
    super(dataContainer);
  }

  public void changePlayerVisibility() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pv", PlayerVisibility.getByOrdinal((long) preferences.get("pv")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changePrivateMessages() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pm", PrivateMessages.getByOrdinal((long) preferences.get("pm")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeBloodAndGore() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("bg", BloodAndGore.getByOrdinal((long) preferences.get("bg")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeProtectionLobby() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pl", ProtectionLobby.getByOrdinal((long) preferences.get("pl")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeChatMention() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("cm", ChatMention.getByOrdinal((Long)preferences.get("cm")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeMatchMaking() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("mm", MatchMaking.getByOrdinal((Long)preferences.get("mm")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }



  public MatchMaking getMatchMaking() {
    return MatchMaking.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("mm"));
  }

  public PlayerVisibility getPlayerVisibility() {
    return PlayerVisibility.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pv"));
  }

  public PrivateMessages getPrivateMessages() {
    return PrivateMessages.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pm"));
  }

  public BloodAndGore getBloodAndGore() {
    return BloodAndGore.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("bg"));
  }

  public ProtectionLobby getProtectionLobby() {
    return ProtectionLobby.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pl"));
  }

  public ChatMention getChatMention() {
    return ChatMention.getByOrdinal((Long)this.dataContainer.getAsJsonObject().get("cm"));
  }
}
