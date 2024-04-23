package com.ssafy.healingdiary.global.config;

import com.google.gson.Gson;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    @Bean
    public Gson gson() { return new Gson(); }

    @Bean
    public CloseableHttpClient httpClient() { return HttpClients.createDefault(); }
}
