package com.kaibai.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具类
 *
 * @author kaibai
 * @date 2023/11/23
 */
public class SignUtil {

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
