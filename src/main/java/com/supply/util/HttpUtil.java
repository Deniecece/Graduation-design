package com.supply.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@Component
public class HttpUtil {

    private static RestTemplate restTemplate;

    /**
     * 不格式化参数
     *
     * @return
     */
    static {
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(20000);
        factory.setConnectTimeout(500000);
        restTemplate = new RestTemplate(factory);
        uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        restTemplate.setUriTemplateHandler(uriBuilderFactory);
    }

    /**
     * 通用post请求
     * @param url
     * @param headers
     * @param data
     * @param t
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T postForUrlencoded(String url, HttpHeaders headers, MultiValueMap<String, Object> data, Class<T> t) throws Exception {
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, new HttpEntity<>(data, headers), t);
        if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
            return responseEntity.getBody();
//            if (!"1".equals(body.getString("code"))) {
//                logger.error("post请求失败，url:{}", url);
//            }
//            if (body.get("data") != null) {
//                HashMap<String, Object> map = (HashMap<String, Object>) body.get("data");
//                if (map.containsKey("uid") && map.size() > 1 && StringUtils.hasText(String.valueOf(map.get("uid")))) {
//                }
//            }
        }
        throw new Exception("post request is fail...");
    }

    public static String getForUrl(String url, Map<String, String> params) {
        return restTemplate.getForObject(url, String.class, params);
    }
}
