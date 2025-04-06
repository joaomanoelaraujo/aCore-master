package me.joaomanoel.d4rkk.dev.utils;

import org.bukkit.entity.Player;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class PlayerIPUtils {

    public static String getPlayerCountry(Player player) {
        try {
            // Obtendo o IP do jogador (considerando BungeeCord)
            String ip = player.getAddress() != null ? player.getAddress().getHostString() : null;
            if (ip == null || ip.startsWith("127.") || ip.startsWith("192.168.") || ip.equals("0.0.0.0")) {
                ip = getServerIP(); // Pega o IP público do servidor se o IP do jogador for inválido
            }

            String urlStr = "http://ip-api.com/json/" + ip;
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Definindo o User-Agent para simular um navegador
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36");
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject) parser.parse(response.toString());
            String countryCode = (String) responseObject.get("countryCode");

            // Verifica se o código do país é nulo ou vazio
            if (countryCode == null || countryCode.isEmpty()) {
                return "US";
            }

            return countryCode;

        } catch (Exception e) {
            e.printStackTrace();
            return "US";
        }
    }

    private static String getServerIP() {
        try {
            URL url = new URL("https://checkip.amazonaws.com");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String ip = br.readLine();
                return ip;
            }
        } catch (Exception e) {
            return "0.0.0.0";
        }
    }

}
