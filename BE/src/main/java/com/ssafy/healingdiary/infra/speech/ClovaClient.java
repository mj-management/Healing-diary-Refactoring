package com.ssafy.healingdiary.infra.speech;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClovaClient {

    private String CLIENT_ID;
    private String SECRET;
    private final Header[] HEADERS = new Header[] {
        new BasicHeader("Content-Type", "application/json"),
        new BasicHeader("X-NCP-APIGW-API-KEY-ID", null),
        new BasicHeader("X-NCP-APIGW-API-KEY", null),
    };

    @Value("${clova.sentiment.client-id}")
    private void setClientId(String clientId){
        CLIENT_ID = clientId;
        HEADERS[1] = new BasicHeader("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
    }

    @Value("${clova.sentiment.client-secret}")
    private void setClientSecret(String secret){
        SECRET = secret;
        HEADERS[2] = new BasicHeader("X-NCP-APIGW-API-KEY", SECRET);
    }


    private final CloseableHttpClient httpClient;
    private final Gson gson;

    public String analyze(String text){
        HttpPost httpPost = new HttpPost("https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze");
        httpPost.setHeaders(HEADERS);

        Map<String, Object> body = new HashMap<>();
        body.put("content", text);
        body.put("config.negativeClassification", true);

        StringEntity httpEntity = new StringEntity(gson.toJson(body), ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);

        return execute(httpPost);
    }

    public String summerize(String text){
        HttpPost httpPost = new HttpPost("https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize");
        httpPost.setHeaders(HEADERS);

        Map<String, Object> document = new HashMap<>();
        document.put("content", text);

        Map<String, Object> option = new HashMap<>();
        option.put("language", "ko");
        option.put("summaryCount", 3);

        Map<String, Object> body = new HashMap<>();
        body.put("document", document);
        body.put("option", option);

        StringEntity httpEntity = new StringEntity(gson.toJson(body), ContentType.APPLICATION_JSON);
        httpPost.setEntity(httpEntity);

        return execute(httpPost);
    }


    private String execute(HttpPost httpPost) {
        try (final CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            final HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
