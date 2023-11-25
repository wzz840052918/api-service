package com.kaibai.project.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.Map;

/**
 * @author kaibai
 * @date 2023/11/24
 */
public class SignUtils {

    /**
     * 生成签名
     * 1。 使用SHA256算法的Digester
     * 2. 拼接秘钥作为content
     * 3. 计算content照耀并返回
     * @param secretKey
     * @return
     */
    public static String generateSign(String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content =  "." + secretKey;
        return md5.digestHex(content);
    }
}
