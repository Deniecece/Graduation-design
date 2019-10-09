package com.book.mall.mall.util;

import com.supply.util.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HttpUtilTest {

    @Test
    public void httpTest() {

        String url = "https://api.weixin.qq.com/sns/jscode2session";

        Map<String, String> param = new HashMap<>();
//        param.put("id", "20");
        param.put("appid", "wxb39a7d55e5d5b0f1");
        param.put("secret", "1f657127f1282b3594344c46f90c62da");
        param.put("js_code", "zgGBWqaLRa-s_Oy0a");
        param.put("grant_type", "authorization_code");

//        String resBean = HttpUtil.postHttpForJson(url, param);
        String resBean = HttpUtil.getForUrl(url, param);
        System.out.println(resBean);

    }
}
