/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.morettic.univoxer.push.io.task;

import static br.com.morettic.univoxer.push.io.WatchManagerServlet.getJSON;
import br.com.morettic.univoxer.push.io.helper.PushAndroid;
import static java.net.URLEncoder.encode;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author LuisAugusto
 */
public class TimerTask implements Job {

    private final static String[] messages = {
                                            "Compartilhe os points da sua cidade!",
                                            "Vai tomar uma gelada? Mostre onde é o pico na sua cidade!",
                                            "Ajude nos visualizando uma propagando no menu!",
                                            "Segurança, Saude, Politica, Esporte? Registre uma ocorrência",
                                            "News are coming!", 
                                            "Registre uma ocorrência e compartilhe!", 
                                            "Fique de olho no que está acontecendo!", 
                                            "Know what is happening", 
                                            "Muitas informações para você", 
                                            "Alertas & News",
                                            "Compartilhe o que sua empresa faz. Registre aqui é de graça!",
                                            "Denuncie a corrupção em sua cidade!",
                                            "Anuncie em nosso mapa! Consulte nos!"};
    
    private static String URL = "https://gaeloginendpoint.appspot.com/infosegcontroller.exec?action=20&msg=%";

    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        System.out.println("Hello Quartz!" + new Date().toLocaleString());
        String msg;

        Random random3 = new Random();
        int rand3 = random3.nextInt(messages.length);
        msg = messages[rand3];
        
        String urlFinal = URL.replace("%", encode(msg));

        //out.print(urlFinal);
        JSONObject txtUrl;
        try {
            txtUrl = getJSON(urlFinal);
            //out.print(txtUrl.toString());
            JSONArray ja = txtUrl.getJSONArray("devices");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject js = ja.getJSONObject(i);
            //out.print(js.toString());
              PushAndroid.sendNotification(js.getString("msg"), js.getString("token"),1);
            }
        } catch (Exception ex) {
            Logger.getLogger(TimerTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
