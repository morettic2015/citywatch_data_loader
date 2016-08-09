/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io.helper;

import com.google.gson.JsonObject;
import com.sun.media.jfxmedia.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 *
 * @author LuisAugusto
 */
public class PushAndroid {

    private static final String API_KEY = "AIzaSyA5k_C6HkUcBdBPC8-9iU1hAZOZ-G6kmys";
    private static final String GCM_URL = "https://fcm.googleapis.com/fcm/send";

    
    /**
     
       public function android($data, $reg_id) {
        $url = 'https://android.googleapis.com/gcm/send';
        $message = array(
            'title' => $data['mtitle'],
            'message' => $data['mdesc'],
            'subtitle' => '',
            'tickerText' => '',
            'msgcnt' => 1,
            'vibrate' => 1
        );

        $headers = array(
            'Authorization: key=' . self::$API_ACCESS_KEY,
            'Content-Type: application/json'
        );

        $fields = array(
            'registration_ids' => array($reg_id),
            'data' => $message,
        );

        return $this->useCurl($url, $headers, json_encode($fields));
    }
     */
    
    public static JsonObject sendNotification(String messageBody, String deviceId) {
        JsonObject js = new JsonObject();
        try {

            URL url = new URL(GCM_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String apiKey = API_KEY;

            String credentials = "key=" + apiKey;
            //String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(credentials.getBytes()));

            conn.setRequestProperty("Authorization", basicAuth);

            String notfnStr = "{\"body\": \"Univoxer.com\", \"title\": \"WAKE_UP\"}";
            String dataStr = "{\"key1\": \"value1\", \"key2\": \"value2\"}";

            String bodyStr = "{\"priority\": \"high\", \"to\": \"" + deviceId + "\",\"notification\": " + notfnStr + ", \"data\": " + dataStr + "}";

            OutputStream os = conn.getOutputStream();
            os.write(bodyStr.getBytes());
            os.flush();
            js.addProperty("status", conn.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder outputB = new StringBuilder();
            String output;
           
            while ((output = br.readLine()) != null) {
                outputB.append(output);
            }
            js.addProperty("response", outputB.toString());
            conn.disconnect();
        } catch (Exception e) {
            Logger.logMsg(Logger.ERROR, e.toString());
            js.addProperty("error",e.getMessage());
            js.addProperty("status",500);
        } finally{
            return js;
        }
    }
}
