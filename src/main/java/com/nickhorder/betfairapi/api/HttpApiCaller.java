package com.nickhorder.betfairapi.api;

import com.nickhorder.betfairapi.exceptions.APINGException;
//import com.betfair.aping.util.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nickhorder.betfairapi.util.RescriptResponseHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class HttpApiCaller {
    public HttpApiCaller() {
        super();
    }

    private final String HTTP_HEADER_X_APPLICATION = "X-Application";
    private final String HTTP_HEADER_X_AUTHENTICATION = "X-Authentication";
    private final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    private final String HTTP_HEADER_ACCEPT = "Accept";
    private final String HTTP_HEADER_ACCEPT_CHARSET = "Accept-Charset";

    private String sendPostRequest(String param, String operation, String appKey, String ssoToken, String URL, ResponseHandler<String> reqHandler) {
        String jsonRequest = param;

        HttpPost post = new HttpPost(URL);
        String resp = null;
        try {
            post.setHeader(HTTP_HEADER_CONTENT_TYPE, ApiNGAuthMain.getProp().getProperty("APPLICATION_JSON"));
            post.setHeader(HTTP_HEADER_ACCEPT, ApiNGAuthMain.getProp().getProperty("APPLICATION_JSON"));
            post.setHeader(HTTP_HEADER_ACCEPT_CHARSET, ApiNGAuthMain.getProp().getProperty("ENCODING_UTF8"));
            post.setHeader(HTTP_HEADER_X_APPLICATION, appKey);
            post.setHeader(HTTP_HEADER_X_AUTHENTICATION, ssoToken);

            post.setEntity(new StringEntity(jsonRequest, ApiNGAuthMain.getProp().getProperty("ENCODING_UTF8")));

            HttpClient httpClient = new DefaultHttpClient();

            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, Integer.parseInt(ApiNGAuthMain.getProp().getProperty("TIMEOUT")));
            HttpConnectionParams.setSoTimeout(httpParams, Integer.parseInt(ApiNGAuthMain.getProp().getProperty("TIMEOUT")));

            resp = httpClient.execute(post, reqHandler);

        } catch (UnsupportedEncodingException e1) {
            //Do something

        } catch (ClientProtocolException e) {
            //Do something

        } catch (IOException ioE) {
            //Do something

        }

        return resp;

    }


    public String sendRequest(String param, String operation, String appKey, String ssoToken) throws APINGException {
        String apiNgURL = ApiNGAuthMain.getProp().getProperty("APING_URL") + ApiNGAuthMain.getProp().getProperty("RESCRIPT_SUFFIX") + operation + "/";

        return sendPostRequest(param, operation, appKey, ssoToken, apiNgURL, new RescriptResponseHandler());
    }

    public static String makeRequest(String operation, Map<String, Object> params, String appKey, String ssoToken) throws APINGException, JsonProcessingException {
        String requestString;
        //Handling the Rescript request
        params.put("id", 1);

        ObjectMapper mapper = new ObjectMapper();
        requestString = mapper.writeValueAsString(params);
    //    requestString = JsonConverter.convertToJson(params);
        if (ApiNGAuthMain.isDebug())
            System.out.println("\nmakeRequest Request: " + requestString);

        //We need to pass the "sendPostRequest" method a string in util format:  requestString
        HttpApiCaller requester = new HttpApiCaller();
        String response = requester.sendRequest(requestString, operation, appKey, ssoToken);
        if (response != null)
            return response;
        else
            throw new APINGException();
    }
}