package com.nickhorder.betfairapi.api;
import com.nickhorder.betfairapi.entities.Login;
//import com.betfair.aping.util.JsonConverter;
import com.nickhorder.betfairapi.exceptions.APINGException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;


public class HttpAuth {

    public HttpAuth() {
        super();
    }

    public Login sendAuthRequest(int port, String applicationKey, String username, String password, String keyStorePassword) throws APINGException, IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();

        String responseString = null;
        String authContainer = null;
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManager[] keyManagers = getKeyManagers("pkcs12", new FileInputStream(new File("C:\\Users\\X1 Carbon\\BFAPIKeystore.p12")), keyStorePassword);
            ctx.init(keyManagers, null, new SecureRandom());
            SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());

            ClientConnectionManager manager = httpClient.getConnectionManager();
            manager.getSchemeRegistry().register(new Scheme("https", port, factory));
            HttpPost httpPost = new HttpPost("https://identitysso-cert.betfair.com/api/certlogin");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", username));
            nvps.add(new BasicNameValuePair("password", password));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps));

            httpPost.setHeader("X-Application", applicationKey);
            //System.out.println("Executing This Request: " + httpPost.getRequestLine());

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();

            //System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                responseString = EntityUtils.toString(entity);
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(responseString, Login.class);

    }

    private static KeyManager[] getKeyManagers(String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyStorePassword.toCharArray());
        return kmf.getKeyManagers();
    }

}

