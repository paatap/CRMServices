/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author paata
 */
public class SendSMS {

    static JsonElement request(HttpRequestBase http, HttpServletRequest request, boolean out) {
        try {

            CloseableHttpClient client = HttpClients.createDefault();
            //System.out.println("222222222222222222");
            RequestConfig.Builder requestConfig = RequestConfig.custom();

            requestConfig.setConnectTimeout(5000);
            requestConfig.setConnectionRequestTimeout(5000);
            requestConfig.setSocketTimeout(5000);

            http.setConfig(requestConfig.build());

            http.setHeader("Accept", "application/json");
            http.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(http);
            System.out.println("=code==" + response.getStatusLine().getStatusCode());
            System.out.println("=reason==" + response.getStatusLine().getReasonPhrase());

            String result = EntityUtils.toString(response.getEntity());

            if (out) {
                if (result.length() > 1000) {
                    System.out.println("=result==" + result.substring(0, 1000));
                } else {
                    System.out.println("=result==" + result);
                }
            }
            if (response.getStatusLine().getStatusCode() != 200) {
                client.close();
                return null;
            }

            client.close();
            JsonElement el = new JsonParser().parse(result);

            return el;
        } catch (Exception e) {
            System.out.println("eeeeeeeeeerrrr================" + e.toString());
            e.printStackTrace();
            return null;
        }
    }

    public static JsonObject getjson(String ss, HttpServletRequest request) {
        String addr = "https://ptmsg.pasha-insurance.az/send/message";
        HttpPost http = new HttpPost(addr);
        System.out.println(ss);
        StringEntity entity = new StringEntity(ss, StandardCharsets.UTF_8);
        http.setEntity(entity);
        JsonElement el = request(http, request, true);
        return el.getAsJsonObject();

    }
}
