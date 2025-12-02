package me.joaomanoel.d4rkk.dev.utils;

import me.joaomanoel.d4rkk.dev.bungee.LanguageBungee;

/**
 * VERSÃO BUNGEECORD - Wrapper para LanguageBungee
 * Esta classe existe para manter compatibilidade com o código existente
 */
public class LanguageUtilsBungee {

    public static String get(String key) {
        try {
            // Converte o formato "party$invite" para o campo correto do LanguageBungee
            String fieldName = key.replace("$", "_").replace("-", "_");
            
            // Acessa o campo diretamente da classe LanguageBungee
            return (String) LanguageBungee.class.getField(fieldName).get(null);
        } catch (Exception e) {
            // Se não encontrar, tenta usar o método getMessage
            try {
                return LanguageBungee.getMessage(key);
            } catch (Exception ex) {
                return "Missing lang: " + key;
            }
        }
    }
}