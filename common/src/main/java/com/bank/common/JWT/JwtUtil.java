package com.bank.common.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

/**
 * JWT工具类
 *
 * @author easwift-boot
 * @date 2019/8/10
 **/
public class JwtUtil {

    /**
     * 过期时间30分钟（微秒）
     */
    public static final long EXPIRE_TIME_30M = 30 * 60 * 1000;

    /**
     * 过期时间12小时（秒）
     */
    public static final long EXPIRE_TIME_12H = 60 * 60 * 12;

    /**
     * 过期时间24小时（秒）
     */
    public static final long EXPIRE_TIME_24H = 60 * 60 * 24;

    /**
     * 过期时间7天（秒）
     */
    public static final long EXPIRE_TIME_7D = 60 * 60 * 24 * 7;

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,12H后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME_7D * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);

    }

//    /**
//     * 根据request中的token获取用户账号
//     *
//     * @param request
//     * @return
//     * @throws SystemException
//     */
//    public static String getUserNameByToken(HttpServletRequest request) throws Exception {
//        String accessToken = request.getHeader("X-Access-Token");
//        String username = getUsername(accessToken);
//        if (ConvertUtils.isEmpty(username)) {
//            throw new SystemException("未获取到用户");
//        }
//        return username;
//    }
//
//    /**
//     * 从session中获取变量
//     *
//     * @param key
//     * @return
//     */
//    public static String getSessionData(String key) {
//        //${myVar}%
//        //得到${} 后面的值
//        String moshi = "";
//        if (key.contains("}")) {
//            moshi = key.substring(key.indexOf("}") + 1);
//        }
//        String returnValue = null;
//        if (key.contains("#{")) {
//            key = key.substring(2, key.indexOf("}"));
//        }
//        if (ConvertUtils.isNotEmpty(key)) {
//            HttpSession session = SpringContextUtils.getHttpServletRequest().getSession();
//            returnValue = (String) session.getAttribute(key);
//        }
//        //结果加上${} 后面的值
//        if (returnValue != null) {
//            returnValue = returnValue + moshi;
//        }
//        return returnValue;
//    }

}
