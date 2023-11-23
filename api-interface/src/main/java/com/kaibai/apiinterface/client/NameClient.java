package com.kaibai.apiinterface.client;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.kaibai.apiinterface.entity.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author kaibai
 * @date 2023-11-06 21:33
 **/
public class NameClient {
    public String getName(@RequestParam String name) {
        String myname = HttpUtil.get("http://localhost:8888/name?name=" + name);
        System.out.println(myname);
        return myname;
    }

    public String getNameByPost(@RequestParam String name) {
        String myname = HttpUtil.post("http://localhost:8888/name", name);
        System.out.println(myname);
        return myname;
    }

    public String getNamed(@RequestBody User user) {
        String jsonStr = JSONUtil.toJsonStr(user);

        String name = HttpUtil.post("http://localhost:8888/named", jsonStr);
        System.out.println(name);
        return name;
    }
}
