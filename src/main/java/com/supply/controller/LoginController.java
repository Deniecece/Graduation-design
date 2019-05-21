package com.supply.controller;

import com.supply.core.KkbResponse;
import com.supply.core.KkbStatus;
import com.supply.domain.Form;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.KDCOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @PostMapping("/login")
    public KkbResponse login(@RequestBody Form data) {
        if("shantong".equals(data.getUsername()) && "123".equals(data.getPassword())) {
            return new KkbResponse();
        }
        return new KkbResponse(KkbStatus.FAILURE);
    }

    @PostMapping("/kind/list")
    public KkbResponse kinds() {
        List<Map> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", 1);
        map1.put("name", "书籍");
        map1.put("createTime", "2019-04-20 14:23:12");
        list.add(map1);
        Map<String, Object> map2= new HashMap<>();
        map2.put("id", 2);
        map2.put("name", "衣物");
        map2.put("createTime", "2019-04-20 14:27:56");
        list.add(map2);
        Map<String, Object> map3= new HashMap<>();
        map3.put("id", 3);
        map3.put("name", "学习资料");
        map3.put("createTime", "2019-04-20 14:47:59");
        list.add(map3);
        Map<String, Object> map4= new HashMap<>();
        map4.put("id", 4);
        map4.put("name", "工具");
        map4.put("createTime", "2019-04-20 14:47:59");
        list.add(map4);
        Map<String, Object> map5= new HashMap<>();
        map5.put("id", 5);
        map5.put("name", "其他");
        map5.put("createTime", "2019-04-20 14:47:59");
        list.add(map5);
        return new KkbResponse(list);
    }
}
