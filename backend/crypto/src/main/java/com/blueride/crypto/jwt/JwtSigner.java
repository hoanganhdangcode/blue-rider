package com.blueride.crypto.jwt;

import java.util.Map;

public interface JwtSigner {

    String sign(Map<String, Object> claims);
}
