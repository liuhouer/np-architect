package com.imooc.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.imooc.auth.service.pojo.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 半仙.
 */
@Slf4j
@Service
public class JwtService {

    // 生产环境不能这么用
    private static final String KEY = "changeIt";
    private static final String ISSUER = "yao";

    private static final long TOKEN_EXP_TIME = 24 * 3600 * 1000;
    private static final String USER_ID = "userid";

    /**
     * 生成Token
     *
     * @param acct
     * @return
     */
    public String token(Account acct) {
        Date now = new Date();
        // 这里提供了很多种加密算法，生产环境可以用更高等级的加密算法，比如
        // 【最常用】采用非对称密钥加密，auth-service只负责生成jwt-token
        //  由各个业务方（或网关层）在自己的代码里用key校验token的正确性
        //  优点：符合规范，并且节约了一次HTTP Call
        //
        //  咱这里用了简单的token生成方式，同学们可以试着把上面的场景在本地实现
        Algorithm algorithm = Algorithm.HMAC256(KEY);

        String token = JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + TOKEN_EXP_TIME))
                .withClaim(USER_ID, acct.getUserId())
//                .withClaim("ROLE", "")
                .sign(algorithm);

        log.info("jwt generated, userId={}", acct.getUserId());
        return token;
    }

    /**
     * 校验Token
     *
     * @param token
     * @param userId
     * @return
     */
    public boolean verify(String token, String userId) {
        log.info("verifying jwt - username={}", userId);

        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .withClaim(USER_ID, userId)
                    .build();

            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("auth failed, userId={}", userId);
            return false;
        }
    }

}
