package com.blueride.coreservice.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.keys")
public class JwtKeyProperties {

    private String directory = "/app/keys";
    private String privateKey;
    private String publicKey;
    private int rsaKeySize = 2048;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getRsaKeySize() {
        return rsaKeySize;
    }

    public void setRsaKeySize(int rsaKeySize) {
        this.rsaKeySize = rsaKeySize;
    }

    public String resolvedPrivateKey() {
        return privateKey != null && !privateKey.isBlank() ? privateKey : directory + "/private.key";
    }

    public String resolvedPublicKey() {
        return publicKey != null && !publicKey.isBlank() ? publicKey : directory + "/public.key";
    }
}
