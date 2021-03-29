/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.HttpClientBuilder;
/**
 *
 * @author paata
 */
public class SendSMS {

     private static String sendPOST(String url,String smsbodytext) throws IOException {

        String result = "";
        HttpPost post = new HttpPost(url);
//        String smsbodytext = "{\n"
//                + "\"Body\":  \"Message template 1\",\n"
//                + "    \"SourceInfo\":  {\n"
//                + "                       \"Date\":  \"2021-03-17 16:23:52\",\n"
//                + "                   },\n"
//                + "    \"MessageType\":  \"SMS\",\n"
//                + "    \"Subject\":  \"smssubject\",\n"
//                + "    \"System\":  \"CiscoMedical\",\n"
//                + "    \"Recipients\":  \"994507903917\"\n"
//                + "}";
       
        StringBuilder json = new StringBuilder();
        System.out.println("smsbodytext==========" + smsbodytext);

        json.append(smsbodytext);

         System.out.println("111111111111");
       try{ 
           System.out.println("222222222");
        post.setEntity(new StringEntity(json.toString()));
        
        post.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
           System.out.println("444444444");
         HttpClient client = getHttpClient();
           System.out.println("555555555");
        HttpResponse response = client.execute(post);
        result=EntityUtils.toString(response.getEntity());
        
        System.out.println("7777777777="+result);
    }catch (Exception e) {e.printStackTrace();}
//        try ( CloseableHttpClient httpClient = HttpClients.createDefault();  CloseableHttpResponse response = httpClient.execute(post)) {
//
//            result = EntityUtils.toString(response.getEntity());
//        }

        return result;
    }
 private static class SSLUtil {
 
        protected static SSLConnectionSocketFactory getInsecureSSLConnectionSocketFactory()
                throws KeyManagementException, NoSuchAlgorithmException {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
 
                    @Override
                    public void checkClientTrusted(
                            final java.security.cert.X509Certificate[] arg0, final String arg1)
                            throws CertificateException {
                        // do nothing and blindly accept the certificate
                    }
 
                    @Override
                    public void checkServerTrusted(
                            final java.security.cert.X509Certificate[] arg0, final String arg1)
                            throws CertificateException {
                        // do nothing and blindly accept the server
                    }
                }
            };
 
            final SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
 
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext, new String[]{"TLSv1.2"}, null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
 
            return sslsf;
        }
    }
 
 static HttpClient getHttpClient() throws Exception {
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
 
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(requestBuilder.build());
        builder.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory());
        HttpClient httpClient = builder.build();
        return httpClient;
    }
    
    
   
    public static String getResult(String ss, HttpServletRequest request) {
        String addr = "https://ptmsg.pasha-insurance.az/send/message";
         try {
             return sendPOST(addr, ss);
             
         } catch (Exception e) {
             e.printStackTrace();
             return e.toString();
         }
        
       

    }
}
