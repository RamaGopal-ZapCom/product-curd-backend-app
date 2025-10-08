package com.product.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 *
 *
 *
 *
 * | Step | Description                                                            |
 * | ---- | ---------------------------------------------------------------------- |
 * | 1    | Add Jasypt dependency                                                  |
 *
 * <dependency>
 *     <groupId>com.github.ulisesbocchio</groupId>
 *     <artifactId>jasypt-spring-boot-starter</artifactId>
 *     <version>3.0.5</version>
 * </dependency>
 *
 * | 2    | Encrypt the sensitive property (DB password)                           |
 * | 3    | Replace plain value with `ENC(...)` in `application.yml`               |
 *
 *      spring:
 *   datasource:
 *     url: jdbc:mysql://localhost:3306/full_stack_db
 *     username: root
 *     #password: rama123
 *     password: ENC(RC8E36jlBmU5aH+jowlYfQ==)
 *     driver-class-name: com.mysql.cj.jdbc.Driver
 *
 *
 * | 4    | Run Spring Boot app with the key `                                      |
 *
 *    #password encryptor
 * jasypt:
 *   encryptor:
 *     algorithm: PBEWithMD5AndDES
 *     password: secretkey
 *
 *# for testing below
 *      public class TestJspy {
 *     public static void main(String[] args) {
 *         BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
 *         textEncryptor.setPassword("secretkey");
 *         String encrypted = textEncryptor.encrypt("rama123");
 *         System.out.println("Encrypted password: " + encrypted);
 *     }
 * }
 *
 * | 5    | Jasypt decrypts automatically during startup                           |
 *
 */
@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String encryptorPassword;

    @Value("${jasypt.encryptor.algorithm}")
    private String encryptorAlgorithm;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encryptorPassword);
        encryptor.setAlgorithm(encryptorAlgorithm);
        return encryptor;
    }
}
