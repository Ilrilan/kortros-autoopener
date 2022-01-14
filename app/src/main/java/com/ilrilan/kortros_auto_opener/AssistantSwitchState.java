package com.ilrilan.kortros_auto_opener;

import android.os.StrictMode;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AssistantSwitchState {
    static String authToken = "";
    static String assistUrl = "";
    static String switchId = "switch.domofon_auto_open";

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static boolean getAsistantSwitchState() throws Exception {
        // Sending get request
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = new URL("https://" + assistUrl + "/api/states/" + switchId);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setRequestProperty("Authorization", "Bearer " + authToken);

        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String jsonText = readAll(in);
        JSONObject json = new JSONObject(jsonText);
        in.close();
        return json.getString("state").equals("on");

    }
}
