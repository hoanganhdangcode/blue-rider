package com.blueride.crypto.jwt;

import java.util.Map;

public interface JwtVerifier {

    Map<String, Object> verify(String token);
}
