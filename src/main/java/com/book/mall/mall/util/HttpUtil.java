package com.book.mall.mall.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class HttpUtil {

    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate template;

    public String postHttpForJson(String url, Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = template.postForEntity(url, requestEntity, String.class);

        assert response != null;
        int code = response.getStatusCodeValue();
        if(code != 200) {
            return "请求异常, code :" + code;
        }
        return response.getBody();
    }

    public String getHttpForUrl(String url, Map<String, String> params) {

        String response = template.getForObject(url, String.class, params);
        return response;
    }
}