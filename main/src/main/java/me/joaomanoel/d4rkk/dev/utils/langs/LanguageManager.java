package me.joaomanoel.d4rkk.dev.utils.langs;

import me.joaomanoel.d4rkk.dev.Core;
import me.joaomanoel.d4rkk.dev.languages.GLanguage;
import me.joaomanoel.d4rkk.dev.plugin.config.KConfig;

import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LanguageManager {
    private final Map<String, Language> languages = new HashMap<>();

    /**
     * Carrega os idiomas a partir da pasta "translate" no data folder do plugin.
     * Se o arquivo YAML não possuir uma chave "name", o nome do idioma será definido
     * com base no nome do arquivo (sem a extensão).
     */
    public void loadLanguages() {
        File langDir = new File(Core.getInstance().getDataFolder(), "translate");
        if (!langDir.exists() || !langDir.isDirectory()) {
            Core.getInstance().getLogger().warning("Diretório de idiomas não encontrado: " + langDir.getAbsolutePath());
            return;
        }
        File[] files = langDir.listFiles((dir, name) -> name.endsWith(".yml") || name.endsWith(".yaml"));
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                // Remove a extensão para usar como fallback para o nome do idioma
                String fileName = file.getName().replaceAll("\\.(yml|yaml)$", "");
                // Usa o método do Core para pegar a configuração
                KConfig config = Core.getInstance().getConfig("translate/" + fileName);
                // Tenta obter o nome do idioma a partir da chave "name", se existir
                String langName = config.getString("name");
                if (langName == null) {
                    langName = fileName;
                }
                // Obtém a seção inteira e "achata" a estrutura aninhada
                ConfigurationSection section = config.getSection("");
                Map<String, String> translations = flattenConfigurationSection(section, "");
                Language lang = new YamlLanguage(langName, translations);
                languages.put(langName, lang);
                Core.getInstance().getLogger().info("Idioma carregado: " + langName);
            } catch (Exception e) {
                Core.getInstance().getLogger().severe("Erro ao carregar idioma do arquivo " + file.getName());
                e.printStackTrace();
            }
        }
    }

    /**
     * Método auxiliar para transformar uma ConfigurationSection aninhada em um Map plano.
     * As chaves serão formadas juntando os nomes dos níveis com ponto.
     * Exemplo: profileo.menu.levelInfo
     */
    private Map<String, String> flattenConfigurationSection(ConfigurationSection section, String prefix) {
        Map<String, String> map = new HashMap<>();
        for (String key : section.getKeys(false)) {
            Object obj = section.get(key);
            String newKey = prefix.isEmpty() ? key : prefix + "." + key;
            if (obj instanceof ConfigurationSection) {
                map.putAll(flattenConfigurationSection((ConfigurationSection) obj, newKey));
            } else {
                map.put(newKey, String.valueOf(obj));
            }
        }
        return map;
    }
    public Language getLanguageById(int id) {
        for (Language lang : languages.values()) {
            if (lang instanceof GLanguage) {
                if (((GLanguage) lang).getId() == id) {
                    return lang;
                }
            }
        }
        return null;
    }

    public Language getLanguage(String name) {
        return languages.get(name);
    }

    public Set<String> getAvailableLanguageNames() {
        return languages.keySet();
    }
}
