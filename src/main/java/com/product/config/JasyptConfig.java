package com.product.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
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
