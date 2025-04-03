package me.joaomanoel.d4rkk.dev.utils.langs;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class LanguageMessage {

    private static final Map<String, String> countryLanguageMap = new HashMap<>();
    private static final Map<String, String> languageMessageMap = new HashMap<>();

    static {
        countryLanguageMap.put("BR", "pt"); // Brasil
        countryLanguageMap.put("PT", "pt"); // Portugal
        countryLanguageMap.put("ES", "es"); // Espanha
        countryLanguageMap.put("MX", "es"); // México
        countryLanguageMap.put("AR", "es"); // Argentina
        countryLanguageMap.put("FR", "fr"); // França
        countryLanguageMap.put("DE", "de"); // Alemanha
        countryLanguageMap.put("JP", "ja"); // Japão
        countryLanguageMap.put("IT", "it"); // Itália
        countryLanguageMap.put("RU", "ru"); // Rússia
        countryLanguageMap.put("CN", "zh"); // China
        countryLanguageMap.put("KR", "ko"); // Coreia do Sul
        countryLanguageMap.put("IN", "hi"); // Índia
        countryLanguageMap.put("US", "en"); // EUA
        countryLanguageMap.put("GB", "en"); // Reino Unido
        countryLanguageMap.put("CA", "en"); // Canadá (Inglês)
        countryLanguageMap.put("AU", "en"); // Austrália
        countryLanguageMap.put("NZ", "en"); // Nova Zelândia
        // Países adicionais com idioma padrão inglês
        String[] englishCountries = {"IE", "ZA", "PH", "SG", "NG", "PK"};
        for (String country : englishCountries) {
            countryLanguageMap.put(country, "en");
        }

        // Mensagens por idioma
        languageMessageMap.put("pt", "§aSua linguagem foi definida para §ePortuguês§a com base na sua localidade. "
                + "Caso deseje alterar a linguagem, acesse o menu de linguagens.");
        languageMessageMap.put("es", "§aSu idioma ha sido configurado a §eEspañol§a según su ubicación. "
                + "Si desea cambiar el idioma, acceda al menú de idiomas.");
        languageMessageMap.put("fr", "§aVotre langue a été définie sur §eFrançais§a en fonction de votre emplacement. "
                + "Si vous souhaitez changer de langue, accédez au menu des langues.");
        languageMessageMap.put("de", "§aIhre Sprache wurde basierend auf Ihrem Standort auf §eDeutsch§a eingestellt. "
                + "Wenn Sie die Sprache ändern möchten, öffnen Sie das Sprachmenü.");
        languageMessageMap.put("ja", "§aあなたの地域に基づいて言語が§e日本語§aに設定されました。"
                + " 言語を変更したい場合は、言語メニューにアクセスしてください。");
        languageMessageMap.put("it", "§aLa tua lingua è stata impostata su §eItaliano§a in base alla tua posizione. "
                + "Se desideri modificare la lingua, accedi al menu delle lingue.");
        languageMessageMap.put("ru", "§aВаш язык был установлен на §eРусский§a в зависимости от вашего местоположения. "
                + "Если хотите изменить язык, зайдите в меню языков.");
        languageMessageMap.put("zh", "§a根据您的位置，语言已设置为§e中文§a。"
                + " 如果您想更改语言，请访问语言菜单。");
        languageMessageMap.put("ko", "§a귀하의 지역에 따라 언어가 §e한국어§a로 설정되었습니다. "
                + "언어를 변경하려면 언어 메뉴에 액세스하십시오.");
        languageMessageMap.put("hi", "§aआपकी भाषा को आपके स्थान के आधार पर §eहिन्दी§a में सेट कर दिया गया है। "
                + "यदि आप भाषा बदलना चाहते हैं, तो भाषा मेनू पर जाएं.");
        languageMessageMap.put("en", "§aYour language has been set to §eEnglish§a based on your location. "
                + "If you wish to change the language, access the language menu.");
    }

    public static String getLanguageMessage(Player player, String countryCode) {
        String language = countryLanguageMap.getOrDefault(countryCode.toUpperCase(), "en");
        return languageMessageMap.getOrDefault(language, languageMessageMap.get("en"));
    }
}
