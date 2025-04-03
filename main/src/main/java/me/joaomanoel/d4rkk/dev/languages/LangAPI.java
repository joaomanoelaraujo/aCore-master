package me.joaomanoel.d4rkk.dev.languages;

import me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic;
import me.joaomanoel.d4rkk.dev.languages.translates.EN_US;
import me.joaomanoel.d4rkk.dev.languages.translates.PT_BR;
import me.joaomanoel.d4rkk.dev.player.Profile;
import org.json.JSONObject;

import static me.joaomanoel.d4rkk.dev.cosmetic.Cosmetic.findById;

public class LangAPI {

    /**
     * Obtém a linguagem selecionada do perfil do jogador.
     *
     * @param profile O perfil do jogador.
     * @return A linguagem selecionada, ou null se nenhuma estiver configurada.
     */
    public static GLanguage getLanguage(Profile profile) {
        if (profile == null) {
            return null;
        }
        long languageId = profile.getDataContainer("aCoreProfile", "cselected").getAsLong();
        return Cosmetic.findLanguageById(languageId);
    }

    public static GLanguage getLanguageId(Profile profile) {
        if (profile == null) {
            // Retornar um idioma padrão em vez de null
            return findById(GLanguage.class, 1); // 1 para EN_US
        }

        try {
            String languageData = profile.getDataContainer("aCoreProfile", "cselected").getAsString();

            // Verificar se languageData não é null ou vazio
            if (languageData == null || languageData.isEmpty()) {
                return findById(GLanguage.class, 1);
            }

            JSONObject json = new JSONObject(languageData);

            // Verificar se as chaves necessárias existem
            if (!json.has("LANGUAGE") || !json.getJSONObject("LANGUAGE").has("1")) {
                return findById(GLanguage.class, 1);
            }

            int languageId = json.getJSONObject("LANGUAGE").getInt("1");
            GLanguage language = findById(GLanguage.class, languageId);

            // Se não encontrar o idioma, retornar o padrão
            return language != null ? language : findById(GLanguage.class, 1);

        } catch (Exception e) {
            e.printStackTrace();
            // Em caso de erro, retornar o idioma padrão
            return findById(GLanguage.class, 1);
        }
    }

    public static String getTranslatedText(String key, Profile profile) {
        GLanguage languageId = getLanguageId(profile);

        if (languageId == null) {
            return getFieldValue(EN_US.class, key);
        }

        return (languageId.getId() == 1)
                ? getFieldValue(EN_US.class, key)
                : getFieldValue(PT_BR.class, key);
    }

    public static int getTranslatedInt(String key, Profile profile) {
        GLanguage languageId = getLanguageId(profile);
        return (languageId.getId() == 1)
                ? getFieldValueAsInt(EN_US.class, key)
                : getFieldValueAsInt(PT_BR.class, key);
    }

    private static String getFieldValue(Class<?> clazz, String key) {
        try {
            return (String) clazz.getField(key).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return "§cTranslation missing!";
        }
    }

    private static int getFieldValueAsInt(Class<?> clazz, String key) {
        try {
            return (int) clazz.getField(key).get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
