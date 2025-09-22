package com.product.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */
@Service
public class TokenBlacklistService {

    private Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }
}

