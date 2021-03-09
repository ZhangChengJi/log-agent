//package com.log.agent.utils;
//
//import com.nimbusds.jose.JWSObject;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jwt.SignedJWT;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
//
//import javax.servlet.http.HttpServletRequest;
//import java.security.KeyPair;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.text.ParseException;
//
//public class TokenUtils {
//     /**
//      * Created by macro on 2020/6/22.
//      */
//          public RSAKey getDefaultRSAKey() {
//               //从classpath下获取RSA秘钥对
//               KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
//               KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
//               //获取RSA公钥
//               RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//               //获取RSA私钥
//               RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//               return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
//          }
//     }
//
//}
